package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComponentItem
import com.example.ui.components.ComponentCard
import com.example.ui.theme.PartfindrRed

@Composable
fun FavoritesScreen(
  favoriteItems: List<ComponentItem>,
  userCreatedItems: List<ComponentItem>,
  compareList: List<ComponentItem>,
  onCardClick: (ComponentItem) -> Unit,
  onToggleFavorite: (ComponentItem) -> Unit,
  onToggleCompare: (ComponentItem) -> Unit,
  onDeleteComponent: (ComponentItem) -> Unit,
  onNavigateToFeed: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTabFilter by remember { mutableStateOf("All Saved") }

  // Combine favoriteItems and userCreatedItems to guarantee every article added is always included in saved!
  val combinedSavedList = remember(favoriteItems, userCreatedItems, selectedTabFilter) {
    val allUnique = (favoriteItems + userCreatedItems).distinctBy { it.id }
    when (selectedTabFilter) {
      "My Articles" -> allUnique.filter { it.isUserCreated }
      "With Datasheets" -> allUnique.filter { it.datasheetUrl.isNotBlank() }
      else -> allUnique
    }
  }

  if (combinedSavedList.isEmpty()) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(24.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(
          imageVector = Icons.Default.BookmarkBorder,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = if (selectedTabFilter == "My Articles") "No user articles added yet" else "No saved hardware articles yet",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Every article you publish in the Publish tab is automatically saved here! You can also tap the bookmark icon on any catalog component.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        ElevatedButton(
          onClick = onNavigateToFeed,
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = PartfindrRed,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.testTag("saved_explore_feed_btn")
        ) {
          Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.size(4.dp))
          Text("Browse Catalog Feed")
        }
      }
    }
  } else {
    LazyColumn(
      modifier = modifier.fillMaxSize(),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
      item {
        Column(modifier = Modifier.padding(bottom = 12.dp)) {
          Text(
            text = "Saved Articles (${combinedSavedList.size})",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Every article you add is saved permanently and accessible here offline",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Filter chips
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            val filters = listOf("All Saved", "My Articles", "With Datasheets")
            filters.forEach { filter ->
              val isSelected = selectedTabFilter == filter
              FilterChip(
                selected = isSelected,
                onClick = { selectedTabFilter = filter },
                label = { Text(filter, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PartfindrRed,
                  selectedLabelColor = Color.White
                )
              )
            }
          }
        }
      }

      items(combinedSavedList, key = { it.id }) { item ->
        val isCompared = compareList.any { it.id == item.id }
        ComponentCard(
          item = item,
          onCardClick = { onCardClick(item) },
          onToggleFavorite = { onToggleFavorite(item) },
          onToggleCompare = { onToggleCompare(item) },
          isCompared = isCompared,
          onDelete = if (item.isUserCreated) { { onDeleteComponent(item) } } else null,
          modifier = Modifier.padding(vertical = 8.dp)
        )
      }

      item {
        Spacer(modifier = Modifier.height(60.dp))
      }
    }
  }
}
