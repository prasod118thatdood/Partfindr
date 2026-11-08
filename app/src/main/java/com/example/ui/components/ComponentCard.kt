package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.ui.theme.PartfindrDarkRed
import com.example.ui.theme.PartfindrRed
import com.example.ui.theme.PartfindrRedBorderLight
import com.example.ui.theme.PartfindrRedContainerLight
import com.example.ui.theme.ZincDarkBg

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ComponentCard(
  item: ComponentItem,
  onCardClick: () -> Unit,
  onToggleFavorite: () -> Unit,
  onToggleCompare: () -> Unit,
  isCompared: Boolean,
  onDelete: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val specPairs = item.getSpecPairs()

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onCardClick() }
      .testTag("component_card_${item.id}"),
    shape = RoundedCornerShape(8.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Bold Red Top Stripe matching CSS `.component-card { border-top: 4px solid var(--primary-red); }`
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(4.dp)
          .background(PartfindrRed)
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        // Component Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = item.title,
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              // Category Badge matching CSS badge `.badge`
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
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Bold
                )
              }

              if (item.isUserCreated) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFECFDF5))
                    .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                  Text(
                    text = "✓ Saved • Added by You",
                    color = Color(0xFF047857),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }

          // Bookmark action
          IconButton(
            onClick = onToggleFavorite,
            modifier = Modifier.testTag("favorite_btn_${item.id}")
          ) {
            Icon(
              imageVector = if (item.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = if (item.isFavorite) "Remove from saved" else "Save component",
              tint = if (item.isFavorite) PartfindrRed else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Toolbar (Datasheet, Compare, Share)
        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Download Datasheet Button matching `.btn-download`
          ElevatedButton(
            onClick = {
              if (item.datasheetUrl.isNotBlank()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.datasheetUrl))
                try {
                  context.startActivity(intent)
                } catch (_: Exception) {
                  onCardClick()
                }
              } else {
                onCardClick()
              }
            },
            colors = ButtonDefaults.elevatedButtonColors(
              containerColor = ZincDarkBg,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.testTag("datasheet_btn_${item.id}")
          ) {
            Icon(
              imageVector = Icons.Default.Description,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = Color.White
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "📄 Datasheet (PDF)",
              fontSize = 12.5.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          // Compare Toggle Button
          FilledTonalButton(
            onClick = onToggleCompare,
            colors = ButtonDefaults.filledTonalButtonColors(
              containerColor = if (isCompared) PartfindrRed else MaterialTheme.colorScheme.surfaceVariant,
              contentColor = if (isCompared) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.testTag("compare_btn_${item.id}")
          ) {
            Icon(
              imageVector = Icons.Default.CompareArrows,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isCompared) "Comparing" else "Compare",
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }

          // Share Button
          FilledTonalIconButton(
            onClick = {
              val shareText = buildString {
                append("${item.title}\n")
                append("Category: ${item.categoryBadge}\n\n")
                append("${item.overview}\n\n")
                append("Key Specifications:\n")
                specPairs.forEach { (k, v) ->
                  append("• $k: $v\n")
                }
                if (item.datasheetUrl.isNotBlank()) {
                  append("\nDatasheet: ${item.datasheetUrl}\n")
                }
                append("\nFound on partfindr.net — by FIND Data Systems")
              }
              val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_TITLE, item.title)
                type = "text/plain"
              }
              context.startActivity(Intent.createChooser(sendIntent, "Share Hardware Specs"))
            },
            colors = IconButtonDefaults.filledTonalIconButtonColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.size(40.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share specs",
              modifier = Modifier.size(18.dp)
            )
          }

          // Delete button for user-published articles
          if (item.isUserCreated && onDelete != null) {
            IconButton(
              onClick = onDelete,
              modifier = Modifier.size(40.dp)
            ) {
              Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "Delete article",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        Spacer(modifier = Modifier.height(12.dp))

        // Info Section 1: Overview & Function
        Text(
          text = "OVERVIEW & FUNCTION",
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          letterSpacing = 0.6.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = item.overview,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Info Section 2: Hardware Specifications Table
        Text(
          text = "HARDWARE SPECIFICATIONS",
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          letterSpacing = 0.6.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        SpecsTableView(specPairs = specPairs)
      }
    }
  }
}
