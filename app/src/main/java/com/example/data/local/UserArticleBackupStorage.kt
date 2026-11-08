package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.ComponentItem
import org.json.JSONArray
import org.json.JSONObject

class UserArticleBackupStorage(context: Context) {
  private val prefs: SharedPreferences =
    context.applicationContext.getSharedPreferences("partfindr_user_articles_backup", Context.MODE_PRIVATE)

  companion object {
    private const val KEY_SAVED_ARTICLES = "saved_user_articles_json"
    private const val KEY_DRAFT_TITLE = "draft_title"
    private const val KEY_DRAFT_CATEGORY = "draft_category"
    private const val KEY_DRAFT_OVERVIEW = "draft_overview"
    private const val KEY_DRAFT_SPECS = "draft_specs"
    private const val KEY_DRAFT_DATASHEET = "draft_datasheet"
  }

  fun backupArticle(item: ComponentItem) {
    val existing = getBackedUpArticles().toMutableList()
    // Remove if already exists with same title to avoid duplicates
    existing.removeAll { it.title.equals(item.title, ignoreCase = true) }
    existing.add(item)
    saveArticles(existing)
  }

  fun removeArticle(item: ComponentItem) {
    val existing = getBackedUpArticles().toMutableList()
    existing.removeAll { it.title.equals(item.title, ignoreCase = true) }
    saveArticles(existing)
  }

  fun getBackedUpArticles(): List<ComponentItem> {
    val rawJson = prefs.getString(KEY_SAVED_ARTICLES, null) ?: return emptyList()
    return try {
      val array = JSONArray(rawJson)
      val list = mutableListOf<ComponentItem>()
      for (i in 0 until array.length()) {
        val obj = array.getJSONObject(i)
        list.add(
          ComponentItem(
            id = 0,
            title = obj.optString("title"),
            categoryBadge = obj.optString("categoryBadge"),
            overview = obj.optString("overview"),
            specsRaw = obj.optString("specsRaw"),
            datasheetUrl = obj.optString("datasheetUrl"),
            isFavorite = true, // User added articles are always saved!
            timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
            isUserCreated = true
          )
        )
      }
      list
    } catch (_: Exception) {
      emptyList()
    }
  }

  private fun saveArticles(articles: List<ComponentItem>) {
    try {
      val array = JSONArray()
      for (item in articles) {
        val obj = JSONObject().apply {
          put("title", item.title)
          put("categoryBadge", item.categoryBadge)
          put("overview", item.overview)
          put("specsRaw", item.specsRaw)
          put("datasheetUrl", item.datasheetUrl)
          put("timestamp", item.timestamp)
        }
        array.put(obj)
      }
      prefs.edit().putString(KEY_SAVED_ARTICLES, array.toString()).apply()
    } catch (_: Exception) {}
  }

  // Draft Auto-saving methods
  fun saveDraft(title: String, category: String, overview: String, specs: String, datasheet: String) {
    prefs.edit()
      .putString(KEY_DRAFT_TITLE, title)
      .putString(KEY_DRAFT_CATEGORY, category)
      .putString(KEY_DRAFT_OVERVIEW, overview)
      .putString(KEY_DRAFT_SPECS, specs)
      .putString(KEY_DRAFT_DATASHEET, datasheet)
      .apply()
  }

  fun loadDraft(): DraftData {
    return DraftData(
      title = prefs.getString(KEY_DRAFT_TITLE, "") ?: "",
      category = prefs.getString(KEY_DRAFT_CATEGORY, "") ?: "",
      overview = prefs.getString(KEY_DRAFT_OVERVIEW, "") ?: "",
      specs = prefs.getString(KEY_DRAFT_SPECS, "") ?: "",
      datasheet = prefs.getString(KEY_DRAFT_DATASHEET, "") ?: ""
    )
  }

  fun clearDraft() {
    prefs.edit()
      .remove(KEY_DRAFT_TITLE)
      .remove(KEY_DRAFT_CATEGORY)
      .remove(KEY_DRAFT_OVERVIEW)
      .remove(KEY_DRAFT_SPECS)
      .remove(KEY_DRAFT_DATASHEET)
      .apply()
  }

  data class DraftData(
    val title: String,
    val category: String,
    val overview: String,
    val specs: String,
    val datasheet: String
  )
}
