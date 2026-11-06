# TUS Equipment Booking Mobile App

An Android application being developed as part of the BSc (Hons) Mobile & Web Computing programme at Technological University of the Shannon (TUS).

The application aims to allow students to browse college equipment and, as development progresses, book equipment for academic use.

## Project Information

- **Project:** TUS Equipment Booking Mobile App
- **Platform:** Android
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Design:** Material 3
- **Local Database:** Room
- **Navigation:** Navigation Compose
- **IDE:** Android Studio

## Current Features

The following functionality has been implemented:

- Android project setup using Kotlin and Jetpack Compose.
- Equipment catalogue displaying six sample equipment items.
- Equipment categories: Laptops, Cameras and Audio Equipment.
- Reusable equipment card components.
- Equipment details screen.
- Navigation between the home screen and equipment details.
- Room database entities for Category, Equipment and Booking.
- Room DAO and database configuration.
- Repository for database operations and initial sample data seeding.
- ViewModel and UI state management.
- Equipment catalogue connected to Room.
- Local database persistence across application restarts.

## Project Structure

```text
TUSEquipmentBookingMobileApp/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/example/mytusequipmentbooking/
│           │       ├── components/
│           │       │   └── EquipmentCard.kt
│           │       ├── data/
│           │       │   ├── SampleData.kt
│           │       │   └── ...
│           │       ├── model/
│           │       │   ├── Category.kt
│           │       │   ├── Equipment.kt
│           │       │   └── Booking.kt
│           │       ├── screens/
│           │       │   ├── HomeScreen.kt
│           │       │   └── EquipmentDetailsScreen.kt
│           │       ├── viewmodel/
│           │       │   └── EquipmentBookingViewModel.kt
│           │       ├── AppNavigation.kt
│           │       └── MainActivity.kt
│           │
│           └── AndroidManifest.xml
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

*Note: The data package also contains the Room database, DAO and repository files.*

## Equipment Categories

The application currently includes:

| Category | Sample Equipment |
|---|---|
| Laptops | Dell Latitude Laptop, MacBook Pro |
| Cameras | Canon EOS Camera, Sony Video Camera |
| Audio Equipment | Rode Microphone, Wireless Headphones |

## Development Progress

### Completed
- [x] Android project setup
- [x] Equipment and category models
- [x] Sample equipment data
- [x] Reusable equipment card
- [x] Home screen
- [x] Equipment details screen
- [x] Navigation
- [x] Room database setup
- [x] DAO and repository
- [x] Initial database seeding
- [x] ViewModel and UI state
- [x] Room integration with the equipment catalogue
- [x] Database persistence testing

### Planned
Further development will follow the assignment requirements and the material covered in class.

- [ ] Firebase authentication
- [ ] User registration and login
- [ ] Equipment booking functionality
- [ ] My Bookings screen
- [ ] Booking validation and cancellation
- [ ] Final application testing and documentation

## Database

The application uses Room for local data persistence.

The database currently contains three main entities:

- **Category:** Stores equipment categories.
- **Equipment:** Stores equipment information and its associated category ID.
- **Booking:** Defines the booking information structure.

Equipment and categories are populated using sample data when the local database is initially empty.

## Testing

The following tests have been completed:

- Application builds successfully.
- Six equipment items display correctly.
- Equipment categories display correctly.
- Equipment details can be opened.
- Back navigation works.
- Equipment data remains available after restarting the application.

## Academic Development

This project is being developed using the techniques and coding approaches demonstrated in the lecturer-provided practicals, including EventHub Navigation, EventHub Room Database and User Management App.

Additional functionality will be implemented as the relevant topics are covered during class.

---

**Repository:** [TUSEquipmentBookingMobileApp](https://github.com/realAbdulKhan/TUSEquipmentBookingMobileApp)

**Developer:** Abdul Ahad Khan  
**Institution:** Technological University of
