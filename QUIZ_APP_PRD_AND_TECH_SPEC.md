# Product Requirements Document (PRD) & Technical Specification
## Project: Q-Master Elite (Premium Quiz Experience)
**Version:** 1.0.0-PROD  
**Target Platforms:** iOS & Android (Built with Flutter)  
**Architecture:** Clean Architecture + Feature-First (Riverpod 2.x)  
**Backend:** Google Firebase (Auth, Firestore, Cloud Functions, Cloud Storage, Crashlytics, Remote Config)  
**Monetization:** Unity Ads SDK (Banner, Interstitial, Rewarded Video)  
**Offline Cache Engine:** Hive Local Key-Value & Object Store  

---

## 1. Executive Summary & Product Vision

### 1.1 Product Statement
**Q-Master Elite** is an intellectual trivia and competitive quiz game designed to provide an executive-tier, tactile trivia experience. Unlike hyper-casual, ad-cluttered quiz clones, Q-Master Elite combines **100% verified factual questions** (curated from international academic, historical, scientific, and cultural records, plus domestic Bangladesh history, geography, sports, and heritage) with a high-end, bespoke neo-morphic aesthetic, zero visual clutter, and balanced monetization.

### 1.2 Core Pillars
1. **Verifiable Truth & Zero Hallucination**: Strict editorial integrity. Every question is vetted against authoritative historical, scientific, and statistical sources. Absolutely no synthetic AI hallucinations or questionable trivia.
2. **Infinite Scale Question Architecture**: Decoupled question sharding and deterministic local batch caching via Firestore and Hive, enabling offline play while supporting millions of unique questions without performance degradation.
3. **Elevated Visual & Haptic Craftsmanship**: Bespoke dark/light mode system with rich obsidian, deep indigo, brushed brass/amber accents, smooth spring physics, fluid micro-interactions, and spatial audio feedback.
4. **Player-First Monetization**: Non-disruptive Unity Ads placement. Zero interruption during live gameplay rounds; player-initiated rewarded ads with authentic value exchange (Extra Lives, Lifelines, Double Multipliers).

---

## 2. Comprehensive Feature Requirements

### 2.1 Gameplay Mechanics

#### 2.1.1 Game Modes
- **Quick Play**:
  - Instant matchmaking against a randomized 10-question set pulled across all unlocked categories according to user rank.
  - Time per question: Dynamic timer based on difficulty (Easy: 15s, Medium: 12s, Hard: 10s).
- **Category Deep Dive**:
  - Players select a specific domain (e.g., *Bangladesh Liberation War*, *Astrophysics*, *World Cinema*).
  - Progressive tier stages (Bronze, Silver, Gold, Platinum mastery per category).
- **Daily Intellectual Challenge (The Grand Daily)**:
  - Synchronized global 15-question gauntlet updated daily at 00:00 UTC.
  - Identical questions for all users worldwide; single-attempt leaderboard run.
  - Rewarded with unique badge relics, double XP, and exclusive collectible avatar frames.

#### 2.1.2 Scoring, Streaks & Combo Multiplier
- **Base Score Formula**:
  $$\text{Points} = (\text{Base Difficulty Score}) \times (\text{Remaining Time Factor}) \times (\text{Streak Multiplier})$$
  - Base Difficulty: Easy = 100 pts, Medium = 200 pts, Hard = 350 pts.
  - Remaining Time Factor: $1.0 + \frac{\text{Remaining Milliseconds}}{\text{Total Allocated Milliseconds}} \times 0.5$
  - Streak Multiplier:
    - 1–2 correct in a row: $1.0\times$
    - 3–5 correct in a row: $1.25\times$ (Audio frequency shifts + dynamic particle glow)
    - 6–9 correct in a row: $1.5\times$ (Card borders ignite with amber flare)
    - 10+ streak: $2.0\times$ ("Trivia Master" state)
