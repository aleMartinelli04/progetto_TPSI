import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CSVFile {

    private final File file;
    private final Map<String, List<String>> rows;

    public CSVFile(File file) {
        this.file = file;
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

    public File getFile() {
        return file;
    }

    public Map<String, List<String>> getRows() {
        return rows;
    }
}
