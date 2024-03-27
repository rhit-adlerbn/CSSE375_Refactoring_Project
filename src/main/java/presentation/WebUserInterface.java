package presentation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import datasource.ASMAdapter;
import domain.checks.LintCheck;
import domain.checks.SingletonCheck;
import domain.model.ClassModel;
public class WebUserInterface {
    private static final int PORT = 8000;
    private static String dataFilePath;
    private static String checksToRun;
    public static void main( String[] args ) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    handleClient(client);
                }
            }
        }
    }

    private static void handleClient(Socket client) throws IOException {
        resetData();
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

        //parseing request
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while (!(line = br.readLine()).isBlank()) {
            requestBuilder.append(line).append("\r\n");
        }
        String request = requestBuilder.toString();
        String[] requestsLines = request.split("\r\n");
        String[] requestLine = requestsLines[0].split(" ");
        String path = requestLine[1];

        List<String> result = new ArrayList<>();
        //geting data from request
        int queryIndex = path.indexOf('?');
        if (queryIndex != -1) {
            dataFilePath = URLDecoder.decode(path.substring(path.indexOf("=")+1, path.indexOf("&")),StandardCharsets.UTF_8.toString());
            checksToRun = URLDecoder.decode( path.substring(path.indexOf("=", path.indexOf("=")+1), path.length()),StandardCharsets.UTF_8.toString());
            System.out.println("FilePath:"+dataFilePath);
            System.out.println("Test:" + checksToRun);
            List<ClassModel> classes = ASMAdapter.parseASM(dataFilePath);
            result =  runChecks(classes);
            System.out.println(result);
            path = path.substring(0, queryIndex);
        }
        
        //sending response
        Path filePath = getFilePath(path);
        System.out.println(filePath);
        if (Files.exists(filePath)) {
            String contentType = guessContentType(filePath);
            sendResponse(client, "200 OK", contentType, Files.readAllBytes(filePath),result);
        } else {
            byte[] notFoundContent = "<h1>Not found</h1>".getBytes();
            sendResponse(client, "404 Not Found", "text/html", notFoundContent,result);
        }
    }

    private static void resetData() {
        dataFilePath = null;
        checksToRun = null;
    }

    private static void sendResponse(Socket client, String status, String contentType, byte[] content, List<String> result) throws IOException {    
        // Send the response with the updated content
        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 " + status + "\r\n").getBytes());
        clientOutput.write(("Content-Type: " + contentType + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write(content);
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();
        client.close();
    }

    private static Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "src\\main\\java\\presentation\\index.html";   
        }
        return Paths.get(path);
    }

    private static String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }
    private static List<String> runChecks(List<ClassModel> classes) {
        LintCheck single = new SingletonCheck();
        return single.runLintCheck(classes);
    }
    // @Override
    // String getCheckToRun() {
    //    return checksToRun;
    // }

    // @Override
    // void displayChecks() {
    //     // Implement logic to display the checks in the web UI
    // }

    // @Override
    // void startDisplay() {
    //     // Implement logic to start the web UI
    // }

    // @Override
    // void close() {
    //     // Implement logic to close the web UI
    // }

    // @Override
    // String getFilePath() {
    //     // Implement logic to get the file path from the web UI
    //     return "path/to/file"; // For example, return a hardcoded file path
    // }

    // @Override
    // void displayResults(List<String> results) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'displayResults'");
    // }
}