- **Life System (Hearts)**:
  - Default maximum: 5 Hearts.
  - 1 heart lost on quiz defeat (running out of time or scoring below 70% in a competitive session).
  - Heart regeneration rate: 1 Heart per 20 minutes (persisted via UTC server timestamp in Firestore and local Hive alarm).
  - Instant Refill: Purchase with 250 Gems or watch 1 Unity Rewarded Ad (cooldown: 15 minutes between ad-based refills).

#### 2.1.3 Power-ups (Lifelines)
- **50/50 (Eliminator)**: Eliminates 2 incorrect options. (Cost: 50 Coins).
- **Time Freeze (+10 Seconds)**: Pauses timer bar for 10 seconds. (Cost: 35 Coins).
- **Scholar's Skip**: Skips current question without breaking streak or deducting lives. (Cost: 75 Coins).
- **Double Harvest**: Doubles coin earnings for the current round. (Cost: 60 Coins or 1 Rewarded Ad).

---

### 2.2 Categories & Taxonomy Matrix

#### International Taxonomy
1. **World History & Civilizations**: Ancient Empires, World Wars, Renaissance, Cold War, Treaties.
2. **Astrophysics & Space Exploration**: Planetary systems, cosmology, space missions, telescopes.
3. **General Science & Modern Medicine**: Physics, biochemistry, neurology, Nobel laureates.
4. **Global Geography & Cartography**: Capitals, tectonic rifts, rivers, demographic shifts.
5. **Literature, Philosophy & Fine Arts**: Classical literature, philosophical movements, architecture.
6. **World Cinema & Musical Heritage**: Oscar archives, classical composers, film history.
7. **Technology & Computing**: Silicon pioneers, network protocols, computer science history.
8. **World Sports & Olympics**: FIFA World Cups, Olympic records, cricket, Grand Slam tennis.

#### Domestic Taxonomy (Bangladesh Focus)
1. **History of Bangladesh & Bengal**: Ancient Bengal (Pundravardhana, Gauda, Sena), Mughal Subah Bangalah, British Colonial Era.
2. **Language Movement & Liberation War**: 1952 Bhasha Andolon, 1966 Six-Point Movement, 1969 Mass Uprising, 1971 Liberation War (Sectors, Bir Sreshtho, Historic Dates, Declaration of Independence).
3. **Geography, Rivers & Delta**: 64 Districts, Sundarbans mangrove ecosystem, Padma/Meghna/Jamuna systems, Cox's Bazar, Haor wetlands.
4. **Literature & Cultural Arts**: Rabindranath Tagore in Bengal, Kazi Nazrul Islam (National Poet), Jashimuddin, Baul tradition (Lalon Shah), Nakshi Kantha, Bengali New Year (Pohela Boishakh).
5. **National Affairs, Constitution & Institutions**: Bangladesh Constitution (1972), Parliamentary system (Jatiya Sangsad), Central Bank, National symbols.
6. **Bangladesh Sports Heritage**: Test Cricket milestones (debut in 2000, historic wins), National game Kabaddi (Hadudu), SAFF Championship football, ICC trophies.
7. **National Economy & Mega-Projects**: Padma Multipurpose Bridge, Bangabandhu Tunnel, Dhaka Metro Rail (MRT-6), Rooppur Nuclear Power Plant, RMG & Jute export heritage.

---

## 3. System Architecture & Technical Specifications

```
+-----------------------------------------------------------------------------------+
|                               Flutter Presentation Layer                          |
|  [Screens & Composables] <---> [Riverpod Notifiers] <---> [Design System & Theme] |
+-----------------------------------------------------------------------------------+
                                         |
                                         v
+-----------------------------------------------------------------------------------+
|                                 Domain Layer                                      |
|      [Use Cases & Interactors]  <--->  [Entities]  <--->  [Repository Contracts]  |
+-----------------------------------------------------------------------------------+
                                         |
                                         v
+-----------------------------------------------------------------------------------+
|                                  Data Layer                                       |
|  [Repository Implementations] <------------------------+                          |
|            |                                           |                          |
|            v                                           v                          |
|  [Remote: Firestore / Functions]           [Local: Hive Database / SharedPreferences] |
|   - Question Pool Shards                    - Encrypted Session Tokens            |
|   - Global Leaderboards                     - 500 Question Offline Rolling Cache  |
|   - Real-time User Profile                  - Offline Progress Sync Queue         |
+-----------------------------------------------------------------------------------+
```

