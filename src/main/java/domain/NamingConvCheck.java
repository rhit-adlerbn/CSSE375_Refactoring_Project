package domain;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class NamingConvCheck implements LintCheck{
    private final String PASCAL_REGEX = "[A-Z][a-zA-Z0-9]*";
    private final String CAMEL_REGEX = "[a-z][a-zA-Z0-9_$]*";


    public NamingConvCheck(){}

    public List<String> runLintCheck(List<ClassModel> classes) {

        ArrayList<String> msgs = new ArrayList<String>();
        for (ClassModel c : classes) {
            msgs.add(runNameCheck(PASCAL_REGEX, c.getName()));
            for (MethodModel m : c.getMethods()) {
                String name = m.getName();
                if(!name.equals("<init>")) msgs.add(runNameCheck(CAMEL_REGEX, name));
            }
            for (FieldModel f : c.getFields()) {
                msgs.add(runNameCheck(CAMEL_REGEX, f.getName()));
            }
        }

        return msgs;
    }

    private String runNameCheck(String pattern,String name){
        if(regexCheck(pattern, name)) return name + " is named correctly" ;
        else return "Issue: "+ name+" is named incorrectly";
    }
    private boolean regexCheck(String pattern, String name){
        return Pattern.compile(pattern).matcher(name).matches();
    }

}
