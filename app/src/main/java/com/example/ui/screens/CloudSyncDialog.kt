package com.example.ui.screens

import android.app.Activity
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.GoogleAuthHelper
import com.example.ui.theme.PartfindrDarkRed
import com.example.ui.theme.PartfindrRed
import com.example.ui.theme.ZincDarkBg
import com.google.firebase.auth.FirebaseUser

@Composable
fun CloudSyncDialog(
  currentUser: FirebaseUser?,
  onDismiss: () -> Unit,
  onSyncTriggered: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  var isLoading by remember { mutableStateOf(false) }
  var statusMessage by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = if (currentUser != null) Icons.Default.CloudDone else Icons.Default.CloudSync,
          contentDescription = null,
          tint = if (currentUser != null) Color(0xFF16A34A) else PartfindrRed,
          modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = if (currentUser != null) "Cloud Backup Active" else "Cloud Backup & Sync",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      }
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        if (currentUser != null) {
          // Signed-in view
          Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            modifier = Modifier
              .fillMaxWidth()
              .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(8.dp))
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = Color(0xFF15803D),
                modifier = Modifier.size(36.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = currentUser.displayName ?: "Google User",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Color(0xFF14532D)
                )
                Text(
                  text = currentUser.email ?: "Signed in with Google",
                  fontSize = 12.sp,
                  color = Color(0xFF15803D)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "✓ Every article and saved hardware spec you add is automatically synced to Cloud Firestore. If you uninstall and reinstall the app, simply open it to restore all your data.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedButton(
            onClick = {
              onSyncTriggered()
              statusMessage = "Cloud sync completed successfully!"
            },
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth().testTag("dialog_sync_now_btn")
          ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Sync Now with Cloud", fontSize = 13.sp)
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedButton(
            onClick = {
              GoogleAuthHelper.signOut(context, coroutineScope) {
                statusMessage = "Signed out"
              }
            },
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth().testTag("dialog_sign_out_btn")
          ) {
            Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Sign Out", fontSize = 13.sp)
          }
        } else {
          // Unauthenticated view
          Text(
            text = "To guarantee that all your published articles and saved specs are preserved when you uninstall or switch devices, connect your Google Account with 1 tap.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 21.sp
          )

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = {
              val activity = context as? Activity
              if (activity != null) {
                isLoading = true
                GoogleAuthHelper.signInWithGoogle(
                  activity = activity,
                  onAuthSuccess = {
                    isLoading = false
                    statusMessage = "Connected to Google Cloud Backup!"
                    onSyncTriggered()
                  },
                  onAuthError = { err ->
                    isLoading = false
                    statusMessage = "Error: $err"
                  },
                  scope = coroutineScope,
                  onAuthCancelled = {
                    isLoading = false
                  }
                )
              }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
              containerColor = ZincDarkBg,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("dialog_google_sign_in_btn")
          ) {
            if (isLoading) {
              CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
              )
            } else {
              Icon(Icons.Default.CloudSync, contentDescription = null, tint = PartfindrRed)
              Spacer(modifier = Modifier.width(8.dp))
              Text("Sign in with Google", fontWeight = FontWeight.Bold)
            }
          }
        }

        if (statusMessage != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = statusMessage!!,
            fontSize = 12.sp,
            color = PartfindrDarkRed,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Close", fontWeight = FontWeight.Bold)
      }
    }
  )
}
