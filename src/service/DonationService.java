package service;

import model.Donation;
import structures.MyArrayList;
import structures.MyHashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

public class DonationService {

    private MyArrayList<Donation> donations;

    public DonationService() {
        this.donations = new MyArrayList<>();
    }

    public int loadData(String filePath) {
        donations.clear();
        int count = 0;
        try (BufferedReader br = new BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(filePath), java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                // Simple CSV split, assuming no commas in values for now or handling basic quotes
                // For a robust solution, a proper parser is needed, but this is a simple implementation.
                // We use semicolon or comma. Let's try to detect or just split by regex.
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                // If parts is 1, maybe it's semicolon separated
                if (parts.length <= 1) {
                     parts = line.split(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                }

                if (parts.length >= 47) { // We expect around 47 fields now
                    Donation d = new Donation();
                    d.setAnoEje(getPart(parts, 0));
                    // Index 1 is likely Department/Location, skipping for now as it's not in model
                    d.setTipoGobierno(getPart(parts, 2));
                    // Index 3 is likely TipoGobiernoNombre

                    d.setSector(getPart(parts, 4));
                    d.setPliego(getPart(parts, 6));
                    d.setEjecutora(getPart(parts, 9));
                    d.setNombreAlmacen(getPart(parts, 13));
                    d.setNroOrden(getPart(parts, 17));
                    d.setNombreProveedor(getPart(parts, 19));
                    d.setFechaMovimto(getPart(parts, 21));
                    d.setMesMovimto(getPart(parts, 22));
                    d.setTipoUso(getPart(parts, 24));
                    d.setTipoBien(getPart(parts, 32));
                    d.setNombreItem(getPart(parts, 40));
                    d.setNombreMarca(getPart(parts, 43));

                    try {
                        d.setCantArticulo(Double.parseDouble(getPart(parts, 44).replace("\"", "")));
                        d.setPrecioUnit(Double.parseDouble(getPart(parts, 45).replace("\"", "")));
                        d.setValorTotal(Double.parseDouble(getPart(parts, 46).replace("\"", "")));
                    } catch (Exception e) {
                        // Ignore parse errors
                    }

                    donations.add(d);
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    private String getPart(String[] parts, int index) {
        if (index >= parts.length) return "";
        return parts[index].replace("\"", "").trim();
    }

    public MyArrayList<Donation> getAll() {
        return donations;
    }

    public MyArrayList<Donation> search(String field, String value) {
        MyArrayList<Donation> result = new MyArrayList<>();
        String lowerValue = value.toLowerCase();

        for (Donation d : donations) {
            String fieldValue = getFieldValue(d, field);
            if (fieldValue != null && fieldValue.toLowerCase().contains(lowerValue)) {
                result.add(d);
            }
        }
        return result;
    }

    // Sort using MyArrayList's sort (which uses Arrays.sort or we can use our bubble/quick sort)
    // The user wants "Sorting methods studied". I'll use the quickSort I implemented in MyArrayList.
    public void sort(String field, boolean ascending) {
        donations.quickSort((d1, d2) -> {
            String v1 = getFieldValue(d1, field);
            String v2 = getFieldValue(d2, field);
            if (v1 == null) v1 = "";
            if (v2 == null) v2 = "";
            return ascending ? v1.compareTo(v2) : v2.compareTo(v1);
        });
    }

    private String getFieldValue(Donation d, String field) {
        switch (field) {
            case "Nombre Item": return d.getNombreItem();
            case "Proveedor": return d.getNombreProveedor();
            case "Nro Orden": return d.getNroOrden();
            case "Fecha": return d.getFechaMovimto();
            case "Almacen": return d.getNombreAlmacen();
            case "Tipo Uso": return d.getTipoUso();
            case "Mes": return d.getMesMovimto();
            // Add more as needed
            default: return "";
        }
    }
}
