package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizCategory
import com.example.ui.components.SponsorAdBanner
import com.example.ui.theme.CrimsonError
import com.example.ui.theme.EmeraldDark
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
import com.example.ui.viewmodel.AdRewardType
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizViewModel

@Composable
fun HomeScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        containerColor = ObsidianBg,
        bottomBar = {
            Column {
                SponsorAdBanner(
                    onAdClick = {
                        viewModel.showRewardedAd(AdRewardType.BONUS_COINS)
                    }
                )
                NavigationBar(
                    containerColor = ObsidianSurface,
                    contentColor = TextPrimary
                ) {
                    NavigationBarItem(
                        selected = true,
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Play") },
                        label = { Text("Play", fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TextDark,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        ),
                        modifier = Modifier.testTag("nav_play_tab")
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.navigateTo(AppScreen.LEADERBOARD) },
                        icon = { Icon(Icons.Default.Leaderboard, contentDescription = "Ranks") },
                        label = { Text("Leaderboard") },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        ),
                        modifier = Modifier.testTag("nav_leaderboard_tab")
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.navigateTo(AppScreen.STORE) },
                        icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Shop") },
                        label = { Text("Store") },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        ),
                        modifier = Modifier.testTag("nav_store_tab")
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("home_content_grid"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: Player Profile Bar
            item(span = { GridItemSpan(2) }) {
                PlayerProfileHeader(
                    name = userProfile.name,
                    level = userProfile.level,
                    xp = userProfile.xp,
                    coins = userProfile.coins,
                    hearts = userProfile.hearts,
                    maxHearts = userProfile.maxHearts,
                    streak = userProfile.currentStreak,
                    onRefillHearts = {
                        viewModel.showRewardedAd(AdRewardType.REFILL_HEARTS)
                    },
                    onOpenStore = {
                        viewModel.navigateTo(AppScreen.STORE)
                    }
                )
            }

            // Daily Challenge Hero Card
            item(span = { GridItemSpan(2) }) {
                DailyChallengeBanner(
                    streak = userProfile.currentStreak,
                    onPlay = { viewModel.startDailyChallenge() },
                    onClaimStreak = { viewModel.claimDailyReward() }
                )
            }

            // Quick Play Button
            item(span = { GridItemSpan(2) }) {
                QuickPlayHeroCard(
                    hearts = userProfile.hearts,
                    onStart = { viewModel.startQuickPlay() }
                )
            }

            // Section Header: Categories
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quiz Categories",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Domestic & World",
                        fontSize = 12.sp,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 8 Categories
            items(QuizCategory.values()) { category ->
                val isUnlocked = userProfile.unlockedCategories.contains(category.id)
                val stars = userProfile.categoryStars[category.id] ?: 0

                CategoryCard(
                    category = category,
                    isUnlocked = isUnlocked,
                    stars = stars,
                    onPlay = { viewModel.startCategoryGame(category) },
                    onUnlock = { viewModel.unlockCategory(category) }
                )
            }

            // Bottom Spacing
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PlayerProfileHeader(
    name: String,
    level: Int,
    xp: Int,
    coins: Int,
    hearts: Int,
    maxHearts: Int,
    streak: Int,
    onRefillHearts: () -> Unit,
    onOpenStore: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Avatar + Level
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(GoldPrimary, GoldSecondary))
                            )
                            .border(2.dp, GoldPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.take(1).uppercase(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextDark
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GoldPrimary.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "LVL $level",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🔥 $streak day streak",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                // Hearts & Coins Status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Hearts
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, CrimsonError.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .clickable { if (hearts < maxHearts) onRefillHearts() }
                            .testTag("hearts_counter"),
                        color = ObsidianCard
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Hearts",
                                tint = CrimsonError,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$hearts/$maxHearts",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Coins
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .clickable { onOpenStore() }
                            .testTag("coins_counter"),
                        color = ObsidianCard
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = "Coins",
                                tint = GoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$coins",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // XP Progress Bar
            val xpNextLevel = level * 300
            val xpCurrent = xp % 300
            val xpProgress = (xpCurrent.toFloat() / 300f).coerceIn(0f, 1f)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rank Progress",
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Text(
                    text = "$xpCurrent / 300 XP",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { xpProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = EmeraldSuccess,
                trackColor = ObsidianSurfaceElevated
            )
        }
    }
}

@Composable
fun DailyChallengeBanner(
    streak: Int,
    onPlay: () -> Unit,
    onClaimStreak: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.5.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            .testTag("daily_challenge_card"),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            EmeraldDark.copy(alpha = 0.45f),
                            ObsidianCard
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DAILY CHALLENGE",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldPrimary,
                            letterSpacing = 1.sp
                        )
                    }

                    Text(
                        text = "🎁 +150 Coins Bonus",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSuccess
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "5 Verified Rapid Questions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Test your skills across Bangladesh History and World Geography!",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onPlay,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("play_daily_challenge_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                    ) {
                        Text(
                            text = "Play Challenge",
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }

                    Button(
                        onClick = onClaimStreak,
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("claim_daily_streak_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ObsidianSurfaceElevated)
                    ) {
                        Text(
                            text = "Daily Bonus",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickPlayHeroCard(
    hearts: Int,
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onStart() }
            .testTag("quick_play_card"),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurfaceElevated)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(EmeraldSuccess, EmeraldDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Quick Play",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "8 Fast Adaptive Questions • 1 Heart",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(GoldPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start",
                    tint = TextDark,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: QuizCategory,
    isUnlocked: Boolean,
    stars: Int,
    onPlay: () -> Unit,
    onUnlock: () -> Unit
) {
    val categoryIcon = when (category) {
        QuizCategory.BD_LIBERATION -> Icons.Default.MilitaryTech
        QuizCategory.BD_GEOGRAPHY -> Icons.Default.Water
        QuizCategory.BD_HERITAGE -> Icons.Default.Palette
        QuizCategory.BD_SPORTS_ECONOMY -> Icons.Default.SportsCricket
        QuizCategory.WORLD_HISTORY -> Icons.Default.HistoryEdu
        QuizCategory.WORLD_GEOGRAPHY -> Icons.Default.Public
        QuizCategory.SCIENCE -> Icons.Default.Science
        QuizCategory.TECH_SPACE -> Icons.Default.RocketLaunch
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (isUnlocked) ObsidianCardBorder else CrimsonError.copy(alpha = 0.3f),
                RoundedCornerShape(16.dp)
            )
            .clickable {
                if (isUnlocked) onPlay() else onUnlock()
            }
            .testTag("category_card_${category.id}"),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (category.isDomestic) EmeraldSuccess.copy(alpha = 0.2f)
                            else GoldPrimary.copy(alpha = 0.2f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = categoryIcon,
                        contentDescription = null,
                        tint = if (category.isDomestic) EmeraldSuccess else GoldPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                if (!isUnlocked) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CrimsonError.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = CrimsonError,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "100",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CrimsonError
                            )
                        }
                    }
                } else {
                    Row {
                        repeat(3) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < stars) GoldPrimary else ObsidianCardBorder,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Column {
                if (category.isDomestic) {
                    Text(
                        text = "BANGLADESH",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSuccess,
                        letterSpacing = 0.5.sp
                    )
                } else {
                    Text(
                        text = "GLOBAL",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    text = category.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = category.subtitle,
                    fontSize = 11.sp,
                    color = TextMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
