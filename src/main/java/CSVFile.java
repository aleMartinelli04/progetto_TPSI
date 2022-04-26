import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CSVFile {
    private final Map<String, List<String>> rows;

    /**
     * Legge il file csv e lo trasforma in una mappa contenente la riga e la riga divisa
     * @param file Il file .csv da leggere
     */
    public CSVFile(File file) {
        this.rows = new TreeMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                rows.put(line, Arrays.asList(row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> getRows() {
        return rows;
    }
}
