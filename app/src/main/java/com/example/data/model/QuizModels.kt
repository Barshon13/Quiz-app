package com.example.data.model

enum class Difficulty(val label: String, val multiplier: Float) {
    EASY("Easy", 1.0f),
    MEDIUM("Medium", 1.5f),
    HARD("Hard", 2.0f)
}

enum class QuizCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconName: String,
    val isDomestic: Boolean
) {
    BD_LIBERATION(
        id = "bd_liberation",
        title = "Liberation War 1971",
        subtitle = "Heroes, Battles & Independence",
        iconName = "military_tech",
        isDomestic = true
    ),
    BD_GEOGRAPHY(
        id = "bd_geography",
        title = "Rivers & Geography",
        subtitle = "Delta, Sundarbans & Hills",
        iconName = "water",
        isDomestic = true
    ),
    BD_HERITAGE(
        id = "bd_heritage",
        title = "Culture & Heritage",
        subtitle = "Art, Language & Traditions",
        iconName = "palette",
        isDomestic = true
    ),
    BD_SPORTS_ECONOMY(
        id = "bd_sports_economy",
        title = "Sports & Economy",
        subtitle = "Cricket, RMG & Milestones",
        iconName = "sports_cricket",
        isDomestic = true
    ),
    WORLD_HISTORY(
        id = "world_history",
        title = "World History",
        subtitle = "Ancient Civilizations & Empires",
        iconName = "history_edu",
        isDomestic = false
    ),
    WORLD_GEOGRAPHY(
        id = "world_geography",
        title = "World Geography",
        subtitle = "Mountains, Oceans & Nations",
        iconName = "public",
        isDomestic = false
    ),
    SCIENCE(
        id = "science",
        title = "Science & Discovery",
        subtitle = "Physics, Biology & Inventions",
        iconName = "science",
        isDomestic = false
    ),
    TECH_SPACE(
        id = "tech_space",
        title = "Space & Tech",
        subtitle = "Cosmos, AI & Exploration",
        iconName = "rocket_launch",
        isDomestic = false
    )
}

data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val category: QuizCategory,
    val difficulty: Difficulty,
    val explanation: String
)

enum class LifelineType(val title: String, val cost: Int, val description: String) {
    FIFTY_FIFTY("50:50", 40, "Removes two incorrect answers"),
    EXTRA_TIME("+15s", 30, "Adds 15 seconds to timer"),
    SKIP("Skip", 50, "Skip question with no penalty"),
    DOUBLE_POINTS("2x Points", 60, "Double score for this question")
}

data class UserProfile(
    val name: String = "Quiz Pioneer",
    val level: Int = 1,
    val xp: Int = 120,
    val coins: Int = 350,
    val hearts: Int = 5,
    val maxHearts: Int = 5,
    val currentStreak: Int = 3,
    val highestStreak: Int = 7,
    val highScore: Int = 1450,
    val totalQuizzes: Int = 12,
    val correctCount: Int = 58,
    val lifelines: Map<LifelineType, Int> = mapOf(
        LifelineType.FIFTY_FIFTY to 3,
        LifelineType.EXTRA_TIME to 3,
        LifelineType.SKIP to 2,
        LifelineType.DOUBLE_POINTS to 2
    ),
    val unlockedCategories: Set<String> = setOf(
        QuizCategory.BD_LIBERATION.id,
        QuizCategory.BD_GEOGRAPHY.id,
        QuizCategory.WORLD_HISTORY.id,
        QuizCategory.SCIENCE.id
    ),
    val categoryStars: Map<String, Int> = mapOf(
        QuizCategory.BD_LIBERATION.id to 3,
        QuizCategory.BD_GEOGRAPHY.id to 2,
        QuizCategory.WORLD_HISTORY.id to 2,
        QuizCategory.SCIENCE.id to 1
    )
)

data class QuestionReview(
    val question: Question,
    val selectedIndex: Int?,
    val isCorrect: Boolean,
    val timeSpentSeconds: Int
)

data class QuizResult(
    val score: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val maxCombo: Int,
    val coinsEarned: Int,
    val xpEarned: Int,
    val stars: Int,
    val reviews: List<QuestionReview>
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val score: Int,
    val region: String,
    val badge: String,
    val isCurrentUser: Boolean = false
)
