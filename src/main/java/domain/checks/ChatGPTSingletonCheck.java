package domain.checks;

import domain.model.ClassModel;

import java.util.List;

public class ChatGPTSingletonCheck extends ChatGPTCheck{

    @Override
    String buildQuery(List<ClassModel> classes) {
        StringBuilder query = new StringBuilder(
                "Determine if each class is a singleton instance in the following Java classes." +
                        "You will be provided classes in the format {class name : class representation.}" +
                        "Start the analysis of each class on a new line." +
                        "Your response should have as many lines as classes provided"
        );
        for (ClassModel model: classes) {
            query.append("{")
                    .append(model.getName())
                    .append(" : ")
                    .append(model.toString().replaceAll("\\P{Print}", ""))
                    .append("} ");
        }
        return query.toString();
    }
}
