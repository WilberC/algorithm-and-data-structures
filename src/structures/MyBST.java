package structures;

public class MyBST<K extends Comparable<K>, V> {

    private class Node {
        K key;
        V value;
        Node left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;

    public void insert(K key, V value) {
        root = insertRec(root, key, value);
    }

    private Node insertRec(Node root, K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            return root;
        }

        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            root.left = insertRec(root.left, key, value);
        } else if (cmp > 0) {
            root.right = insertRec(root.right, key, value);
        } else {
            // Update value if key exists (or handle duplicates as needed, for now update)
            // But for statistics "Count", I might usually do get(key), increment, insert.
            // So let's provide a get method.
            root.value = value;
        }
        return root;
    }

    public V get(K key) {
        return getRec(root, key);
    }

    private V getRec(Node root, K key) {
        if (root == null) return null;
        int cmp = key.compareTo(root.key);
        if (cmp < 0) return getRec(root.left, key);
        else if (cmp > 0) return getRec(root.right, key);
        else return root.value;
    }

    // In-order traversal to get sorted list
    public void inOrder(MyLinkedList<Entry<K, V>> list) {
        inOrderRec(root, list);
    }

    private void inOrderRec(Node root, MyLinkedList<Entry<K, V>> list) {
        if (root != null) {
            inOrderRec(root.left, list);
            list.add(new Entry<>(root.key, root.value));
            inOrderRec(root.right, list);
        }
    }

    public static class Entry<K, V> {
        public K key;
        public V value;
        public Entry(K k, V v) { key = k; value = v; }
    }
}
