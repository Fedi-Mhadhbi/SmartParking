# SmartParking: File Structure (source of truth)

Owners: **D1** = Dev 1 (UI), **D2** = Dev 2 (reservations), **D3** = Dev 3 (identity/data),
**ALL** = written together and changed only through a pull request all three agree to.

Android base path: `app/src/main/java/com/fedi/smartparking/`
There are NO XML layouts. Every screen is a Compose `.kt` file.

## 1. domain/ (ALL: this is the contract between the three parts)

```
domain/model/
  User.kt  UserRole.kt  AppMode.kt
  Vehicle.kt  VehicleType.kt
  Parking.kt  ParkingType.kt  ListingStatus.kt  GeoLocation.kt  OpeningHours.kt  Facility.kt
  Space.kt  SpaceType.kt  OperationalStatus.kt
  Reservation.kt  ReservationStatus.kt  BookingTicket.kt
  Review.kt
  AppNotification.kt  NotificationType.kt
  OwnerProfile.kt  OwnerType.kt  VerificationStatus.kt  OwnerStats.kt
  ParkingFilter.kt  SortOption.kt
  Result.kt  AppError.kt

domain/repository/
  AuthRepository.kt  UserRepository.kt  VehicleRepository.kt  ParkingRepository.kt
  SpaceRepository.kt  ReservationRepository.kt  ReviewRepository.kt
  NotificationRepository.kt  OwnerRepository.kt  StorageRepository.kt  LocationRepository.kt

domain/usecase/
  auth/         LoginUseCase.kt  RegisterUserUseCase.kt  LogoutUseCase.kt
                ResetPasswordUseCase.kt  SendEmailVerificationUseCase.kt  ObserveAuthStateUseCase.kt
  user/         GetCurrentUserUseCase.kt  UpdateProfileUseCase.kt  SwitchModeUseCase.kt
                EnableOwnerModeUseCase.kt
  vehicle/      GetVehiclesUseCase.kt  AddVehicleUseCase.kt  UpdateVehicleUseCase.kt
                DeleteVehicleUseCase.kt
  parking/      GetNearbyParkingsUseCase.kt  SearchParkingsUseCase.kt  GetParkingDetailsUseCase.kt
                GetParkingAvailabilityUseCase.kt  CreateParkingListingUseCase.kt
                UpdateParkingUseCase.kt  UpdateParkingAvailabilityUseCase.kt
                GetOwnerParkingsUseCase.kt
  space/        GetSpacesUseCase.kt  CreateSpaceUseCase.kt  UpdateSpaceStatusUseCase.kt
  reservation/  CreateReservationUseCase.kt  CancelReservationUseCase.kt
                GetMyReservationsUseCase.kt  GetReservationDetailsUseCase.kt
                GetOwnerReservationsUseCase.kt  CheckInVehicleUseCase.kt
                CheckOutVehicleUseCase.kt  ValidateBookingQrUseCase.kt
  owner/        SubmitOwnerVerificationUseCase.kt  GetOwnerDashboardUseCase.kt  GetEarningsUseCase.kt
  review/       CreateReviewUseCase.kt  GetParkingReviewsUseCase.kt
  notification/ GetNotificationsUseCase.kt  MarkNotificationReadUseCase.kt
```

Enums (exact values):
- UserRole / AppMode: `DRIVER`, `OWNER`
- VehicleType: `CAR`, `MOTORCYCLE`, `VAN`, `OTHER`
- ParkingType: `PUBLIC`, `PRIVATE`
- ListingStatus: `PENDING`, `APPROVED`, `REJECTED`, `SUSPENDED`
- SpaceType: `STANDARD`, `ACCESSIBLE`, `ELECTRIC`
- OperationalStatus: `AVAILABLE`, `OCCUPIED`, `MAINTENANCE`
- ReservationStatus: `PENDING`, `CONFIRMED`, `ACTIVE`, `COMPLETED`, `CANCELLED`, `EXPIRED`

