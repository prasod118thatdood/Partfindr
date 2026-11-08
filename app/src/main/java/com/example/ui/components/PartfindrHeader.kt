package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PartfindrRed
import com.example.ui.theme.ZincDarkBg
import com.google.firebase.auth.FirebaseUser

@Composable
fun PartfindrHeader(
  currentUser: FirebaseUser?,
  onAccountClicked: () -> Unit,
  onAddArticleClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(ZincDarkBg)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f, fill = false)
      ) {
        // Brand circular icon
        Image(
          painter = painterResource(id = R.drawable.ic_partfindr_logo),
          contentDescription = "Partfindr Logo",
          modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .testTag("app_brand_logo")
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Text(
            text = "partfindr.net",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            letterSpacing = (-0.5).sp
          )
          Text(
            text = "BY FIND DATA SYSTEMS",
            color = PartfindrRed,
            fontWeight = FontWeight.SemiBold,
            fontSize = 9.5.sp,
            letterSpacing = 1.sp
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        // Cloud Sync / Account Status Icon
        IconButton(
          onClick = onAccountClicked,
          modifier = Modifier.size(36.dp).testTag("header_cloud_sync_btn")
        ) {
          if (currentUser != null) {
            Icon(
              imageVector = Icons.Default.CloudDone,
              contentDescription = "Cloud Sync Active",
              tint = Color(0xFF4ADE80),
              modifier = Modifier.size(22.dp)
            )
          } else {
            Icon(
              imageVector = Icons.Default.CloudSync,
              contentDescription = "Connect Google Cloud Backup",
              tint = Color(0xFFFBBF24),
              modifier = Modifier.size(22.dp)
            )
          }
        }

        Spacer(modifier = Modifier.width(6.dp))

        ElevatedButton(
          onClick = onAddArticleClicked,
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = PartfindrRed,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(6.dp),
          contentPadding = ButtonDefaults.ContentPadding,
          modifier = Modifier.testTag("header_add_article_button")
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Add",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }

    // Prominent Red Bottom Border matching CSS `border-bottom: 3px solid var(--primary-red);`
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(3.dp)
        .background(PartfindrRed)
    )
  }
}
