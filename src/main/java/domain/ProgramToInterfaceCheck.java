package domain;

import java.util.ArrayList;


public class ProgramToInterfaceCheck implements LintCheck{
    public ProgramToInterfaceCheck(){}

    public ArrayList<String> runLintCheck(ArrayList<ClassModel> classes) {
        ArrayList<String> msgs = new ArrayList<String>();
        ArrayList<String> concretes = getConcretes(classes);
        for (ClassModel c : classes) {
            String cName = c.getName();
            for(MethodModel m : c.getMethods()){
                String name = m.getName();
                if(concretes.contains(m.getReturnType())){
                    msgs.add("Issue: Method " + name + " in class: " + cName + " returns a concrete implementation");
                }
                for(String p: m.getParams()){
                    if(concretes.contains(p)){
                        msgs.add("Issue: Method " + name + " in class: " + cName + " expects a concrete implementation");
                    }
                }
            }
            for(FieldModel f : c.getFields()){
                if(concretes.contains(f.getType())){
                    msgs.add("Issue: Field " + f.getName() + " in class: " + cName + " is a concrete instance");
                }
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