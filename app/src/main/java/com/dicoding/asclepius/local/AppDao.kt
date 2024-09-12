package com.dicoding.asclepius.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {
    @Query("SELECT * FROM classification ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<Classification>>

    @Query("SELECT * FROM classification WHERE id = :id")
    fun getClassificationById(id: Int): LiveData<Classification>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg classification: Classification)

    @Delete
    suspend fun delete(classification: Classification)
}