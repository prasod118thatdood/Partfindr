package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.UserArticleBackupStorage
import com.example.data.model.ComponentItem
import com.example.data.remote.FirestoreArticleRepository
import com.example.data.repository.ComponentRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val title: String) {
  BROWSE("Feed"),
  COMPARE("Compare"),
  FAVORITES("Saved"),
  PUBLISH("Publish"),
  ABOUT("About")
}

class ComponentViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: ComponentRepository
  private val backupStorage: UserArticleBackupStorage = UserArticleBackupStorage(application)
  private val firestoreRepo: FirestoreArticleRepository = FirestoreArticleRepository(application)

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow("All")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  private val _currentTab = MutableStateFlow(AppTab.BROWSE)
  val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

  private val _selectedComponentForDetail = MutableStateFlow<ComponentItem?>(null)
  val selectedComponentForDetail: StateFlow<ComponentItem?> = _selectedComponentForDetail.asStateFlow()

  // Compare mode: up to 2 components
  private val _compareItems = MutableStateFlow<List<ComponentItem>>(emptyList())
  val compareItems: StateFlow<List<ComponentItem>> = _compareItems.asStateFlow()

  // Google User Auth state
  private val _currentUser = MutableStateFlow<FirebaseUser?>(Firebase.auth.currentUser)
  val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

  // Publish Form States with persistent draft recovery
  val formTitle = MutableStateFlow("")
  val formCategory = MutableStateFlow("")
  val formOverview = MutableStateFlow("")
  val formSpecs = MutableStateFlow("")
  val formDatasheet = MutableStateFlow("")
  val formError = MutableStateFlow<String?>(null)

  init {
    val db = AppDatabase.getDatabase(application, viewModelScope)
    repository = ComponentRepository(db.componentDao(), backupStorage, firestoreRepo)

    // Listen for Google Auth changes and auto-sync cloud articles
    Firebase.auth.addAuthStateListener { auth ->
      _currentUser.value = auth.currentUser
      if (auth.currentUser != null) {
        viewModelScope.launch {
          repository.syncCloudArticles()
        }
      }
    }

    // Load any saved draft
    val draft = backupStorage.loadDraft()
    if (draft.title.isNotBlank() || draft.overview.isNotBlank() || draft.specs.isNotBlank()) {
      formTitle.value = draft.title
      formCategory.value = draft.category
      formOverview.value = draft.overview
      formSpecs.value = draft.specs
      formDatasheet.value = draft.datasheet
    }

    viewModelScope.launch {
      repository.ensureInitialized()
    }
  }

  val allComponents: StateFlow<List<ComponentItem>> = repository.allComponents
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val favoriteComponents: StateFlow<List<ComponentItem>> = repository.favoriteComponents
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val userCreatedComponents: StateFlow<List<ComponentItem>> = repository.userCreatedComponents
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val filteredComponents: StateFlow<List<ComponentItem>> = combine(
    allComponents,
    _searchQuery,
    _selectedCategory
  ) { list, query, category ->
    list.filter { item ->
      val matchesCategory = when (category) {
        "All" -> true
        "My Articles" -> item.isUserCreated
        else -> item.categoryBadge.contains(category, ignoreCase = true)
      }
      matchesCategory && item.matchesQuery(query)
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun syncNow() {
    viewModelScope.launch {
      repository.syncCloudArticles()
    }
  }

  fun onSearchQueryChanged(query: String) {
    _searchQuery.value = query
  }

  fun onCategorySelected(category: String) {
    _selectedCategory.value = category
  }

  fun onTabSelected(tab: AppTab) {
    _currentTab.value = tab
  }

  fun openComponentDetail(item: ComponentItem) {
    _selectedComponentForDetail.value = item
  }

  fun closeComponentDetail() {
    _selectedComponentForDetail.value = null
  }

  fun toggleFavorite(item: ComponentItem) {
    viewModelScope.launch {
      repository.toggleFavorite(item)
      if (_selectedComponentForDetail.value?.id == item.id) {
        _selectedComponentForDetail.value = item.copy(isFavorite = !item.isFavorite)
      }
    }
  }

  fun toggleCompareItem(item: ComponentItem) {
    val current = _compareItems.value.toMutableList()
    val existingIndex = current.indexOfFirst { it.id == item.id }
    if (existingIndex >= 0) {
      current.removeAt(existingIndex)
    } else {
      if (current.size >= 2) {
        current.removeAt(0)
      }
      current.add(item)
    }
    _compareItems.value = current
  }

  fun clearCompare() {
    _compareItems.value = emptyList()
  }

  fun deleteComponent(item: ComponentItem) {
    viewModelScope.launch {
      repository.deleteArticle(item)
      if (_selectedComponentForDetail.value?.id == item.id) {
        _selectedComponentForDetail.value = null
      }
      _compareItems.value = _compareItems.value.filter { it.id != item.id }
    }
  }

  // Live draft updating
  fun updateDraftTitle(value: String) {
    formTitle.value = value
    saveCurrentDraft()
  }

  fun updateDraftCategory(value: String) {
    formCategory.value = value
    saveCurrentDraft()
  }

  fun updateDraftOverview(value: String) {
    formOverview.value = value
    saveCurrentDraft()
  }

  fun updateDraftSpecs(value: String) {
    formSpecs.value = value
    saveCurrentDraft()
  }

  fun updateDraftDatasheet(value: String) {
    formDatasheet.value = value
    saveCurrentDraft()
  }

  fun clearDraft() {
    formTitle.value = ""
    formCategory.value = ""
    formOverview.value = ""
    formSpecs.value = ""
    formDatasheet.value = ""
    formError.value = null
    backupStorage.clearDraft()
  }

  private fun saveCurrentDraft() {
    backupStorage.saveDraft(
      title = formTitle.value,
      category = formCategory.value,
      overview = formOverview.value,
      specs = formSpecs.value,
      datasheet = formDatasheet.value
    )
  }

  fun publishArticle(onSuccess: (ComponentItem) -> Unit) {
    val title = formTitle.value.trim()
    val category = formCategory.value.trim()
    val overview = formOverview.value.trim()
    val specs = formSpecs.value.trim()
    val datasheet = formDatasheet.value.trim()

    if (title.isBlank()) {
      formError.value = "Component / Device name is required"
      return
    }
    if (category.isBlank()) {
      formError.value = "Category badge is required"
      return
    }
    if (overview.isBlank()) {
      formError.value = "Overview & function is required"
      return
    }
    if (specs.isBlank()) {
      formError.value = "Specifications are required (e.g. Architecture: Zen 5, Cores: 8)"
      return
    }

    formError.value = null
    viewModelScope.launch {
      val newItem = ComponentItem(
        title = title,
        categoryBadge = category,
        overview = overview,
        specsRaw = specs,
        datasheetUrl = datasheet,
        isFavorite = true, // Crucial: Every article added is automatically saved!
        timestamp = System.currentTimeMillis(),
        isUserCreated = true
      )
      val newId = repository.insertArticle(newItem)
      clearDraft()

      // Switch to browse tab
      _currentTab.value = AppTab.BROWSE
      _searchQuery.value = ""
      _selectedCategory.value = "All"
      onSuccess(newItem.copy(id = newId))
    }
  }
}
