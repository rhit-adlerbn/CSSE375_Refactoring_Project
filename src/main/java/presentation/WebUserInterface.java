package presentation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import domain.Result;
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

        //parse request
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while (!(line = br.readLine()).isBlank()) {
            requestBuilder.append(line).append("\r\n");
        }
        String request = requestBuilder.toString();
        String[] requestsLines = request.split("\r\n");
        String[] requestLine = requestsLines[0].split(" ");
        String path = requestLine[1];

        //generate result
        int queryIndex = path.indexOf('?');
        if (queryIndex != -1) {
            runChecks(path);
            path = path.substring(0, queryIndex);
        }
        
        //send response
        Path filePath = getFilePath(path);
        System.out.println(filePath);
        if (Files.exists(filePath)) {
            if ("/result".equals(path)) {
                sendResponse(client, "302 Found", Files.readAllBytes(filePath), path);
                return;
            }
            sendResponse(client, "200 OK", Files.readAllBytes(filePath), path);
        } else {
            byte[] notFoundContent = "<h1>Not found</h1>".getBytes();
            sendResponse(client, "404 Not Found", notFoundContent, "");
        }
    }

    private static void runChecks(String path) throws IOException{
        List<Result> result;
        dataFilePath = URLDecoder.decode(path.substring(path.indexOf("=")+1, path.indexOf("&")),StandardCharsets.UTF_8.toString());
        checksToRun = URLDecoder.decode(path.substring(path.indexOf("=", path.indexOf("=")+1)+1, path.length()),StandardCharsets.UTF_8.toString());
        
        List<ClassModel> classes = UserInterface.getClassesFromFile(dataFilePath);
        List<Integer> checks = UserInterface.convertInput(checksToRun);
        result = UserInterface.runChecks(checks, classes);
        generateResultHtml(result);
       
        System.out.println(result);
        System.out.println("FilePath:"+dataFilePath);
        System.out.println("Test:" + checksToRun);
    }

    private static void generateResultHtml(List<Result> results) throws IOException {
        Path path = Paths.get("src/main/java/presentation/result.html");
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n");
        htmlBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        htmlBuilder.append("<title>Linter Results</title>\n</head>\n<body>\n<div class=\"container\">\n");
        htmlBuilder.append("<h1>Linter Results</h1>\n<ul>\n");
        for (Result result : results) {
            htmlBuilder.append("<li>").append(result.toString()).append("</li>\n");
        }
        htmlBuilder.append("</ul>\n</div>\n</body>\n</html>");
        Files.write(path, htmlBuilder.toString().getBytes());
    }
    private static void sendResponse(Socket client, String status, byte[] content, String redirectUrl) throws IOException {    
        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 " + status + "\r\n").getBytes());
        clientOutput.write(("Content-Type: text/html" + "\r\n").getBytes());
        clientOutput.write(("Location: " + redirectUrl + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write(content);
        clientOutput.write("\r\n".getBytes());
        clientOutput.flush();
        client.close();
    }
    private static Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "src\\main\\java\\presentation\\index.html";   
        }else if("/result".equals(path)){
            path = "src\\main\\java\\presentation\\result.html";
        }
        return Paths.get(path);
    }
    private static void resetData() {
        dataFilePath = null;
        checksToRun = null;
    }
}