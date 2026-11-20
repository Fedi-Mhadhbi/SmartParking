# EasyPar — Smart Parking Mobile App

EasyPar is a modern Android parking-reservation application for discovering parking spaces in Tunis, choosing an hourly slot and exact parking place, completing a demo payment, and keeping a QR reservation ticket.

**Project:** SmartParking

**Package:** `com.fedi.smartparking`

**Platform:** Android 8.0+ (API 26+)

**Stack:** Kotlin, Jetpack Compose, Material 3, Firebase Authentication, Firebase Functions, OpenStreetMap

---

## App preview

<p align="center">
  <img src="docs/screenshots/home-preview.jpg" alt="EasyPar home parking preview" width="32%">
  <img src="docs/screenshots/explore-preview.jpg" alt="EasyPar parking map and photos" width="32%">
  <img src="docs/screenshots/details-preview.jpg" alt="EasyPar parking details preview" width="32%">
</p>

---

## Features

### For drivers

- Firebase email and password authentication
- Persistent login session and account logout
- Home dashboard with nearby and recommended parking
- Interactive OpenStreetMap parking map
- Search and filter parkings by nearby location, price, or availability
- Detailed parking screen with photo, address, hourly price, distance, amenities, and available spaces
- Date and hourly start-time reservation
- Vehicle selection with a locally managed My Vehicles section
- Visual parking-place selection using sections A, B, and C
- Green available places, red unavailable places, and blue selected places
- Reservation confirmation and demo payment
- QR reservation ticket containing the parking and exact place
- Download and share a reservation receipt
- Reservation history and cancellation
- Notification center with read/unread state
- English, French, and Arabic interface with RTL support
- Help and support topics

### Demo scope

This MVP intentionally uses local demo state for parkings, vehicles, availability, and reservations. The user’s Firebase account controls login and profile identity. Production Firestore persistence, real payment processing, FCM, and owner workflows can be added in later releases.

---

## Architecture

EasyPar follows the Android presentation flow defined for the team:

```text
Compose Screen
      ↓
ViewModel
      ↓
UseCase
      ↓
Repository interface
      ↓
Repository implementation / DataSource
      ↓
Firebase or local demo store
```

The app uses Jetpack Compose only for UI. Firebase reservation-critical operations will use Cloud Functions in the production architecture. The current demo payment does not charge a real account.

---

## Team

| Role | Area |
|---|---|
| Dev 1 | Android UI, Compose, navigation, themes, localization |
| Dev 2 | Reservation engine, QR, slots, pricing, Cloud Functions |
| Dev 3 | Firebase identity, Firestore data, security, storage, owner side |

Two application roles are supported: `DRIVER` and `OWNER`. New accounts begin with the `DRIVER` role. Owner and admin operations are outside the current Android MVP scope.

---

## Requirements

- Android Studio Ladybug or a newer compatible release
- Android SDK 35
- Java 17
- Android device or emulator running API 26 or higher
- Internet connection
- A Firebase project with Email/Password Authentication enabled
- Node.js 20 and Firebase CLI for emulator/backend work

---

## Download and run the Android app

### Option 1: Install an APK

1. Open the **Releases** page on GitHub.
2. Download the latest `EasyPar` APK.
3. Copy it to an Android phone or download it directly.
4. On Android, allow installation from the downloaded file if prompted.
5. Open the app and create an account or sign in.

> If GitHub Releases is not available in the repository yet, build the APK locally using the instructions below.

### Option 2: Run from Android Studio

```bash
git clone https://github.com/Fedi-Mhadhbi/SmartParking.git
cd SmartParking
```

1. Open the repository in Android Studio.
2. Install Android SDK 35 and Java 17 if they are missing.
3. Download Firebase configuration for the Android app.
4. Place `google-services.json` in `app/google-services.json`.
5. Enable **Authentication → Sign-in method → Email/Password** in Firebase Console.
6. Run the `app` configuration on an emulator or physical device.

`google-services.json` is intentionally ignored by Git and must never be committed.

### Option 3: Build the APK with Gradle

```bash
./gradlew assembleDebug
```

The APK is created at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Install it with ADB:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## Firebase setup

EasyPar requires Firebase Authentication for account access.

1. Create or open a Firebase project.
2. Register an Android app with the package name:

   ```text
   com.fedi.smartparking
   ```

3. Download `google-services.json`.
4. Place it at:

   ```text
   app/google-services.json
   ```

5. Enable **Email/Password** authentication.
6. The app will create or sign in Firebase users normally.

The Android emulator may require emulator host configuration. When using Firebase Emulator Suite from Android, use `10.0.2.2`, not `127.0.0.1`.

---

## Firebase Emulator Suite

Install dependencies and run the Firebase backend locally:

```bash
npm install -g firebase-tools
cd functions
npm install
npm run build
cd ..
firebase emulators:start --only auth,functions,firestore
```

Local emulator ports:

| Service | Address |
|---|---|
| Emulator UI | `http://127.0.0.1:4000` |
| Authentication | `127.0.0.1:9099` |
| Cloud Functions | `127.0.0.1:5001` |
| Firestore | `127.0.0.1:8085` |
| Storage | `127.0.0.1:9199` |

---

## Team setup

```bash
git clone https://github.com/Fedi-Mhadhbi/SmartParking.git
cd SmartParking
git checkout -b feature/your-feature
./gradlew assembleDebug
```

Before changing shared interfaces or contracts:

1. Read `architecture.md`.
2. Read `file-structure.md`.
3. Add contract changes through a team pull request.
4. Do not invent or rename files outside the agreed structure.
5. Never commit Firebase credentials, keystores, `.env` files, or API keys.

---

## Project structure

```text
app/src/main/java/com/fedi/smartparking/
├── core/
├── data/
├── di/
├── domain/
├── features/
├── navigation/
├── service/
└── ui/

app/src/main/res/
├── drawable/
├── drawable-nodpi/
├── values/
├── values-fr/
└── values-ar/

functions/src/
├── auth/
├── parking/
├── reservations/
├── users/
└── index.ts
```

---

## Important demo behavior

- Payment is simulated and does not process real money.
- Reservation data, vehicles, and parking availability are local demo state.
- The QR ticket encodes a demo reservation reference plus parking and place details.
- Local reservation state may reset if app data is cleared or the app is reinstalled.
- Real Firestore transactions and Cloud Functions are the next step for production reservation locking.

---

## Build status

The Android debug application is built with:

```bash
./gradlew assembleDebug
```

Cloud Functions are built with:

```bash
cd functions
npm run lint
npm run build
```
