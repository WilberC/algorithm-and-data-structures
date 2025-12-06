package structures;

public class MyStack<T> {
    private MyLinkedList<T> list;

    public MyStack() {
        this.list = new MyLinkedList<>();
    }

    // Push adds to the front (head) for O(1) in singly linked list?
    // Wait, my MyLinkedList adds to the end (tail).
    // For stack, push/pop at same end.
    // If I use tail, I can't pop efficiently (O(n) to find new tail).
    // So I should implement push as "addFirst".
    // But MyLinkedList only has "add" (addLast).

    // Let's modify MyLinkedList or just implement Stack logic here directly using Nodes or let's use composition but add "addFirst" to LinkedList if needed.
    // Actually simpler: Implement Stack using a Node class directly or specific stack logic.
    // But user asked for "Pila Dinámica: Implementación mediante listas enlazadas."

    private class Node {
        T data;
        Node next;
        Node(T data) { this.data = data; }
    }

    private Node top;
    private int size;

    public void push(T data) {
        Node newNode = new Node(data);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) return null;
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) return null;
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }
}