### 3.1 State Management (Flutter Riverpod 2.x Architecture)
- Immutable state records using `freezed` and `json_annotation`.
- **`AsyncNotifierProvider`** for network-driven, reactive asynchronous operations (Profile, Leaderboard, Question Sets).
- **`StateNotifierProvider`** or `NotifierProvider` for localized active gameplay loops (Timer ticker, Streak counter, Lifeline inventory).
- Strict separation between Presentation, Domain, and Data:
  - Repositories expose typed `Result<T, Failure>` models.
  - Zero raw Firebase dependencies inside Presentation Widgets.

### 3.2 Offline First Strategy & Infinite Question Scale

#### Problem Statement
Hosting tens of thousands to millions of questions directly in Firestore can result in excessive read costs if queried naively one-by-one. Conversely, bundling all questions directly into the APK blows up app download size.

#### Infinite Scale Sharded Bucket Architecture
1. **Sharded Chunking**: Questions are grouped by Category and Difficulty into chunk documents containing 50 questions each (`chunks/{categoryId}_{difficulty}_{chunkIndex}`).
2. **Metadata Catalog (`/metadata/catalog`)**: A lightweight document outlining total question counts, latest hash revision, and available chunks.
3. **Hive Rolling Cache**:
   - The Flutter client maintains an indexed local Hive database of 500-1000 questions across preferred categories.
   - When the device is on Wi-Fi or background network, it fetches and updates 1 or 2 chunks (50-100 questions) and stores them in Hive.
   - Each question played in the client is tagged with a local `last_presented_timestamp` to ensure no question repeats until the user has exhausted at least 90% of the category catalog.
4. **Zero-Read Offline Play**: If connectivity drops, gameplay continues seamlessly from the Hive cache. User score, coins, and XP accumulate in a local Hive `PendingSyncQueue` and automatically reconcile with Firestore upon network reconnect.

---

## 4. Complete Firestore Schema & Data Models

### 4.1 Collection: `users`
**Path**: `/users/{userId}`
```json
{
  "uid": "usr_948f2c81",
  "displayName": "Tanvir Rahman",
  "email": "tanvir.rahman@example.com",
  "avatarUrl": "https://cdn.qmaster.app/avatars/av_04.png",
  "countryCode": "BD",
  "level": 14,
  "currentXp": 4850,
  "xpToNextLevel": 6000,
  "coins": 1820,
  "gems": 45,
  "hearts": 5,
  "lastHeartRefillTimestamp": 1726483200000,
  "stats": {
    "totalGamesPlayed": 142,
    "totalWins": 118,
    "correctAnswers": 1284,
    "incorrectAnswers": 136,
    "currentStreak": 14,
    "highestStreak": 28,
    "winRate": 0.83
  },
  "categoryMastery": {
    "bd_history": { "level": 5, "questionsAnswered": 320, "accuracy": 0.89 },
    "world_science": { "level": 3, "questionsAnswered": 180, "accuracy": 0.78 }
  },
  "inventory": {
    "fiftyFiftyCount": 6,
    "timeFreezeCount": 4,
    "skipCount": 2,
    "doubleHarvestCount": 5
  },
  "createdAt": 1725000000000,
  "lastActiveAt": 1726485000000
}
```

### 4.2 Collection: `categories`
**Path**: `/categories/{categoryId}`
```json
{
  "id": "bd_history",
  "title": "Bangladesh History & Liberation War",
  "shortTitle": "BD History",
  "description": "The heritage, language movement, 1971 liberation war, and founding pioneers of Bengal.",
  "scope": "domestic",
  "iconAsset": "assets/icons/cat_bd_history.svg",
  "accentColor": "#006A4E",
  "totalQuestions": 850,
  "requiredLevel": 1,
  "isPremium": false
}
```

