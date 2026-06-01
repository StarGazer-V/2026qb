package com.medgttracker.data.repository

import com.medgttracker.data.local.GtScoreEntity
import com.medgttracker.data.local.MedDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class GtRepository @Inject constructor(private val dao: MedDao) {
    fun observeScores(userId: String): Flow<List<GtScoreEntity>> = dao.observeGtScores(userId)
    suspend fun addScore(score: GtScoreEntity): Long = dao.insertGtScore(score)
}
