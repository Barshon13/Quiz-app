package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.Difficulty
import com.example.data.model.LeaderboardEntry
import com.example.data.model.LifelineType
import com.example.data.model.Question
import com.example.data.model.QuizCategory
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuizRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("quiz_master_prefs", Context.MODE_PRIVATE)

    private val _userProfile = MutableStateFlow(loadUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Curated verified questions bank (Both Domestic Bangladesh & International)
    val questionBank: List<Question> = listOf(
        // Bangladesh Liberation War 1971
        Question(
            id = "bd_lib_1",
            text = "On which historic date did Bangabandhu deliver his landmark speech declaring 'The struggle this time is for our emancipation'?",
            options = listOf("26 March 1971", "7 March 1971", "16 December 1971", "25 March 1971"),
            correctIndex = 1,
            category = QuizCategory.BD_LIBERATION,
            difficulty = Difficulty.EASY,
            explanation = "On 7 March 1971, Bangabandhu Sheikh Mujibur Rahman delivered his momentous speech at the Suhrawardy Udyan (formerly Race Course Maidan) in Dhaka, recognized by UNESCO on the Memory of the World Register."
        ),
        Question(
            id = "bd_lib_2",
            text = "How many war heroes were awarded the highest military gallantry title 'Bir Sreshtho' in Bangladesh?",
            options = listOf("5", "7", "11", "68"),
            correctIndex = 1,
            category = QuizCategory.BD_LIBERATION,
            difficulty = Difficulty.EASY,
            explanation = "7 heroic freedom fighters received the highest bravery honor 'Bir Sreshtho' for their supreme martyrdom during the 1971 Liberation War."
        ),
        Question(
            id = "bd_lib_3",
            text = "Where was the historic Mujibnagar Government sworn in on 17 April 1971?",
            options = listOf("Meherpur (Baidyanathtala)", "Kushtia", "Tungipara", "Jessore"),
            correctIndex = 0,
            category = QuizCategory.BD_LIBERATION,
            difficulty = Difficulty.MEDIUM,
            explanation = "The Provisional Government of the People's Republic of Bangladesh was officially sworn in at Baidyanathtala in Meherpur, later renamed Mujibnagar."
        ),
        Question(
            id = "bd_lib_4",
            text = "During the 1971 Liberation War, Bangladesh was divided into how many operational administrative sectors?",
            options = listOf("7 Sectors", "9 Sectors", "11 Sectors", "14 Sectors"),
            correctIndex = 2,
            category = QuizCategory.BD_LIBERATION,
            difficulty = Difficulty.MEDIUM,
            explanation = "Under Commander-in-Chief General M. A. G. Osmani, the entire territory was strategically partitioned into 11 military operational sectors."
        ),
        Question(
            id = "bd_lib_5",
            text = "Who was the Commander of Sector 2 of the Mukti Bahini, famous for leading operations around Dhaka and Comilla?",
            options = listOf("Major Ziaur Rahman", "Major Khaled Mosharraf", "Major K. M. Shafiullah", "Major Abu Taher"),
            correctIndex = 1,
            category = QuizCategory.BD_LIBERATION,
            difficulty = Difficulty.HARD,
            explanation = "Major Khaled Mosharraf initially commanded Sector 2 and the elite 'K-Force' until he was wounded in battle in late October 1971, after which Major A. T. M. Haider took command."
        ),

        // Bangladesh Rivers & Geography
        Question(
            id = "bd_geo_1",
            text = "Which is the largest mangrove forest in the world, shared predominantly between Bangladesh and India?",
            options = listOf("Sundarbans", "Amazon Basin", "Pichavaram", "Daintree Rainforest"),
            correctIndex = 0,
            category = QuizCategory.BD_GEOGRAPHY,
            difficulty = Difficulty.EASY,
            explanation = "The Sundarbans delta represents the planet's largest continuous mangrove biome and is home to the Royal Bengal Tiger."
        ),
        Question(
            id = "bd_geo_2",
            text = "Where do the two majestic rivers Padma and Meghna converge in Bangladesh?",
            options = listOf("Aricha", "Chandpur", "Goalundo", "Bhairab"),
            correctIndex = 1,
            category = QuizCategory.BD_GEOGRAPHY,
            difficulty = Difficulty.MEDIUM,
            explanation = "The mighty Padma and Meghna unite at Chandpur before flowing south together as the Lower Meghna into the Bay of Bengal."
        ),
        Question(
            id = "bd_geo_3",
            text = "Which unbroken natural sandy sea beach in Bangladesh is renowned as the longest in the world?",
            options = listOf("Kuakata Beach", "Cox's Bazar Beach", "Patenga Beach", "Inani Beach"),
            correctIndex = 1,
            category = QuizCategory.BD_GEOGRAPHY,
            difficulty = Difficulty.EASY,
            explanation = "Cox's Bazar spans an uninterrupted natural sandy stretch of approximately 120 km (75 miles) along the Bay of Bengal."
        ),
        Question(
            id = "bd_geo_4",
            text = "What is considered the highest recognized peak in Bangladesh by official geographical survey?",
            options = listOf("Keokradong", "Saka Haphong (Mowdok Mual)", "Tajingdong", "Garo Hills"),
            correctIndex = 1,
            category = QuizCategory.BD_GEOGRAPHY,
            difficulty = Difficulty.HARD,
            explanation = "Saka Haphong (also known as Mowdok Mual), located on the Bangladesh-Myanmar border in Bandarban, stands at ~1,052 meters (3,451 ft)."
        ),
        Question(
            id = "bd_geo_5",
            text = "The river Brahmaputra changes its principal channel name upon entering Bangladesh. What is it called?",
            options = listOf("Jamuna", "Karnafuli", "Surma", "Teesta"),
            correctIndex = 0,
            category = QuizCategory.BD_GEOGRAPHY,
            difficulty = Difficulty.EASY,
            explanation = "After an earthquake in 1787, the Brahmaputra shifted westward to form the massive braided Jamuna River."
        ),

        // Bangladesh Culture & Heritage
        Question(
            id = "bd_her_1",
            text = "Which colorful procession celebrated on Pohela Boishakh was inscribed on the UNESCO Intangible Cultural Heritage list in 2016?",
            options = listOf("Mangal Shobhajatra", "Boshonto Utshob", "Nouka Baich", "Lalon Mela"),
            correctIndex = 0,
            category = QuizCategory.BD_HERITAGE,
            difficulty = Difficulty.EASY,
            explanation = "Mangal Shobhajatra, initiated by students and faculty of Dhaka University's Faculty of Fine Arts in 1989, celebrates humanity and the Bengali New Year."
        ),
        Question(
            id = "bd_her_2",
            text = "Which traditional handloom woven fabric from Bangladesh earned the prestigious UNESCO Intangible Cultural Heritage recognition?",
            options = listOf("Khadi", "Muslin Jamdani", "Monipuri Silk", "Katan"),
            correctIndex = 1,
            category = QuizCategory.BD_HERITAGE,
            difficulty = Difficulty.MEDIUM,
            explanation = "Jamdani is a vividly patterned, sheer cotton weave developed over centuries by artisans along the Shitalakshya River in Narayanganj."
        ),
        Question(
            id = "bd_her_3",
            text = "Who wrote the lyrics and composed the melody for Bangladesh's national anthem 'Amar Sonar Bangla'?",
            options = listOf("Kazi Nazrul Islam", "Rabindranath Tagore", "Dwijendralal Ray", "Atulprasad Sen"),
            correctIndex = 1,
            category = QuizCategory.BD_HERITAGE,
            difficulty = Difficulty.EASY,
            explanation = "Nobel laureate Rabindranath Tagore composed 'Amar Sonar Bangla' in 1905 during the Swadeshi movement; its first 10 lines form Bangladesh's anthem."
        ),
        Question(
            id = "bd_her_4",
            text = "Which architectural landmark in Paharpur is recognized as the largest Buddhist vihara south of the Himalayas?",
            options = listOf("Somapura Mahavihara", "Mainamati Vihara", "Shalban Vihara", "Mahasthangarh"),
            correctIndex = 0,
            category = QuizCategory.BD_HERITAGE,
            difficulty = Difficulty.HARD,
            explanation = "Somapura Mahavihara at Paharpur in Naogaon district, constructed during the 8th century Pala Empire, is a UNESCO World Heritage Site."
        ),

        // Bangladesh Sports & Economy
        Question(
            id = "bd_sp_1",
            text = "In what year did Bangladesh achieve full Test status in international cricket from the ICC?",
            options = listOf("1997", "1999", "2000", "2003"),
            correctIndex = 2,
            category = QuizCategory.BD_SPORTS_ECONOMY,
            difficulty = Difficulty.MEDIUM,
            explanation = "Bangladesh became the 10th Test-playing nation on 26 June 2000 and played its inaugural Test match against India at Bangabandhu National Stadium in November 2000."
        ),
        Question(
            id = "bd_sp_2",
            text = "What is the total length of the multi-purpose double-deck Padma Bridge connecting southwestern districts?",
            options = listOf("4.80 km", "6.15 km", "7.25 km", "9.10 km"),
            correctIndex = 1,
            category = QuizCategory.BD_SPORTS_ECONOMY,
            difficulty = Difficulty.EASY,
            explanation = "The Padma Multipurpose Bridge measures exactly 6.15 kilometers across the Padma River, inaugurating a historic direct transport corridor in June 2022."
        ),
        Question(
            id = "bd_sp_3",
            text = "Which sector serves as the single largest contributor to Bangladesh's export earnings?",
            options = listOf("Jute & Jute Goods", "Ready-Made Garments (RMG)", "Pharmaceuticals", "Leather"),
            correctIndex = 1,
            category = QuizCategory.BD_SPORTS_ECONOMY,
            difficulty = Difficulty.EASY,
            explanation = "The Ready-Made Garments (RMG) industry accounts for over 80% of Bangladesh's total merchandise export revenues globally."
        ),
        Question(
            id = "bd_sp_4",
            text = "Who is the legendary Bangladeshi all-rounder who holds the record for most wickets and runs combination in ICC ODI Cricket World Cups?",
            options = listOf("Mashrafe Mortaza", "Shakib Al Hasan", "Mushfiqur Rahim", "Tamim Iqbal"),
            correctIndex = 1,
            category = QuizCategory.BD_SPORTS_ECONOMY,
            difficulty = Difficulty.EASY,
            explanation = "Shakib Al Hasan is widely regarded as one of cricket history's greatest all-rounders, boasting exceptional scoring and wicket records in ICC tournaments."
        ),

        // World History
        Question(
            id = "wh_1",
            text = "In which year did the historic Fall of the Western Roman Empire traditionally take place?",
            options = listOf("330 AD", "410 AD", "476 AD", "1453 AD"),
            correctIndex = 2,
            category = QuizCategory.WORLD_HISTORY,
            difficulty = Difficulty.MEDIUM,
            explanation = "In 476 AD, Germanic chieftain Odoacer deposed the last Western Roman Emperor, Romulus Augustulus, marking the start of the European Middle Ages."
        ),
        Question(
            id = "wh_2",
            text = "Which English constitutional charter was signed by King John in 1215, laying the foundation for modern rule of law?",
            options = listOf("Bill of Rights", "Magna Carta", "Habeas Corpus Act", "Treaty of Versailles"),
            correctIndex = 1,
            category = QuizCategory.WORLD_HISTORY,
            difficulty = Difficulty.EASY,
            explanation = "The Magna Carta (Great Charter) established for the first time the principle that everyone, including the sovereign king, was subject to the law."
        ),
        Question(
            id = "wh_3",
            text = "Who was the first Emperor of a unified China, known for constructing the Terracotta Army?",
            options = listOf("Kublai Khan", "Qin Shi Huang", "Sun Tzu", "Han Wudi"),
            correctIndex = 1,
            category = QuizCategory.WORLD_HISTORY,
            difficulty = Difficulty.MEDIUM,
            explanation = "Qin Shi Huang unified the Warring States in 221 BC, initiated the Great Wall linkage, and was buried with the famed Terracotta Army in Xi'an."
        ),
        Question(
            id = "wh_4",
            text = "The Battle of Waterloo in 1815 resulted in the definitive defeat of which military commander?",
            options = listOf("Napoleon Bonaparte", "Duke of Wellington", "Otto von Bismarck", "Alexander the Great"),
            correctIndex = 0,
            category = QuizCategory.WORLD_HISTORY,
            difficulty = Difficulty.EASY,
            explanation = "Napoleon Bonaparte suffered his decisive defeat near Waterloo in modern-day Belgium against Anglo-allied and Prussian forces."
        ),

        // World Geography
        Question(
            id = "wg_1",
            text = "Which is the deepest known oceanic trench on planet Earth?",
            options = listOf("Puerto Rico Trench", "Java Trench", "Mariana Trench", "Tonga Trench"),
            correctIndex = 2,
            category = QuizCategory.WORLD_GEOGRAPHY,
            difficulty = Difficulty.EASY,
            explanation = "The Mariana Trench in the western Pacific Ocean reaches the Challenger Deep at nearly 11,000 meters (36,000 feet) below sea level."
        ),
        Question(
            id = "wg_2",
            text = "Which African country possesses the highest number of ancient pyramids in the world?",
            options = listOf("Egypt", "Sudan", "Libya", "Ethiopia"),
            correctIndex = 1,
            category = QuizCategory.WORLD_GEOGRAPHY,
            difficulty = Difficulty.MEDIUM,
            explanation = "Sudan has between 200 and 255 known pyramids built by the ancient Kingdom of Kush, surpassing Egypt's ~138 pyramids."
        ),
        Question(
            id = "wg_3",
            text = "What is the only sea in the world with no terrestrial land coastline, bounded entirely by ocean currents?",
            options = listOf("Sargasso Sea", "Coral Sea", "Weddell Sea", "Tasman Sea"),
            correctIndex = 0,
            category = QuizCategory.WORLD_GEOGRAPHY,
            difficulty = Difficulty.HARD,
            explanation = "The Sargasso Sea in the North Atlantic is defined purely by four circulating ocean currents rather than land borders."
        ),

        // Science & Discovery
        Question(
            id = "sci_1",
            text = "Who is credited with discovering the Double Helix structure of DNA alongside James Watson in 1953?",
            options = listOf("Francis Crick", "Gregor Mendel", "Louis Pasteur", "Alexander Fleming"),
            correctIndex = 0,
            category = QuizCategory.SCIENCE,
            difficulty = Difficulty.EASY,
            explanation = "Francis Crick and James Watson deduced the double-helix geometry of DNA, supported critically by Rosalind Franklin's X-ray crystallography Photo 51."
        ),
        Question(
            id = "sci_2",
            text = "Which elementary subatomic particle carries no electrical charge and has an extremely tiny mass?",
            options = listOf("Electron", "Positron", "Neutrino", "Proton"),
            correctIndex = 2,
            category = QuizCategory.SCIENCE,
            difficulty = Difficulty.MEDIUM,
            explanation = "Neutrinos are neutral leptons with infinitesimal mass that interact only via gravity and the weak subatomic force."
        ),
        Question(
            id = "sci_3",
            text = "What is the only chemical element whose atomic nucleus contains no neutrons in its most abundant isotope?",
            options = listOf("Helium", "Hydrogen", "Lithium", "Carbon"),
            correctIndex = 1,
            category = QuizCategory.SCIENCE,
            difficulty = Difficulty.MEDIUM,
            explanation = "Standard protium (the most abundant isotope of Hydrogen) consists of a single proton orbited by one electron, containing zero neutrons."
        ),

        // Space & Technology
        Question(
            id = "tech_1",
            text = "Which infrared space observatory launched on Christmas Day 2021 as the scientific successor to Hubble?",
            options = listOf("Kepler", "James Webb Space Telescope", "Spitzer", "Chandra"),
            correctIndex = 1,
            category = QuizCategory.TECH_SPACE,
            difficulty = Difficulty.EASY,
            explanation = "The James Webb Space Telescope (JWST) orbits the Sun-Earth L2 Lagrange point, capturing views of the earliest galaxies."
        ),
        Question(
            id = "tech_2",
            text = "What was the name of the first human-made satellite placed into Earth's orbit by the Soviet Union in 1957?",
            options = listOf("Vostok 1", "Sputnik 1", "Explorer 1", "Luna 2"),
            correctIndex = 1,
            category = QuizCategory.TECH_SPACE,
            difficulty = Difficulty.EASY,
            explanation = "Sputnik 1 was launched on 4 October 1957, initiating the global Space Race."
        ),
        Question(
            id = "tech_3",
            text = "In computer science, what does the abbreviation 'GPU' stand for?",
            options = listOf("General Processing Utility", "Graphics Processing Unit", "Graphical Performance Unified", "Global Packet Unit"),
            correctIndex = 1,
            category = QuizCategory.TECH_SPACE,
            difficulty = Difficulty.EASY,
            explanation = "A Graphics Processing Unit (GPU) is a specialized electronic circuit designed to rapidly manipulate memory for accelerated image rendering and AI tensor compute."
        )
    )

    private fun loadUserProfile(): UserProfile {
        val name = prefs.getString("user_name", "Quiz Pioneer") ?: "Quiz Pioneer"
        val level = prefs.getInt("user_level", 1)
        val xp = prefs.getInt("user_xp", 180)
        val coins = prefs.getInt("user_coins", 420)
        val hearts = prefs.getInt("user_hearts", 5)
        val currentStreak = prefs.getInt("user_streak", 4)
        val highestStreak = prefs.getInt("user_max_streak", 7)
        val highScore = prefs.getInt("user_high_score", 1680)
        val totalQuizzes = prefs.getInt("user_total_quizzes", 14)
        val correctCount = prefs.getInt("user_correct_count", 64)

        return UserProfile(
            name = name,
            level = level,
            xp = xp,
            coins = coins,
            hearts = hearts,
            currentStreak = currentStreak,
            highestStreak = highestStreak,
            highScore = highScore,
            totalQuizzes = totalQuizzes,
            correctCount = correctCount
        )
    }

    private fun saveUserProfile(profile: UserProfile) {
        prefs.edit().apply {
            putString("user_name", profile.name)
            putInt("user_level", profile.level)
            putInt("user_xp", profile.xp)
            putInt("user_coins", profile.coins)
            putInt("user_hearts", profile.hearts)
            putInt("user_streak", profile.currentStreak)
            putInt("user_max_streak", profile.highestStreak)
            putInt("user_high_score", profile.highScore)
            putInt("user_total_quizzes", profile.totalQuizzes)
            putInt("user_correct_count", profile.correctCount)
            apply()
        }
    }

    fun getQuestionsForGame(category: QuizCategory?, count: Int = 10): List<Question> {
        val pool = if (category != null) {
            questionBank.filter { it.category == category }
        } else {
            questionBank
        }
        return pool.shuffled().take(count.coerceAtMost(pool.size))
    }

    fun getDailyChallengeQuestions(): List<Question> {
        // 5 curated diverse questions across categories
        val domestic = questionBank.filter { it.category.isDomestic }.shuffled().take(3)
        val world = questionBank.filter { !it.category.isDomestic }.shuffled().take(2)
        return (domestic + world).shuffled()
    }

    fun deductHeart(): Boolean {
        var success = false
        _userProfile.update { current ->
            if (current.hearts > 0) {
                success = true
                val updated = current.copy(hearts = current.hearts - 1)
                saveUserProfile(updated)
                updated
            } else {
                current
            }
        }
        return success
    }

    fun refillHearts() {
        _userProfile.update { current ->
            val updated = current.copy(hearts = current.maxHearts)
            saveUserProfile(updated)
            updated
        }
    }

    fun addRewards(coins: Int, xp: Int, wonScore: Int, correct: Int) {
        _userProfile.update { current ->
            val newCoins = current.coins + coins
            val newXp = current.xp + xp
            val newLevel = (newXp / 300) + 1
            val newHighScore = maxOf(current.highScore, wonScore)
            val newStreak = current.currentStreak + 1
            val newMaxStreak = maxOf(current.highestStreak, newStreak)

            val updated = current.copy(
                coins = newCoins,
                xp = newXp,
                level = newLevel,
                highScore = newHighScore,
                currentStreak = newStreak,
                highestStreak = newMaxStreak,
                totalQuizzes = current.totalQuizzes + 1,
                correctCount = current.correctCount + correct
            )
            saveUserProfile(updated)
            updated
        }
    }

    fun useLifeline(type: LifelineType): Boolean {
        var used = false
        _userProfile.update { current ->
            val count = current.lifelines[type] ?: 0
            if (count > 0) {
                used = true
                val newMap = current.lifelines.toMutableMap()
                newMap[type] = count - 1
                current.copy(lifelines = newMap)
            } else {
                current
            }
        }
        return used
    }

    fun buyLifeline(type: LifelineType): Boolean {
        var bought = false
        _userProfile.update { current ->
            if (current.coins >= type.cost) {
                bought = true
                val newMap = current.lifelines.toMutableMap()
                newMap[type] = (newMap[type] ?: 0) + 1
                val updated = current.copy(
                    coins = current.coins - type.cost,
                    lifelines = newMap
                )
                saveUserProfile(updated)
                updated
            } else {
                current
            }
        }
        return bought
    }

    fun claimDailyReward(): Int {
        val rewardAmount = 150
        _userProfile.update { current ->
            val updated = current.copy(coins = current.coins + rewardAmount)
            saveUserProfile(updated)
            updated
        }
        return rewardAmount
    }

    fun unlockCategory(category: QuizCategory, cost: Int = 100): Boolean {
        var unlocked = false
        _userProfile.update { current ->
            if (current.coins >= cost && !current.unlockedCategories.contains(category.id)) {
                unlocked = true
                val updatedSet = current.unlockedCategories + category.id
                val updated = current.copy(
                    coins = current.coins - cost,
                    unlockedCategories = updatedSet
                )
                saveUserProfile(updated)
                updated
            } else {
                current
            }
        }
        return unlocked
    }

    fun getLeaderboard(isNational: Boolean): List<LeaderboardEntry> {
        val currentProfile = _userProfile.value
        return if (isNational) {
            listOf(
                LeaderboardEntry(1, "Tanvir Ahmed", 4950, "Dhaka", "🏆 Grandmaster"),
                LeaderboardEntry(2, "Sadia Nusrat", 4420, "Chittagong", "🥇 Master"),
                LeaderboardEntry(3, "Nayeem Hasan", 3880, "Sylhet", "🥈 Expert"),
                LeaderboardEntry(4, "Farhan Kabir", 3250, "Rajshahi", "🥉 Scholar"),
                LeaderboardEntry(5, "Nusrat Jahan", 2990, "Khulna", "⭐ Veteran"),
                LeaderboardEntry(6, currentProfile.name, currentProfile.highScore, "Bangladesh", "⚡ Pioneer", isCurrentUser = true),
                LeaderboardEntry(7, "Arif Hossain", 1420, "Barisal", "🌟 Challenger"),
                LeaderboardEntry(8, "Mehzabin R.", 1180, "Rangpur", "🎯 Scholar")
            ).sortedByDescending { it.score }
                .mapIndexed { index, item -> item.copy(rank = index + 1) }
        } else {
            listOf(
                LeaderboardEntry(1, "Elena Rostova", 7820, "United Kingdom", "👑 World Champion"),
                LeaderboardEntry(2, "Kenji Sato", 6940, "Japan", "🏆 Grandmaster"),
                LeaderboardEntry(3, "Marcus Vance", 6120, "United States", "🥇 Legend"),
                LeaderboardEntry(4, "Amira Al-Mansoor", 5430, "UAE", "🥈 Elite"),
                LeaderboardEntry(5, "Lucas Silva", 4710, "Brazil", "🥉 Scholar"),
                LeaderboardEntry(6, currentProfile.name, currentProfile.highScore, "Global", "⚡ Pioneer", isCurrentUser = true),
                LeaderboardEntry(7, "Chloe Martin", 1580, "France", "⭐ Challenger"),
                LeaderboardEntry(8, "Rohan Sharma", 1340, "India", "🎯 Aspirant")
            ).sortedByDescending { it.score }
                .mapIndexed { index, item -> item.copy(rank = index + 1) }
        }
    }
}
