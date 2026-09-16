package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.components.DailyRewardDialog
import com.example.ui.components.SimulatedRewardedAdModal
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ResultsScreen
import com.example.ui.screens.StoreScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBg
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                QuizMasterApp(viewModel = quizViewModel)
            }
        }
    }
}

@Composable
fun QuizMasterApp(viewModel: QuizViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isShowingAd by viewModel.isShowingAd.collectAsState()
    val adCountdown by viewModel.adCountdown.collectAsState()
    val pendingReward by viewModel.pendingReward.collectAsState()
    val dailyRewardAmount by viewModel.dailyRewardAmount.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ObsidianBg,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 60.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ObsidianBg)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                    AppScreen.QUIZ -> QuizScreen(viewModel = viewModel)
                    AppScreen.RESULTS -> ResultsScreen(viewModel = viewModel)
                    AppScreen.LEADERBOARD -> LeaderboardScreen(viewModel = viewModel)
                    AppScreen.STORE -> StoreScreen(viewModel = viewModel)
                }
            }

            // Rewarded Video Ad Modal
            if (isShowingAd) {
                SimulatedRewardedAdModal(
                    countdown = adCountdown,
                    rewardType = pendingReward,
                    onComplete = { viewModel.completeRewardedAd() },
                    onDismiss = { viewModel.dismissAdWithoutReward() }
                )
            }

            // Daily Login Reward Dialog
            dailyRewardAmount?.let { amount ->
                DailyRewardDialog(
                    coinsWon = amount,
                    onDismiss = { viewModel.dismissDailyReward() }
                )
            }
        }
    }
}
