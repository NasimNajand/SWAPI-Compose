An Android app built with Kotlin that leverages Jetpack Compose for UI, Hilt for dependency injection, and Retrofit with Moshi for network operations. The app interfaces with the Star Wars API (SWAPI) to fetch and display data.

## Features

- **Search Functionality**: Users can search for characters from the Star Wars universe.
- **Paginated List**: A paginated list displays search results to manage large datasets efficiently.
- **Detail View**: Shows detailed information about a selected character, including additional API calls for fetching related data.
- **Asynchronous Data Handling**: Handles API calls asynchronously to ensure a smooth user experience.

## Architecture

- **Domain Layer**: Contains business logic and domain models.
- **Data Layer**: Manages data operations and integrates with SWAPI using Retrofit and Moshi for network operations and JSON parsing.
- **Presentation Layer**: Uses Jetpack Compose for the UI and handles user interactions.

## Libraries Used

- **Jetpack Compose**: For building the user interface.
- **Hilt**: For dependency injection.
- **Retrofit**: For making network requests.
- **Moshi**: For JSON parsing.
- **Paging**: For handling paginated data.

## Screens

### Search Screen

- Displays a list of characters with pagination.
- Users can search for characters by name.

### Detail Screen

- Displays detailed information about a selected character.
- Fetches additional data such as species, homeworld, and films asynchronously.
