# Algorithm and Data Structures Project

This is a Java Swing application for user management.

## Prerequisites

- Java Development Kit (JDK) 8 or higher.

## Project Structure

- `src/`: Source code files.
- `data/`: Data files (e.g., `users.csv`).
- `out/`: Compiled class files (created after compilation).

## How to Compile

1. Open a terminal.
2. Navigate to the project root directory.
3. Create the output directory if it doesn't exist:
   ```sh
   mkdir -p out
   ```
4. Compile the source code:
   ```sh
   javac -d out -sourcepath src src/Main.java
   ```

## How to Run

After compiling, run the application using the following command:

```sh
java -cp out Main
```

## Usage

1. The application will launch a Login window.
2. Use the "Import Users" button to load users from a CSV file (e.g., `data/users.csv`).
3. Enter username and password to login.
