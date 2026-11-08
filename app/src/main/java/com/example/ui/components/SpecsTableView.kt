package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpecsTableView(
  specPairs: List<Pair<String, String>>,
  modifier: Modifier = Modifier
) {
  if (specPairs.isEmpty()) {
    Text(
      text = "No detailed specifications listed.",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = modifier.padding(vertical = 4.dp)
    )
    return
  }

  val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
  val keyHeaderBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(6.dp))
      .border(1.dp, borderColor, RoundedCornerShape(6.dp))
  ) {
    specPairs.forEachIndexed { index, (key, value) ->
      Row(
        modifier = Modifier
          .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Spec Key Column (38% width)
        Box(
          modifier = Modifier
            .weight(0.38f)
            .background(keyHeaderBg)
            .padding(horizontal = 12.dp, vertical = 9.dp)
        ) {
          Text(
            text = key,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Spec Value Column (62% width)
        Box(
          modifier = Modifier
            .weight(0.62f)
            .padding(horizontal = 12.dp, vertical = 9.dp)
        ) {
          Text(
            text = value,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      if (index < specPairs.lastIndex) {
        HorizontalDivider(color = borderColor, thickness = 1.dp)
      }
    }
  }
}
