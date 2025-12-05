# Algorithm and Data Structures Project

The data has been imported from a CSV file.
The source of the [data](https://www.datosabiertos.gob.pe/dataset/donaciones-covid-19-ministerio-de-econom%C3%ADa-y-finanzas-mef) is from the Plataforma nacional de datos abiertos of Peru.

## Prerequisites

- Java Development Kit (JDK) 8 or higher.

## Project Structure

- `src/`: Source code files.
  - `structures/`: Custom data structure implementations (e.g., MyHashMap).
- `data/`: Data files (e.g., `users.csv`).
- `out/`: Compiled class files (created after compilation).

## Technical Constraints

- **No Java Collections**: This project does not use Java Collection Libraries (ArrayList, List, Map, Set, etc.). Instead, custom data structures are implemented to demonstrate understanding of algorithms and data structures. For example, `MyHashMap` is used instead of `java.util.HashMap`.

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
