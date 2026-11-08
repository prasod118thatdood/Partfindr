package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.PartfindrDarkRed
import com.example.ui.theme.PartfindrRed
import com.example.ui.theme.PartfindrRedBorderLight
import com.example.ui.theme.PartfindrRedContainerLight

@Composable
fun CompareScreen(
  compareList: List<ComponentItem>,
  allComponents: List<ComponentItem>,
  onRemoveFromCompare: (ComponentItem) -> Unit,
  onClearCompare: () -> Unit,
  onAddToCompare: (ComponentItem) -> Unit,
  onNavigateToFeed: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
      .padding(bottom = 60.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Component Spec Comparison",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Compare hardware specifications side by side",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      if (compareList.isNotEmpty()) {
        IconButton(
          onClick = onClearCompare,
          modifier = Modifier.testTag("clear_compare_btn")
        ) {
          Icon(Icons.Default.Clear, contentDescription = "Clear comparison", tint = PartfindrRed)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (compareList.size < 2) {
      // Prompt card to select components
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            imageVector = Icons.Default.CompareArrows,
            contentDescription = null,
            tint = PartfindrRed,
            modifier = Modifier.size(44.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = if (compareList.isEmpty()) {
              "Select 2 components to compare"
            } else {
              "1 selected: ${compareList.first().title}"
            },
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = if (compareList.isEmpty()) {
              "Tap 'Compare' on any two cards in the Feed to analyze architecture, clocks, cores, and TDP side-by-side."
            } else {
              "Choose one more component below to start the side-by-side comparison matrix."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Quick pick chips from available components
          Text(
            text = "Tap to add to comparison:",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(8.dp))

          allComponents.filter { item -> compareList.none { it.id == item.id } }.take(5).forEach { candidate ->
            OutlinedButton(
              onClick = { onAddToCompare(candidate) },
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
            ) {
              Icon(Icons.Default.PlaylistAdd, contentDescription = null, modifier = Modifier.size(16.dp), tint = PartfindrRed)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "${candidate.title} (${candidate.categoryBadge})",
                fontSize = 12.sp,
                maxLines = 1
              )
            }
          }
        }
      }
    } else {
      // Side by Side Table
      val comp1 = compareList[0]
      val comp2 = compareList[1]
      val specs1 = comp1.getSpecPairs().toMap()
      val specs2 = comp2.getSpecPairs().toMap()

      // Union of all spec keys
      val allKeys = (comp1.getSpecPairs().map { it.first } + comp2.getSpecPairs().map { it.first }).distinct()

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(4.dp)
              .background(PartfindrRed)
          )

          // Titles Header Row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = comp1.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Box(
                modifier = Modifier
                  .padding(top = 4.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(PartfindrRedContainerLight)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(comp1.categoryBadge, fontSize = 10.sp, color = PartfindrDarkRed, fontWeight = FontWeight.Bold)
              }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = comp2.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Box(
                modifier = Modifier
                  .padding(top = 4.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(PartfindrRedContainerLight)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(comp2.categoryBadge, fontSize = 10.sp, color = PartfindrDarkRed, fontWeight = FontWeight.Bold)
              }
            }
          }

          HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

          // Spec Rows
          allKeys.forEachIndexed { index, key ->
            val val1 = specs1[key] ?: "—"
            val val2 = specs2[key] ?: "—"
            val isDiff = val1 != val2 && val1 != "—" && val2 != "—"

            Column(
              modifier = Modifier
                .fillMaxWidth()
                .background(
                  if (index % 2 == 0) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                  else MaterialTheme.colorScheme.surface
                )
            ) {
              // Row Label
              Text(
                text = key,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.5.sp,
                color = PartfindrDarkRed,
                modifier = Modifier.padding(start = 12.dp, top = 8.dp)
              )

              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = val1,
                  fontSize = 13.sp,
                  fontWeight = if (isDiff) FontWeight.SemiBold else FontWeight.Normal,
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                  text = val2,
                  fontSize = 13.sp,
                  fontWeight = if (isDiff) FontWeight.SemiBold else FontWeight.Normal,
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.weight(1f)
                )
              }

              HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            }
          }

          // Overview Comparison
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "OVERVIEW SUMMARY",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
              Text(
                text = comp1.overview,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = comp2.overview,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }
    }
  }
}
