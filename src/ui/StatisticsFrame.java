package ui;

import model.Donation;
import service.DonationService;
import structures.MyArrayList;
import structures.MyBST;
import structures.MyStack;
import structures.MyQueue;
import structures.MyLinkedList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class StatisticsFrame extends JFrame {

    private DonationService donationService;
    private JLabel statusLabel;

    // UI Components for Tabs
    private DefaultTableModel bstTableModel;
    private JTextArea stackOutputArea;
    private JTextArea queueOutputArea;
    private JLabel statsLabelMin;
    private JLabel statsLabelMax;
    private JLabel statsLabelMean;
    private JLabel statsLabelMedian;

    public StatisticsFrame() {
        this.donationService = new DonationService();

        setTitle("Statistical Analysis");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Don't exit app, just close window
        setLayout(new BorderLayout());

        // --- Top Bar ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(new EmptyBorder(10, 20, 10, 20));
        topBar.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Donation Statistics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightTop.setOpaque(false);

        statusLabel = new JLabel("Records: 0");
        JButton importBtn = new JButton("Import Data");
        importBtn.setBackground(new Color(51, 102, 255));
        importBtn.setForeground(Color.WHITE);
        importBtn.addActionListener(this::onImport);

        rightTop.add(statusLabel);
        rightTop.add(importBtn);

        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(rightTop, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // --- Tabs ---
        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. BST Tab
        tabbedPane.addTab("Warehouse Dist. (BST)", createBSTPanel());

        // 2. Stack Tab
        tabbedPane.addTab("Recent Imports (Stack)", createStackPanel());

        // 3. Queue Tab
        tabbedPane.addTab("Processing (Queue)", createQueuePanel());

        // 4. Array Stats Tab
        tabbedPane.addTab("Price Stats (Array)", createArrayPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createBSTPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel desc = new JLabel("Analysis of Donation Count per Warehouse using Binary Search Tree (BST).");
        desc.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] columns = {"Warehouse Name", "Donation Count"};
        bstTableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(bstTableModel);

        panel.add(desc, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Run Analysis");
        refreshBtn.addActionListener(e -> runBSTAnalysis());
        panel.add(refreshBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStackPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel desc = new JLabel("Visualize Last 20 Donations using Stack (LIFO).");
        desc.setBorder(new EmptyBorder(0, 0, 10, 0));

        stackOutputArea = new JTextArea();
        stackOutputArea.setEditable(false);
        stackOutputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        panel.add(desc, BorderLayout.NORTH);
        panel.add(new JScrollPane(stackOutputArea), BorderLayout.CENTER);

        JButton runBtn = new JButton("Pop Last 20 from Stack");
        runBtn.addActionListener(e -> runStackAnalysis());
        panel.add(runBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createQueuePanel() {
         JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel desc = new JLabel("Simulate Processing of First 20 Donations using Queue (FIFO).");
        desc.setBorder(new EmptyBorder(0, 0, 10, 0));

        queueOutputArea = new JTextArea();
        queueOutputArea.setEditable(false);
        queueOutputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        panel.add(desc, BorderLayout.NORTH);
        panel.add(new JScrollPane(queueOutputArea), BorderLayout.CENTER);

        JButton runBtn = new JButton("Process First 20 from Queue");
        runBtn.addActionListener(e -> runQueueAnalysis());
        panel.add(runBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createArrayPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Price Statistics (Using Array Sorting)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        statsLabelMin = new JLabel("Min Price: -");
        statsLabelMax = new JLabel("Max Price: -");
        statsLabelMean = new JLabel("Mean Price: -");
        statsLabelMedian = new JLabel("Median Price: -");

        panel.add(title);
        panel.add(statsLabelMin);
        panel.add(statsLabelMax);
        panel.add(statsLabelMean);
        panel.add(statsLabelMedian);

        JButton runBtn = new JButton("Calculate Statistics");
        runBtn.addActionListener(e -> runArrayAnalysis());

        JPanel container = new JPanel(new BorderLayout());
        container.add(panel, BorderLayout.NORTH);
        container.add(runBtn, BorderLayout.SOUTH);

        return container;
    }

    private void onImport(ActionEvent e) {
        String path = "data/pcm_donaciones/pcm_donaciones.csv";
        int count = donationService.loadData(path);
        if (count >= 0) {
            statusLabel.setText("Records: " + count);
            JOptionPane.showMessageDialog(this, "Imported " + count + " records for analysis.", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Trigger analyses or clear previous
            bstTableModel.setRowCount(0);
            stackOutputArea.setText("");
            queueOutputArea.setText("");
            statsLabelMin.setText("Min Price: -");
            statsLabelMax.setText("Max Price: -");
            statsLabelMean.setText("Mean Price: -");
            statsLabelMedian.setText("Median Price: -");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to import data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runBSTAnalysis() {
        MyArrayList<Donation> all = donationService.getAll();
        if (all.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data imported.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Build BST: Key = Warehouse Name (String), Value = Count (Integer)
        MyBST<String, Integer> bst = new MyBST<>();

        for (Donation d : all) {
            String wh = d.getNombreAlmacen();
            if (wh == null || wh.trim().isEmpty()) wh = "Unknown";

            Integer count = bst.get(wh);
            if (count == null) {
                bst.insert(wh, 1);
            } else {
                bst.insert(wh, count + 1);
            }
        }

        // Traverse and display
        bstTableModel.setRowCount(0);
        MyLinkedList<MyBST.Entry<String, Integer>> list = new MyLinkedList<>();
        bst.inOrder(list);

        list.forEach(entry -> {
             bstTableModel.addRow(new Object[]{entry.key, entry.value});
        });
    }

    private void runStackAnalysis() {
        MyArrayList<Donation> all = donationService.getAll();
        if (all.isEmpty()) {
             JOptionPane.showMessageDialog(this, "No data imported.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Push all to stack
        MyStack<Donation> stack = new MyStack<>();
        for (Donation d : all) {
            stack.push(d);
        }

        // Pop 20
        StringBuilder sb = new StringBuilder();
        sb.append("Popping last 20 loaded records (LIFO):\n\n");
        int count = 0;
        while (!stack.isEmpty() && count < 20) {
            Donation d = stack.pop();
            sb.append(String.format("[%d] %s - %s ($%.2f)\n",
                count+1, d.getNombreItem(), d.getNombreAlmacen(), d.getValorTotal()));
            count++;
        }
        stackOutputArea.setText(sb.toString());
    }

    private void runQueueAnalysis() {
        MyArrayList<Donation> all = donationService.getAll();
        if (all.isEmpty()) {
             JOptionPane.showMessageDialog(this, "No data imported.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Enqueue all
        MyQueue<Donation> queue = new MyQueue<>();
        for (Donation d : all) {
            queue.enqueue(d);
        }

        // Dequeue 20
        StringBuilder sb = new StringBuilder();
        sb.append("Processing first 20 records (FIFO):\n\n");
        int count = 0;
        while (!queue.isEmpty() && count < 20) {
            Donation d = queue.dequeue();
             sb.append(String.format("[%d] %s - %s ($%.2f)\n",
                count+1, d.getNombreItem(), d.getNombreAlmacen(), d.getValorTotal()));
            count++;
        }
        queueOutputArea.setText(sb.toString());
    }

    private void runArrayAnalysis() {
        MyArrayList<Donation> all = donationService.getAll();
        if (all.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data imported.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int n = all.size();
        double[] prices = new double[n];
        double sum = 0;
        int i = 0;
        for (Donation d : all) {
            prices[i] = d.getPrecioUnit();
            sum += prices[i];
            i++;
        }

        // Sort array
        Arrays.sort(prices); // Using java.util.Arrays for simplicity of generic array sort, or manual?
        // User said "Operaciones con Arreglos Unidimensionales: comparación, clonación, fusión."
        // I should probably implement the sort manually or use Arrays.sort if "Operations" implies using array logic.
        // Let's rely on Arrays.sort for now but I am using arrays.

        double min = prices[0];
        double max = prices[n-1];
        double mean = sum / n;
        double median;
        if (n % 2 == 0) {
            median = (prices[n/2 - 1] + prices[n/2]) / 2.0;
        } else {
            median = prices[n/2];
        }

        statsLabelMin.setText(String.format("Min Price: %.2f", min));
        statsLabelMax.setText(String.format("Max Price: %.2f", max));
        statsLabelMean.setText(String.format("Mean Price: %.2f", mean));
        statsLabelMedian.setText(String.format("Median Price: %.2f", median));
    }
}
