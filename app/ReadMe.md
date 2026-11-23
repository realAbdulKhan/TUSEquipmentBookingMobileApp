# TUS Equipment Booking Mobile App

GitHub repository: https://github.com/realAbdulKhan/TUSEquipmentBookingMobileApp

## 1. Project overview

The TUS Equipment Booking application is an Android application for browsing university equipment and creating and managing equipment bookings. It provides account registration and login, equipment and category information, booking creation, a personal bookings screen, booking cancellation, and role-based equipment-management controls.

The application uses:
- Kotlin and Jetpack Compose for the Android application and user interface.
- Room for local persistence of equipment, categories and bookings.
- Firebase Authentication for email/password accounts.
- Cloud Firestore for user role documents.
- Android Navigation Compose for movement between application screens.

## 2. Requirements

Use a compatible version of Android Studio with:
- Android SDK Platform 37 (the project compile/target SDK configuration should be checked in the project Gradle files).
- A device or emulator running Android 7.0 (API 24) or later.
- Internet access for Firebase registration, login and Firestore role lookup.
- A compatible JDK/Gradle environment. Use the Gradle JDK configured by the project/Android Studio and resolve any Gradle compatibility prompt using the versions declared by the project.

A physical Android device or an Android Studio emulator can be used. The emulator/device must have Google Play services and network access for Firebase features.

## 3. Download and open the project

1. Clone the repository:

   ```bash
   git clone https://github.com/realAbdulKhan/TUSEquipmentBookingMobileApp.git
   ```

2. Open the cloned project folder in Android Studio.
3. Allow Android Studio to complete the Gradle sync.
4. Select an Android emulator or connect an Android device with USB debugging enabled.
5. Build the project using **Build > Make Project**.
6. Run the application using the **Run** button.

If Gradle sync or the build fails, check the Android Studio Build output and confirm that the required Android SDK and Gradle JDK are installed.

## 4. Firebase configuration

The repository must be configured with a Firebase project before Firebase-backed authentication and role lookup can be tested.

1. Create or use a Firebase project that you control.
2. Register an Android application in Firebase using the exact application ID from the app-level Gradle file. The current package/application ID used by the project is:

   `com.example.mytusequipmentbooking`

3. Download that Firebase project's `google-services.json`.
4. Place `google-services.json` in the Android app module directory (normally `app/google-services.json`). Do not commit a file containing private project configuration unless the project owner has explicitly decided it is safe to publish.
5. In Firebase Authentication, enable the **Email/Password** sign-in provider.
6. Create a Cloud Firestore database.
7. Ensure the Firestore dependency and Google Services Gradle configuration are enabled in the project. The app uses Firebase Authentication and Firestore.
8. Run the app and register a test account. The application uses the authenticated user's Firebase UID to look up a document in the Firestore `users` collection.

### Firestore user role document

For each registered account, the role document is expected at:

`users/{Firebase Authentication UID}`

The document contains a string field named `role`.

Example normal-user document:

```json
{
  "role": "user"
}
```

Example administrator document:

```json
{
  "role": "admin"
}
```

To configure a test administrator:
1. Register the account through the app.
2. In Firebase Authentication, find that account and copy its UID.
3. In Firestore, create or update `users/{UID}`.
4. Set the `role` field to the string `admin`.
5. Sign out and sign in again (or otherwise allow the app to refresh the role) and verify the administrator controls.

A normal account should have `role` set to `user`. Do not share account passwords, private keys, service-account JSON files or other administrative credentials in this repository or submission.

**Security note:** The role check currently controls the application UI and management callbacks. Hiding controls in an Android app is not, by itself, a security boundary. Any future cloud-side administrative operations must also be protected by appropriate Firestore Security Rules or trusted backend authorization. The local Room database is stored on the device.

## 5. Test-account instructions for the marker

The marker can create their own accounts without being given a password:

1. Configure the app with their own Firebase project as described above.
2. Launch the app and use the registration option to create a normal test account with an email address and password chosen by the marker.
3. Repeat registration with a second email address to create a second account.
4. To test administrator-only controls, assign `role = admin` to one test account's `users/{UID}` Firestore document using the steps above.
5. Keep the test passwords private and do not place them in the README or source repository.

If the marker is supplied with a preconfigured Firebase project by the module team, follow the credentials and access instructions supplied separately by the module team. No passwords or administrative service-account credentials are included in this repository documentation.

## 6. Main application structure

| Area | Main files | Responsibility |
|---|---|---|
| Entry point and navigation | `MainActivity.kt`, `AppNavigation.kt` | Starts the Compose UI and navigates between screens. |
| Authentication and roles | `AuthViewModel.kt`, `LoginScreen.kt` | Handles registration, login, logout and Firestore role lookup. |
| Data models | `Equipment.kt`, `Category.kt`, `Booking.kt` | Represent equipment, categories and bookings. |
| Local persistence | Room database, DAO and repository files | Store and retrieve equipment, categories and booking data. |
| Application state | `EquipmentBookingViewModel.kt` | Exposes data and UI state to the screens and handles user actions. |
| Screens and components | `screens/`, `components/` | Display the welcome, equipment, details, bookings, login and settings interfaces. |

The exact file names and package layout can be inspected in the repository.

## 7. Data and booking behaviour

- A booking records the selected equipment, the booking user's Firebase UID, requested duration, purpose and status.
- The personal bookings data flow filters bookings using the signed-in user's UID. The DAO query is the database-level location to inspect for this filter.
- Booking cancellation updates the booking status.
- Equipment, categories and bookings are persisted locally using Room.

The implementation must also be tested to confirm that equipment already booked by another user is unavailable, and that cancelling the booking makes it available again. Personal booking filtering alone does not prove this availability rule.

## 8. Repository and development history

Repository: https://github.com/realAbdulKhan/TUSEquipmentBookingMobileApp

The repository's commit history provides evidence of incremental development. To inspect it locally:

```bash
git log --oneline --decorate --all
git status
```