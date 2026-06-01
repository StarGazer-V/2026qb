# Medical GT Score Tracker & Flashcard Engine

An offline-first Android architecture for medical students preparing for grand tests and entrance exams. The codebase is scaffolded with Kotlin, Jetpack Compose, Room, Firebase Auth, Hilt, DataStore, Navigation Compose, and a charting dependency for production graphing.

## 1. Architecture & Tech Stack

- **UI:** Jetpack Compose + Material 3 for dashboard, GT entry, profile, settings, and flashcard surfaces.
- **Navigation:** `androidx.navigation:navigation-compose` for centralized dashboard routing.
- **Dependency Injection:** Hilt for Firebase, Room, repositories, and ViewModels.
- **Authentication:** Firebase Auth for email/password and phone OTP login.
- **Sticky Login:** Jetpack DataStore stores the authenticated user id and sticky-login flag.
- **Offline Persistence:** Room stores profiles, GT scores, flashcard notes, and review scheduling data locally.
- **Charts:** Vico dependencies are included for production Compose charts; the current GT screen also includes a lightweight Canvas fallback for offline previewability.
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
