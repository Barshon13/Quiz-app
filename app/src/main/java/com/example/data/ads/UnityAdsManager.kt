package com.example.data.ads

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAdsShowOptions
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

data class UnityAdConfig(
    val androidGameId: String = "5432100",
    val rewardedPlacementId: String = "Rewarded_Android",
    val interstitialPlacementId: String = "Interstitial_Android",
    val bannerPlacementId: String = "Banner_Android",
    val isTestMode: Boolean = true,
    val enableBanner: Boolean = true,
    val enableInterstitial: Boolean = true,
    val enableRewarded: Boolean = true
)

object UnityAdsManager {
    private const val TAG = "UnityAdsManager"

    var config: UnityAdConfig = UnityAdConfig()
        private set

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _isRewardedLoaded = MutableStateFlow(false)
    val isRewardedLoaded: StateFlow<Boolean> = _isRewardedLoaded.asStateFlow()

    private val _isInterstitialLoaded = MutableStateFlow(false)
    val isInterstitialLoaded: StateFlow<Boolean> = _isInterstitialLoaded.asStateFlow()

    private val _adStatusMessage = MutableStateFlow("Unity Ads Initializing...")
    val adStatusMessage: StateFlow<String> = _adStatusMessage.asStateFlow()

    fun initialize(context: Context) {
        val appCtx = context.applicationContext
        loadConfig(appCtx)

        UnityAds.debugMode = true

        Log.d(TAG, "Initializing Unity Ads with Game ID: ${config.androidGameId}, Test Mode: ${config.isTestMode}")
        _adStatusMessage.value = "Initializing Unity Ads (Test Mode: ${config.isTestMode})..."

        UnityAds.initialize(
            appCtx,
            config.androidGameId,
            config.isTestMode,
            object : IUnityAdsInitializationListener {
                override fun onInitializationComplete() {
                    Log.i(TAG, "Unity Ads Initialization Complete!")
                    _isInitialized.value = true
                    _adStatusMessage.value = "Unity Ads Initialized (Test Mode)"
                    loadRewardedAd()
                    loadInterstitialAd()
                }

                override fun onInitializationFailed(
                    error: UnityAds.UnityAdsInitializationError?,
                    message: String?
                ) {
                    val errMsg = "Unity Ads init failed: $error - $message"
                    Log.e(TAG, errMsg)
                    _isInitialized.value = false
                    _adStatusMessage.value = errMsg
                }
            }
        )
    }

    private fun loadConfig(context: Context) {
        try {
            val jsonStr = context.assets.open("ad_config.json").bufferedReader().use { it.readText() }
            val json = JSONObject(jsonStr)
            config = UnityAdConfig(
                androidGameId = json.optString("android_game_id", "5432100"),
                rewardedPlacementId = json.optString("rewarded_placement_id", "Rewarded_Android"),
                interstitialPlacementId = json.optString("interstitial_placement_id", "Interstitial_Android"),
                bannerPlacementId = json.optString("banner_placement_id", "Banner_Android"),
                isTestMode = json.optBoolean("is_test_mode", true),
                enableBanner = json.optBoolean("enable_banner", true),
                enableInterstitial = json.optBoolean("enable_interstitial", true),
                enableRewarded = json.optBoolean("enable_rewarded", true)
            )
            Log.d(TAG, "Loaded Unity Ad Config: $config")
        } catch (e: Exception) {
            Log.w(TAG, "Failed to load ad_config.json from assets, using defaults: ${e.message}")
        }
    }

    fun updateConfig(newConfig: UnityAdConfig, context: Context) {
        config = newConfig
        initialize(context)
    }

    fun loadRewardedAd() {
        if (!config.enableRewarded) return
        Log.d(TAG, "Loading Rewarded Ad: ${config.rewardedPlacementId}")
        UnityAds.load(
            config.rewardedPlacementId,
            object : IUnityAdsLoadListener {
                override fun onUnityAdsAdLoaded(placementId: String?) {
                    Log.i(TAG, "Rewarded Ad Loaded successfully: $placementId")
                    _isRewardedLoaded.value = true
                    _adStatusMessage.value = "Unity Rewarded Test Ad Ready"
                }

                override fun onUnityAdsFailedToLoad(
                    placementId: String?,
                    error: UnityAds.UnityAdsLoadError?,
                    message: String?
                ) {
                    Log.e(TAG, "Rewarded Ad Failed to Load: $placementId, error: $error, $message")
                    _isRewardedLoaded.value = false
                    _adStatusMessage.value = "Ad load error: $message"
                }
            }
        )
    }

