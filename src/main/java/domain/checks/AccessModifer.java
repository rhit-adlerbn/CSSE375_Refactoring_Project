package domain.checks;

import domain.model.ClassModel;
import domain.model.MethodModel;
import domain.model.Model;
import domain.Result;
import java.util.ArrayList;
import java.util.List;


public class AccessModifer implements LintCheck{
    private final String CHECK_NAME = "AccessModifer";
    /**
     * Checks each class for unaccesed public and accessed private fields and methods
     * makes all methods and fields private that are not accessed by any outside class
     * makes all methods and fields public that are accessed by outside classes
     * @param classes a list of class models to alter
     */
    @Override
    public List<Result> runLintCheck(List<ClassModel> classes) {
        List<Result> results = new ArrayList<>();
        for(ClassModel c : classes) {
            results.addAll(accessCheck(c, classes));
        }
        return results;
    }

    /**
     *
     * @param subject the class under examination
     * @param classes the full list of classes in the package
     * privatizes all fields and methods(Models) of subject that aren't accessed
     * by any other class
     */
    private List<Result> accessCheck(ClassModel subject, List<ClassModel> classes) {
        List<Result> res = new ArrayList<>();
        List<ClassModel> allOtherClasses = new ArrayList<>(classes);
        List<Model> modelsToCheck = new ArrayList<>();
        
        allOtherClasses.remove(subject);
        modelsToCheck.addAll(subject.getFields());
        modelsToCheck.addAll(subject.getMethods());

        String className = subject.getName();
        
        for(Model m : modelsToCheck) {
            boolean priv = m.isPrivate();
            boolean accessed = isAccessed(m, subject, allOtherClasses);
            
            if(!priv && !accessed){
                res.add(new Result(className, CHECK_NAME, m.getName()+" was changed from public to private"));
                m.privatize();
            }
            if(priv && accessed){
                res.add(new Result(className, CHECK_NAME, m.getName()+" was changed from private to public"));
                m.publicize();
            } 
        }

        if(res.isEmpty()){
            res.add(new Result(className, CHECK_NAME, "found no access modifers to change"));
        }

        return res;
    }
    /**
     *
     * @param element the field/method to check
     * @param subject the class the element is from
     * @param allOtherClasses
     * @return true if the element is accessed by at least one of allOtherClasses
     */
    private boolean isAccessed(Model element, ClassModel subject, List<ClassModel> allOtherClasses) {
        for(ClassModel c : allOtherClasses) {
            for(MethodModel m : c.getMethods()) {
                for(String name : m.getNodeNames()) {
                    if(match(name, element)) return true;
                }
            }
        }
        return false;
    }

    private boolean match(String name, Model m){
        return name.equals(m.getName());
    }
   
}
