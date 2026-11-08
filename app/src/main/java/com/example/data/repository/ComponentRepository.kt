package com.example.data.repository

import com.example.data.local.ComponentDao
import com.example.data.local.InitialData
import com.example.data.local.UserArticleBackupStorage
import com.example.data.model.ComponentItem
import com.example.data.remote.FirestoreArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class ComponentRepository(
  private val dao: ComponentDao,
  private val backupStorage: UserArticleBackupStorage,
  private val firestoreRepo: FirestoreArticleRepository
) {
  val allComponents: Flow<List<ComponentItem>> = dao.getAllComponents()
  val favoriteComponents: Flow<List<ComponentItem>> = dao.getFavoriteComponents()
  val userCreatedComponents: Flow<List<ComponentItem>> = dao.getUserCreatedComponents()

  suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
    if (dao.getCount() == 0) {
      dao.insertAll(InitialData.getPresetComponents())
    }

    // 1. Restore local backup articles
    val backedUp = backupStorage.getBackedUpArticles()
    if (backedUp.isNotEmpty()) {
      for (userArticle in backedUp) {
        dao.insert(userArticle)
      }
    }

    // 2. Sync from cloud if user is signed in
    syncCloudArticles()
  }

  suspend fun syncCloudArticles() = withContext(Dispatchers.IO) {
    try {
      if (firestoreRepo.currentUserId() != null) {
        val cloudArticles = firestoreRepo.observeCloudArticles().firstOrNull() ?: emptyList()
        for (article in cloudArticles) {
          dao.insert(article)
          backupStorage.backupArticle(article)
        }

        // Also upload any local user-created articles that haven't synced yet
        val localUserArticles = backupStorage.getBackedUpArticles()
        for (local in localUserArticles) {
          firestoreRepo.saveArticleToCloud(local)
        }
      }
    } catch (_: Exception) {}
  }

  suspend fun insertArticle(item: ComponentItem): Long = withContext(Dispatchers.IO) {
    val id = dao.insert(item)
    val savedItem = item.copy(id = id)
    if (item.isUserCreated) {
      backupStorage.backupArticle(savedItem)
      firestoreRepo.saveArticleToCloud(savedItem)
    }
    id
  }

  suspend fun updateArticle(item: ComponentItem) = withContext(Dispatchers.IO) {
    dao.update(item)
    if (item.isUserCreated) {
      backupStorage.backupArticle(item)
      firestoreRepo.saveArticleToCloud(item)
    }
  }

  suspend fun deleteArticle(item: ComponentItem) = withContext(Dispatchers.IO) {
    dao.delete(item)
    if (item.isUserCreated) {
      backupStorage.removeArticle(item)
      firestoreRepo.deleteArticleFromCloud(item)
    }
  }

  suspend fun toggleFavorite(item: ComponentItem) = withContext(Dispatchers.IO) {
    val newFav = !item.isFavorite
    dao.setFavorite(item.id, newFav)
    if (item.isUserCreated) {
      val updated = item.copy(isFavorite = newFav)
      backupStorage.backupArticle(updated)
      firestoreRepo.saveArticleToCloud(updated)
    }
  }

  fun getBackupStorage(): UserArticleBackupStorage = backupStorage
  fun getFirestoreRepo(): FirestoreArticleRepository = firestoreRepo
}
