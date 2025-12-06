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

- **Specification**: An array-based list implementation that manages dynamic resizing. It uses a generic `Object[]` array.
- **How it works**:
  - **Internal Storage**: Maintains a standard fixed-size Java array.
  - **Resizing Logic**: When `add()` is called and the array is full, the list creates a new array with **double the capacity** of the old one. It then copies all existing elements to the new array before adding the new element. This geometric expansion ensures that the amortized cost of insertion remains low.
- **Complexity**:
  - **Access (`get`)**: **O(1)** - Constant time access via index.
  - **Insertion (`add`)**: **Amortized O(1)** - Inserting is fast, except when resizing is triggered (O(n)).
- **Why used**: Arrays provide the best cache locality and simplest random access. For datasets where we primarily read data by index after loading, this is optimal.

### 2. Hash Map (`MyHashMap`)

- **Specification**: A dictionary implementation using **Separate Chaining** for collision resolution.
- **How it works**:
  - **Hashing**: Converts a key into an array index using a hash function: `index = Math.abs(key.hashCode()) % table.length`.
  - **Collision Handling**: Each slot (bucket) in the array points to the head of a Linked List. If two keys hash to the same index (collision), the new entry is appended to that bucket's linked list.
  - **Retrieval**: To find a value, it computes the hash index and linearly searches the linked list at that specific bucket.
- **Complexity**:
  - **Search/Insert**: **O(1) Average** - Assuming a good distribution of hash codes.
  - **Worst Case**: **O(n)** - If all keys hash to the same bucket (linked list degradation).
- **Why used**: We need to associate unique keys (like IDs) with values effortlessly. Implementing this manually demonstrates understanding of hash functions and dealing with collisions, which is core to Computer Science theory.

### 3. Binary Search Tree (`MyBST`)

- **Specification**: A recursive tree data structure where for any node, the left child's key is smaller and the right child's key is larger.
- **How it works**:
  - **Insertion**: Starts at the root. If the new key is smaller than the current node, it recursively attempts to insert into the left subtree. If larger, it goes specifically to the right. This continues until a null position is found for the new node.
  - **Traversal**: We use **In-Order Traversal** (Left -> Root -> Right) to visit nodes. This mathematically guarantees that we visit the keys in sorted ascending order.
- **Complexity**:
  - **Search/Insert**: **O(log n)** (Average) - logarithmic time complexity makes it much faster than linear search O(n).
- **Why used**: Used for efficient searching and maintaining a sorted order of elements.

### 4. Singly Linked List (`MyLinkedList`)

- **Specification**: A linear collection of data elements where each element points to the next.
- **How it works**:
  - **Nodes**: Composed of `Node` objects, where each holds `data` and a `next` reference.
  - **Chaining**: The list holds a reference to the `head` (first node). Traversing involves following `next` pointers until `null` is reached.
- **Why used**: Serves as the fundamental building block for other structures like Stacks, Queues, and the buckets in our HashMap. It allows O(1) insertion at the tip without memory reallocation overhead.

### 5. Stack (`MyStack`) & Queue (`MyQueue`)

- **Specification**: Abstract Data Types (ADTs) that dictate the order of processing.
- **How it works**:
  - **Stack (LIFO)**: Implemented via a Linked List. `push()` adds a new node to the _head_ of the list, and `pop()` removes from the _head_. Everything happens at one end.
  - **Queue (FIFO)**: Maintains two pointers, `front` and `rear`. `enqueue()` adds to the `rear`, while `dequeue()` removes from the `front`.
- **Why used**: Essential for specific algorithmic processing orders. For example, Queues are used for breadth-first processing and Stacks for depth-first or backtracking operations.

## Sorting Algorithms

We implemented sorting directly within `MyArrayList` to demonstrate the difference between naive and efficient approaches:

1.  **QuickSort (O(n log n))**:

    - **Mechanism**: The algorithm selects a "pivot" element (often the last element). It then **partitions** the array into two halves: elements smaller than the pivot are moved to the left, and elements larger are moved to the right. This process is recursively applied to the sub-arrays.
    - **Why**: The primary sorting algorithm. It follows the **Divide and Conquer** paradigm. It partitions the array around a pivot and recursively sorts sub-arrays. It is significantly faster than O(n²) sorts for large datasets like our CSV imports.

2.  **BubbleSort (O(n²))**:
    - **Mechanism**: Repeats steps through the list, compares adjacent elements and swaps them if they are in the wrong order. The pass through the list is repeated until the list is sorted.
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
