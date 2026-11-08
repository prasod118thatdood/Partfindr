package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PartfindrDarkRed
import com.example.ui.theme.PartfindrRed
import com.example.ui.theme.ZincDarkBg
import com.google.firebase.auth.FirebaseUser

@Composable
fun AboutScreen(
  totalCount: Int,
  favoritesCount: Int,
  currentUser: FirebaseUser?,
  onManageCloudSync: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
      .padding(bottom = 60.dp)
  ) {
    // Brand Banner Card with `banner_partfindr_full.jpg`
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(8.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(PartfindrRed)
        )

        Image(
          painter = painterResource(id = R.drawable.banner_partfindr_full),
          contentDescription = "Partfindr banner - The Wikipedia of Components and Devices",
          contentScale = ContentScale.FillWidth,
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1638f / 338f)
            .padding(8.dp)
            .clip(RoundedCornerShape(6.dp))
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Cloud Persistence & Uninstall Protection Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(8.dp),
      colors = CardDefaults.cardColors(
        containerColor = if (currentUser != null) Color(0xFFF0FDF4) else MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = if (currentUser != null) Icons.Default.CloudDone else Icons.Default.CloudSync,
            contentDescription = null,
            tint = if (currentUser != null) Color(0xFF16A34A) else PartfindrRed,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "CLOUD BACKUP & UNINSTALL RECOVERY",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            letterSpacing = 0.6.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (currentUser != null) {
          Text(
            text = "Connected to Google Cloud: ${currentUser.email}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.5.sp,
            color = Color(0xFF15803D)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Your articles and bookmarked datasheets are backed up to Cloud Firestore. If you uninstall and reinstall the app, all your articles are automatically restored.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
          )
        } else {
          Text(
            text = "Backup not connected to Google yet",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.5.sp,
            color = PartfindrDarkRed
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Connect Google Cloud Backup to ensure all the articles you add survive when you uninstall and reinstall the app.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
          onClick = onManageCloudSync,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (currentUser != null) Color(0xFF15803D) else ZincDarkBg,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.fillMaxWidth().testTag("about_manage_cloud_sync_btn")
        ) {
          Icon(
            imageVector = if (currentUser != null) Icons.Default.SettingsBackupRestore else Icons.Default.CloudSync,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (currentUser != null) "Manage Cloud Backup" else "Sign in with Google to Enable Backup",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Overview Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(8.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Image(
            painter = painterResource(id = R.drawable.ic_partfindr_logo),
            contentDescription = null,
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(6.dp))
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "partfindr.net",
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "BY FIND DATA SYSTEMS",
              color = PartfindrRed,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              letterSpacing = 1.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "The Wikipedia of Components and Devices",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Partfindr is a comprehensive, open hardware encyclopedia curated for computer engineers, electronics hobbyists, and device builders. Easily look up microarchitectures, core topologies, TDP ratings, clock speeds, and direct PDF manufacturer datasheets.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

        Spacer(modifier = Modifier.height(14.dp))

        // Live Applet Stats
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.DeviceHub, contentDescription = null, tint = PartfindrRed)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "$totalCount",
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(text = "Catalog Items", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Bookmark, contentDescription = null, tint = PartfindrRed)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "$favoritesCount",
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(text = "Saved Specs", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Category, contentDescription = null, tint = PartfindrRed)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "7",
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(text = "Categories", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Community & Web Links Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(8.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = "COMMUNITY & RESOURCES",
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          letterSpacing = 0.6.sp,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
          onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://partfindr.net"))
            try { context.startActivity(intent) } catch (_: Exception) {}
          },
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp), tint = PartfindrRed)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Visit partfindr.net Web Portal", fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        ElevatedButton(
          onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg"))
            try { context.startActivity(intent) } catch (_: Exception) {}
          },
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = ZincDarkBg,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp), tint = PartfindrRed)
          Spacer(modifier = Modifier.width(8.dp))
          Text("FIND Data Systems Community", fontSize = 13.sp)
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Footer matching website footer
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(6.dp))
        .background(ZincDarkBg)
        .padding(16.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "© 2026 partfindr.net — A product of FIND Data Systems. All rights reserved.",
        color = Color(0xFFA1A1AA),
        fontSize = 11.5.sp,
        textAlign = TextAlign.Center
      )
    }
  }
}
