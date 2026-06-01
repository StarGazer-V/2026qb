# Medical GT Score Tracker & Flashcard Engine

An offline-first Android architecture for medical students preparing for grand tests and entrance exams. The codebase is scaffolded with Kotlin, Jetpack Compose, Room, Firebase Auth, Hilt, DataStore, Navigation Compose, and a charting dependency for production graphing.

## 1. Architecture & Tech Stack

- **UI:** Jetpack Compose + Material 3 for dashboard, GT entry, profile, settings, and flashcard surfaces.
- **Navigation:** `androidx.navigation:navigation-compose` for centralized dashboard routing.
- **Dependency Injection:** Hilt for Firebase, Room, repositories, and ViewModels.
- **Authentication:** Firebase Auth for email/password and phone OTP login.
- **Sticky Login:** Jetpack DataStore stores the authenticated user id and sticky-login flag.
- **Offline Persistence:** Room stores profiles, GT scores, flashcard notes, and review scheduling data locally.
- **Charts:** The GT progress graph uses a lightweight Jetpack Compose Canvas implementation so the debug APK builds without extra charting dependency conflicts. Vico or MPAndroidChart can be added later once the base app is compiling.
- **Flashcards:** A Room-backed note/review model supports Basic, Cloze Deletion, and Image Occlusion notes plus an Anki-style scheduler.

## 2. Room Database Schema

Core entities live in `app/src/main/java/com/medgttracker/data/local/Entities.kt`:

- `ProfileEntity` stores name, email, phone, college, and target exam details.
- `GtScoreEntity` stores platform, corrects, incorrects, skipped, percentile, rank, two worst subjects, and notes.
- `FlashcardNoteEntity` stores Basic, Cloze, and Image Occlusion note payloads.
- `FlashcardReviewEntity` stores scheduling state such as interval, ease factor, repetitions, lapses, and due dates.

DAO operations live in `MedDao.kt` and expose Flow-based observers for reactive Compose screens.

## 3. Compose UI Layouts

- `DashboardScreen.kt` provides the centralized dashboard, exam countdown card, randomized motivational quote, and navigation buttons.
- `GtTrackerScreen.kt` provides the score input form, score report summary, and visual progress chart.
- `ProfileScreen.kt` and `SettingsScreen.kt` are lightweight destinations that demonstrate where profile persistence and app-level preferences attach.

## Firebase Setup

Add a real `google-services.json` in `app/`, enable Email/Password and Phone providers in the Firebase console, and apply the `com.google.gms.google-services` Gradle plugin before running against a real Firebase project.

## Build an APK from an iPad

You cannot compile an Android APK directly on iPadOS, and an iPad cannot install or preview an APK because APK files run on Android devices. The easiest no-computer workflow is to let GitHub Actions build the APK in the cloud, then download the generated file from Safari on the iPad.

1. Push this project to a GitHub repository.
2. Open the repository in Safari on the iPad.
3. Go to **Actions** → **Build debug APK** → **Run workflow**.
4. Wait until the workflow finishes successfully.
5. Open the completed workflow run and download the **med-gt-tracker-debug-apk** artifact.
6. Share the downloaded APK to an Android phone/tablet and install it there. You may need to enable **Install unknown apps** on the Android device.

The workflow that performs this cloud build lives at `.github/workflows/build-debug-apk.yml` and runs `gradle :app:assembleDebug --no-daemon`. The APK artifact contains the debug APK from `app/build/outputs/apk/debug/`.


## Troubleshooting GitHub Actions APK builds

If GitHub Actions fails while resolving `:app:debugRuntimeClasspath` through `com.patrykandpatrick.vico:compose-m3:2.0.0`, update to this version of the project and run **Build debug APK** again. The app currently renders the GT progress graph with Compose Canvas, so the Vico dependency is intentionally removed from `app/build.gradle.kts` to avoid pulling the AppCompat/vector-drawable dependency chain that caused the debug build failure.