## 2. data/ (implementations of the interfaces)

```
data/remote/auth/        FirebaseAuthDataSource.kt                                    (D3)
data/remote/firestore/   UserFirestoreDataSource.kt  VehicleFirestoreDataSource.kt
                         ParkingFirestoreDataSource.kt  SpaceFirestoreDataSource.kt
                         ReviewFirestoreDataSource.kt  NotificationFirestoreDataSource.kt (D3)
                         ReservationFirestoreDataSource.kt                            (D2)
data/remote/functions/   CloudFunctionsClient.kt  FunctionNames.kt                    (D2 + D3)
data/remote/storage/     StorageDataSource.kt                                         (D3)
data/dto/                UserDto.kt  VehicleDto.kt  ParkingDto.kt  SpaceDto.kt
                         ReviewDto.kt  NotificationDto.kt                             (D3)
                         ReservationDto.kt  TicketDto.kt                              (D2)
data/mapper/             UserMapper.kt  VehicleMapper.kt  ParkingMapper.kt  SpaceMapper.kt
                         ReviewMapper.kt  NotificationMapper.kt                       (D3)
                         ReservationMapper.kt                                         (D2)
data/repository/         AuthRepositoryImpl.kt  UserRepositoryImpl.kt  VehicleRepositoryImpl.kt
                         ParkingRepositoryImpl.kt  SpaceRepositoryImpl.kt
                         ReviewRepositoryImpl.kt  NotificationRepositoryImpl.kt
                         OwnerRepositoryImpl.kt  StorageRepositoryImpl.kt             (D3)
                         ReservationRepositoryImpl.kt                                 (D2)
                         LocationRepositoryImpl.kt                                    (D1)
data/fake/               FakeAuthRepository.kt  FakeUserRepository.kt  FakeVehicleRepository.kt
                         FakeParkingRepository.kt  FakeSpaceRepository.kt
                         FakeReservationRepository.kt  FakeReviewRepository.kt
                         FakeNotificationRepository.kt  FakeOwnerRepository.kt
                         FakeStorageRepository.kt                                     (D1)
data/local/              dao/ + database/ (Room, optional)
service/                 SmartParkingMessagingService.kt  (FCM)                       (D3)
```

## 3. core/, di/, navigation/, ui/

```
core/constants/    Collections.kt  AppConstants.kt                       (ALL)
core/common/       UiState.kt  UiText.kt                                 (D1)
core/extensions/   DateExtensions.kt  StringExtensions.kt                (D1)
core/permissions/  PermissionHelper.kt  (location, camera)               (D1)
core/utils/        TimeUtils.kt  PriceFormatter.kt  GeoUtils.kt          (ALL)

di/                AppModule.kt (D1)  FirebaseModule.kt (D3)
                   RepositoryModule.kt (binds interface -> Fake or Real: THE connection point)
                   UseCaseModule.kt (D1)

navigation/        AppNavigation.kt  NavRoutes.kt  BottomNavigation.kt
                   AuthNavGraph.kt  DriverNavGraph.kt  OwnerNavGraph.kt  (D1)

ui/theme/          Color.kt  Theme.kt  Type.kt  Shape.kt  Spacing.kt     (D1)
ui/components/     SmartButton.kt  SmartTextField.kt  SmartTopBar.kt  ParkingCard.kt
                   ParkingImageGallery.kt  VehicleCard.kt  ReservationCard.kt  StatusBadge.kt
                   PriceLabel.kt  SearchBar.kt  FilterBottomSheet.kt  LocationSelector.kt
                   AvailabilityIndicator.kt  SpaceGrid.kt  LoadingState.kt  EmptyState.kt
                   ErrorState.kt  ConfirmDialog.kt                       (D1)

MainActivity.kt    SmartParkingApp.kt (@HiltAndroidApp)                  (D1)
```

## 4. features/ (all D1: each feature = Screen files + ViewModel + UiState)

