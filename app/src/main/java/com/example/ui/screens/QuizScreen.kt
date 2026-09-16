package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LifelineType
import com.example.ui.theme.CrimsonError
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val gameState by viewModel.gameState.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    var showQuitDialog by remember { mutableStateOf(false) }

    val question = gameState.currentQuestion

    if (showQuitDialog) {
        AlertDialog(
            onDismissRequest = { showQuitDialog = false },
            title = { Text("Leave Quiz?") },
            text = { Text("Your round progress and lives spent will not be restored if you exit now.") },
            confirmButton = {
                Button(
                    onClick = {
                        showQuitDialog = false
                        viewModel.navigateTo(AppScreen.HOME)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonError)
                ) {
                    Text("Exit Round", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuitDialog = false }) {
                    Text("Continue Playing", color = TextPrimary)
                }
            },
            containerColor = ObsidianSurface,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("quiz_screen"),
        containerColor = ObsidianBg,
        topBar = {
            QuizTopHeader(
                categoryName = gameState.categoryName,
                currentIndex = gameState.currentIndex,
                totalQuestions = gameState.questions.size,
                score = gameState.score,
                combo = gameState.combo,
                hearts = userProfile.hearts,
                onExit = { showQuitDialog = true }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Timer Bar & Progress
            TimerAndStreakBar(
                timeRemaining = gameState.timeRemaining,
                isRevealed = gameState.isRevealed,
                combo = gameState.combo,
                isDoublePoints = gameState.isDoublePointsActive
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (question != null) {
                // Question Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, ObsidianCardBorder, RoundedCornerShape(20.dp))
                        .testTag("question_card"),
                    colors = CardDefaults.cardColors(containerColor = ObsidianSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GoldPrimary.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = question.category.title.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ObsidianSurfaceElevated)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = question.difficulty.label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = question.text,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            lineHeight = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Lifelines Bar
                LifelinesRow(
                    lifelines = userProfile.lifelines,
                    isRevealed = gameState.isRevealed,
                    disabledOptions = gameState.disabledOptions,
                    isDoublePointsActive = gameState.isDoublePointsActive,
                    onFiftyFifty = { viewModel.useFiftyFifty() },
                    onExtraTime = { viewModel.useExtraTime() },
                    onSkip = { viewModel.useSkipQuestion() },
                    onDoublePoints = { viewModel.useDoublePoints() }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 4 Options
                val letters = listOf("A", "B", "C", "D")
                question.options.forEachIndexed { index, optionText ->
                    val isSelected = (gameState.selectedIndex == index)
                    val isCorrectAnswer = (index == question.correctIndex)
                    val isDisabled = gameState.disabledOptions.contains(index)

                    OptionButton(
                        letter = letters.getOrElse(index) { "?" },
                        text = optionText,
                        index = index,
                        isSelected = isSelected,
                        isRevealed = gameState.isRevealed,
                        isCorrectAnswer = isCorrectAnswer,
                        isDisabled = isDisabled,
                        onClick = { viewModel.selectAnswer(index) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Explanation & Next Question Card
                AnimatedVisibility(
                    visible = gameState.isRevealed,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
                ) {
                    ExplanationCard(
                        isCorrect = gameState.selectedIndex == question.correctIndex,
                        isTimeUp = gameState.isTimeUp,
                        explanation = question.explanation,
                        isLastQuestion = (gameState.currentIndex == gameState.questions.size - 1),
                        onNext = { viewModel.nextQuestion() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuizTopHeader(
    categoryName: String,
    currentIndex: Int,
    totalQuestions: Int,
    score: Int,
    combo: Int,
    hearts: Int,
    onExit: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("quiz_top_header"),
        color = ObsidianSurface
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onExit,
                    modifier = Modifier.testTag("exit_quiz_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit",
                        tint = TextSecondary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = categoryName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GoldPrimary
                    )
                    Text(
                        text = "Question ${currentIndex + 1} of $totalQuestions",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                // Score Badge
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                    color = ObsidianCard
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$score",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                    }
                }
            }

            // Progress bar
            val progress = if (totalQuestions > 0) (currentIndex.toFloat() / totalQuestions) else 0f
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = GoldPrimary,
                trackColor = ObsidianCardBorder
            )
        }
    }
}

@Composable
fun TimerAndStreakBar(
    timeRemaining: Int,
    isRevealed: Boolean,
    combo: Int,
    isDoublePoints: Boolean
) {
    val timerColor by animateColorAsState(
        targetValue = when {
            timeRemaining > 8 -> EmeraldSuccess
            timeRemaining > 3 -> GoldPrimary
            else -> CrimsonError
        },
        label = "timer_color"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Timer Ring representation
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, timerColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
            color = ObsidianSurface
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Timer",
                    tint = timerColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${timeRemaining}s",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = timerColor
                )
            }
        }

        // Active Streak / Multiplier
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isDoublePoints) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoldSecondary)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "2X BOOST",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
            }

            if (combo > 1) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.horizontalGradient(listOf(GoldPrimary, GoldSecondary))
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "🔥 ${combo}X COMBO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun LifelinesRow(
    lifelines: Map<LifelineType, Int>,
    isRevealed: Boolean,
    disabledOptions: Set<Int>,
    isDoublePointsActive: Boolean,
    onFiftyFifty: () -> Unit,
    onExtraTime: () -> Unit,
    onSkip: () -> Unit,
    onDoublePoints: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lifelines_row"),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LifelinePill(
            title = "50:50",
            count = lifelines[LifelineType.FIFTY_FIFTY] ?: 0,
            isActive = disabledOptions.isNotEmpty(),
            isEnabled = !isRevealed && disabledOptions.isEmpty(),
            onClick = onFiftyFifty,
            modifier = Modifier.weight(1f)
        )
        LifelinePill(
            title = "+15s",
            count = lifelines[LifelineType.EXTRA_TIME] ?: 0,
            isActive = false,
            isEnabled = !isRevealed,
            onClick = onExtraTime,
            modifier = Modifier.weight(1f)
        )
        LifelinePill(
            title = "Skip",
            count = lifelines[LifelineType.SKIP] ?: 0,
            isActive = false,
            isEnabled = !isRevealed,
            onClick = onSkip,
            modifier = Modifier.weight(1f)
        )
        LifelinePill(
            title = "2x",
            count = lifelines[LifelineType.DOUBLE_POINTS] ?: 0,
            isActive = isDoublePointsActive,
            isEnabled = !isRevealed && !isDoublePointsActive,
            onClick = onDoublePoints,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun LifelinePill(
    title: String,
    count: Int,
    isActive: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        isActive -> EmeraldSuccess
        isEnabled && count > 0 -> GoldPrimary.copy(alpha = 0.4f)
        else -> ObsidianCardBorder
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(enabled = isEnabled && count > 0) { onClick() }
            .testTag("lifeline_$title"),
        color = if (isActive) EmeraldSuccess.copy(alpha = 0.15f) else ObsidianCard
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isEnabled && count > 0) TextPrimary else TextMuted
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (count > 0) GoldPrimary else TextMuted.copy(alpha = 0.3f))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = "$count",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun OptionButton(
    letter: String,
    text: String,
    index: Int,
    isSelected: Boolean,
    isRevealed: Boolean,
    isCorrectAnswer: Boolean,
    isDisabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isDisabled -> ObsidianCard.copy(alpha = 0.3f)
        isRevealed && isCorrectAnswer -> EmeraldSuccess.copy(alpha = 0.22f)
        isRevealed && isSelected && !isCorrectAnswer -> CrimsonError.copy(alpha = 0.22f)
        else -> ObsidianSurfaceElevated
    }

    val borderColor = when {
        isDisabled -> ObsidianCardBorder.copy(alpha = 0.3f)
        isRevealed && isCorrectAnswer -> EmeraldSuccess
        isRevealed && isSelected && !isCorrectAnswer -> CrimsonError
        else -> ObsidianCardBorder
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(enabled = !isRevealed && !isDisabled) { onClick() }
            .testTag("option_${index}"),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isRevealed && isCorrectAnswer -> EmeraldSuccess
                                isRevealed && isSelected && !isCorrectAnswer -> CrimsonError
                                else -> ObsidianCard
                            }
                        )
                        .border(1.dp, borderColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isRevealed && (isCorrectAnswer || isSelected)) Color.Black else TextSecondary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDisabled) TextMuted else TextPrimary
                )
            }

            if (isRevealed) {
                if (isCorrectAnswer) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Correct",
                        tint = EmeraldSuccess,
                        modifier = Modifier.size(22.dp)
                    )
                } else if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Wrong",
                        tint = CrimsonError,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExplanationCard(
    isCorrect: Boolean,
    isTimeUp: Boolean,
    explanation: String,
    isLastQuestion: Boolean,
    onNext: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.5.dp,
                if (isCorrect) EmeraldSuccess.copy(alpha = 0.6f) else CrimsonError.copy(alpha = 0.6f),
                RoundedCornerShape(16.dp)
            )
            .testTag("explanation_card"),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (isCorrect) EmeraldSuccess else CrimsonError,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            isCorrect -> "Correct! Verified Fact"
                            isTimeUp -> "Time Expired!"
                            else -> "Incorrect Answer"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCorrect) EmeraldSuccess else CrimsonError
                    )
                }

                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = explanation,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("next_question_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCorrect) EmeraldSuccess else GoldPrimary
                )
            ) {
                Text(
                    text = if (isLastQuestion) "View Final Results" else "Next Question",
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TextDark,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
