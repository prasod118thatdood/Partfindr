package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.InitialData
import com.example.data.local.UserArticleBackupStorage
import com.example.data.model.ComponentItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Partfindr", appName)
  }

  @Test
  fun `component item specs parsing correctly splits key values`() {
    val item = ComponentItem(
      title = "AMD Ryzen 9 9800X3D Processor",
      categoryBadge = "CPU / Desktop Processor",
      overview = "Zen 5 gaming CPU",
      specsRaw = "Architecture: Zen 5, Cores / Threads: 8 Cores / 16 Threads, TDP: 120 W"
    )
    val pairs = item.getSpecPairs()
    assertEquals(3, pairs.size)
    assertEquals("Architecture", pairs[0].first)
    assertEquals("Zen 5", pairs[0].second)
    assertEquals("Cores / Threads", pairs[1].first)
    assertEquals("8 Cores / 16 Threads", pairs[1].second)
    assertEquals("TDP", pairs[2].first)
    assertEquals("120 W", pairs[2].second)
  }

  @Test
  fun `component search query matches title, category, and specs`() {
    val item = InitialData.getPresetComponents().first { it.title.contains("9800X3D") }
    assertTrue(item.matchesQuery("ryzen"))
    assertTrue(item.matchesQuery("9800X3D"))
    assertTrue(item.matchesQuery("Zen 5"))
    assertTrue(item.matchesQuery("CPU"))
  }

  @Test
  fun `user article backup storage saves and restores added articles`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val storage = UserArticleBackupStorage(context)
    val testArticle = ComponentItem(
      title = "NVIDIA RTX 5090 Custom Edition",
      categoryBadge = "GPU / Desktop Graphics",
      overview = "Flagship Blackwell GPU",
      specsRaw = "VRAM: 32GB GDDR7, Bus: 512-bit",
      datasheetUrl = "https://nvidia.com/rtx5090",
      isFavorite = true,
      isUserCreated = true
    )

    storage.backupArticle(testArticle)
    val restored = storage.getBackedUpArticles()
    assertEquals(1, restored.size)
    assertEquals("NVIDIA RTX 5090 Custom Edition", restored[0].title)
    assertTrue(restored[0].isFavorite)
    assertTrue(restored[0].isUserCreated)

    // Test draft auto-save
    storage.saveDraft("Intel Core Ultra 9", "CPU", "Arrow Lake", "Cores: 24", "")
    val draft = storage.loadDraft()
    assertEquals("Intel Core Ultra 9", draft.title)
    assertEquals("Arrow Lake", draft.overview)

    storage.clearDraft()
    val cleared = storage.loadDraft()
    assertEquals("", cleared.title)
  }
}
