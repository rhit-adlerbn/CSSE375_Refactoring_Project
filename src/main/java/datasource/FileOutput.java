package datasource;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileOutput {
    public static void saveResults(List<String> results) {
        String csvFilePath = "files/output.csv";
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            for(String result : results)
            writer.writeNext(new String[]{result});
            
            System.out.println("CSV file created successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
