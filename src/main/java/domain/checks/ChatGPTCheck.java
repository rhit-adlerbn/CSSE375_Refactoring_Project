package domain.checks;

import datasource.ASMAdapter;
import domain.model.ClassModel;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatGPTCheck implements LintCheck{

    private final String url = "https://api.openai.com/v1/chat/completions";
    private final String apiKey = "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";
    private final String model = "gpt-3.5-turbo";

    public static void main(String[] args) {
        ChatGPTCheck gpt = new ChatGPTCheck();
        ASMAdapter asm = new ASMAdapter();
        List<ClassModel> models = asm.parseASM(
                "C:\\Users\\garvinac\\CSSE375_Refactoring_Project\\src\\test\\resources\\CouplingTests"
        );
        List<String> output = gpt.runLintCheck(models);
        for (String s : output) {
            System.out.println(s);
        }
    }

    public String chatGPT(String prompt) {

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

    private String buildQuery(List<ClassModel> classes) {
        StringBuilder query = new StringBuilder(
                "Analyze and determine the coupling level of the following Java classes." +
                        "They will be provided in the format {class name : class representation.}" +
                        "Start each analysis with the class name in braces"
        );
        for (ClassModel model: classes) {
            query.append(model.getName())
                    .append(" : ")
                    .append(model.toString().replace("\n", ""));
        }
        return query.toString();
    }

    @Override
    public List<String> runLintCheck(List<ClassModel> classes) {
        ArrayList<String> result = new ArrayList<>();

        String query = buildQuery(classes);
        String lintChecks = chatGPT(query);
        String[] splitChecks = lintChecks.split("[{]");
        for (String s: splitChecks) {
            if (!s.isEmpty()) {
                result.add(s.replace("}", ""));
            }
        }

        return result;
    }
}
