package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComponentItem
import com.example.ui.components.ComponentCard
import com.example.ui.components.HeroSearchSection
import com.example.ui.theme.PartfindrRed

@Composable
fun BrowseFeedScreen(
  searchQuery: String,
  onSearchQueryChanged: (String) -> Unit,
  selectedCategory: String,
  onCategorySelected: (String) -> Unit,
  components: List<ComponentItem>,
  compareList: List<ComponentItem>,
  onCardClick: (ComponentItem) -> Unit,
  onToggleFavorite: (ComponentItem) -> Unit,
  onToggleCompare: (ComponentItem) -> Unit,
  onDeleteComponent: (ComponentItem) -> Unit,
  onNavigateToPublish: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    // Top Hero Search Section
    item {
      HeroSearchSection(
        searchQuery = searchQuery,
        onSearchQueryChanged = onSearchQueryChanged,
        selectedCategory = selectedCategory,
        onCategorySelected = onCategorySelected
      )
    }

    // Results Header Count
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (searchQuery.isNotBlank() || selectedCategory != "All") {
            "Found ${components.size} match${if (components.size == 1) "" else "es"}"
          } else {
            "Catalog Articles (${components.size})"
          },
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )

        if (compareList.isNotEmpty()) {
          Text(
            text = "${compareList.size}/2 ready to compare",
            color = PartfindrRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    // Empty State matching CSS `.no-results-msg`
    if (components.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
              width = 1.dp,
              color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
              shape = RoundedCornerShape(8.dp)
            )
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.SearchOff,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "No components found matching your search.",
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface,
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "Try adding a new article or clearing your filter keywords.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            ElevatedButton(
              onClick = onNavigateToPublish,
              colors = ButtonDefaults.elevatedButtonColors(
                containerColor = PartfindrRed,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("empty_state_add_article_btn")
            ) {
              Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.size(4.dp))
              Text("Publish New Article")
            }
          }
        }
      }
    } else {
      // List of articles
      items(components, key = { it.id }) { item ->
        val isCompared = compareList.any { it.id == item.id }
        ComponentCard(
          item = item,
          onCardClick = { onCardClick(item) },
          onToggleFavorite = { onToggleFavorite(item) },
          onToggleCompare = { onToggleCompare(item) },
          isCompared = isCompared,
          onDelete = if (item.isUserCreated) { { onDeleteComponent(item) } } else null,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
      }
    }
  }
}
