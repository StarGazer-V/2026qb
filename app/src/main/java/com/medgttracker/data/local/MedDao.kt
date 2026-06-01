package com.medgttracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profiles WHERE userId = :userId LIMIT 1")
    fun observeProfile(userId: String): Flow<ProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGtScore(score: GtScoreEntity): Long

    @Update
    suspend fun updateGtScore(score: GtScoreEntity)

    @Delete
    suspend fun deleteGtScore(score: GtScoreEntity)

    @Query("SELECT * FROM gt_scores WHERE userId = :userId ORDER BY attemptedAtEpochMillis ASC")
    fun observeGtScores(userId: String): Flow<List<GtScoreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcardNote(note: FlashcardNoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcardReview(review: FlashcardReviewEntity): Long

    @Transaction
    suspend fun createCardWithReview(note: FlashcardNoteEntity, firstDueAtEpochMillis: Long): Long {
        val noteId = insertFlashcardNote(note)
        insertFlashcardReview(FlashcardReviewEntity(noteId = noteId, dueAtEpochMillis = firstDueAtEpochMillis))
        return noteId
    }

    @Query("SELECT * FROM flashcard_notes WHERE userId = :userId AND deckName = :deckName ORDER BY updatedAtEpochMillis DESC")
    fun observeDeckNotes(userId: String, deckName: String): Flow<List<FlashcardNoteEntity>>

    @Query("SELECT * FROM flashcard_reviews WHERE dueAtEpochMillis <= :nowEpochMillis ORDER BY dueAtEpochMillis ASC")
    fun observeDueReviews(nowEpochMillis: Long): Flow<List<FlashcardReviewEntity>>

    @Update
    suspend fun updateReview(review: FlashcardReviewEntity)
}
