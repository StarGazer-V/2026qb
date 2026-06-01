package com.medgttracker.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.random.Random

private data class ExamCountdown(val name: String, val date: LocalDate)

private val majorExams = listOf(
    ExamCountdown("NEET PG 2025", LocalDate.of(2025, 8, 30)),
    ExamCountdown("UPSC CMS", LocalDate.of(2025, 8, 2)),
    ExamCountdown("INI-CET", LocalDate.of(2025, 11, 1)),
)

private val motivationalQuotes = listOf(
    "Small daily revisions become rank-changing recall.",
    "A wrong answer today is a saved mark on exam day.",
    "Consistency beats intensity when the exam is months away.",
    "Review your misses; they are the shortest path to improvement.",
)

@Composable
fun DashboardScreen(
    onOpenGtTracker: () -> Unit,
    onOpenFlashcards: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val quote = remember { motivationalQuotes[Random.nextInt(motivationalQuotes.size)] }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("Medical GT Tracker", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Scores, spaced repetition, and exam readiness in one place.")
        }
        item { CountdownCard(quote = quote) }
        item {
            DashboardActionGrid(
                onOpenGtTracker = onOpenGtTracker,
                onOpenFlashcards = onOpenFlashcards,
                onOpenProfile = onOpenProfile,
                onOpenSettings = onOpenSettings,
            )
        }
    }
}

@Composable
private fun CountdownCard(quote: String) {
    val today = LocalDate.now()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Exam countdown", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            majorExams.forEach { exam ->
                val days = ChronoUnit.DAYS.between(today, exam.date)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(exam.name, fontWeight = FontWeight.SemiBold)
                    Text(if (days >= 0) "$days days left" else "Completed")
                }
            }
            Spacer(Modifier.height(4.dp))
            Text("“$quote”", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun DashboardActionGrid(
    onOpenGtTracker: () -> Unit,
    onOpenFlashcards: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardButton("GT Scores", onOpenGtTracker, Modifier.weight(1f))
            DashboardButton("Flashcards", onOpenFlashcards, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardButton("Profile", onOpenProfile, Modifier.weight(1f))
            DashboardButton("Settings", onOpenSettings, Modifier.weight(1f))
        }
    }
}

@Composable
private fun DashboardButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onClick, modifier = modifier.height(72.dp), shape = RoundedCornerShape(18.dp)) {
        Text(label)
    }
}
