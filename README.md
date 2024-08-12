# Stock-App

### Set up 

1. Dowlonad the latest version of [Android Studio](https://developer.android.com/studio)
2. Clone the repository
3. Sync the project
4. Make sure you have SDK 24 or later installed
6. Create an Emulator
5. Run the `App` confiiguration

### Architecture
The project was implemented using MVVM (Model-View-ViewModel) pattern, the View contains the UI components like the Main Activity and the Composables screens and components. The ViewModel is the bridge that connecs the UI and the Model/business logic, VM controls the UI related data and handling the logic to update so the View can react to it via Kotlin Flow. The Repository controls the data access, which interacts with the Data Source. The Data Source provids data via local (eg. databases) and/or remote sources (eg. webapi).

### Assumptions
- `HomeScreen`: It always tries to load stocks from Remote at least once - to keep stocks up-to-date. After first load, Home screen uses cached data when it is possible. The screen handles errors and empty. The search feature has a debounced flow so the app has a better handling when the user is typing and doesn't try to reload data every character the user entered.
- `HomeViewModel`: It is injected in the Home Screen and it connects the UI and the repostory.
- `StockRepository`: It is responsible for data access and handles when it should use data from the database or fetch data from the api. It is also responsible for emitting error/success state.
- `StockDao`: It provides the database data. `getStocksByNameOrTicker` is responsible for case-insensity query and partial ticker/company name query. 

### Main Third-party libraries
| Lib | Description |
|----|----|
| hiltAndroid | It makes easy to dependency injection - Reduce boilerplate code |
| retrofit | Standard HTTP client for Android |
| room  | Standard database library for Android - SQLite abstraction |
| navigationCompose | This lib makes simplifies navigation in Jetpack Compose |
| material  | it provides Material Design components |
| mockk  |mocking library for unit tests which makes simplifies mocks creation |
