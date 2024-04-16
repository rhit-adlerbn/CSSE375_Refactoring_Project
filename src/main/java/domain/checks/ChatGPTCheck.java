package domain.checks;

import datasource.ASMAdapter;
import domain.Result;
import domain.model.ClassModel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public abstract class ChatGPTCheck implements LintCheck{

    private final String url = "https://api.openai.com/v1/chat/completions";
    private final String apiKey = "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";
    private final String model = "gpt-3.5-turbo";

    private String chatGPT(String prompt) {
        String body = this.chatGPTPrompt(prompt);

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

    abstract String buildQuery(List<ClassModel> classes);

    @Override
    public List<Result> runLintCheck(List<ClassModel> classes) {
        ArrayList<Result> results = new ArrayList<>();

        String query = buildQuery(classes);
        String lintChecks = chatGPT(query);
        String[] splitChecks = lintChecks.split(Pattern.quote("\\n"));

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