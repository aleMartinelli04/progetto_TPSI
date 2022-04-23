import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Form extends JFrame {
    private JTextField txtField;
    private JTextArea txtArea;
    private JButton BtnRIcerca;
    private JPanel panel1;
    private JTable table;

    private final CSVFile file;

    public Form() {
        setContentPane(panel1);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        file = new CSVFile(new File("db.csv"));


        BtnRIcerca.addActionListener(e -> {
            Map<String, Double> temperatures = new TreeMap<>();
            file.getRows().forEach((line, list) -> {
                if (!list.get(2).equalsIgnoreCase(txtField.getText())) {
                    return;
                }

                temperatures.put(list.get(0),  Math.floor(Double.parseDouble(list.get(1))));
            });

            DefaultTableModel model = new DefaultTableModel(new Object[]{"Data", "Temperature °C"}, 0);
            temperatures.forEach((date, temperature) -> {
                model.addRow(new Object[]{date, temperature});
            });

            table.setModel(model);
        });
    }


}
