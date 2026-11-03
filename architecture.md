# SmartParking: Architecture

## 1. What the app is
SmartParking connects drivers who need parking with owners who rent out parking spaces.
- **Driver**: finds parking (map/list/filters), reserves a time slot, gets a QR ticket, manages vehicles.
- **Owner**: publishes parkings, sets prices and schedules, manages spaces, validates QR codes
  (check-in / check-out), sees reservations and estimated earnings.

### Roles
- Only two roles: `DRIVER` and `OWNER`. A user can have both.
- Every new account starts with `roles: ["DRIVER"]` and `activeMode: "DRIVER"`.
- `roles` = what the user is ALLOWED to do. Only the backend changes it (`enableOwnerMode`).
- `activeMode` = which side of the app the user is looking at. It is a UI preference only; it must
  always be one of the user's roles. Permissions are always checked by the backend.
- There is no ADMIN in the Android app. Listings start as `PENDING`; a future JEE admin app
  approves them. During development, set `listingStatus` to `APPROVED` through the emulator/seed.

### Out of scope (MVP)
Admin screens, IoT/sensors, camera plate recognition, real online payments, AI predictions.

## 2. Layers (Android)
```
PRESENTATION  Compose screens + reusable components + ViewModels + UiState   (Dev 1)
DOMAIN        models, repository interfaces, use cases                        (written together)
DATA          repository implementations, Firebase data sources, DTOs,        (Dev 2 / Dev 3)
              mappers, Cloud Functions client, optional Room cache
```
Data flow: `Compose Screen -> ViewModel -> UseCase -> Repository (interface) -> DataSource -> Firebase`

- The domain layer must NOT import Firebase classes.
- ViewModels expose `StateFlow<...UiState>`; screens use `collectAsStateWithLifecycle()`.
- ViewModels only call UseCases. Screens only render state and send events to the ViewModel.
- Hilt provides dependencies. `di/RepositoryModule.kt` binds each repository interface to either
  the Fake (Dev 1, mock data) or the Real implementation. Switching is a one-line change.

## 3. Backend
- Firebase Authentication (email/password), Cloud Firestore, Cloud Storage, FCM, App Check.
- Cloud Functions in TypeScript (strict mode) for every critical operation.
- Firebase Emulator Suite for all development (no Blaze plan needed locally).
- Firestore rules enforce access even if the app is modified. Clients never write `roles`,
  `listingStatus`, reservations or `slotInventory` directly.

### Reservation engine (the critical part)
- Every space is divided into 30-minute slots. A booking from 10:00 to 12:00 needs 4 slots.
- `slotInventory` documents have a deterministic ID derived from `spaceId` + slot start time.
- `createReservation` claims ALL needed slots in ONE Firestore transaction. If one slot is taken,
  the whole request fails. The call is idempotent (a retry must not create a duplicate booking).
- Operational status of a space (AVAILABLE/OCCUPIED/MAINTENANCE) is NOT enough to know whether it
  can be booked: a space can be empty now and reserved for later.
- Cancellation (MVP): allowed before start time; releases future slots; reservation stays in history.
- Check-in: owner scans QR -> `checkInVehicle` -> CONFIRMED to ACTIVE. Check-out: -> COMPLETED.

### QR security
The QR contains only an opaque random ticket token (never email, phone or plate). The backend
stores only its hash (`qrTokenHash`). Every scan is validated by the backend (token, parking id,
status, time window). An owner can only check in bookings of their own parking.

## 4. Time, money, location
- Store timestamps in UTC. Display in Africa/Tunis. Slot boundaries every 30 minutes.
- Currency TND. The backend calculates prices (billable hours x hourly rate) and stores the price
  and currency on the reservation at booking time.
- Nearby search: geohash-based (GeoFire-style), then exact distance filtering on the client/server.
  Firestore has no arbitrary-radius geo query.

## 5. Security checklist
- No secrets in Git. Never put Admin SDK credentials in the Android app.
- Storage paths: `parking-images/{ownerId}/{parkingId}/{imageId}`,
  `owner-documents/{ownerId}/{documentId}` (private), `profile-images/{userId}/{imageId}`.
- Request location permission only when needed; allow manual location search.
- Test: unauthenticated access, other user's data, self-approval of a listing, duplicate booking,
  QR reuse, private document access, invalid state transitions.

## 6. Design system
Navy `#142B45`, Emerald `#16A67A`, Background `#F4F6F8`, Amber `#F59E0B`, Red `#DC2626`, White.
Material 3, 16-20dp screen padding, 14-20dp card radius, 52-56dp primary buttons, touch targets
at least 48dp. Status must use text/icon as well as color (accessibility).
Bottom navigation: Driver = Home, Explore, Bookings, Profile. Owner = Dashboard, My Parkings,
Reservations, Profile.

## 7. Languages
English, French, Arabic (RTL). All user-facing text in string resources from day one.
