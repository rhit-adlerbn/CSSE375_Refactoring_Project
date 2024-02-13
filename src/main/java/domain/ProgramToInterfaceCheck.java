package domain;

import java.util.ArrayList;


public class ProgramToInterfaceCheck implements LintCheck{
    public ProgramToInterfaceCheck(){}

    public ArrayList<String> runLintCheck(ArrayList<ClassModel> classes) {
        ArrayList<String> msgs = new ArrayList<String>();
        ArrayList<String> concretes = getConcretes(classes);
        for (ClassModel c : classes) {
            for(MethodModel m : c.getMethods()){

                for(LocalVarModel l : m.getLocalVars()){

                }
            }
            for(FieldModel f : c.getFields()){

            }
        }
        return msgs;
    }
    private ArrayList<String> getConcretes(ArrayList<ClassModel> classes){
        ArrayList<String> supers = new ArrayList<String>();
        ArrayList<String> concretes = new ArrayList<String>();
        for(ClassModel c : classes){
            if(c.isInterface() || c.isAbstract()){
                supers.add(c.getName());
            }
        }
        for(ClassModel c : classes) {
            if (supers.contains(c.getSuperName()) || containsAny(supers, (ArrayList<String>) c.getInterfaces())) {
                concretes.add(c.getName());
            }
        }
        return concretes;
    }
    private boolean containsAny(ArrayList<String> mainList, ArrayList<String> subList){
        if(mainList.isEmpty()){
            return false;
        }
        for(String s : subList){
            if(mainList.contains(s)){
                return true;
            }
        }
        return false;
    }
}
