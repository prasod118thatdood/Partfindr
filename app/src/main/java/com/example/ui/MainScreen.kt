package com.example.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.auth.GoogleAuthHelper
import com.example.ui.components.PartfindrHeader
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.BrowseFeedScreen
import com.example.ui.screens.CloudSyncDialog
import com.example.ui.screens.CompareScreen
import com.example.ui.screens.ComponentDetailSheet
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.PublishArticleScreen
import com.example.ui.theme.PartfindrRed
import com.example.ui.theme.ZincDarkBg
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
  viewModel: ComponentViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }
  var showCloudSyncDialog by remember { mutableStateOf(false) }

  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
  val filteredComponents by viewModel.filteredComponents.collectAsStateWithLifecycle()
  val allComponents by viewModel.allComponents.collectAsStateWithLifecycle()
  val favoriteComponents by viewModel.favoriteComponents.collectAsStateWithLifecycle()
  val compareList by viewModel.compareItems.collectAsStateWithLifecycle()
  val detailItem by viewModel.selectedComponentForDetail.collectAsStateWithLifecycle()

  // Form states
  val formTitle by viewModel.formTitle.collectAsStateWithLifecycle()
  val formCategory by viewModel.formCategory.collectAsStateWithLifecycle()
  val formOverview by viewModel.formOverview.collectAsStateWithLifecycle()
  val formSpecs by viewModel.formSpecs.collectAsStateWithLifecycle()
  val formDatasheet by viewModel.formDatasheet.collectAsStateWithLifecycle()
  val formError by viewModel.formError.collectAsStateWithLifecycle()

  // Silent auto sign-in on launch to restore cloud data if user reinstalled the app
  LaunchedEffect(Unit) {
    GoogleAuthHelper.attemptAutoSignIn(
      context = context,
      onAuthSuccess = {
        viewModel.syncNow()
      },
      onUnauthenticated = {},
      scope = coroutineScope
    )
  }

  // BackHandler: return to BROWSE if on secondary tabs
  BackHandler(enabled = currentTab != AppTab.BROWSE) {
    viewModel.onTabSelected(AppTab.BROWSE)
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
      PartfindrHeader(
        currentUser = currentUser,
        onAccountClicked = { showCloudSyncDialog = true },
        onAddArticleClicked = {
          viewModel.onTabSelected(AppTab.PUBLISH)
        }
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = ZincDarkBg,
        contentColor = Color.White,
        modifier = Modifier.testTag("bottom_nav_bar")
      ) {
        val navItems = listOf(
          Triple(AppTab.BROWSE, Icons.Default.Memory, "Feed"),
          Triple(AppTab.COMPARE, Icons.Default.CompareArrows, "Compare"),
          Triple(AppTab.FAVORITES, Icons.Default.Bookmark, "Saved"),
          Triple(AppTab.PUBLISH, Icons.Default.EditNote, "Publish"),
          Triple(AppTab.ABOUT, Icons.Default.Info, "About")
        )

        navItems.forEach { (tab, icon, label) ->
          val isSelected = currentTab == tab
          NavigationBarItem(
            selected = isSelected,
            onClick = { viewModel.onTabSelected(tab) },
            icon = {
              if (tab == AppTab.COMPARE && compareList.isNotEmpty()) {
                BadgedBox(
                  badge = {
                    Badge(
                      containerColor = PartfindrRed,
                      contentColor = Color.White
                    ) {
                      Text("${compareList.size}")
                    }
                  }
                ) {
                  Icon(imageVector = icon, contentDescription = label)
                }
              } else if (tab == AppTab.FAVORITES && favoriteComponents.isNotEmpty()) {
                BadgedBox(
                  badge = {
                    Badge(
                      containerColor = PartfindrRed,
                      contentColor = Color.White
                    ) {
                      Text("${favoriteComponents.size}")
                    }
                  }
                ) {
                  Icon(imageVector = icon, contentDescription = label)
                }
              } else {
                Icon(imageVector = icon, contentDescription = label)
              }
            },
            label = {
              Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = Color.White,
              selectedTextColor = PartfindrRed,
              indicatorColor = PartfindrRed,
              unselectedIconColor = Color(0xFFA1A1AA),
              unselectedTextColor = Color(0xFFA1A1AA)
            ),
            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      AnimatedContent(
        targetState = currentTab,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "TabContentAnimation"
      ) { targetTab ->
        when (targetTab) {
          AppTab.BROWSE -> {
            BrowseFeedScreen(
              searchQuery = searchQuery,
              onSearchQueryChanged = viewModel::onSearchQueryChanged,
              selectedCategory = selectedCategory,
              onCategorySelected = viewModel::onCategorySelected,
              components = filteredComponents,
              compareList = compareList,
              onCardClick = viewModel::openComponentDetail,
              onToggleFavorite = { item ->
                viewModel.toggleFavorite(item)
                coroutineScope.launch {
                  val msg = if (item.isFavorite) "Removed from saved" else "Saved to favorites"
                  snackbarHostState.showSnackbar(msg)
                }
              },
              onToggleCompare = { item ->
                viewModel.toggleCompareItem(item)
                coroutineScope.launch {
                  val inComp = compareList.any { it.id == item.id }
                  val msg = if (inComp) "Removed from compare" else "Added to compare (Max 2)"
                  snackbarHostState.showSnackbar(msg)
                }
              },
              onDeleteComponent = { item ->
                viewModel.deleteComponent(item)
                coroutineScope.launch {
                  snackbarHostState.showSnackbar("Deleted ${item.title}")
                }
              },
              onNavigateToPublish = { viewModel.onTabSelected(AppTab.PUBLISH) }
            )
          }

          AppTab.COMPARE -> {
            CompareScreen(
              compareList = compareList,
              allComponents = allComponents,
              onRemoveFromCompare = viewModel::toggleCompareItem,
              onClearCompare = viewModel::clearCompare,
              onAddToCompare = viewModel::toggleCompareItem,
              onNavigateToFeed = { viewModel.onTabSelected(AppTab.BROWSE) }
            )
          }

          AppTab.FAVORITES -> {
            val userCreatedComponents by viewModel.userCreatedComponents.collectAsStateWithLifecycle()
            FavoritesScreen(
              favoriteItems = favoriteComponents,
              userCreatedItems = userCreatedComponents,
              compareList = compareList,
              onCardClick = viewModel::openComponentDetail,
              onToggleFavorite = viewModel::toggleFavorite,
              onToggleCompare = viewModel::toggleCompareItem,
              onDeleteComponent = { item ->
                viewModel.deleteComponent(item)
                coroutineScope.launch {
                  snackbarHostState.showSnackbar("Deleted ${item.title}")
                }
              },
              onNavigateToFeed = { viewModel.onTabSelected(AppTab.BROWSE) }
            )
          }

          AppTab.PUBLISH -> {
            PublishArticleScreen(
              title = formTitle,
              onTitleChange = viewModel::updateDraftTitle,
              category = formCategory,
              onCategoryChange = viewModel::updateDraftCategory,
              overview = formOverview,
              onOverviewChange = viewModel::updateDraftOverview,
              specs = formSpecs,
              onSpecsChange = viewModel::updateDraftSpecs,
              datasheet = formDatasheet,
              onDatasheetChange = viewModel::updateDraftDatasheet,
              errorMessage = formError,
              onClearDraft = viewModel::clearDraft,
              onSubmit = {
                viewModel.publishArticle(
                  onSuccess = { newItem ->
                    Toast.makeText(context, "Saved & Published: ${newItem.title}", Toast.LENGTH_SHORT).show()
                    coroutineScope.launch {
                      snackbarHostState.showSnackbar("Article saved to database & cloud backup!")
                    }
                  }
                )
              }
            )
          }

          AppTab.ABOUT -> {
            AboutScreen(
              totalCount = allComponents.size,
              favoritesCount = favoriteComponents.size,
              currentUser = currentUser,
              onManageCloudSync = { showCloudSyncDialog = true }
            )
          }
        }
      }
    }
  }

  // Cloud Sync / Google Account Dialog
  if (showCloudSyncDialog) {
    CloudSyncDialog(
      currentUser = currentUser,
      onDismiss = { showCloudSyncDialog = false },
      onSyncTriggered = { viewModel.syncNow() }
    )
  }

  // Detail Modal Bottom Sheet
  detailItem?.let { selected ->
    val isFav = favoriteComponents.any { it.id == selected.id }
    val isComp = compareList.any { it.id == selected.id }

    ComponentDetailSheet(
      item = selected,
      isFavorite = isFav,
      isCompared = isComp,
      onDismiss = viewModel::closeComponentDetail,
      onToggleFavorite = { viewModel.toggleFavorite(selected) },
      onToggleCompare = { viewModel.toggleCompareItem(selected) }
    )
  }
}