### 4.3 Collection: `questions` (Master Repository)
**Path**: `/questions/{questionId}`
```json
{
  "id": "q_bd_his_0108",
  "categoryId": "bd_history",
  "sourceType": "domestic",
  "difficulty": "medium",
  "questionText": "Who was the Commander-in-Chief of the Bangladesh Armed Forces during the 1971 Liberation War?",
  "options": [
    "General M. A. G. Osmani",
    "Major Ziaur Rahman",
    "Major General K. M. Shafiullah",
    "Colonel Khaled Mosharraf"
  ],
  "correctAnswerIndex": 0,
  "correctAnswerText": "General M. A. G. Osmani",
  "explanation": "General Muhammad Ataul Gani Osmani was appointed Supreme Commander of the Mukti Bahini and Bangladesh Armed Forces by the Mujibnagar Government in April 1971.",
  "verifiedBy": "Editorial Committee - Historians Panel",
  "citationSource": "Official Documents of the Liberation War, Ministry of Liberation War Affairs BD",
  "playCount": 14200,
  "correctRate": 0.74,
  "createdAt": 1718000000000,
  "isActive": true
}
```

### 4.4 Collection: `daily_challenges`
**Path**: `/daily_challenges/{dateKey}` (e.g. `2026-09-16`)
```json
{
  "dateKey": "2026-09-16",
  "questionIds": ["q_int_sci_001", "q_bd_geo_042", "q_int_his_119", "... 15 IDs"],
  "rewardCoins": 500,
  "rewardGems": 10,
  "badgeId": "badge_sep_16_champion",
  "totalParticipants": 34820
}
```

### 4.5 Collection: `leaderboards`
**Path**: `/leaderboards/{period}/entries/{userId}` (periods: `daily`, `weekly`, `all_time`, `bangladesh_rank`)
```json
{
  "uid": "usr_948f2c81",
  "displayName": "Tanvir Rahman",
  "avatarUrl": "https://cdn.qmaster.app/avatars/av_04.png",
  "countryCode": "BD",
  "score": 48250,
  "rank": 14,
  "updatedAt": 1726485000000
}
```

---

## 5. Security Rules (Production-Ready)

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    function isAuthenticated() {
      return request.auth != null;
    }
    
    function isOwner(userId) {
      return isAuthenticated() && request.auth.uid == userId;
    }
    
    function isAdmin() {
      return isAuthenticated() && request.auth.token.admin == true;
    }

    // User Profile: Only user can read/write their data, cannot manipulate coins/gems arbitrarily
    match /users/{userId} {
      allow read: if isAuthenticated();
      allow create: if isOwner(userId);
      allow update: if isOwner(userId) 
        && (!request.resource.data.diff(resource.data).affectedKeys().hasAny(['coins', 'gems', 'level']) || isAdmin());
    }

    // Categories: Publicly readable, admin write-only
    match /categories/{categoryId} {
      allow read: if true;
      allow write: if isAdmin();
    }

    // Questions: Authenticated users can read active questions, no direct write
    match /questions/{questionId} {
      allow read: if isAuthenticated() && resource.data.isActive == true;
      allow write: if isAdmin();
    }
    
    // Sharded Chunks: For efficient batch reads
    match /question_chunks/{chunkId} {
      allow read: if isAuthenticated();
      allow write: if isAdmin();
    }

    // Daily Challenges: Read-only for authenticated users
    match /daily_challenges/{challengeId} {
      allow read: if isAuthenticated();
      allow write: if isAdmin();
    }

    // Leaderboards: Read by all authenticated users; written only by Cloud Functions
    match /leaderboards/{period}/entries/{userId} {
      allow read: if isAuthenticated();
      allow write: if false; // Cloud Functions exclusively
    }
  }
}
```

---

## 6. Serverless Cloud Functions (Node.js / TypeScript)

1. **`submitQuizSession` (Callable)**:
   - Takes: `sessionId`, `questionResponses: [{ questionId, selectedIndex, timeTakenMs }]`.
   - Validates response correctness against hidden server questions table.
   - Calculates verified score, XP gain, coin rewards, and increments streak.
   - Prevents client-side game state forging.
2. **`claimDailyReward` (Callable)**:
   - Validates consecutive login day sequence using server time.
   - Credits coins, lifelines, or gems atomically in Firestore transactions.
3. **`cronCalculateLeaderboards` (Scheduled Pub/Sub every 1 hour)**:
   - Aggregates rolling weekly and daily scores, sorts them, and writes the top 500 records into `/leaderboards/{period}`.
4. **`verifyUnityAdReward` (Webhook or Callable)**:
   - Unity Ads S2S (Server-to-Server) callback handler to credit rewards securely upon completed video view.

---

## 7. Premium UI/UX Design System Specification

### 7.1 Visual Philosophy: "Obsidian Luminary"
The interface avoids cheap neon cartoon aesthetics. It adopts an editorial luxury look:
- **Canvas**: Deep Obsidian Slate (`#0B0E14`) paired with rich charcoal surface tiers (`#151B26`).
- **Primary Accent**: Regal Imperial Amber (`#F5A623`) with glowing specular falloff.
- **Secondary Accent**: Bangladesh Deep Emerald (`#006A4E`) and Vivid Viridian (`#00A86B`) celebrating domestic culture with sovereign elegance.
- **Surface Elevation**: Subtle 1px inner ambient borders (`rgba(255, 255, 255, 0.08)`), refined directional drop-shadows with Gaussian blur (sigma = 16.0).
- **Typography**:
  - Display & Headers: *Cabinet Grotesk* or *Outfit* (Geometric, bold, authoritative).
  - Body & Options: *Plus Jakarta Sans* or *Inter* (High x-height, flawless optical legibility).
  - Numerals & Timer: *JetBrains Mono* (Tabular numbers to eliminate timer width jitter).

