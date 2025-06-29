# 📰 NewsApp

A modern Android app built with Jetpack Compose that fetches and displays the latest headlines using the News API. The app includes searching, saving articles, pagination and offline support.

---

## 🚀 Features

* 🏠 Home screen with latest news
* 🔍 Search articles with debounce
* 📂 Save and delete articles locally
* ↕️ Pagination using Paging 3
* 🔄 Pull to refresh support
* 🛁 Swipe to delete saved articles
* 🌙 Light/Dark theme toggle
---

// Videos placeholder

---

## 🔧 Getting Started

### 1. Clone the Repo

```bash
git clone https://github.com/your-username/NewsApp.git
cd NewsApp
```

### 2. Setup NewsAPI Key

Get an API key from [https://newsapi.org](https://newsapi.org) and add it to your `local.properties`:

```
NEWS_API_KEY=your_api_key_here
```

Or update it in the appropriate `BuildConfig` field.

### 3. Run the App

Open the project in **Android Studio** and run on an emulator or device.

---

## 🧱 Architecture Overview

The app follows **Clean Architecture** principles with a layered separation of concerns:

### 🧩 Layers

* **UI Layer**: Jetpack Compose + Navigation
* **Domain Layer**: ViewModels (Hilt injected)
* **Data Layer**:

    * `Room` for local DB
    * `Retrofit` for networking
    * `PagingSource` for API and DB

### ⟳ Flow

* `HomeScreen` and `SearchScreen` use **Paging3** to load paginated articles.
* `SavedScreen` fetches from Room using a Room `PagingSource`.
* All data flows through **Repository** injected via **Hilt**.

---

## 🧪 Testing

* ✅ `HomeViewModelTest` for verifying ViewModel logic
* ✅ `NewsRepositoryTest` for repository behavior using fake PagingSources
* ✅ Coroutine and Flow tests using `kotlinx-coroutines-test`
* 🧪 Run tests:

```bash
./gradlew test
```

---

## 📦 Dependencies

* Jetpack Compose
* Hilt
* Paging 3
* Room
* Retrofit + Gson
* Kotlin Coroutines + Flow
* Coil (for image loading)
* JUnit, Mockito, Coroutine Test

---

## 📂 Folder Structure

```plaintext
🔻 data/
│   🔻 local/        # Room DB & DAO
│   🔻 mapper/       # Mapping between DTOs and domain models
│   🔻 remote/       # Retrofit service
│   🔻 model/        # Data models (DTOs + domain)
│   🔻 paging/       # PagingSources (API + DB)
│   └️repository/    # NewsRepository
│
🔻 ui/
│   🔻 screens/      # Home, Search, Detail, Saved
│   🔻 views/        # Reusable UI
│   🔻 theme/        # Theme for the app
│   🔻 viewmodel/        # Hilt-injected HomeViewModel
🔻 di/               # Hilt modules
🔻 utils/            # Utility classes
🔻 MainActivity/     # App entry point
🔻 NewsPulseApplication/            #App class
```

---

## 🧐 Architecture Decisions

* **Single ViewModel**: One `HomeViewModel` shared across all screens for better state sharing and simplicity.
* **State Hoisting**: Search query and selected article are lifted to ViewModel and used across composables.
* **Paging3**: Used for both network and Room-backed pagination with lazy loading.
* **Testability**: Repository and ViewModel are fully unit tested with coroutine and paging sources mocked.

---