```
features/auth/           SplashScreen  WelcomeScreen  LoginScreen  RegisterScreen
                         ForgotPasswordScreen  AuthViewModel  AuthUiState
features/home/           DriverHomeScreen  HomeViewModel  HomeUiState
features/explore/        ExploreMapScreen  ParkingListScreen  SearchFilterScreen
                         ExploreViewModel  ExploreUiState
features/parking/        ParkingDetailsScreen  ParkingDetailsViewModel  ParkingDetailsUiState
features/booking/        ReservationConfigScreen  BookingConfirmationScreen  BookingSuccessScreen
                         MyReservationsScreen  ReservationDetailsScreen  TicketQrView
                         BookingViewModel  BookingUiState
features/vehicles/       MyVehiclesScreen  AddEditVehicleScreen  VehicleViewModel  VehicleUiState
features/owner/onboarding/    OwnerOnboardingScreen  OwnerVerificationScreen  OwnerOnboardingViewModel
features/owner/dashboard/     OwnerDashboardScreen  OwnerDashboardViewModel
features/owner/listings/      MyParkingsScreen  AddParkingScreen  LocationPickerScreen
                              UploadPhotosScreen  PricingScheduleScreen  ReviewListingScreen
                              OwnerParkingDetailsScreen  AvailabilityCalendarScreen
                              ListingViewModel  ListingUiState
features/owner/spaces/        ManageSpacesScreen  SpacesViewModel
features/owner/reservations/  IncomingReservationsScreen  OwnerReservationDetailsScreen
                              QrScannerScreen  CheckInOutScreen  OwnerReservationsViewModel
features/owner/earnings/      EarningsScreen  EarningsViewModel
features/notifications/  NotificationsScreen  NotificationsViewModel
features/profile/        ProfileScreen  EditProfileScreen  SettingsScreen  LanguageScreen
                         PrivacySecurityScreen  ProfileViewModel
features/reviews/        ReviewScreen  ReviewViewModel
features/support/        HelpSupportScreen
```
(Each name above is a `.kt` file; for example `LoginScreen` means `LoginScreen.kt`.)

## 5. The only XML files (D1)
```
app/src/main/AndroidManifest.xml
res/values/strings.xml  res/values-fr/strings.xml  res/values-ar/strings.xml
res/values/themes.xml   res/xml/file_paths.xml  res/xml/locales_config.xml
res/xml/backup_rules.xml  res/xml/data_extraction_rules.xml
res/drawable/ic_*.xml (vector icons)
```

## 6. Backend (Cloud Functions, TypeScript)
One exported function per file; function name = file name (camelCase).

```
functions/src/
  index.ts                        (shared: one export line per function)
  shared/       errors.ts  time.ts  validation.ts  types.ts  admin.ts            (ALL)
  auth/         permissions.ts                                                    (ALL)
  reservations/ createReservation.ts  cancelReservation.ts  checkInVehicle.ts
                checkOutVehicle.ts  reservationValidation.ts  slotInventory.ts
                pricing.ts                                                        (D2)
  qr/           generateTicket.ts  validateTicket.ts                              (D2)
  scheduled/    expireReservations.ts                                             (D2)
  users/        onUserCreate.ts  enableOwnerMode.ts  submitOwnerVerification.ts   (D3)
  parking/      createParking.ts  updateParking.ts  updateAvailability.ts
                manageSpaces.ts  geohash.ts                                       (D3)
  notifications/ sendNotification.ts                                              (D3)
  reviews/      createReview.ts                                                   (D3)
functions/test/
  reservations/ createReservation.test.ts  concurrency.test.ts                    (D2)
  parking/      parking.test.ts                                                   (D3)
  rules/        firestore.rules.test.ts                                           (D3, D2 for reservations rules)
Root: firebase.json  firestore.rules  firestore.indexes.json  storage.rules  .firebaserc
docs/api.md  (D3: public documentation of backend operations for the future JEE admin app)
```
