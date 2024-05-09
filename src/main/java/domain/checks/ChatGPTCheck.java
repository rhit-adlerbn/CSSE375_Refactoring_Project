package domain.checks;

import domain.Result;
import domain.model.ClassModel;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public abstract class ChatGPTCheck implements LintCheck{

    private final String url = "https://api.openai.com/v1/chat/completions";
    private String apiKey = "OPEN AI KEY";
    private final String model = "gpt-3.5-turbo";

    public ChatGPTCheck(String keyPath) {
        apiKey = this.getKey(keyPath);
    }

    private String getKey(String keyFilePath) {
        File keyFile = new File(keyFilePath);
        try {
            Scanner scanner = new Scanner(keyFile);
            String line = scanner.nextLine();
            if (line.startsWith("sk-")) {
                return line;
            }
        } catch (FileNotFoundException e) {
            // We can run without keyfile existing
            System.err.println("No ChatGPT key file found!");
        }
        return "";
    }

    private String chatGPT(List<String> prompts) {
        StringBuilder overallPrompt = new StringBuilder();

        for (String prompt : prompts) {
            String body = this.chatGPTPrompt(prompt);

            try {
                HttpURLConnection connection = this.chatGPTConnection(body);

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()
                ));

                // This prompt could be read successfully
                overallPrompt.append(prompt);
                br.close();

            } catch (IOException e) {
                System.err.println("Prompt could not be read by ChatGPT");
                e.printStackTrace();
            }
        }

        String body = this.chatGPTPrompt(overallPrompt.toString());

        try {
            HttpURLConnection connection = this.chatGPTConnection(body);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()
            ));

            String line;
            StringBuilder response = new StringBuilder();
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            return extractMessageFromJSONResponse(response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String chatGPTPrompt(String prompt) {
        return "{\"model\": \"" + model + "\"," +
                "\"messages\": [{\"role\": \"user\", " +
                "\"content\": \"" + prompt + "\"}]}";
    }

    private HttpURLConnection chatGPTConnection(String body) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(body);
        writer.flush();
        writer.close();
        return connection;
    }

    private String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content") + 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end);
    }

    abstract List<String> buildQuery(List<ClassModel> classes);

    @Override
    public List<Result> runLintCheck(List<ClassModel> classes) {
        ArrayList<Result> results = new ArrayList<>();

        List<String> query = buildQuery(classes);
        String lintChecks = chatGPT(query);
        String[] splitChecks = lintChecks.split(Pattern.quote("\\n"));

        System.out.println(lintChecks);
        int classIndex = 0;
        for (String s: splitChecks) {
            if (!s.isEmpty()) {
                results.add(new Result(classes.get(classIndex).getName(), this.getClass().getSimpleName(), s));
                classIndex++;
            }
        }

        if (results.size() != classes.size()) {
            System.err.println("WARNING: Number of outputs does not match number of classes");
        }
        return results;
    }
}