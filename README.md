# Quiz Master 🏆

A high-performance, feature-complete mobile trivia game with remote monetization management, dual-region leaderboards, interactive lifelines, stamina heart system, and full verified domestic (Bangladesh) & international question banks.

---

## 🚀 Key Features

- **🎮 Engaging Trivia Gameplay**: 
  - 15-second dynamic timer with intuitive visual warnings.
  - Multiplier streak system (up to $2\times$ combo scoring).
  - 4 interactive lifelines: **50:50**, **+15s Time Extension**, **Question Skip**, and **$2\times$ Point Booster**.
- **📚 Curated & Fact-Checked Content**:
  - Domestic category (Bangladesh Liberation War 1971, Rivers & Geography, Culture, Economy & Sports).
  - International category (World History, World Geography, Science & Discovery, Space & Technology).
  - Instant question review cards with verified explanations.
- **💎 Economy & Progression**:
  - Stamina system (5 Hearts) with automatic and ad-supported refills.
  - XP levels, badges, and coin rewards.
  - Daily login streak bonuses with 7-day reward tiers.
- **🌍 Dual-Region Leaderboards**:
  - Bangladesh National leaderboard & Worldwide Global standings with top-3 podium and personal ranking cards.
- **📺 Remote Monetization Management**:
  - Remote GitHub-hosted JSON configuration (`ad_config.json`) to toggle and adjust banners, interstitials, and rewarded video ads on the fly without republishing.
  - Offline local caching fallback (via Hive/Room/SharedPreferences).

---

## 📡 Remote Unity Ads Configuration

The repository includes [`ad_config.json`](./ad_config.json) at the root. Once pushed to your GitHub repository, you can fetch it remotely via:

```
https://raw.githubusercontent.com/<YOUR_GITHUB_USERNAME>/<YOUR_REPO>/main/ad_config.json
```

### Configuration Schema

| Field | Type | Description |
|---|---|---|
| `enable_banner` | `boolean` | Toggles banner ad display across screens |
| `enable_interstitial` | `boolean` | Enables interstitial video transitions between quiz sets |
| `enable_rewarded` | `boolean` | Enables rewarded ads for extra lives and coin doubling |
| `interstitial_interval` | `integer` | Number of quiz rounds completed before showing interstitial ad |
| `android_game_id` | `string` | Unity Ads Game ID for Android |
| `ios_game_id` | `string` | Unity Ads Game ID for iOS |
| `banner_placement_id` | `string` | Unity Placement ID for Banner |
| `interstitial_placement_id` | `string` | Unity Placement ID for Interstitial |
| `rewarded_placement_id` | `string` | Unity Placement ID for Rewarded Video |
| `is_test_mode` | `boolean` | `true` during development/testing; set to `false` in production |

---

## 🛠️ Project Structure

```
├── ad_config.json                   # Remote ad configuration template
├── QUIZ_APP_PRD_AND_TECH_SPEC.md    # Complete Product Requirements & Architecture Spec
├── quiz_seed_questions.json         # Seed questions database
├── app/                             # Android (Kotlin & Jetpack Compose) module
│   ├── src/main/java/com/example/
│   │   ├── data/
│   │   │   ├── model/               # Question, UserProfile, Leaderboard, Lifeline models
│   │   │   └── repository/          # QuizRepository with state and local storage
│   │   ├── ui/
│   │   │   ├── components/          # Sponsor banners, Ad modals, Daily dialogs
│   │   │   ├── screens/             # Home, Quiz, Results, Leaderboard, Store screens
│   │   │   ├── theme/               # Obsidian & Imperial Gold Material 3 theme
│   │   │   └── viewmodel/           # QuizViewModel state engine
│   │   └── MainActivity.kt          # Single-activity edge-to-edge entry point
│   └── src/test/                    # JVM unit & Robolectric tests
└── gradle/                          # Gradle wrapper & version catalogs
```

---

## 🧪 Testing & Verification

Run local unit and Robolectric tests:

```bash
gradle :app:testDebugUnitTest
```

Compile and build debug APK:

```bash
gradle :app:assembleDebug
```

---

## 🚢 Pushing to GitHub

To push this repository to GitHub:

1. Create a new empty repository on [GitHub](https://github.com/new).
2. Set the remote URL:
   ```bash
   git remote add origin https://github.com/<YOUR_USERNAME>/<REPO_NAME>.git
   ```
3. Push the `main` branch:
   ```bash
   git branch -M main
   git push -u origin main
   ```
*(Or use the **Push to GitHub** feature in the AI Studio platform menu.)*
