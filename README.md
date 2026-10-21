# 💸 Expense Book — Personal Finance Tracker

**A modern Android expense tracker built with Kotlin and Jetpack Compose, designed to make everyday money management simple and intuitive.**

Expense Book helps users record spending, organize expenses, and understand their financial habits through a clean, user-friendly experience.

## ✨ Features

- **Expense Tracking** — Easily record and manage daily expenses.
- **Category Management** — Organize spending into meaningful categories.
- **Spending Insights** — Understand expenses and track financial activity.
- **Daily Reminders** — Scheduled notifications to maintain consistent tracking.
- **PDF Export** — Generate and share expense reports.
- **Offline-First Experience** — Manage expenses using local data persistence.

## 🛠 Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin, Jetpack Compose | Native Android development and declarative UI |
| MVVM, Coroutines, Flow | Reactive state management and asynchronous operations |
| Hilt | Dependency injection |
| Room | Local database persistence |
| WorkManager | Scheduled background tasks |
| Firebase Analytics, Crashlytics | Usage analytics and crash monitoring |
| Gradle Kotlin DSL | Modular build configuration |

## 🏗 Architecture

Built with a **modular architecture** emphasizing separation of concerns, maintainability, and scalability.

```text
expensebook/
├── app/                 # App entry point and features
├── core/
│   ├── ui/              # Shared UI components
│   ├── common/          # Common utilities
│   └── database/        # Local persistence
└── .github/workflows/   # CI workflows
```

🚀 Getting Started

Clone the repository:

git clone https://github.com/shahsamir97/expensebook.git

Open the project in Android Studio.

Create a Firebase project in the Firebase Console, register an Android app using the project's applicationId, and enable Firebase Crashlytics.

Download the google-services.json configuration file and place it in the app/ directory:

expensebook/
└── app/
    └── google-services.json

Sync Gradle and run the app on an Android device or emulator.

Note: google-services.json is not included in this repository. A valid Firebase configuration is required to build and run the project.


*Built with a focus on clean architecture, modern Android development practices, and everyday usability.*
