package structures;

public class MyHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 128;
    private Entry<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    public void put(K key, V value) {
        if (key == null) return;
        int index = Math.abs(key.hashCode()) % table.length;
        Entry<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;
    }

    public V get(K key) {
        if (key == null) return null;
        int index = Math.abs(key.hashCode()) % table.length;
        Entry<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        table = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    public int size() {
        return size;
    }

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
