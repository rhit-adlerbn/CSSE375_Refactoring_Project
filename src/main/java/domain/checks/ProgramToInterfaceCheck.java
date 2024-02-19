package domain.checks;

import domain.model.ClassModel;
import domain.model.FieldModel;
import domain.model.MethodModel;

import java.util.ArrayList;
import java.util.List;


public class ProgramToInterfaceCheck implements LintCheck{
    public ProgramToInterfaceCheck(){}
    /**
     * Runs the lint check
     * @param classes a list of class models to lint over
     * @return msgs a list of strings
     */
    public List<String> runLintCheck(List<ClassModel> classes) {
        ArrayList<String> msgs = new ArrayList<String>();
        ArrayList<String> concretes = getConcretes((ArrayList<ClassModel>) classes);
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

    /**
     * Finds the concrete ClassModels in the given classes
     * @param classes the list of ClassModels
     * @return a list of concrete ClassModels
     */
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

    /**
     * Checks if a list contains any object in a sublist
     * @param mainList
     * @param subList
     * @return if an object in the sublist is in the mainlist
     */
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