### 7.2 Haptic & Motion Design
- **Card Tap**: Micro scale response (`scale: 0.98`) with 40ms light haptic click.
- **Correct Answer Reveal**:
  - Selected tile transitions from charcoal to glowing emerald (`#00A86B`) via a 220ms easeOutCubic curve.
  - Subtly emits 8–12 radial gold stardust micro-particles.
  - Medium haptic success pulse.
- **Incorrect Answer Reveal**:
  - Selected tile shifts to crimson scarlet (`#E63946`) with a 3-cycle horizontal micro-shake (amplitude 6dp, total 200ms).
  - Correct answer illuminates simultaneously in calm jade to instruct the user.
  - Subtle heavy double buzz haptic.
- **Timer Countdown**:
  - Continuous radial or linear gradient bar.
  - Changes dynamically: Green (>50%), Amber (25–50%), Crimson Pulse (<25% with heartbeat haptic cadence).

---

## 8. Unity Ads Placement & Monetization Strategy

### 8.1 Principles of Luxury Monetization
Ads must never feel like spam. They must be contextual, predictable, and respect user focus.

| Ad Type | Format | Trigger Point | Frequency Cap | Player Value Proposition |
|---|---|---|---|---|
| **Rewarded Video** | 1080p High Quality Video | Game Over Screen / Out of Lives | User initiated only | Instant Heart refill (+3 hearts), or Second Chance revival |
| **Rewarded Video** | Full Video | Post-Quiz Summary Screen | User initiated only | "Double Your Coins" (2x multiplier on earnings) |
| **Rewarded Video** | Full Video | Store / Power-up Depot | Max 3 times/day | Free Daily Lifeline Pack (1x 50/50 + 1x Time Freeze) |
| **Interstitial** | Non-skippable for 5s, then skippable | Between Game Sessions | Strictly after every **3 complete matches** | None (Pacing buffer; suppressed if user made any in-app purchase) |
| **Adaptive Banner** | 320x50 or 728x90 collapsible | Bottom of Home & Category Selection Screens | Continuous refresh (45s) | Kept off all active gameplay question screens |

### 8.2 Unity Ads Implementation Checklist
1. Initialize Unity Ads during splash boot:
   `UnityAds.init(gameId: Platform.isAndroid ? "ANDROID_ID" : "IOS_ID", testMode: false);`
