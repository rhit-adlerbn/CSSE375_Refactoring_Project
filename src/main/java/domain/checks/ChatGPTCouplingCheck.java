package domain.checks;

import domain.model.ClassModel;

import java.util.List;

public class ChatGPTCouplingCheck extends ChatGPTCheck{

    @Override
    String buildQuery(List<ClassModel> classes) {
        StringBuilder query = new StringBuilder(
                "Analyze and determine the coupling level of the following Java classes." +
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
