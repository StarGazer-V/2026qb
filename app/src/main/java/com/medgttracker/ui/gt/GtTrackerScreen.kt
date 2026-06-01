package com.medgttracker.ui.gt

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.medgttracker.data.local.GtScoreEntity
import com.medgttracker.data.local.accuracyPercent
import com.medgttracker.data.local.netScore

@Composable
fun GtTrackerRoute(onBack: () -> Unit, viewModel: GtTrackerViewModel = hiltViewModel()) {
    val scores by viewModel.scores.collectAsState()
    GtTrackerScreen(scores = scores, onBack = onBack, onSave = viewModel::addScore)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GtTrackerScreen(scores: List<GtScoreEntity>, onBack: () -> Unit, onSave: (GtScoreInput) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GT Score Tracker") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { GtScoreForm(onSave = onSave) }
            item { ProgressReport(scores = scores) }
            item { ScoreProgressChart(scores = scores) }
        }
    }
}

@Composable
private fun GtScoreForm(onSave: (GtScoreInput) -> Unit) {
    var platform by remember { mutableStateOf("") }
    var testName by remember { mutableStateOf("") }
    var corrects by remember { mutableStateOf("") }
    var incorrects by remember { mutableStateOf("") }
    var skipped by remember { mutableStateOf("") }
    var percentile by remember { mutableStateOf("") }
    var rank by remember { mutableStateOf("") }
    var worstOne by remember { mutableStateOf("") }
    var worstTwo by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Add mock test", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            GtTextField("Platform", platform) { platform = it }
            GtTextField("Test name", testName) { testName = it }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GtTextField("Corrects", corrects, Modifier.weight(1f), KeyboardType.Number) { corrects = it }
                GtTextField("Incorrects", incorrects, Modifier.weight(1f), KeyboardType.Number) { incorrects = it }
                GtTextField("Skipped", skipped, Modifier.weight(1f), KeyboardType.Number) { skipped = it }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GtTextField("Percentile", percentile, Modifier.weight(1f), KeyboardType.Decimal) { percentile = it }
                GtTextField("Rank", rank, Modifier.weight(1f), KeyboardType.Number) { rank = it }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GtTextField("Worst subject 1", worstOne, Modifier.weight(1f)) { worstOne = it }
                GtTextField("Worst subject 2", worstTwo, Modifier.weight(1f)) { worstTwo = it }
            }
            GtTextField("Notes", notes) { notes = it }
            Button(
                onClick = {
                    onSave(
                        GtScoreInput(
                            platform = platform.ifBlank { "Unknown" },
                            testName = testName.ifBlank { "Grand Test" },
                            corrects = corrects.toIntOrNull() ?: 0,
                            incorrects = incorrects.toIntOrNull() ?: 0,
                            skipped = skipped.toIntOrNull() ?: 0,
                            percentile = percentile.toDoubleOrNull(),
                            rank = rank.toIntOrNull(),
                            worstSubjectOne = worstOne,
                            worstSubjectTwo = worstTwo,
                            notes = notes.ifBlank { null },
                        ),
                    )
                    platform = ""; testName = ""; corrects = ""; incorrects = ""; skipped = ""
                    percentile = ""; rank = ""; worstOne = ""; worstTwo = ""; notes = ""
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Save score offline") }
        }
    }
}

@Composable
private fun GtTextField(
    label: String,
    value: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    keyboardType: KeyboardType = KeyboardType.Text,
    onChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier,
        singleLine = true,
    )
}

@Composable
private fun ProgressReport(scores: List<GtScoreEntity>) {
    val latest = scores.lastOrNull()
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Score report", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Tests logged: ${scores.size}")
            Text("Latest net score: ${latest?.netScore ?: 0.0}")
            Text("Latest accuracy: ${String.format("%.1f", latest?.accuracyPercent ?: 0.0)}%")
            Text("Repeated weak areas: ${scores.takeLast(5).flatMap { listOf(it.worstSubjectOne, it.worstSubjectTwo) }.filter { it.isNotBlank() }.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: "Add more tests"}")
        }
    }
}

@Composable
private fun ScoreProgressChart(scores: List<GtScoreEntity>) {
    val lineColor = MaterialTheme.colorScheme.primary
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Progress graph", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
.            Text("This chart uses Compose Canvas so the debug APK builds without an extra charting dependency.")
            Canvas(Modifier.fillMaxWidth().height(220.dp).padding(top = 12.dp)) {
                if (scores.size < 2) return@Canvas
                val values = scores.map { it.netScore.toFloat() }
                val min = values.minOrNull() ?: 0f
                val max = values.maxOrNull() ?: 1f
                val span = (max - min).takeIf { it > 0f } ?: 1f
                val stepX = size.width / (values.lastIndex.coerceAtLeast(1))
                val path = Path()
                values.forEachIndexed { index, score ->
                    val x = index * stepX
                    val y = size.height - ((score - min) / span) * size.height
                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    drawCircle(color = lineColor, radius = 6.dp.toPx(), center = Offset(x, y))
                }
                drawPath(path = path, color = lineColor, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx()))
                drawLine(Color.LightGray, Offset(0f, size.height), Offset(size.width, size.height))
            }
        }
    }
}
