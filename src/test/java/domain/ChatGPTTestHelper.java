package domain;

import datasource.ASMAdapter;
import domain.checks.ChatGPTCouplingCheck;
import domain.checks.LintCheck;
import domain.model.ClassModel;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatGPTTestHelper {

    private final String url = "https://api.openai.com/v1/chat/completions";
    private final String apiKey = "sk-proj-A0laHLgOtie27TwJf9xmT3BlbkFJlDoZoOD02BcU0ouD8jAe";
    private final String model = "gpt-3.5-turbo";

    public boolean interpretResponse(Result r, String filterPrompt, String checkFor) {
        return chatGPT(r.toString() +  " " + filterPrompt).toLowerCase()
                .contains(checkFor.toLowerCase());
    }

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

}
