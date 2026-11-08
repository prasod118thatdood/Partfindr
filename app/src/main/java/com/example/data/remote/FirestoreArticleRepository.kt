package com.example.data.remote

import android.content.Context
import com.example.R
import com.example.data.model.ComponentItem
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreArticleRepository(private val context: Context) {
  private val db: FirebaseFirestore by lazy {
    FirebaseFirestore.getInstance(context.getString(R.string.firestore_database_id))
  }
  private val auth = Firebase.auth

  fun currentUserId(): String? = auth.currentUser?.uid

  fun observeCloudArticles(): Flow<List<ComponentItem>> = flow {
    val uid = auth.currentUser?.uid
    if (uid == null) {
      emit(emptyList())
      return@flow
    }

    db.collection("users").document(uid).collection("articles")
      .snapshots()
      .map { snapshot ->
        snapshot.documents.mapNotNull { doc ->
          val title = doc.getString("title") ?: return@mapNotNull null
          val category = doc.getString("categoryBadge") ?: "Hardware"
          val overview = doc.getString("overview") ?: ""
          val specs = doc.getString("specsRaw") ?: ""
          val datasheet = doc.getString("datasheetUrl") ?: ""
          val isFavorite = doc.getBoolean("isFavorite") ?: true
          val timestamp = doc.getTimestamp("createdAt")?.toDate()?.time ?: System.currentTimeMillis()

          ComponentItem(
            id = 0,
            title = title,
            categoryBadge = category,
            overview = overview,
            specsRaw = specs,
            datasheetUrl = datasheet,
            isFavorite = isFavorite,
            timestamp = timestamp,
            isUserCreated = true
          )
        }
      }
      .collect { emit(it) }
  }

  suspend fun saveArticleToCloud(item: ComponentItem): Boolean {
    val uid = auth.currentUser?.uid ?: return false
    val docRef = db.collection("users").document(uid).collection("articles").document(item.title.hashCode().toString())

    val payload = hashMapOf<String, Any>(
      "userId" to uid,
      "title" to item.title,
      "categoryBadge" to item.categoryBadge,
      "overview" to item.overview,
      "specsRaw" to item.specsRaw,
      "datasheetUrl" to item.datasheetUrl,
      "isFavorite" to item.isFavorite,
      "createdAt" to FieldValue.serverTimestamp(),
      "updatedAt" to FieldValue.serverTimestamp()
    )

    return try {
      docRef.set(payload).await()
      true
    } catch (_: Exception) {
      false
    }
  }

  suspend fun deleteArticleFromCloud(item: ComponentItem): Boolean {
    val uid = auth.currentUser?.uid ?: return false
    val docRef = db.collection("users").document(uid).collection("articles").document(item.title.hashCode().toString())
    return try {
      docRef.delete().await()
      true
    } catch (_: Exception) {
      false
    }
  }
}
