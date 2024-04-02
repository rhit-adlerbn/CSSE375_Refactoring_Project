package datasource;

import com.opencsv.CSVWriter;

import domain.Result;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileOutput {
    private static String[] header = new String[]{"Class tested", "Test ran", "Output"};
    public static void saveResults(List<Result> results) {
        String csvFilePath = "files/output.csv";
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            writer.writeNext(header);
            for(Result result : results){
                writer.writeNext(result.getResults());
            }
            System.out.println("CSV file created successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
