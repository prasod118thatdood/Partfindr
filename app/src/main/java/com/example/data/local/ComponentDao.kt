package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ComponentItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ComponentDao {
  @Query("SELECT * FROM components ORDER BY timestamp DESC")
  fun getAllComponents(): Flow<List<ComponentItem>>

  @Query("SELECT * FROM components WHERE isFavorite = 1 OR isUserCreated = 1 ORDER BY timestamp DESC")
  fun getFavoriteComponents(): Flow<List<ComponentItem>>

  @Query("SELECT * FROM components WHERE isUserCreated = 1 ORDER BY timestamp DESC")
  fun getUserCreatedComponents(): Flow<List<ComponentItem>>

  @Query("SELECT * FROM components WHERE id = :id LIMIT 1")
  suspend fun getComponentById(id: Long): ComponentItem?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: ComponentItem): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(items: List<ComponentItem>)

  @Update
  suspend fun update(item: ComponentItem)

  @Delete
  suspend fun delete(item: ComponentItem)

  @Query("UPDATE components SET isFavorite = :isFav WHERE id = :id")
  suspend fun setFavorite(id: Long, isFav: Boolean)

  @Query("SELECT COUNT(*) FROM components")
  suspend fun getCount(): Int
}
