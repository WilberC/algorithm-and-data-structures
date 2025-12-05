package structures;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayList<T> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public MyArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void add(T element) {
        if (size == elements.length) {
            ensureCapacity();
        }
        elements[size++] = element;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) elements[index];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
        elements = new Object[DEFAULT_CAPACITY];
    }

    private void ensureCapacity() {
        int newCapacity = elements.length * 2;
        elements = Arrays.copyOf(elements, newCapacity);
    }

    public void sort(Comparator<? super T> c) {
        Arrays.sort((T[]) elements, 0, size, c);
    }

    // Simple bubble sort if Arrays.sort is also frowned upon, but usually Arrays.sort is fine as it's an algorithm, not a collection.
    // However, the user said "using the methods of Sorting and Searching studied in the course".
    // So I should probably implement a sort method myself.
    public void bubbleSort(Comparator<? super T> c) {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                T o1 = get(j);
                T o2 = get(j + 1);
                if (c.compare(o1, o2) > 0) {
                    elements[j] = o2;
                    elements[j + 1] = o1;
                }
            }
        }
    }

    // QuickSort implementation
    public void quickSort(Comparator<? super T> c) {
        quickSort(0, size - 1, c);
    }

    private void quickSort(int low, int high, Comparator<? super T> c) {
        if (low < high) {
            int pi = partition(low, high, c);
            quickSort(low, pi - 1, c);
            quickSort(pi + 1, high, c);
        }
    }

    private int partition(int low, int high, Comparator<? super T> c) {
        T pivot = get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (c.compare(get(j), pivot) <= 0) {
                i++;
                T temp = get(i);
                elements[i] = elements[j];
                elements[j] = temp;
            }
        }
        T temp = get(i + 1);
        elements[i + 1] = elements[high];
        elements[high] = temp;
        return i + 1;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return get(currentIndex++);
            }
        };
    }
}
