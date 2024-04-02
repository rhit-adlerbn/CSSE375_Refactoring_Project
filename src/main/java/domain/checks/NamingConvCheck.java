package domain.checks;
import domain.Result;
import domain.model.ClassModel;
import domain.model.FieldModel;
import domain.model.MethodModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class NamingConvCheck implements LintCheck{
    private final String PASCAL_REGEX = "[A-Z][a-zA-Z0-9]*";
    private final String CAMEL_REGEX = "[a-z][a-zA-Z0-9_$]*";


    public NamingConvCheck(){}

    /**
     * Runs the lint check
     * @param classes a list of class models to lint over
     * @return msgs a list of strings
     */
    public List<Result> runLintCheck(List<ClassModel> classes) {
        String testName = this.getClass().getSimpleName();
        ArrayList<Result> results = new ArrayList<>();
        for (ClassModel c : classes) {
            String cName = c.getName();
            results.add(new Result(cName,testName,runNameCheck(PASCAL_REGEX, c.getName())));

            for (MethodModel m : c.getMethods()) {
                String name = m.getName();
                if(!name.equals("<init>")) 
                    results.add(new Result(cName,testName,runNameCheck(CAMEL_REGEX, name)));
            }
            for (FieldModel f : c.getFields()) {
                results.add(new Result(cName,testName,runNameCheck(CAMEL_REGEX, f.getName())));
            }
        }

        return results;
    }

    /**
     * Creates the check message
     * @param pattern the pattern to match
     * @param name the name to check
     * @return msg
     */
    private String runNameCheck(String pattern,String name){
        if(regexCheck(pattern, name)) return name + " is named correctly" ;
        else return "Issue: "+ name+" is named incorrectly";
    }

    /**
     * Checks if matches the pattern
     * @param pattern
     * @param name
     * @return matches pattern
     */
    private boolean regexCheck(String pattern, String name){
        return Pattern.compile(pattern).matcher(name).matches();
    }

}
