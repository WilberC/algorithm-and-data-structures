# Algorithm and Data Structures Project

This project focuses on the implementation and analysis of fundamental data structures and algorithms from scratch, without relying on the standard Java Collections Framework. The goal is to demonstrate a deep understanding of how these structures work under the hood, their time complexities, and appropriate use cases for academic purposes.

The source of the [data](https://www.datosabiertos.gob.pe/dataset/donaciones-covid-19-ministerio-de-econom%C3%ADa-y-finanzas-mef) is from the Plataforma nacional de datos abiertos of Peru.

## Academic Constraints & Technical Objectives

- **No Java Collections**: Usage of `java.util.List`, `java.util.ArrayList`, `java.util.Map`, `java.util.Set` etc., is **strictly prohibited**.
- **Custom Implementations**: All data storage and manipulation are handled by custom-built data structures (`src/structures/`).
- **Algorithmic Analysis**: The project emphasizes explaining _why_ specific structures and algorithms were chosen based on their theoretical performance (Big O).

## Data Structured & Algorithms Analysis

The following custom structures have been implemented to replace standard libraries:

### 1. Dynamic Array (`MyArrayList`)

- **Specification**: An array-based list implementation that manages dynamic resizing. It uses a generic `Object[]` array. When the array reaches capacity, a new array of size `current_capacity * 2` is allocated, and elements are copied over (Growth Policy).
- **complexity**:
  - **Access (`get`)**: **O(1)** - Constant time access via index.
  - **Insertion (`add`)**: **Amortized O(1)** - Inserting is fast, except when resizing is triggered (O(n)).
- **Why used**: Arrays provide the best cache locality and simplest random access. For datasets where we primarily read data by index after loading, this is optimal.

### 2. Hash Map (`MyHashMap`)

- **Specification**: A dictionary implementation using **Separate Chaining** for collision resolution. It uses an internal array of pointers to linked list nodes (`Entry<K,V>`).
- **Complexity**:
  - **Search/Insert**: **O(1) Average** - Assuming a good distribution of hash codes.
  - **Worst Case**: **O(n)** - If all keys hash to the same bucket (linked list degradation).
- **Why used**: We need to associate unique keys (like IDs) with values effortlessly. Implementing this manually demonstrates understanding of hash functions (`hashCode() % length`) and dealing with collisions, which is core to Computer Science theory.

### 3. Binary Search Tree (`MyBST`)

- **Specification**: A recursive tree data structure where for any node, the left child's key is smaller and the right child's key is larger.
- **Complexity**:
  - **Search/Insert**: **O(log n)** (Average) - logarithmic time complexity makes it much faster than linear search O(n).
- **Why used**: Used for efficient searching and maintaining a sorted order of elements. It allows us to perform `In-Order Traversal` to retrieve data sorted by keys without an explicit sort step after insertion.

### 4. Singly Linked List (`MyLinkedList`)

- **Specification**: A linear collection of data elements where each element points to the next.
- **Why used**: Serves as the fundamental building block for other structures like Stacks, Queues, and the buckets in our HashMap. It allows O(1) insertion at the tip without memory reallocation overhead.

### 5. Stack (`MyStack`) & Queue (`MyQueue`)

- **Specification**:
  - **Stack**: LIFO (Last-In, First-Out) using Linked List nodes.
  - **Queue**: FIFO (First-In, First-Out) using `front` and `rear` pointers.
- **Why used**: Essential for specific algorithmic processing orders. For example, Queues are used for breadth-first processing and Stacks for depth-first or backtracking operations.

## Sorting Algorithms

We implemented sorting directly within `MyArrayList` to demonstrate the difference between naive and efficient approaches:

1.  **QuickSort (O(n log n))**:

    - **Why**: The primary sorting algorithm. It follows the **Divide and Conquer** paradigm. It partitions the array around a pivot and recursively sorts sub-arrays. It is significantly faster than O(n²) sorts for large datasets like our CSV imports.

2.  **BubbleSort (O(n²))**:
    - **Why**: Included for **academic comparison**. It demonstrates how a naive approach (swapping adjacent elements) becomes inefficient as dataset size grows, highlighting the importance of algorithmic choice.

---

## Project Structure

- `src/`: Source code files.
  - `structures/`: **Custom Data Structures** (The core academic component).
  - `util/`: Helpers and Parsing logic.
- `data/`: Data files (e.g., `users.csv`).
- `out/`: Compiled class files.

## How to Compile

1.  Create the output directory:
    ```sh
    mkdir -p out
    ```
2.  Compile the source code:
    ```sh
    javac -d out -sourcepath src src/Main.java
    ```

## How to Run

After compiling, run the application:

```sh
java -cp out Main
```
