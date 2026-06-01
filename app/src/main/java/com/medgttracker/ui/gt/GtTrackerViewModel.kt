package com.medgttracker.ui.gt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medgttracker.auth.AuthRepository
import com.medgttracker.data.local.GtScoreEntity
import com.medgttracker.data.repository.GtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class GtTrackerViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val gtRepository: GtRepository,
) : ViewModel() {
    private val userId = authRepository.currentUserId ?: "local-demo-user"

    val scores: StateFlow<List<GtScoreEntity>> = gtRepository.observeScores(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addScore(input: GtScoreInput) {
        viewModelScope.launch {
            gtRepository.addScore(
                GtScoreEntity(
                    userId = userId,
                    platform = input.platform,
                    testName = input.testName,
                    attemptedAtEpochMillis = System.currentTimeMillis(),
                    corrects = input.corrects,
                    incorrects = input.incorrects,
                    skipped = input.skipped,
                    percentile = input.percentile,
                    rank = input.rank,
                    worstSubjectOne = input.worstSubjectOne,
                    worstSubjectTwo = input.worstSubjectTwo,
                    notes = input.notes,
                ),
            )
        }
    }
}

data class GtScoreInput(
    val platform: String,
    val testName: String,
    val corrects: Int,
    val incorrects: Int,
    val skipped: Int,
    val percentile: Double?,
    val rank: Int?,
    val worstSubjectOne: String,
    val worstSubjectTwo: String,
    val notes: String?,
)
