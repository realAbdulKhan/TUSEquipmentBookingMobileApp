# TUS Equipment Booking

An Android application developed as part of the BSc (Hons) Mobile and Web Computing programme at Technological University of the Shannon (TUS).

The application allows users to browse equipment, view equipment details and create and manage their own equipment bookings.

## Features

- **User authentication:** Register, log in and log out using Firebase Authentication with email and password.
- **Session persistence:** Remain signed in when reopening the application.
- **Equipment catalogue:** Browse equipment organised by category.
- **Equipment management:** Add and delete equipment.
- **Equipment details:** View information about selected equipment.
- **Equipment bookings:** Create bookings by selecting a duration and providing a purpose.
- **My Bookings:** View and cancel bookings belonging to the currently signed-in user.
- **User-specific bookings:** Each booking is associated with the Firebase Authentication user ID.
- **Settings:** Switch between light and dark appearance.
- **Navigation:** Move between application screens using the navigation drawer and bottom navigation bar.

## Technologies Used

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- Room Database
- Firebase Authentication
- Kotlin Coroutines and Flow
- Android Navigation Compose
- SharedPreferences

## Application Architecture

The application uses Room for local data storage.

The main components include:

- **Entities:** Define the database tables for equipment, categories and bookings.
- **DAO:** Contains the database queries and operations.
- **Repository:** Provides the data operations used by the ViewModel.
- **ViewModel:** Manages application data and screen state.
- **Jetpack Compose screens:** Display the user interface.

Firebase Authentication is used for account registration, login and user identity. Equipment and booking records are stored locally using Room; Firebase is not used as a cloud database.

## Database

The local Room database contains three main entities:

| Entity | Purpose |
|---|---|
| Category | Stores equipment categories |
| Equipment | Stores equipment information |
| Booking | Stores booking details, including the equipment ID and user UID |

Bookings are filtered using the authenticated user's UID so that users only see their own bookings.

## Getting Started

1. Clone or download the project.
2. Open the project in Android Studio.
3. Configure the Firebase project for the application.
4. Add the appropriate `google-services.json` file to the `app` directory.
5. Ensure Email/Password authentication is enabled in the Firebase Console.
6. Allow Gradle to synchronise the project.
7. Run the application on an Android emulator or compatible Android device.

## Android Configuration

- **Application ID:** `com.example.mytusequipmentbooking`
- **Minimum SDK:** 24
- **Compile SDK:** 37

## Project Purpose

This project demonstrates the development of an Android equipment booking application using Kotlin, Jetpack Compose, Room Database and Firebase Authentication. It applies concepts including local database management, user authentication, application navigation and reactive user interface development.