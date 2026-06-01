package com.medgttracker.flashcards

import com.medgttracker.data.local.FlashcardReviewEntity
import com.medgttracker.data.local.ReviewGrade
import kotlin.math.max
import kotlin.math.roundToInt

object SpacedRepetitionScheduler {
    private const val DayMillis = 86_400_000L

    fun schedule(review: FlashcardReviewEntity, grade: ReviewGrade, nowEpochMillis: Long): FlashcardReviewEntity {
        val next = when (grade) {
            ReviewGrade.AGAIN -> review.copy(
                intervalDays = 0,
                easeFactor = max(1.3, review.easeFactor - 0.2),
                lapses = review.lapses + 1,
                dueAtEpochMillis = nowEpochMillis + 10 * 60_000L,
            )
            ReviewGrade.HARD -> review.copy(
                intervalDays = max(1, (review.intervalDays * 1.2).roundToInt()),
                easeFactor = max(1.3, review.easeFactor - 0.15),
            )
            ReviewGrade.GOOD -> review.copy(
                intervalDays = if (review.repetitions == 0) 1 else max(2, (review.intervalDays * review.easeFactor).roundToInt()),
            )
            ReviewGrade.EASY -> review.copy(
                intervalDays = if (review.repetitions == 0) 4 else max(4, (review.intervalDays * (review.easeFactor + 0.3)).roundToInt()),
                easeFactor = review.easeFactor + 0.15,
            )
        }
        return next.copy(
            repetitions = if (grade == ReviewGrade.AGAIN) 0 else review.repetitions + 1,
            lastReviewedAtEpochMillis = nowEpochMillis,
            lastGrade = grade,
            dueAtEpochMillis = if (grade == ReviewGrade.AGAIN) next.dueAtEpochMillis else nowEpochMillis + next.intervalDays * DayMillis,
        )
    }
}
