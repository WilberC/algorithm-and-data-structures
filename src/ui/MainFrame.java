package ui;

import model.Donation;
import service.DonationService;
import structures.MyArrayList;
import structures.MyHashMap;
import util.DonationMetadata;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class MainFrame extends JFrame {

    private DonationService donationService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> searchFieldCombo;
    private JComboBox<String> searchInput;
    private JLabel statusLabel;
    private MyArrayList<String> currentSearchOptions;

    // Dynamic Filters
    // Map<CategoryKey, List<CheckBoxes>>
    private MyHashMap<String, MyArrayList<JCheckBox>> filterCheckboxes;

    public MainFrame() {
        this.donationService = new DonationService();
        this.filterCheckboxes = new MyHashMap<>();

        setTitle("Donation Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Top Bar ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(new EmptyBorder(10, 20, 10, 20));
        topBar.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Donation Search");
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

        // --- Center Content ---
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Criteria"));

        searchFieldCombo = new JComboBox<>(new String[]{"Nombre Item", "Proveedor", "Nro Orden", "Almacen"});
        searchFieldCombo.addActionListener(e -> updateSearchSuggestions());

        searchInput = new JComboBox<>();
        searchInput.setEditable(true);
        searchInput.setPreferredSize(new Dimension(200, 25));

        // Setup AutoComplete listener
        Component editor = searchInput.getEditor().getEditorComponent();
        if (editor instanceof JTextField) {
            JTextField tf = (JTextField) editor;
            tf.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent e) {
                     // Invoke later to let the text update first
                     SwingUtilities.invokeLater(() -> {
                         String text = tf.getText();
                         filterSearchSuggestions(text);
                     });
                }
            });
        }

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(this::onSearch);

        JButton sortBtn = new JButton("Sort by Selected Field");
        sortBtn.addActionListener(this::onSort);

        searchPanel.add(new JLabel("Search By:"));
        searchPanel.add(searchFieldCombo);
        searchPanel.add(new JLabel("Value:"));
        searchPanel.add(searchInput);
        searchPanel.add(searchBtn);
        searchPanel.add(sortBtn);

        // Filter Panel
        JPanel filterContainer = new JPanel(new BorderLayout());
        filterContainer.setBorder(BorderFactory.createTitledBorder("Filters"));
        filterContainer.setPreferredSize(new Dimension(250, 0));

        JPanel filterContent = new JPanel();
        filterContent.setLayout(new BoxLayout(filterContent, BoxLayout.Y_AXIS));

        // Add dynamic filters
        addFilterCategory(filterContent, "Tipo Gobierno", DonationMetadata.TIPO_GOBIERNO, "TIPO_GOBIERNO");
        addFilterCategory(filterContent, "Mes Movimiento", DonationMetadata.MES_MOVIMTO, "MES_MOVIMTO");
        addFilterCategory(filterContent, "Tipo Uso", DonationMetadata.TIPO_USO, "TIPO_USO");


        JScrollPane filterScroll = new JScrollPane(filterContent);
        filterScroll.setBorder(null);
        filterContainer.add(filterScroll, BorderLayout.CENTER);

        // Table
        // Table
        String[] columns = {
            "Año", "Tipo Gob", "Sector", "Pliego", "Sec Ejec", "Ejecutora",
            "Almacen", "Sec Almacen", "Nom Almacen", "Tipo Transac", "Nro Mov",
            "Nro Orden", "Proveedor", "Nom Proveedor", "Observacion", "Fecha Mov",
            "Mes Mov", "Tipo Uso", "Nro Guia", "Nro Factura", "Est Kardex",
            "Fecha Reg", "Doc Confirma", "Fecha Confirma", "Glosa", "Tipo Bien",
            "Grupo Bien", "Nom Grupo", "Clase Bien", "Nom Clase", "Familia Bien",
            "Nom Familia", "Item Bien", "Nom Item", "Unidad", "Nom Unidad",
            "Marca", "Nom Marca", "Cantidad", "Precio Unit", "Total"
        };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set column widths
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(150);
        }

        JScrollPane tableScroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tableScroll, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(filterContainer, BorderLayout.WEST);

        // Export Panel (Bottom)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportPdf = new JButton("Export PDF");
        exportPdf.addActionListener(this::onExportPdf);
        JButton exportCsv = new JButton("Export CSV");
        exportCsv.addActionListener(this::onExportCsv);
        bottomPanel.add(exportPdf);
        bottomPanel.add(exportCsv);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addFilterCategory(JPanel panel, String title, MyHashMap<String, String> data, String key) {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setBorder(new EmptyBorder(10, 5, 5, 5));
        categoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoryPanel.add(lblTitle);
        categoryPanel.add(Box.createVerticalStrut(5));

        MyArrayList<JCheckBox> checks = new MyArrayList<>();
        MyArrayList<String> keys = data.getKeys();

        // Sort keys for better UX (e.g. 01, 02, 03...)
        keys.quickSort((s1, s2) -> s1.compareTo(s2));

        for (String k : keys) {
            String desc = data.get(k);
            // Truncate long descriptions
            String label = desc;
            if (label.length() > 25) {
                label = label.substring(0, 22) + "...";
            }
            JCheckBox chk = new JCheckBox(label);
            chk.setToolTipText(desc + " (" + k + ")");
            chk.setActionCommand(k); // Store the code
            chk.addActionListener(e -> applyFilters());
            categoryPanel.add(chk);
            checks.add(chk);
        }

        panel.add(categoryPanel);
        filterCheckboxes.put(key, checks);
    }

    private void onImport(ActionEvent e) {
        String path = "data/pcm_donaciones/pcm_donaciones.csv";
        int count = donationService.loadData(path);
        if (count >= 0) {
            statusLabel.setText("Records: " + count);
            JOptionPane.showMessageDialog(this, "Imported " + count + " records.", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateTable(donationService.getAll());
            updateSearchSuggestions();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to import data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSearch(ActionEvent e) {
        String field = (String) searchFieldCombo.getSelectedItem();
        String value = (String) searchInput.getEditor().getItem();
        if (value == null) value = "";
        MyArrayList<Donation> results = donationService.search(field, value);
        updateTable(results);
    }

    private void updateSearchSuggestions() {
        String field = (String) searchFieldCombo.getSelectedItem();
        currentSearchOptions = donationService.getUniqueValues(field);
        // Initially empty or full? Let's keep it empty until typed or show all if small
        filterSearchSuggestions("");
    }

    private void filterSearchSuggestions(String text) {
        if (currentSearchOptions == null) return;

        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) searchInput.getModel();
        model.removeAllElements();

        // Add current text to avoid it being lost if it's not in the list yet
        // But we want to show suggestions.
        // Logic: if text is empty, maybe show nothing or top 10?

        int count = 0;
        String lower = text.toLowerCase();

        for (String opt : currentSearchOptions) {
            if (opt.toLowerCase().contains(lower)) {
                model.addElement(opt);
                count++;
                if (count > 50) break; // Limit to 50 suggestions
            }
        }

        JTextField tf = (JTextField) searchInput.getEditor().getEditorComponent();
        String currentText = tf.getText();

        if (!currentText.equals(text)) {
            tf.setText(text);
        }

        if (count > 0 && !text.isEmpty() && searchInput.isShowing()) {
            searchInput.showPopup();
        } else {
             searchInput.hidePopup();
        }
    }

    private void onSort(ActionEvent e) {
        String field = (String) searchFieldCombo.getSelectedItem();
        donationService.sort(field, true);
        applyFilters(); // Re-apply filters to show sorted list correctly
    }

    private void applyFilters() {
        MyArrayList<Donation> all = donationService.getAll();
        MyArrayList<Donation> filtered = new MyArrayList<>();

        for (Donation d : all) {
            if (matches(d)) {
                filtered.add(d);
            }
        }
        updateTable(filtered);
    }

    private boolean matches(Donation d) {
        if (!checkCategory(d.getTipoGobierno(), "TIPO_GOBIERNO")) return false;
        if (!checkCategory(d.getMesMovimto(), "MES_MOVIMTO")) return false;
        if (!checkCategory(d.getTipoUso(), "TIPO_USO")) return false;

        return true;
    }

    private boolean checkCategory(String value, String categoryKey) {
        MyArrayList<JCheckBox> checks = filterCheckboxes.get(categoryKey);
        if (checks == null) return true;

        boolean anySelected = false;
        boolean match = false;

        for (JCheckBox chk : checks) {
            if (chk.isSelected()) {
                anySelected = true;
                if (chk.getActionCommand().equals(value)) {
                    match = true;
                }
            }
        }
        // If no checkbox is selected in this category, we ignore the filter (return true).
        // If at least one is selected, the value MUST match one of the selected (OR logic).
        return !anySelected || match;
    }

    private void updateTable(MyArrayList<Donation> list) {
        tableModel.setRowCount(0);
        for (Donation d : list) {
            Vector<Object> row = new Vector<>();
            row.add(d.getAnoEje());
            row.add(d.getTipoGobierno());
            row.add(d.getSector());
            row.add(d.getPliego());
            row.add(d.getSecEjec());
            row.add(d.getEjecutora());
            row.add(d.getAlmacen());
            row.add(d.getSecAlmacen());
            row.add(d.getNombreAlmacen());
            row.add(d.getTipoTransac());
            row.add(d.getNroMovimto());
            row.add(d.getNroOrden());
            row.add(d.getProveedor());
            row.add(d.getNombreProveedor());
            row.add(d.getObservacion());
            row.add(d.getFechaMovimto());
            row.add(d.getMesMovimto());
            row.add(d.getTipoUso());
            row.add(d.getNroGuia());
            row.add(d.getNroFactura());
            row.add(d.getEstadoKardex());
            row.add(d.getFechaReg());
            row.add(d.getDocumConfirma());
            row.add(d.getFechaConfirma());
            row.add(d.getGlosa());
            row.add(d.getTipoBien());
            row.add(d.getGrupoBien());
            row.add(d.getNombreGrupo());
            row.add(d.getClaseBien());
            row.add(d.getNombreClase());
            row.add(d.getFamiliaBien());
            row.add(d.getNombreFamilia());
            row.add(d.getItemBien());
            row.add(d.getNombreItem());
            row.add(d.getUnidadMedida());
            row.add(d.getNombreUmedida());
            row.add(d.getMarca());
            row.add(d.getNombreMarca());
            row.add(d.getCantArticulo());
            row.add(d.getPrecioUnit());
            row.add(d.getValorTotal());
            tableModel.addRow(row);
        }
    }

    private void onExportCsv(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".csv")) {
                path += ".csv";
            }

            try (java.io.PrintWriter pw = new java.io.PrintWriter(path)) {
                StringBuilder header = new StringBuilder();
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    header.append(tableModel.getColumnName(j));
                    if (j < tableModel.getColumnCount() - 1) header.append(",");
                }
                pw.println(header.toString());
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object val = tableModel.getValueAt(i, j);
                        sb.append("\"").append(val != null ? val.toString() : "").append("\"");
                        if (j < tableModel.getColumnCount() - 1) sb.append(",");
                    }
                    pw.println(sb.toString());
                }
                JOptionPane.showMessageDialog(this, "Exported successfully to " + path, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error exporting CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onExportPdf(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "PDF Export requires external libraries (e.g., iText) which are not included.\nCSV Export is available.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
