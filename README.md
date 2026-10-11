# Shopping Product App — Android

This repository contains the Android codebase for a simple e-commerce application built using modern Android development practices. The application retrieves product information from backend services and presents it through a searchable, paginated product catalogue and a product detail screen.

The project demonstrates a separation of presentation, domain, and data responsibilities, along with local data persistence, pagination, dependency injection, and automated testing.

## Contents

- [Description](#description)
- [Features](#features)
- [Platform and Technology Stack](#platform-and-technology-stack)
- [Architecture](#architecture)
- [Data Flow](#data-flow)
- [Pagination and Caching](#pagination-and-caching)
- [How to Run the Codebase](#how-to-run-the-codebase)
- [Unit Tests](#unit-tests)
- [Modules and Package Structure](#modules-and-package-structure)
- [Dependency Management](#dependency-management)
- [Code Flow](#code-flow)
- [Screenshots](#screenshots)
- [Future Improvements](#future-improvements)

## Description

The application retrieves product data from a remote API, transforms the response into domain models, and displays the information in the Android UI.

The main screens are:

1. **Product Catalogue** — displays products in a grid with pagination and search functionality.
2. **Product Details** — displays detailed information about a selected product, including its description, images, pricing, discount, rating, stock, specifications, and reviews where available.

The application uses Room for local persistence and Paging 3 to load product data incrementally. A 30-minute cache freshness window is part of the project requirements.

Product pricing includes support for calculating the discounted price from the original price and discount percentage.

## Features

- Product catalogue displayed using Jetpack Compose.
- Paginated product loading using AndroidX Paging 3.
- Product search.
- Product detail screen with navigation.
- Remote API integration using Retrofit and OkHttp.
- Local product persistence using Room.
- Cache freshness handling with a 30-minute requirement.
- Discounted price calculation.
- Kotlin Coroutines and Flow for asynchronous operations and observable data.
- ViewModel-based UI state management.
- Dependency injection using Hilt.
- Error handling, retry support, and empty/loading states.
- Unit tests for data, domain, and ViewModel logic.
- Light and dark theme support through Material theme configuration.

## Platform and Technology Stack

The project uses the following technologies:

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| UI Components | Material 3 |
| Architecture | Presentation, Domain, and Data separation |
| State Management | ViewModel, StateFlow |
| Asynchronous Programming | Kotlin Coroutines, Flow |
| Networking | Retrofit, OkHttp |
| JSON Serialization | Kotlin Serialization |
| Local Database | Room |
| Pagination | Paging 3, Paging Compose, Room Paging |
| Dependency Injection | Hilt |
| Image Loading | Coil 3 |
| Unit Testing | JUnit, MockK |
| Coroutine Testing | kotlinx-coroutines-test |
| API Testing | MockWebServer |
| Code Generation | KSP |
| Build System | Gradle Kotlin DSL |

### SDK Configuration

The project uses the SDK and build-tool versions configured in its Gradle files. Refer to the app module's `build.gradle.kts` and the version catalog for the current configuration.

## Architecture

The application separates presentation, domain, and data responsibilities to improve maintainability, testability, and separation of concerns.

### 1. Presentation Layer

The presentation layer is responsible for rendering the UI and handling user interactions.

Responsibilities include:

- Building screens and reusable components using Jetpack Compose.
- Managing screen-level state through ViewModels.
- Collecting observable state using lifecycle-aware APIs.
- Displaying paginated products using `collectAsLazyPagingItems()`.
- Handling search input, loading states, errors, and retries.
- Navigating between the product catalogue and product details.
- Mapping domain models into presentation-specific UI models where required.

Compose-specific models and annotations should remain in the presentation layer rather than introducing UI dependencies into the domain layer.

### 2. Domain Layer

The domain layer contains product business models, repository contracts, and use cases.

Responsibilities include:

- Defining the `Product` domain model.
- Defining the `ProductsRepository` contract.
- Providing use cases for retrieving products, searching products, and retrieving an individual product.
- Keeping business rules, such as discounted price calculations, independent of UI and network implementation details.

The domain layer should not depend on Jetpack Compose, Retrofit, or Room.

### 3. Data Layer

The data layer handles communication with external services and local persistence.

Responsibilities include:

- Retrieving product data through the remote data source.
- Making API requests using Retrofit and OkHttp.
- Converting network response models into domain models.
- Persisting and retrieving products through Room.
- Coordinating local and remote data sources.
- Supporting the repository's pagination and cache-refresh strategy.
- Handling network and data-mapping errors.

Network response models, database entities, and domain models should remain separate where their responsibilities differ.

### Dependency Direction

The intended dependency flow is:

```text
Presentation Layer
        |
        v
   Domain Layer
        ^
        |
   Data Layer
```

The presentation layer calls domain use cases. The domain layer defines repository contracts, and the data layer provides their implementations.

Hilt assembles the required implementations through dependency injection.

## Data Flow

### Product Catalogue

1. The product screen collects the paginated product stream from the ViewModel.
2. The ViewModel obtains the stream through the relevant use case.
3. The use case delegates to the repository contract.
4. The data repository coordinates the local database and remote data source.
5. Paging delivers product items incrementally.
6. Jetpack Compose renders the loaded products and corresponding loading or error states.

### Product Search

1. The user enters a search query.
2. The screen forwards the query to the ViewModel.
3. The ViewModel produces a search flow and applies query handling configured by the implementation.
4. The search use case delegates to the repository.
5. The repository returns the matching products through the configured paging strategy.
6. The UI displays the search results and relevant loading, empty, or error states.

### Product Details

1. The user selects a product from the catalogue.
2. Navigation passes the product ID to the detail destination.
3. The detail ViewModel retrieves the selected product through the domain layer.
4. The detail screen renders the available product information and calculated pricing.

## Pagination and Caching

### Paging 3

AndroidX Paging 3 is used to load products incrementally rather than loading the entire catalogue at once.

The Compose integration uses `collectAsLazyPagingItems()` to consume paginated data. The lazy grid renders items as they become available and can respond to Paging load states.

Stable product IDs are used as item keys to help lazy layouts identify products consistently.

### Room Database

Room provides local persistence for product data. Database-backed paging can help the application reuse stored data and coordinate updates without requiring the UI to manage page numbers manually.

### Cache Freshness

The project requirement specifies a cache freshness window of **30 minutes**.

The intended behavior is to reuse sufficiently fresh cached data and refresh stale data from the remote service. Successful refreshes should update the local database so subsequent reads can use the refreshed data.

The cache implementation should be covered by tests for:

- Reusing fresh cached data.
- Refreshing stale cached data.
- Updating stored products after a successful network response.
- Handling network failures when cached data is available.
- Avoiding duplicate products during paging and refresh operations.

## How to Run the Codebase

### Prerequisites

- Android Studio compatible with the project's Gradle and Android Gradle Plugin versions.
- A compatible JDK configured for Gradle.
- The Android SDK required by the project.
- An Android emulator or a physical Android device.
- Network access for remote product requests.

### Setup Instructions

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Allow Gradle to sync and download the required dependencies.
4. Configure any required API base URL or environment-specific settings using the existing project configuration.
5. Build the project.
6. Run the application on an emulator or a connected Android device.

### Build from the Command Line

Run the following command from the project root:

```bash
./gradlew build
```

To run local unit tests:

```bash
./gradlew test
```

If the Gradle wrapper is not executable on macOS or Linux, run:

```bash
chmod +x gradlew
./gradlew build
```

Use `gradlew.bat` on Windows.

## Unit Tests

The project uses JUnit, MockK, and Kotlin coroutine testing utilities to validate application behavior.

### Data Layer Tests

Tests cover repository and data-source behavior, including:

- Mapping remote response models into domain models.
- Verifying pagination parameters.
- Validating product fields such as price, discount, reviews, dimensions, and images.
- Handling API responses and errors.
- Testing networking behavior with MockWebServer where applicable.

### Domain Layer Tests

Tests validate business logic independently of Android UI and network implementation.

Examples include:

- Retrieving products through the repository contract.
- Searching products using the expected query.
- Retrieving an individual product.
- Verifying discounted price calculations.

### ViewModel Tests

Tests validate presentation logic, including:

- Initial UI state.
- Successful data handling.
- Error and retry behavior.
- Search query handling.
- Correct invocation of use cases.
- Paging flow behavior where applicable.

### Running Module-Specific Tests

The exact Gradle task depends on the module names in the repository.

For example, if the Android application module is named `shop`, run:

```bash
./gradlew :shop:testDebugUnitTest
```

To run connected Android tests, if the module has an instrumentation test source set:

```bash
./gradlew :shop:connectedDebugAndroidTest
```

## Modules and Package Structure

The project separates code by architectural responsibility. The exact Gradle module names and package paths should follow the current repository.

A conceptual structure is shown below:

```text
ShoppingProductApp/
├── shop/
│   └── src/
│       ├── main/
│       │   ├── java/com/shopping/demo/
│       │   │   └── products/presentation/
│       │   │       ├── model/
│       │   │       ├── screen/
│       │   │       └── ...
│       │   └── res/
│       ├── test/
│       └── androidTest/
├── data/
│   └── src/main/
│       └── .../products/data/
│           ├── datasource/
│           ├── model/
│           ├── repository/
│           └── ...
├── domain/
│   └── src/main/
│       └── .../products/domain/
│           ├── model/
│           ├── repository/
│           └── usecase/
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

The tree is illustrative; it is not a guarantee that each directory or Gradle module exists under exactly this name.

### Responsibilities

- **Application / Presentation:** Android entry point, Compose screens, navigation, ViewModels, and UI models.
- **Domain:** Product models, repository interfaces, and use cases.
- **Data:** API integration, response mapping, local persistence, and repository implementations.
- **Gradle version catalog:** Centralized dependency and plugin versions.

## Dependency Management

Dependency versions are centralized in `gradle/libs.versions.toml`.

This keeps library versions and dependency aliases in one place and makes dependency maintenance easier across build files.

When removing dependencies:

1. Search all Gradle build files for references to the alias.
2. Check whether the library is required by application code, tests, plugins, or code generation.
3. Remove the dependency from the appropriate module.
4. Remove its version-catalog entry only when no build file references it.
5. Sync Gradle and run the relevant build and tests.

Dependencies required for testing or image loading should not be removed simply because they are not directly imported in a production Kotlin file.

## Code Flow

The main application flow follows the separation between presentation, domain, and data:

```text
Compose Screen
      |
      v
   ViewModel
      |
      v
    Use Case
      |
      v
  Repository Contract
      |
      v
Repository Implementation
      |
      +------> Remote API
      |
      +------> Room Database
      |
      v
  Product Domain Models
      |
      v
 Presentation UI Models
      |
      v
  Compose UI
```

Paging and Flow allow data to be delivered incrementally and observed by the UI. Hilt provides the dependencies required by each layer.

## Screenshots

Add screenshots of the current application here as the UI is finalized.

Suggested sections:

### Light Mode

Add product catalogue and product detail screenshots.

### Dark Mode

Add product catalogue and product detail screenshots.

## Future Improvements

Potential improvements include:

- Additional tests for cache expiration and refresh behavior.
- UI tests for catalogue, search, and product details.
- Improved offline handling when remote refreshes fail.
- Accessibility descriptions for product images and interactive controls.
- Continuous integration for build verification and automated tests.
- Documentation of exact API endpoints and build variants.

## License

Designed and developed by George Pandian

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.