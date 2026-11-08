package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = PartfindrLightRed,
  onPrimary = Color.White,
  primaryContainer = PartfindrRedContainerDark,
  onPrimaryContainer = PartfindrRedOnContainerDark,
  secondary = PartfindrRed,
  onSecondary = Color.White,
  background = ZincDarkerBg,
  surface = ZincDarkBg,
  surfaceVariant = ZincSurfaceDark,
  onBackground = ZincTextMainDark,
  onSurface = ZincTextMainDark,
  onSurfaceVariant = ZincTextSubDark,
  outline = ZincSurfaceVariantDark,
  outlineVariant = Color(0xFF27272A)
)

private val LightColorScheme = lightColorScheme(
  primary = PartfindrRed,
  onPrimary = Color.White,
  primaryContainer = PartfindrRedContainerLight,
  onPrimaryContainer = PartfindrDarkRed,
  secondary = PartfindrDarkRed,
  onSecondary = Color.White,
  background = ZincLightBg,
  surface = ZincSurfaceLight,
  surfaceVariant = Color(0xFFFAFAFA),
  onBackground = ZincTextMainLight,
  onSurface = ZincTextMainLight,
  onSurfaceVariant = ZincTextSubLight,
  outline = ZincBorderLight,
  outlineVariant = PartfindrRedBorderLight
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Keep Partfindr brand identity consistent
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