2. Pre-cache Rewarded placements: Load `Rewarded_Android` / `Rewarded_iOS` in advance so the player experiences zero loading delay when tapping "Watch Ad for +1 Life".
3. Handle graceful fallback: If an ad fails to load or no fill is available, provide the player with a soft notification or credit the lifeline as courtesy to prevent frustration.

---

## 9. Flutter Project Structure (Feature-First Clean Architecture)

```
quiz_master/
├── android/
├── ios/
├── assets/
│   ├── fonts/
│   │   ├── Outfit-Bold.ttf
│   │   ├── Outfit-SemiBold.ttf
│   │   └── PlusJakartaSans-Regular.ttf
│   ├── icons/
│   │   ├── ic_fifty_fifty.svg
│   │   ├── ic_time_freeze.svg
│   │   └── ic_flag_bd.svg
│   └── audio/
│       ├── sfx_correct.mp3
│       ├── sfx_wrong.mp3
│       └── sfx_streak_fanfare.mp3
├── lib/
│   ├── main.dart
│   ├── app.dart
│   ├── core/
│   │   ├── constants/
│   │   │   ├── app_colors.dart
│   │   │   ├── app_typography.dart
│   │   │   └── api_endpoints.dart
│   │   ├── errors/
│   │   │   ├── failures.dart
│   │   │   └── exceptions.dart
│   │   ├── network/
│   │   │   └── network_info.dart
│   │   ├── services/
│   │   │   ├── ads/
│   │   │   │   ├── unity_ads_service.dart
│   │   │   │   └── ad_placement_manager.dart
│   │   │   ├── audio/
│   │   │   │   └── sound_effects_service.dart
│   │   │   └── haptics/
│   │   │       └── haptic_feedback_service.dart
│   │   └── theme/
│   │       ├── app_theme.dart
│   │       └── theme_mode_notifier.dart
│   ├── features/
│   │   ├── auth/
│   │   │   ├── data/
│   │   │   ├── domain/
│   │   │   └── presentation/
│   │   ├── home/
│   │   │   ├── presentation/
│   │   │   │   ├── screens/home_screen.dart
│   │   │   │   └── widgets/user_stats_header.dart
│   │   ├── quiz/
│   │   │   ├── data/
│   │   │   │   ├── datasources/
│   │   │   │   │   ├── quiz_remote_data_source.dart
│   │   │   │   │   └── quiz_local_hive_data_source.dart
│   │   │   │   ├── models/
│   │   │   │   │   ├── question_model.dart
│   │   │   │   │   └── quiz_result_model.dart
│   │   │   │   └── repositories/quiz_repository_impl.dart
│   │   │   ├── domain/
│   │   │   │   ├── entities/question.dart
│   │   │   │   ├── repositories/quiz_repository.dart
│   │   │   │   └── usecases/get_quiz_questions_usecase.dart
│   │   │   └── presentation/
│   │   │       ├── controllers/
│   │   │       │   ├── quiz_session_controller.dart
│   │   │       │   └── quiz_timer_controller.dart
│   │   │       ├── screens/
│   │   │       │   ├── quiz_gameplay_screen.dart
│   │   │       │   └── quiz_result_screen.dart
│   │   │       └── widgets/
│   │   │           ├── option_tile.dart
│   │   │           ├── countdown_timer_bar.dart
│   │   │           └── lifelines_panel.dart
│   │   ├── daily_challenge/
│   │   ├── leaderboards/
│   │   ├── profile/
│   │   └── store/
└── pubspec.yaml
```

---

## 10. Screen-by-Screen Breakdown

### Screen 1: Splash & Initializing Screen
- **Visuals**: Obsidian background, gold foil Q-Master sigil with ambient pulse.
- **Actions**: Initializes Firebase, syncs Remote Config, boots Unity Ads, loads local Hive user state, verifies session. Seamless 1.2s fade into Home Screen.

