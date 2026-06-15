# Blog App

A modern Android client for browsing and managing blog posts, built with **Kotlin** and **Jetpack Compose**.
The app integrates with the [JSONPlaceholder API](https://jsonplaceholder.typicode.com) to simulate CRUD operations and features offline support via Room caching.

## ✨ Features

- 🔐 **Biometric / device PIN authentication** on the start screen
- 📱 **Offline‑first** – posts are cached in Room and visible without internet
- 📄 **Post list** – shows title + short body preview
- 📖 **Post details** – full content with delete option (confirmation dialog)
- ✏️ **Add new post** – form with title/body validation
- 🔄 **Manual refresh** – fetch latest posts from network
- 🗑️ **Delete posts** – API call + local cache removal
- 📡 **Network error handling** – user‑friendly messages when offline or timeout

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Kotlin | Main programming language |
| Jetpack Compose | Modern UI toolkit |
| Material 3 | UI components and theming |
| Room | Local database + offline cache |
| Retrofit + Gson | REST API communication |
| OkHttp (logging interceptor) | Network debugging |
| Coroutines + Flow | Asynchronous operations |
| ViewModel | UI state management |
| Compose Navigation | Screen navigation |
| Biometric KTX | Biometric/PIN authentication |

## 📱 Minimum Requirements

- Android SDK 21 (Android 5.0 Lollipop) or higher
- A device/emulator with biometric hardware (fingerprint, face) or device PIN/pattern set up (for authentication screen)

## 🚀 How to Run the Project

### Prerequisites
- Android Studio Panda | 2025.3.2 or newer
- JDK 21 or higher

### Steps

1. **Clone the repository**  
   ```bash
   git clone https://github.com/tymon-abramczyk/blog-app.git
   cd blog-app
   ```

2. **Open the project in Android Studio**
    - Select `File → Open` and choose the project folder.
    - Wait for Gradle sync to complete.

3. **Build and run**
    - Connect an Android device or start an emulator (API 21+).
    - Click the **Run** button (green triangle) or use `Shift + F10`.

## 🔐 Biometric Authentication Setup

To test the biometric screen on an emulator:
- Go to **Settings → Security → Fingerprint** (exact path depends on Android version).
- Enrol a fingerprint (e.g., via emulator extended controls).
- The app will prompt for authentication on launch. On success, the main screen appears.

On a real device without biometric hardware, the app falls back to device PIN/pattern if available (system handles it).

## 🧪 API Behaviour (JSONPlaceholder)

The API **simulates** CRUD operations:
- `GET /posts` – returns a fixed list of 100 posts.
- `POST /posts` – returns the sent post with a new ID (e.g., 101) – not persisted on the server.
- `DELETE /posts/{id}` – always returns 200 OK, but does not actually delete on the server.

The app treats these as real operations: posts added or deleted are reflected in the local Room database, so the user sees a consistent view.

## 📁 Project Structure (simplified)

```
com.example.blogapp
├── data
│   ├── database        // PostDatabase
│   ├── model           // Post entity + PostDao
│   ├── remote          // ApiService + RetrofitInstance
│   └── repository      // BlogRepository (single source of truth)
├── ui
│   ├── auth            // AuthScreen + Biometric logic
│   ├── post            // Post screens + respective ViewModels
├── navigation          // Screen routes + NavGraph
├── util                // ConnectivityObserver
└── BlogApp             // Main Application class
└── MainActivity        // Main Activity class
```

## 🧑‍💻 Author

Tymon Abramczyk

## 📄 License

This project is for recruitment purposes only.

## 📸 Screenshots

| Post list    | Post details | Post add                                                                                                                             |
|--------------|--------------|--------------------------------------------------------------------------------------------------------------------------------------|
| <img width="1084" height="2412" alt="Image" src="https://github.com/user-attachments/assets/587f5ecc-b1ad-4efd-9fa3-82230db3af59" /> | <img width="1084" height="2412" alt="Image" src="https://github.com/user-attachments/assets/ceb6305b-1d1d-440d-8f05-044b6489a2b6" /> | <img width="1084" height="2412" alt="post_add" src="https://github.com/user-attachments/assets/04d05172-6dc8-41ce-8ba9-849c5d2a7295" /> |
