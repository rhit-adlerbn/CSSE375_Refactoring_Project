package domain.checks;

import domain.model.ClassModel;

import java.util.ArrayList;
import java.util.List;

public class ChatGPTSingletonCheck extends ChatGPTCheck{

    @Override
    List<String> buildQuery(List<ClassModel> classes) {
        List<String> query = new ArrayList<>();
        query.add(
                "Determine if each class is a singleton instance in the following Java classes." +
                        "You will be provided classes in the format {class name : class representation.}" +
                        "Start the analysis of each class on a new line." +
                        "Your response should have as many lines as classes provided"
        );
        for (ClassModel model: classes) {
            String classString = "{" +
                    model.getName() +
                    " : " +
                    model.toString().replaceAll("\\P{Print}", "") +
                    "} ";
            query.add(classString);
        }
        return query;
    }
}
