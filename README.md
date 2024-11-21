Budget Tracker
A modern Android application for tracking personal expenses with an intuitive interface and category-based organization.
Features

Add and manage expenses with title, amount, date, and category
View expenses in a chronological list
Track spending by category with total summaries
Edit or delete existing expenses
Navigation drawer for easy access to different sections
Material Design UI components
Data persistence using Room database

Architecture
The app follows MVVM (Model-View-ViewModel) architecture pattern and uses:

Room Database: For local data persistence
LiveData: For observable data holders
ViewModel: For managing UI-related data
Data Binding: For declarative UI elements
Navigation Component: For handling in-app navigation
Coroutines: For asynchronous operations

Key Components

ExpenseViewModel: Manages expense data operations
DateViewModel: Handles date-related functionality
ExpenseRepository: Mediates between ViewModels and data sources
Room Entities: Expense and CategoryTotal
DAOs: ExpenseDao and CategoryTotalDao

Setup

Clone the repository
Open the project in Android Studio
Sync Gradle files
Run the app on an emulator or physical device

Requirements

Android Studio Arctic Fox or newer
Minimum SDK: Android 21 (Lollipop)
Kotlin 1.8.0 or newer

Libraries Used

AndroidX Core KTX
AndroidX AppCompat
Material Design Components
Navigation Component
Room Database
LiveData and ViewModel
Data Binding
Coroutines
