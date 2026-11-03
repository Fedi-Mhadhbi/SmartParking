# SmartParking

Android app (Kotlin, Jetpack Compose, Material 3) with a Firebase backend (Auth, Firestore,
Cloud Functions in TypeScript, Storage, FCM). Package: `com.fedi.smartparking`. Min SDK 26.

Team of 3:
- **Dev 1**: Android UI (frontend)
- **Dev 2**: reservation engine (backend A)
- **Dev 3**: identity, data, security, owner side (backend B)

SmartParking has exactly TWO roles: **DRIVER** and **OWNER**. One account can have both and
switch modes inside the same app. There is NO admin role, NO admin screens, NO payments and NO
IoT in the MVP. A separate Java EE (JEE) admin project will be built later.

## Read these first (they are the source of truth)
- @docs/architecture.md : layers, data flow, roles, security rules
- @docs/file-structure.md : EXACT file and package names, with the owner of each file
- @docs/contract.md : Firestore schema, enums, Cloud Function names, request/response JSON, errors
- @docs/team-workflow.md : Git branches, pull requests, how to change the contract

## Rules for Claude (always follow)
1. Use ONLY the file, class and package names listed in `docs/file-structure.md`. Never rename,
   move, merge or invent files. If you think something must change, STOP and tell me, so I can
   agree it with my teammates first.
2. Architecture: Screen -> ViewModel -> UseCase -> Repository (interface in `domain/`) ->
   DataSource -> Firebase. No Firestore calls in Composables. No business logic in UI.
3. Compose only. There are NO XML layout files. XML is used only for the manifest and resources
   (strings, themes, xml configs, vector icons).
4. No hardcoded UI strings: use `strings.xml` (English, French, Arabic with RTL). Use theme
   colors and tokens; never hardcode colors in screens.
5. Reservations are created ONLY by the `createReservation` Cloud Function, never by writing to
   Firestore from the app. Prices are calculated by the backend.
6. Timestamps are stored in UTC and displayed in Africa/Tunis. Currency is TND.
7. Do not change `domain/model`, `domain/repository`, `core/constants` or `FunctionNames.kt`
   without asking me to check with my teammates.
8. Work in small steps: one feature or a few files per answer, never the whole app at once.
   Always show the full file path before each code block.
9. Never commit secrets: no `serviceAccount*.json`, no keystores, no `.env`, no API keys.
10. Use stable, mutually compatible dependency versions and explain version choices briefly.

## Who I am (EACH DEVELOPER KEEPS ONLY THEIR OWN LINE)
<!-- Dev 1: keep this line, delete the other two -->
@docs/dev1-guide.md
<!-- Dev 2: replace the line above with: @docs/dev2-guide.md -->
<!-- Dev 3: replace the line above with: @docs/dev3-guide.md -->

## Commands
- Android build: `./gradlew assembleDebug`
- Backend emulators: `firebase emulators:start` (UI at http://127.0.0.1:4000)
- Functions build: `cd functions && npm run build`
- Emulator ports (firebase.json): Auth 9099, Functions 5001, Firestore 8085, Storage 9199, UI 4000
- From the Android emulator, the host computer is `10.0.2.2` (not `127.0.0.1`).
