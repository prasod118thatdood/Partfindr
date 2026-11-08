package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComponentItem
import com.example.ui.components.SpecsTableView
import com.example.ui.theme.PartfindrDarkRed
import com.example.ui.theme.PartfindrRed
import com.example.ui.theme.PartfindrRedBorderLight
import com.example.ui.theme.PartfindrRedContainerLight
import com.example.ui.theme.ZincDarkBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentDetailSheet(
  item: ComponentItem,
  isFavorite: Boolean,
  isCompared: Boolean,
  onDismiss: () -> Unit,
  onToggleFavorite: () -> Unit,
  onToggleCompare: () -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val context = LocalContext.current
  val specPairs = item.getSpecPairs()

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = modifier.testTag("component_detail_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp)
        .padding(bottom = 36.dp)
    ) {
      // Top header with close button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = item.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(4.dp))

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(PartfindrRedContainerLight)
              .border(1.dp, PartfindrRedBorderLight, RoundedCornerShape(4.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = item.categoryBadge,
              color = PartfindrDarkRed,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Row {
          IconButton(onClick = onToggleFavorite) {
            Icon(
              imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = "Save",
              tint = if (isFavorite) PartfindrRed else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Action buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (item.datasheetUrl.isNotBlank()) {
          ElevatedButton(
            onClick = {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.datasheetUrl))
              try { context.startActivity(intent) } catch (_: Exception) {}
            },
            colors = ButtonDefaults.elevatedButtonColors(
              containerColor = ZincDarkBg,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Datasheet", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }

        OutlinedButton(
          onClick = onToggleCompare,
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(
            imageVector = Icons.Default.CompareArrows,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = if (isCompared) PartfindrRed else MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isCompared) "In Compare" else "Add Compare",
            fontSize = 12.sp,
            color = if (isCompared) PartfindrRed else MaterialTheme.colorScheme.onSurface
          )
        }

        OutlinedButton(
          onClick = {
            val shareText = buildString {
              append("${item.title}\n")
              append("Category: ${item.categoryBadge}\n\n")
              append("${item.overview}\n\n")
              append("Specs:\n")
              specPairs.forEach { (k, v) -> append("• $k: $v\n") }
              if (item.datasheetUrl.isNotBlank()) {
                append("\nDatasheet: ${item.datasheetUrl}\n")
              }
              append("\npartfindr.net — FIND Data Systems")
            }
            val sendIntent = Intent().apply {
              action = Intent.ACTION_SEND
              putExtra(Intent.EXTRA_TEXT, shareText)
              type = "text/plain"
            }
            context.startActivity(Intent.createChooser(sendIntent, "Share Specs"))
          },
          shape = RoundedCornerShape(6.dp)
        ) {
          Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
        }
      }

      Spacer(modifier = Modifier.height(18.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "OVERVIEW & FUNCTION",
        fontWeight = FontWeight.Bold,
        fontSize = 12.5.sp,
        letterSpacing = 0.6.sp,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = item.overview,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 22.sp
      )

      Spacer(modifier = Modifier.height(18.dp))

      Text(
        text = "HARDWARE SPECIFICATIONS",
        fontWeight = FontWeight.Bold,
        fontSize = 12.5.sp,
        letterSpacing = 0.6.sp,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(8.dp))
      SpecsTableView(specPairs = specPairs)

      if (item.datasheetUrl.isNotBlank()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "MANUFACTURER DATASHEET URL",
          fontWeight = FontWeight.Bold,
          fontSize = 11.5.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = item.datasheetUrl,
          fontSize = 12.sp,
          color = PartfindrDarkRed
        )
      }
    }
  }
}
