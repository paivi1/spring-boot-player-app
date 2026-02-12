# Spring Boot Game Timer Application

A Spring Boot application with a timer UI for tracking player turn times in games. Each player has individual start/stop controls to measure how long their turn takes.

## Prerequisites

- Java 11 or higher (You have Java 13 installed)
- Maven is NOT required - the Maven Wrapper is included

## Project Structure

```
spring-boot-app/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java       # Main Spring Boot application
│   │   │   └── HelloController.java       # Sample REST controller
│   │   └── resources/
│   │       └── application.properties     # Application configuration
│   └── test/
│       └── java/com/example/demo/
│           └── DemoApplicationTests.java  # Test class
├── pom.xml                                # Maven configuration
├── mvnw.cmd                               # Maven Wrapper for Windows
└── mvnw                                   # Maven Wrapper for Unix/Mac
```

## Running the Application

### Using Maven Wrapper (Recommended)

```powershell
cd spring-boot-app
.\mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`

### Using the Application

- Open your browser and visit: `http://localhost:8080/`
- You'll see the Game Timer UI with a '+' button
- Click '+' to add new players
- Use Start/Stop buttons to track each player's turn time
- Delete players using the '×' button
- H2 Database Console: `http://localhost:8080/h2-console`

### Building the Project

```powershell
.\mvnw.cmd clean install
```

### Running Tests

```powershell
.\mvnw.cmd test
```

## Features

- **Timer UI**: Beautiful web interface for tracking player turn times
- **Multiple Players**: Add unlimited players with individual timers
- **Real-time Updates**: Timers update every 100ms while running
- **Persistent Storage**: Player data saved in H2 database
- **REST API**: Backend API for player/timer operations
- **Spring Boot + JPA**: Modern Java stack
- **Maven Wrapper**: No need to install Maven separately

## Available API Endpoints

- `GET /` - Game Timer UI (main page)
- `POST /api/players` - Create a new player
- `GET /api/players` - Get all players
- `POST /api/players/{id}/start` - Start a player's timer
- `POST /api/players/{id}/stop` - Stop a player's timer
- `DELETE /api/players/{id}` - Delete a player

## H2 Database Console

Access the H2 console at `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

## Next Steps

1. Add more REST controllers in `src/main/java/com/example/demo/`
2. Create JPA entities and repositories for database operations
3. Customize `application.properties` for your needs
4. Add more dependencies in `pom.xml` as needed
