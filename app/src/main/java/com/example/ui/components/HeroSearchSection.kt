package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PartfindrRed
import com.example.ui.theme.ZincDarkBg
import com.example.ui.theme.ZincSurfaceDark

@Composable
fun HeroSearchSection(
  searchQuery: String,
  onSearchQueryChanged: (String) -> Unit,
  selectedCategory: String,
  onCategorySelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val focusManager = LocalFocusManager.current
  val categories = listOf("All", "My Articles", "CPU", "SoC", "GPU", "Storage", "IC", "Mobile")

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(
        brush = Brush.linearGradient(
          colors = listOf(ZincDarkBg, ZincSurfaceDark)
        )
      )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Find Specs, Datasheets & Hardware Info",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Search any laptop, smartphone, IC, or hardware component.",
        color = Color(0xFFA1A1AA),
        fontSize = 13.sp,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Live Search Box matching CSS `.search-box input`
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        placeholder = {
          Text(
            text = "e.g., Ryzen 9 9800X3D, Intel i7, Apple M2...",
            fontSize = 13.5.sp,
            color = Color(0xFF71717A)
          )
        },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = PartfindrRed,
            modifier = Modifier.size(20.dp)
          )
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(
              onClick = { onSearchQueryChanged("") },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear search",
                tint = Color(0xFFA1A1AA),
                modifier = Modifier.size(18.dp)
              )
            }
          }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedTextColor = Color(0xFF18181B),
          unfocusedTextColor = Color(0xFF18181B),
          focusedBorderColor = PartfindrRed,
          unfocusedBorderColor = Color(0xFFE4E4E7)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("hardware_search_input")
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Horizontal Category Filter Chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
      ) {
        categories.forEach { category ->
          val isSelected = selectedCategory == category
          FilterChip(
            selected = isSelected,
            onClick = { onCategorySelected(category) },
            label = {
              Text(
                text = category,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = PartfindrRed,
              selectedLabelColor = Color.White,
              containerColor = Color(0xFF27272A),
              labelColor = Color(0xFFD4D4D8)
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = isSelected,
              borderColor = if (isSelected) PartfindrRed else Color(0xFF3F3F46)
            ),
            modifier = Modifier
              .padding(end = 8.dp)
              .testTag("chip_filter_$category")
          )
        }
      }
    }

    // Bottom Red Accent Stripe matching CSS `border-bottom: 4px solid var(--primary-red);`
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(4.dp)
        .background(PartfindrRed)
    )
  }
}
