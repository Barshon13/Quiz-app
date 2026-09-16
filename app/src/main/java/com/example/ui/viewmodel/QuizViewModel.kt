package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.LifelineType
import com.example.data.model.Question
import com.example.data.model.QuestionReview
import com.example.data.model.QuizCategory
import com.example.data.model.QuizResult
import com.example.data.model.UserProfile
import com.example.data.repository.QuizRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    QUIZ,
    RESULTS,
    LEADERBOARD,
    STORE
}

enum class AdRewardType {
    REFILL_HEARTS,
    DOUBLE_QUIZ_REWARDS,
    BONUS_COINS
}

data class QuizGameState(
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val score: Int = 0,
    val combo: Int = 0,
    val maxCombo: Int = 0,
    val selectedIndex: Int? = null,
    val isRevealed: Boolean = false,
    val disabledOptions: Set<Int> = emptySet(),
    val isDoublePointsActive: Boolean = false,
    val timeRemaining: Int = 15,
    val isTimeUp: Boolean = false,
    val reviews: List<QuestionReview> = emptyList(),
    val categoryName: String = "Quick Play"
) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentIndex)

    val progress: Float
        get() = if (questions.isEmpty()) 0f else (currentIndex.toFloat() / questions.size.toFloat())
}

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuizRepository(application)

    val userProfile: StateFlow<UserProfile> = repository.userProfile

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _gameState = MutableStateFlow(QuizGameState())
    val gameState: StateFlow<QuizGameState> = _gameState.asStateFlow()

    private val _lastResult = MutableStateFlow<QuizResult?>(null)
    val lastResult: StateFlow<QuizResult?> = _lastResult.asStateFlow()

    // Monetization / Rewarded Ad state (Unity Ads Integration)
    private val _isShowingAd = MutableStateFlow(false)
    val isShowingAd: StateFlow<Boolean> = _isShowingAd.asStateFlow()

    private val _adCountdown = MutableStateFlow(0)
    val adCountdown: StateFlow<Int> = _adCountdown.asStateFlow()

    private val _pendingReward = MutableStateFlow<AdRewardType?>(null)
    val pendingReward: StateFlow<AdRewardType?> = _pendingReward.asStateFlow()

    private val _adRequest = MutableStateFlow<AdRewardType?>(null)
    val adRequest: StateFlow<AdRewardType?> = _adRequest.asStateFlow()

    private val _interstitialRequest = MutableStateFlow(false)
    val interstitialRequest: StateFlow<Boolean> = _interstitialRequest.asStateFlow()

    // Daily reward modal
    private val _dailyRewardAmount = MutableStateFlow<Int?>(null)
    val dailyRewardAmount: StateFlow<Int?> = _dailyRewardAmount.asStateFlow()

    // Feedback Toast / Snackbar message
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    private var timerJob: Job? = null

    fun navigateTo(screen: AppScreen) {
        if (screen != AppScreen.QUIZ) {
            timerJob?.cancel()
        }
        _currentScreen.value = screen
    }

    fun startQuickPlay() {
        if (userProfile.value.hearts <= 0) {
            _userMessage.value = "Out of Hearts! Watch a quick video to refill."
            showRewardedAd(AdRewardType.REFILL_HEARTS)
            return
        }
        repository.deductHeart()
        val questions = repository.getQuestionsForGame(null, count = 8)
        startNewGame(questions, "Quick Play Mixed")
    }

    fun startCategoryGame(category: QuizCategory) {
        if (!userProfile.value.unlockedCategories.contains(category.id)) {
            _userMessage.value = "Unlock this category first!"
            return
        }
        if (userProfile.value.hearts <= 0) {
            _userMessage.value = "Out of Hearts! Watch an ad to refill."
            showRewardedAd(AdRewardType.REFILL_HEARTS)
            return
        }
        repository.deductHeart()
        val questions = repository.getQuestionsForGame(category, count = 5)
        startNewGame(questions, category.title)
    }

    fun startDailyChallenge() {
        if (userProfile.value.hearts <= 0) {
            _userMessage.value = "Out of Hearts! Watch an ad to refill."
            showRewardedAd(AdRewardType.REFILL_HEARTS)
            return
        }
        repository.deductHeart()
        val questions = repository.getDailyChallengeQuestions()
        startNewGame(questions, "Daily Quiz Challenge")
    }

    private fun startNewGame(questions: List<Question>, categoryName: String) {
        _gameState.value = QuizGameState(
            questions = questions,
            currentIndex = 0,
            score = 0,
            combo = 0,
            maxCombo = 0,
            selectedIndex = null,
            isRevealed = false,
            disabledOptions = emptySet(),
            isDoublePointsActive = false,
            timeRemaining = 15,
            isTimeUp = false,
            reviews = emptyList(),
            categoryName = categoryName
        )
        _currentScreen.value = AppScreen.QUIZ
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_gameState.value.timeRemaining > 0 && !_gameState.value.isRevealed) {
                delay(1000L)
                _gameState.update { it.copy(timeRemaining = it.timeRemaining - 1) }
            }
            if (_gameState.value.timeRemaining <= 0 && !_gameState.value.isRevealed) {
                // Time up! Auto reveal as wrong
                onTimeExpired()
            }
        }
    }

    fun selectAnswer(index: Int) {
        val state = _gameState.value
        if (state.isRevealed || state.disabledOptions.contains(index)) return

        timerJob?.cancel()
        val question = state.currentQuestion ?: return
        val isCorrect = (index == question.correctIndex)

        val multiplier = when {
            state.combo >= 4 -> 2.0f
            state.combo >= 2 -> 1.5f
            state.combo >= 1 -> 1.25f
            else -> 1.0f
        } * (if (state.isDoublePointsActive) 2.0f else 1.0f) * question.difficulty.multiplier

        val pointsEarned = if (isCorrect) (100 * multiplier).toInt() else 0
        val newCombo = if (isCorrect) state.combo + 1 else 0
        val newMaxCombo = maxOf(state.maxCombo, newCombo)

        val review = QuestionReview(
            question = question,
            selectedIndex = index,
            isCorrect = isCorrect,
            timeSpentSeconds = 15 - state.timeRemaining
        )

        _gameState.update {
            it.copy(
                selectedIndex = index,
                isRevealed = true,
                score = it.score + pointsEarned,
                combo = newCombo,
                maxCombo = newMaxCombo,
                reviews = it.reviews + review
            )
        }
    }

    private fun onTimeExpired() {
        val state = _gameState.value
        val question = state.currentQuestion ?: return
        val review = QuestionReview(
            question = question,
            selectedIndex = null,
            isCorrect = false,
            timeSpentSeconds = 15
        )
        _gameState.update {
            it.copy(
                isRevealed = true,
                isTimeUp = true,
                combo = 0,
                reviews = it.reviews + review
            )
        }
    }

    fun nextQuestion() {
        val state = _gameState.value
        val nextIndex = state.currentIndex + 1

        if (nextIndex < state.questions.size) {
            _gameState.update {
                it.copy(
                    currentIndex = nextIndex,
                    selectedIndex = null,
                    isRevealed = false,
                    disabledOptions = emptySet(),
                    isDoublePointsActive = false,
                    timeRemaining = 15,
                    isTimeUp = false
                )
            }
            startTimer()
        } else {
            finishGame()
        }
    }

    private fun finishGame() {
        timerJob?.cancel()
        val state = _gameState.value
        val correctCount = state.reviews.count { it.isCorrect }
        val totalCount = state.questions.size
        val accuracy = if (totalCount > 0) correctCount.toFloat() / totalCount else 0f

        val stars = when {
            accuracy >= 0.8f -> 3
            accuracy >= 0.5f -> 2
            accuracy > 0.0f -> 1
            else -> 0
        }

        val coinsEarned = (state.score / 10).coerceAtLeast(15) + (correctCount * 10)
        val xpEarned = (state.score / 5).coerceAtLeast(30)

        val result = QuizResult(
            score = state.score,
            correctAnswers = correctCount,
            totalQuestions = totalCount,
            maxCombo = state.maxCombo,
            coinsEarned = coinsEarned,
            xpEarned = xpEarned,
            stars = stars,
            reviews = state.reviews
        )

        _lastResult.value = result
        repository.addRewards(coinsEarned, xpEarned, state.score, correctCount)
        _currentScreen.value = AppScreen.RESULTS
    }

    // Lifelines
    fun useFiftyFifty() {
        val state = _gameState.value
        if (state.isRevealed || state.disabledOptions.isNotEmpty()) return
        val question = state.currentQuestion ?: return

        if (!repository.useLifeline(LifelineType.FIFTY_FIFTY)) {
            _userMessage.value = "No 50:50 lifelines left! Buy in Store."
            return
        }

        val wrongIndices = (0 until question.options.size).filter { it != question.correctIndex }
        val toDisable = wrongIndices.shuffled().take(2).toSet()

        _gameState.update { it.copy(disabledOptions = toDisable) }
    }

    fun useExtraTime() {
        val state = _gameState.value
        if (state.isRevealed) return

        if (!repository.useLifeline(LifelineType.EXTRA_TIME)) {
            _userMessage.value = "No Extra Time lifelines left! Buy in Store."
            return
        }

        _gameState.update { it.copy(timeRemaining = it.timeRemaining + 15) }
    }

    fun useSkipQuestion() {
        val state = _gameState.value
        if (state.isRevealed) return

        if (!repository.useLifeline(LifelineType.SKIP)) {
            _userMessage.value = "No Skip lifelines left! Buy in Store."
            return
        }

        // Move directly to next question without score penalty
        timerJob?.cancel()
        val question = state.currentQuestion ?: return
        val review = QuestionReview(
            question = question,
            selectedIndex = null,
            isCorrect = false,
            timeSpentSeconds = 15 - state.timeRemaining
        )
        _gameState.update { it.copy(reviews = it.reviews + review) }
        nextQuestion()
    }

    fun useDoublePoints() {
        val state = _gameState.value
        if (state.isRevealed || state.isDoublePointsActive) return

        if (!repository.useLifeline(LifelineType.DOUBLE_POINTS)) {
            _userMessage.value = "No 2x Points lifelines left! Buy in Store."
            return
        }

        _gameState.update { it.copy(isDoublePointsActive = true) }
    }

    // Store & Progression Actions
    fun buyLifeline(type: LifelineType) {
        val success = repository.buyLifeline(type)
        if (success) {
            _userMessage.value = "Purchased ${type.title} lifeline!"
        } else {
            _userMessage.value = "Not enough coins!"
        }
    }

    fun unlockCategory(category: QuizCategory) {
        val success = repository.unlockCategory(category)
        if (success) {
            _userMessage.value = "Unlocked ${category.title}!"
        } else {
            _userMessage.value = "Need 100 coins to unlock this category."
        }
    }

    fun claimDailyReward() {
        val coins = repository.claimDailyReward()
        _dailyRewardAmount.value = coins
    }

    fun dismissDailyReward() {
        _dailyRewardAmount.value = null
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    // Monetization (Real Unity Rewarded & Interstitial Ads)
    fun showRewardedAd(rewardType: AdRewardType) {
        _pendingReward.value = rewardType
        _adRequest.value = rewardType
    }

    fun showInterstitialAd() {
        _interstitialRequest.value = true
    }

    fun clearAdRequest() {
        _adRequest.value = null
    }

    fun clearInterstitialRequest() {
        _interstitialRequest.value = false
    }

    fun handleAdError(error: String) {
        _userMessage.value = "Unity Ads Status: $error"
        // In test mode, if ad failed to load/show, grant reward so the tester is not blocked
        completeRewardedAd()
    }

    fun completeRewardedAd() {
        val reward = _pendingReward.value
        _isShowingAd.value = false
        _pendingReward.value = null
        _adRequest.value = null

        when (reward) {
            AdRewardType.REFILL_HEARTS -> {
                repository.refillHearts()
                _userMessage.value = "Hearts completely refilled! ❤️"
            }
            AdRewardType.DOUBLE_QUIZ_REWARDS -> {
                val res = _lastResult.value
                if (res != null) {
                    val bonusCoins = res.coinsEarned
                    val bonusXp = res.xpEarned
                    repository.addRewards(bonusCoins, bonusXp, 0, 0)
                    _lastResult.value = res.copy(
                        coinsEarned = res.coinsEarned * 2,
                        xpEarned = res.xpEarned * 2
                    )
                    _userMessage.value = "Double Rewards Claimed! +$bonusCoins Coins"
                }
            }
            AdRewardType.BONUS_COINS -> {
                repository.addRewards(100, 20, 0, 0)
                _userMessage.value = "Earned +100 Coins from Unity Test Ad!"
            }
            null -> {}
        }
    }

    fun dismissAdWithoutReward() {
        _isShowingAd.value = false
        _pendingReward.value = null
        _adRequest.value = null
        _userMessage.value = "Video skipped early. No reward granted."
    }

    fun getNationalLeaderboard() = repository.getLeaderboard(isNational = true)
    fun getGlobalLeaderboard() = repository.getLeaderboard(isNational = false)
}
