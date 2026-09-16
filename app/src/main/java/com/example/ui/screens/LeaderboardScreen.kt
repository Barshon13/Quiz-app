package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LeaderboardEntry
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val entries = if (selectedTab == 0) viewModel.getNationalLeaderboard() else viewModel.getGlobalLeaderboard()
    val currentUserEntry = entries.find { it.isCurrentUser }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("leaderboard_screen"),
        containerColor = ObsidianBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Global & National Ranks",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        modifier = Modifier.testTag("back_from_leaderboard_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ObsidianSurface)
            )
        },
        bottomBar = {
            Column {
                // Sticky Current User Rank card
                if (currentUserEntry != null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GoldPrimary, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        color = ObsidianSurfaceElevated
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(GoldPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#${currentUserEntry.rank}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextDark
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Your Standing",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "${currentUserEntry.name} (${currentUserEntry.region})",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }

                            Text(
                                text = "${currentUserEntry.score} pts",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldPrimary
                            )
                        }
                    }
                }

                // Bottom Navigation
                NavigationBar(
                    containerColor = ObsidianSurface,
                    contentColor = TextPrimary
                ) {
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Play") },
                        label = { Text("Play") },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = { Icon(Icons.Default.Leaderboard, contentDescription = "Ranks") },
                        label = { Text("Leaderboard", fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TextDark,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.navigateTo(AppScreen.STORE) },
                        icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Store") },
                        label = { Text("Store") },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tab row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = ObsidianSurface,
                contentColor = GoldPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = GoldPrimary
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "🇧🇩 Bangladesh",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) GoldPrimary else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_bangladesh_leaderboard")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "🌍 Worldwide",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) GoldPrimary else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_global_leaderboard")
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Top 3 Podium Cards
                item {
                    PodiumDisplay(entries.take(3))
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(entries) { entry ->
                    LeaderboardRow(entry = entry)
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun PodiumDisplay(topThree: List<LeaderboardEntry>) {
    if (topThree.size < 3) return
    val rank1 = topThree.getOrNull(0) ?: return
    val rank2 = topThree.getOrNull(1) ?: return
    val rank3 = topThree.getOrNull(2) ?: return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        // Rank 2
        PodiumPillar(
            entry = rank2,
            rank = 2,
            color = Color(0xFFC0C0C0),
            height = 90.dp
        )
        // Rank 1
        PodiumPillar(
            entry = rank1,
            rank = 1,
            color = GoldPrimary,
            height = 115.dp
        )
        // Rank 3
        PodiumPillar(
            entry = rank3,
            rank = 3,
            color = Color(0xFFCD7F32),
            height = 75.dp
        )
    }
}

@Composable
fun PodiumPillar(
    entry: LeaderboardEntry,
    rank: Int,
    color: Color,
    height: androidx.compose.ui.unit.Dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(95.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (rank == 1) 48.dp else 40.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f))
                .border(2.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = entry.name.take(1),
                fontSize = if (rank == 1) 18.sp else 15.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = entry.name.split(" ").firstOrNull() ?: entry.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            maxLines = 1
        )
        Text(
            text = "${entry.score}",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .width(85.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(color.copy(alpha = 0.35f), ObsidianCard)
                    )
                )
                .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$rank",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}

@Composable
fun LeaderboardRow(entry: LeaderboardEntry) {
    val isHighlighted = entry.isCurrentUser
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (isHighlighted) GoldPrimary else ObsidianCardBorder,
                RoundedCornerShape(14.dp)
            )
            .testTag("leaderboard_row_${entry.rank}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) ObsidianSurfaceElevated else ObsidianCard
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            when (entry.rank) {
                                1 -> GoldPrimary.copy(alpha = 0.3f)
                                2 -> Color.LightGray.copy(alpha = 0.3f)
                                3 -> Color(0xFFCD7F32).copy(alpha = 0.3f)
                                else -> ObsidianSurfaceElevated
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${entry.rank}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (entry.rank) {
                            1 -> GoldPrimary
                            2 -> Color.White
                            3 -> Color(0xFFFFB703)
                            else -> TextSecondary
                        }
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = entry.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isHighlighted) GoldPrimary else TextPrimary
                        )
                        if (isHighlighted) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GoldPrimary)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "YOU",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextDark
                                )
                            }
                        }
                    }

                    Text(
                        text = "${entry.region} • ${entry.badge}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Text(
                text = "${entry.score} pts",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isHighlighted) GoldPrimary else TextPrimary
            )
        }
    }
}
