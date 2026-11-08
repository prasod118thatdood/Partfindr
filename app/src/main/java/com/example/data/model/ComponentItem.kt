package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "components")
data class ComponentItem(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val categoryBadge: String,
  val overview: String,
  val specsRaw: String,
  val datasheetUrl: String = "",
  val isFavorite: Boolean = false,
  val timestamp: Long = System.currentTimeMillis(),
  val isUserCreated: Boolean = false
) {
  /**
   * Parses the specs string into a structured list of Key to Value pairs.
   * Handles formats like "Key: Value, Key2: Value2" and line breaks.
   */
  fun getSpecPairs(): List<Pair<String, String>> {
    if (specsRaw.isBlank()) return emptyList()

    // Support both comma-separated and newline-separated pairs
    val delimiters = if (specsRaw.contains("\n")) arrayOf("\n") else arrayOf(",")
    val rawTokens = specsRaw.split(*delimiters)

    val pairs = mutableListOf<Pair<String, String>>()
    for (token in rawTokens) {
      val trimmed = token.trim()
      if (trimmed.isEmpty()) continue
      val colonIndex = trimmed.indexOf(':')
      if (colonIndex != -1 && colonIndex < trimmed.length - 1) {
        val key = trimmed.substring(0, colonIndex).trim()
        val value = trimmed.substring(colonIndex + 1).trim()
        pairs.add(key to value)
      } else {
        pairs.add("Detail" to trimmed)
      }
    }
    return pairs
  }

  fun matchesQuery(query: String): Boolean {
    if (query.isBlank()) return true
    val clean = query.trim().lowercase()
    return title.lowercase().contains(clean) ||
      categoryBadge.lowercase().contains(clean) ||
      overview.lowercase().contains(clean) ||
      specsRaw.lowercase().contains(clean)
  }
}
