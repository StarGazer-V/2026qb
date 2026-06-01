package com.medgttracker.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter fun toNoteType(value: String): FlashcardNoteType = FlashcardNoteType.valueOf(value)
    @TypeConverter fun fromNoteType(value: FlashcardNoteType): String = value.name
    @TypeConverter fun toReviewGrade(value: String?): ReviewGrade? = value?.let(ReviewGrade::valueOf)
    @TypeConverter fun fromReviewGrade(value: ReviewGrade?): String? = value?.name
}
