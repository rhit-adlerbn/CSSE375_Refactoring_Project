package domain.checks;

import domain.model.ClassModel;

import java.util.List;

public class ChatGptObserverCheck extends ChatGPTCheck{
    @Override
    String buildQuery(List<ClassModel> classes) {
        StringBuilder query = new StringBuilder(
                "Based on the observer pattern, determine whether each of the following Java classes is an observer, a subject, or neither." +
                        "You will be provided classes in the format {class name : class representation.}" +
                        "Start the analysis of each class on a new line." +
                        "Do not include any other new lines in your response"
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
