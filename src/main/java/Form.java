import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Form extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTable table;
    private JButton searchButton;
    private JComboBox<String> countryBox;
    private JComboBox<Integer> fromYearBox;
    private JComboBox<Integer> toYearBox;
    private JPanel chartPanel;

    private final CSVFile file;

    public Form() {
        setContentPane(mainPanel);

        setSize(600, 600);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        for (int year = 1930; year <= 2013; year++) {
            fromYearBox.addItem(year);
            toYearBox.addItem(Math.abs(year - 2013) + 1930);
        }

        file = new CSVFile(new File("db.csv"));

        Set<String> countries = new HashSet<>();
        file.getRows().values().forEach(s -> countries.add(s.get(2)));

        List<String> sorted = new ArrayList<>(countries);
        Collections.sort(sorted);
        sorted.forEach(country -> countryBox.addItem(country));

        setVisible(true);

        searchButton.addActionListener(e -> {
            setFieldsEnabled(false);

            Map<LocalDate, Double> temperatures = new TreeMap<>();
            file.getRows().forEach((country, list) -> {
                if (!list.get(2).equalsIgnoreCase((String) countryBox.getSelectedItem())) {
                    return;
                }

                if ((int) fromYearBox.getSelectedItem() >= (int) toYearBox.getSelectedItem()) {
                    JOptionPane.showMessageDialog(this, "Intervallo di anni invalido", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String dateString = list.get(0);
                String temperatureString = list.get(1);

                double temperature = Math.floor(Double.parseDouble(temperatureString));
                LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                if (date.getYear() < (int) fromYearBox.getSelectedItem()) {
                    return;
                }

                if (date.getYear() > (int) toYearBox.getSelectedItem()) {
                    return;
                }

                temperatures.put(date, temperature);
            });

            if (temperatures.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid country", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultTableModel model = new DefaultTableModel(new Object[]{"Date", "Temperature °C"}, 0);
            temperatures.forEach((date, temperature) -> model.addRow(new Object[]{date, temperature}));

            table.setModel(model);

            chartPanel.removeAll();
            chartPanel.add(createChart(temperatures));

            tabbedPane.setSelectedIndex(0);

            setFieldsEnabled(true);
        });
    }

    private ChartPanel createChart(Map<LocalDate, Double> temperatures) {
        XYDataset dataset = createDataset(temperatures);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Temperatures",
                "Date",
                "Temperature",
                dataset
        );

        return new ChartPanel(chart);
    }

    private XYDataset createDataset(Map<LocalDate, Double> temperatures) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("Date");

        temperatures.forEach((date, temperature) -> series.add(new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear()), temperature));

        dataset.addSeries(series);
        return dataset;
    }

    private void setFieldsEnabled(boolean enabled) {
        searchButton.setEnabled(enabled);
        fromYearBox.setEnabled(enabled);
        toYearBox.setEnabled(enabled);
    }
}
