package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PartfindrDarkRed
import com.example.ui.theme.PartfindrRed
import com.example.ui.theme.PartfindrRedBorderLight
import com.example.ui.theme.PartfindrRedContainerLight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PublishArticleScreen(
  title: String,
  onTitleChange: (String) -> Unit,
  category: String,
  onCategoryChange: (String) -> Unit,
  overview: String,
  onOverviewChange: (String) -> Unit,
  specs: String,
  onSpecsChange: (String) -> Unit,
  datasheet: String,
  onDatasheetChange: (String) -> Unit,
  errorMessage: String?,
  onClearDraft: () -> Unit,
  onSubmit: () -> Unit,
  modifier: Modifier = Modifier
) {
  val quickCategories = listOf(
    "CPU / Desktop Processor",
    "GPU / Graphics Card",
    "SoC / Mobile Processor",
    "SoC / Custom Silicon",
    "IC / Microcontroller",
    "Storage / NVMe SSD",
    "Smartphone / Device"
  )

  val hasDraftData = title.isNotBlank() || category.isNotBlank() || overview.isNotBlank() || specs.isNotBlank()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
      .padding(bottom = 60.dp)
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(8.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        // Red Top Accent Bar
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(PartfindrRed)
        )

        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Publish New Component Article",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = "Add your own technical breakdown or device overview to the live feed.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            if (hasDraftData) {
              TextButton(
                onClick = onClearDraft,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
              ) {
                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(2.dp))
                Text("Clear", fontSize = 12.sp)
              }
            }
          }

          // Auto-save persistent notice
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFFF0FDF4))
              .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(6.dp))
              .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = Color(0xFF16A34A),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Auto-save active: Drafts and published articles are saved permanently.",
              color = Color(0xFF15803D),
              fontSize = 11.5.sp,
              fontWeight = FontWeight.Medium
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          if (errorMessage != null) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(PartfindrRedContainerLight)
                .border(1.dp, PartfindrRedBorderLight, RoundedCornerShape(6.dp))
                .padding(12.dp)
            ) {
              Text(
                text = errorMessage,
                color = PartfindrDarkRed,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
              )
            }
            Spacer(modifier = Modifier.height(14.dp))
          }

          // 1. Title Input
          Text(
            text = "Component / Device Name",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = { Text("e.g., NVIDIA GeForce RTX 5090") },
            leadingIcon = {
              Icon(Icons.Default.DeviceHub, contentDescription = null, tint = PartfindrRed)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              capitalization = KeyboardCapitalization.Words,
              imeAction = ImeAction.Next
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("publish_input_title")
          )

          Spacer(modifier = Modifier.height(14.dp))

          // 2. Category Input
          Text(
            text = "Category Badge",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(
            value = category,
            onValueChange = onCategoryChange,
            placeholder = { Text("e.g., GPU / Graphics Card") },
            leadingIcon = {
              Icon(Icons.Default.Category, contentDescription = null, tint = PartfindrRed)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              capitalization = KeyboardCapitalization.Words,
              imeAction = ImeAction.Next
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("publish_input_category")
          )

          Spacer(modifier = Modifier.height(6.dp))
          // Quick category chips
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            quickCategories.forEach { tag ->
              AssistChip(
                onClick = { onCategoryChange(tag) },
                label = { Text(text = tag, fontSize = 11.sp) },
                colors = AssistChipDefaults.assistChipColors(
                  containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 3. Overview & Function
          Text(
            text = "Overview & Function",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(
            value = overview,
            onValueChange = onOverviewChange,
            placeholder = { Text("Describe what it does, architecture, and main target application...") },
            leadingIcon = {
              Icon(Icons.Default.Info, contentDescription = null, tint = PartfindrRed)
            },
            minLines = 3,
            maxLines = 6,
            keyboardOptions = KeyboardOptions(
              capitalization = KeyboardCapitalization.Sentences,
              imeAction = ImeAction.Next
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("publish_input_overview")
          )

          Spacer(modifier = Modifier.height(14.dp))

          // 4. Specifications
          Text(
            text = "Key Specifications",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Format as Key: Value separated by commas or lines",
            fontSize = 11.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(
            value = specs,
            onValueChange = onSpecsChange,
            placeholder = { Text("e.g., Architecture: Blackwell, VRAM: 32GB GDDR7, TDP: 600W, Interface: PCIe 5.0") },
            leadingIcon = {
              Icon(Icons.Default.Description, contentDescription = null, tint = PartfindrRed)
            },
            minLines = 2,
            maxLines = 4,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("publish_input_specs")
          )

          Spacer(modifier = Modifier.height(14.dp))

          // 5. Datasheet Link
          Text(
            text = "Datasheet Link (URL)",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(
            value = datasheet,
            onValueChange = onDatasheetChange,
            placeholder = { Text("https://example.com/datasheet.pdf") },
            leadingIcon = {
              Icon(Icons.Default.Link, contentDescription = null, tint = PartfindrRed)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Uri,
              imeAction = ImeAction.Done
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("publish_input_datasheet")
          )

          Spacer(modifier = Modifier.height(24.dp))

          // Submit & Save Button matching `.btn-submit`
          Button(
            onClick = onSubmit,
            colors = ButtonDefaults.buttonColors(
              containerColor = PartfindrRed,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("publish_submit_button")
          ) {
            Icon(Icons.Default.Publish, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Publish & Save Article",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.BookmarkAdded,
              contentDescription = null,
              tint = PartfindrDarkRed,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Every article you add is automatically saved to your Saved list.",
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 12.sp,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  }
}
