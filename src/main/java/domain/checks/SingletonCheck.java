package domain.checks;

import domain.model.ClassModel;
import domain.model.FieldModel;
import domain.model.MethodModel;

import java.util.ArrayList;
import java.util.List;



public class SingletonCheck implements LintCheck{
    public SingletonCheck(){}
    /**
     * Runs the lint check
     * @param classes a list of class models to lint over
     * @return msgs a list of strings
     */
    public List<String> runLintCheck(List<ClassModel> classes) {
        ArrayList<String> msgs = new ArrayList<String>();
        for (ClassModel c : classes) {
            msgs.add(runStaticCheck(c));
        }
        return msgs;
    }

    /**
     * Checks if a class is a singleton
     * @param classMod the ClassModel
     * @return a message indicating if the class is a singleton
     */
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

    /**
     * Determine if a field has the same type as a given classname
     * @param field the FieldModel
     * @param className the Class name
     * @return if the field is of the class type
     */
    private boolean typeMatches(FieldModel field, String className){
        return (field.getType().equals(className));
    }
}
