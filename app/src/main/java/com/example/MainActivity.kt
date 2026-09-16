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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.data.ads.UnityAdsManager
import com.example.data.ads.findActivity
import com.example.ui.components.DailyRewardDialog
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
        // Initialize Real Unity Ads SDK in Test Mode
        UnityAdsManager.initialize(this)
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
    val adRequest by viewModel.adRequest.collectAsState()
    val interstitialRequest by viewModel.interstitialRequest.collectAsState()
    val dailyRewardAmount by viewModel.dailyRewardAmount.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Real Unity Rewarded Video Ad trigger
    LaunchedEffect(adRequest) {
        val req = adRequest
        if (req != null) {
            val activity = context.findActivity()
            if (activity != null) {
                UnityAdsManager.showRewardedAd(
                    activity = activity,
                    onRewardEarned = {
                        viewModel.completeRewardedAd()
                    },
                    onDismissed = {
                        viewModel.dismissAdWithoutReward()
                    },
                    onError = { err ->
                        viewModel.handleAdError(err)
                    }
                )
            } else {
                viewModel.completeRewardedAd()
            }
            viewModel.clearAdRequest()
        }
    }

    // Real Unity Interstitial Ad trigger
    LaunchedEffect(interstitialRequest) {
        if (interstitialRequest) {
            val activity = context.findActivity()
            if (activity != null) {
                UnityAdsManager.showInterstitialAd(
                    activity = activity,
                    onAdClosed = {
                        viewModel.clearInterstitialRequest()
                    }
                )
            } else {
                viewModel.clearInterstitialRequest()
            }
        }
    }

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
