📓 Journal App
A beautiful and secure Android journaling application that allows users to capture their thoughts with text and images. Built with modern Android development practices and powered by Firebase for a seamless, real-time experience.

📸 Screenshots

<img src="/demopic.jpg" width="200">	<img src="/1.jpg" width="200"> <img src="/2.jpg" width="200"> <img src="/4.jpg" width="200">


✨ Features
User Authentication: Secure email and password login and registration using Firebase Auth.

CRUD Operations: Full Create, Read, Update, and Delete functionality for journal entries.

Multimedia Support: Attach images to journal entries, stored securely in Firebase Cloud Storage.

Real-time Sync: Journal data is synchronized in real-time across devices using Cloud Firestore.

Modern UI: Clean, intuitive user interface built with ConstraintLayout and Material Design principles.

Data Persistence: Offline support with Cloud Firestore, allowing users to view and edit journals without an internet connection.

🛠️ Built With
Language: Kotlin

Architecture: Model-View-ViewModel (MVVM)

Backend & Database: Firebase

Authentication

Cloud Firestore

Cloud Storage for Firebase

Asynchronous Programming: Kotlin Coroutines

Networking: Retrofit (for any external APIs)

Android Jetpack Components:

LiveData

ViewBinding

RecyclerView

Image Loading: Glide or Coil (Assumed from functionality)

🏗️ Architecture & Patterns
This app follows the Model-View-ViewModel (MVVM) architecture to ensure a separation of concerns, making the code more maintainable, testable, and scalable.

Model: Represents the data layer, including the Data Access Object (DAO), repository pattern for Firestore, and data classes (JournalUser, Journal).

View: Consists of Activities and Fragments (MainActivity, SignUpActivity, JournalList, AddJournalActivity) that observe the ViewModel and display data.

ViewModel: Acts as a communication center between the Model and the View, holding UI-related data and surviving configuration changes.


⭐️ If you found this project helpful, please give it a star on GitHub!
