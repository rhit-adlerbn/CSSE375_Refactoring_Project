package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

//	Private constructor
//	Private static variable of the same class that is the only instance of the class.
//	Public static method that returns the instance of the class

public class SingletonCheck implements LintCheck{
    public SingletonCheck(){}
    public List<String> runLintCheck(List<ClassModel> classes) {
        ArrayList<String> msgs = new ArrayList<String>();
        for (ClassModel c : classes) {
            msgs.add(runStaticCheck(c));
        }
        return msgs;
    }

    private String runStaticCheck(ClassModel classMod) {
        boolean privateInit = false;
        boolean privateInst = false;
        boolean publicGetInst = false;
        String name = classMod.getName();
        for (MethodModel m : classMod.getMethods()) {
            if ((m.getName().equals("getInstance") || m.getName().equals("getInst")) && m.isPublic() && m.isStatic())
                publicGetInst = true;
            if (m.getName().equals("<init>") && m.isPrivate())
                privateInit = true;
        }
        for (FieldModel f : classMod.getFields()) {
            if (f.isPrivate() && typeMatches(f, name) && f.isStatic() && (f.getName().equals("inst") || f.getName().equals("instance")))
                privateInst = true;
        }
        if (privateInit && privateInst && publicGetInst) return name + " is a Singleton";
        else return name + " is not a Singleton";
    }
    private boolean typeMatches(FieldModel field, String className){
        return (field.getType().equals(className));
    }
}
