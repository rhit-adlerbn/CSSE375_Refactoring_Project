package domain.checks;

import domain.Result;
import domain.model.ClassModel;
import domain.model.MethodModel;
import java.util.ArrayList;
import java.util.List;

public class OCPCheck implements LintCheck{

    /**
     * Checks a class to see if the Open-Closed Principle is held up
     * @param classes a list of class models to lint over
     * @return A list of Strings to display to the user whether the check passed.
     */
    @Override
    public List<Result> runLintCheck(List<ClassModel> classes) {
        String testName = this.getClass().getSimpleName();
        List<Result> results = new ArrayList<>();
        int check = 0;
        for (ClassModel classNode : classes) {
            String className = classNode.getName();
            check = 0;

            List<MethodModel> classMethods = classNode.getMethods();
            for (MethodModel method : classMethods) {
                if (method.isFinal()) {
                    String result = "Methods are final, so not open for extension. Potential violation of OCP in class " + classNode.getName();
                    Result res = new Result(className, testName,result);
                    results.add(res);
                    check = 1;
                    continue;
                }
            }
            if(check == 1){
                continue;
            }

            if (classNode.isFinal()) {
                String result = "Class is final, so not open for extension. Potential violation of OCP in class " + classNode.getName();
                Result res = new Result(className, testName,result);
                results.add(res);
                continue;
            }
            String result = "OCP is held up in class " + classNode.getName();
            Result res = new Result(className, testName,result);
            results.add(res);
           
        }

        return results;
    }
}