    fun loadInterstitialAd() {
        if (!config.enableInterstitial) return
        Log.d(TAG, "Loading Interstitial Ad: ${config.interstitialPlacementId}")
        UnityAds.load(
            config.interstitialPlacementId,
            object : IUnityAdsLoadListener {
                override fun onUnityAdsAdLoaded(placementId: String?) {
                    Log.i(TAG, "Interstitial Ad Loaded successfully: $placementId")
                    _isInterstitialLoaded.value = true
                }

                override fun onUnityAdsFailedToLoad(
                    placementId: String?,
                    error: UnityAds.UnityAdsLoadError?,
                    message: String?
                ) {
                    Log.e(TAG, "Interstitial Ad Failed to Load: $placementId, error: $error, $message")
                    _isInterstitialLoaded.value = false
                }
            }
        )
    }

    fun showRewardedAd(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onDismissed: () -> Unit,
        onError: (String) -> Unit
    ) {
        val placementId = config.rewardedPlacementId
        Log.d(TAG, "Showing Unity Rewarded Ad: $placementId")

        val showListener = object : IUnityAdsShowListener {
            override fun onUnityAdsShowStart(placementId: String?) {
                Log.i(TAG, "Unity Rewarded Ad started: $placementId")
                _adStatusMessage.value = "Playing Unity Test Ad..."
            }

            override fun onUnityAdsShowClick(placementId: String?) {
                Log.i(TAG, "Unity Rewarded Ad clicked: $placementId")
            }

            override fun onUnityAdsShowComplete(
                placementId: String?,
                state: UnityAds.UnityAdsShowCompletionState?
            ) {
                Log.i(TAG, "Unity Rewarded Ad completed: $placementId, state: $state")
                _isRewardedLoaded.value = false
                loadRewardedAd() // Pre-load next ad

                if (state == UnityAds.UnityAdsShowCompletionState.COMPLETED) {
                    _adStatusMessage.value = "Unity Test Ad Completed! Reward Granted."
                    onRewardEarned()
                } else {
                    _adStatusMessage.value = "Unity Test Ad Skipped"
                    onDismissed()
                }
            }

            override fun onUnityAdsShowFailure(
                placementId: String?,
                error: UnityAds.UnityAdsShowError?,
                message: String?
            ) {
                val err = "Unity Ad show failed: $error - $message"
                Log.e(TAG, err)
                _isRewardedLoaded.value = false
                _adStatusMessage.value = err
                loadRewardedAd()
                onError(err)
            }
        }

        // Show ad using Unity Ads SDK
        UnityAds.show(activity, placementId, UnityAdsShowOptions(), showListener)
    }

    fun showInterstitialAd(
        activity: Activity,
        onAdClosed: () -> Unit = {}
    ) {
        if (!config.enableInterstitial) {
            onAdClosed()
            return
        }

        val placementId = config.interstitialPlacementId
        Log.d(TAG, "Showing Unity Interstitial Ad: $placementId")

        UnityAds.show(
            activity,
            placementId,
            UnityAdsShowOptions(),
            object : IUnityAdsShowListener {
                override fun onUnityAdsShowStart(placementId: String?) {
                    Log.i(TAG, "Unity Interstitial started")
                }

                override fun onUnityAdsShowClick(placementId: String?) {
                    Log.i(TAG, "Unity Interstitial clicked")
                }

                override fun onUnityAdsShowComplete(
                    placementId: String?,
                    state: UnityAds.UnityAdsShowCompletionState?
                ) {
                    _isInterstitialLoaded.value = false
                    loadInterstitialAd()
                    onAdClosed()
                }

                override fun onUnityAdsShowFailure(
                    placementId: String?,
                    error: UnityAds.UnityAdsShowError?,
                    message: String?
                ) {
                    _isInterstitialLoaded.value = false
                    loadInterstitialAd()
                    onAdClosed()
                }
            }
        )
    }
}

fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) return currentContext
        currentContext = currentContext.baseContext
    }
    return null
}

/**
 * Real Unity Banner Ad view using AndroidView
 */
@Composable
fun UnityBannerAdView(
    modifier: Modifier = Modifier,
    placementId: String = UnityAdsManager.config.bannerPlacementId
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    if (activity == null || !UnityAdsManager.config.enableBanner) {
        // Fallback banner placeholder
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(ObsidianCard),
            contentAlignment = Alignment.Center
        ) {
            Text("Unity Ads Banner", color = TextMuted, fontSize = 12.sp)
        }
        return
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        factory = { ctx ->
            val banner = BannerView(activity, placementId, UnityBannerSize(320, 50))
            banner.listener = object : BannerView.Listener() {
                override fun onBannerLoaded(bannerView: BannerView?) {
                    Log.d("UnityBannerAd", "Banner loaded successfully")
                }

                override fun onBannerFailedToLoad(
                    bannerView: BannerView?,
                    errorInfo: BannerErrorInfo?
                ) {
                    Log.w("UnityBannerAd", "Banner failed to load: ${errorInfo?.errorMessage}")
                }
            }
            banner.load()
            banner
        },
        update = { bannerView ->
            // BannerView handles updates
        }
    )
}