### Screen 2: Main Dashboard (Home Screen)
- **Top App Bar**:
  - Avatar, Current Level badge ("Lvl 14 Scholar"), Live Hearts counter (e.g. `5/5` with countdown tooltip), Coin & Gem balance pill with `+` top-up triggers.
- **Hero Carousel**:
  - The Daily Grand Challenge card with remaining countdown timer and reward pot.
  - "Quick Arena" Instant Play button (prominent amber gradient).
- **Categorical Exploration**:
  - Horizontal tabs: "International Classics" vs "Bengal & National Heritage".
  - Grid cards displaying Category icon, title, progress bar, and locked/unlocked state.
- **Persistent Bottom Navigation**:
  - Home, Categories, Leaderboards, Store, Profile.
  - Anchored Unity Adaptive Banner at bottom edge (non-intrusive).

### Screen 3: Quiz Active Gameplay Arena
- **Top Status Bar**:
  - Back button (with quit confirmation modal), Question progress indicator (`7 / 10`), Live Streak flame counter (`Streak x3`), Pause/Sound toggles.
- **Timer Component**:
  - Elegant top horizontal countdown bar with remaining seconds counter.
- **Question Card**:
  - Category badge (e.g. *Bangladesh Liberation War*).
  - Clear, high-contrast question text with generous line spacing.
- **Options Array**:
  - 4 vertical tactile cards with prefix badges (A, B, C, D).
  - Dynamic visual state: Idle, Pressed, Correct (Emerald), Wrong (Crimson), Eliminated (50/50 greyed out).
- **Lifelines Deck (Bottom Bar)**:
  - 4 circular action buttons: 50/50, Extra Time, Skip, Double Points, showing available quantity or coin cost.

### Screen 4: Post-Quiz Performance & Rewards Summary
- **Visuals**: Victory laurel / trophy render with particle burst.
- **Statistics Grid**:
  - Total Score, Accuracy percentage, Time taken, New High Streak, XP gained with animated level-up gauge.
- **Reward Multiplier Callout**:
  - "Watch a 20s ad to DOUBLE your coin harvest: **+400 Coins**" (One-tap Unity Rewarded trigger).
- **Action Buttons**:
  - "Next Round", "Review Answers & Explanations", "Return to Home".

### Screen 5: Comprehensive Answer Review Screen
- List of all 10 questions with player's selected choice vs authoritative correct answer, accompanied by the factual verification explanation and historical citation.

### Screen 6: Leaderboard Pantheon
- Tabs: Global vs National (Bangladesh).
- Top 3 podium display (Gold, Silver, Bronze avatar columns with 3D crown badges).
- Paginated scroll of ranks 4 to 100 with current user's pinned rank sticky at bottom.

### Screen 7: Lifeline Emporium & Coin Store
- Power-up bundles, coin packs, heart refills.
- "Watch Ad for Free Daily Bundle" rewarded card with 24-hour reset countdown.

---

## 11. MVP vs Future Roadmap

| Phase | Milestone | Deliverables |
|---|---|---|
| **Phase 1 (MVP)** | Weeks 1–4 | - Core Flutter App with Riverpod & Hive<br>- Firebase Auth (Anonymous & Google Sign-In)<br>- Initial 2,000 Verified Real Questions (International + Bangladesh)<br>- Single-player Quick Play & Category Play<br>- Unity Ads integration (Banner + Interstitial + Rewarded)<br>- Heart regeneration & coins system |
| **Phase 2 (Growth)** | Weeks 5–8 | - Synchronized Daily Intellectual Challenge with global leaderboards<br>- Cloud Functions anti-cheat verification<br>- Sound effects & rich haptics library<br>- Offline sync queue for offline play reconciliation |
| **Phase 3 (Scale & Community)** | Weeks 9–14 | - Real-time 1v1 Quiz Duels via Firebase WebSockets/Firestore listeners<br>- Verified User Question Submission Portal (Community curator panel with editorial review)<br>- Expanded Bangladesh Civil Service (BCS) and University Admission exam preparation quiz packs<br>- Localization into pure Bengali (*Bangla*) with native fonts |
