package com.medgttracker.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val college: String? = null,
    val targetExam: String? = null,
)

@Entity(
    tableName = "gt_scores",
    indices = [Index("userId"), Index("attemptedAtEpochMillis")],
)
data class GtScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val platform: String,
    val testName: String,
    val attemptedAtEpochMillis: Long,
    val corrects: Int,
    val incorrects: Int,
    val skipped: Int,
    val percentile: Double?,
    val rank: Int?,
    val worstSubjectOne: String,
    val worstSubjectTwo: String,
    val notes: String? = null,
)

val GtScoreEntity.totalQuestions: Int get() = corrects + incorrects + skipped
val GtScoreEntity.accuracyPercent: Double get() = if (corrects + incorrects == 0) 0.0 else corrects * 100.0 / (corrects + incorrects)
val GtScoreEntity.netScore: Double get() = corrects * 4.0 - incorrects

enum class FlashcardNoteType { BASIC, CLOZE, IMAGE_OCCLUSION }

enum class ReviewGrade { AGAIN, HARD, GOOD, EASY }

@Entity(
    tableName = "flashcard_notes",
    indices = [Index("userId"), Index("deckName"), Index("noteType")],
)
data class FlashcardNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val deckName: String,
    val noteType: FlashcardNoteType,
    val front: String,
    val back: String,
    val tagsCsv: String = "",
    val clozeText: String? = null,
    val imageUri: String? = null,
    val occlusionJson: String? = null,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long,
)

@Entity(
    tableName = "flashcard_reviews",
    indices = [Index("noteId"), Index("dueAtEpochMillis")],
)
data class FlashcardReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val noteId: Long,
    val intervalDays: Int = 0,
    val easeFactor: Double = 2.5,
    val repetitions: Int = 0,
    val lapses: Int = 0,
    val dueAtEpochMillis: Long,
    val lastReviewedAtEpochMillis: Long? = null,
    val lastGrade: ReviewGrade? = null,
)
