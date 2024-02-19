package domain.checks;

import domain.model.ClassModel;
import domain.model.FieldModel;
import domain.model.MethodModel;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ObserverPatternCheck implements LintCheck{





    private boolean containsMethods(ClassModel classNode, ArrayList<String> Methods){
        List<MethodModel> methods =  classNode.getMethods();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ArrayList<String> unused = new ArrayList<String>();
        for(MethodModel method: methods){
            map.put(method.getName(), 1);
        }
        for (String method : Methods) {
            if(!map.containsKey(method)){
                unused.add(method);
            }
        }
        //for (String needed: unused){
          //  System.out.println("Missing Method: " + needed);
        //}
        return unused.isEmpty();
    }
    private boolean implementsInterface(ClassModel classNode, String interfaceSimpleName) {
        for (String implementedInterface : classNode.getInterfaces()) {
            String name = implementedInterface.substring(implementedInterface.indexOf("/")  + 1);
            //System.out.println(name);
            if(interfaceSimpleName.equals(name)){
                return true;
            }
        }
        return false;
    }


    private boolean checkFields(ClassModel classNode, String fieldName) {
        List<FieldModel> fields =  classNode.getFields();


        for (FieldModel field : fields) {
            Type fieldType;
            if(field.getSignature() == null) {
                fieldType = Type.getType(field.getDesc());
            } else {
                fieldType = Type.getType(field.getSignature());
            }
            String name = fieldType.getClassName();


            if(name.contains(fieldName)){
                return true;
            }
        }
        return false;
    }


    public List<String> runLintCheck(List<ClassModel> classes){
        ArrayList<String> msgs = new ArrayList<String>();
        for(ClassModel clas: classes){
            msgs.add(checkObserverPattern(clas));
        }
        return msgs;
    }


    public String checkObserverPattern(ClassModel classNode){
        String returnValue = "Not Observer Pattern";
        if(implementsInterface(classNode, "Subject")) {
            returnValue = "Subject";
            ArrayList<String> requiredMethods = new ArrayList<String>();
            requiredMethods.add("subscribe");
            requiredMethods.add("unsubscribe");
            requiredMethods.add("Notify");
            if(!containsMethods(classNode, requiredMethods)){
                //System.out.println("Attempting to use observer pattern but doesnt have all the required methods.");
                returnValue = "Failed Subject";
            }
            if (!(checkFields(classNode, "Observer"))) {
                //System.out.println("Attempting to use observer pattern as a Subject but doesnt have a Observer instance.");
                returnValue = "Failed Subject";
            }
            return returnValue;
        } else if (implementsInterface(classNode, "Observer")){
            returnValue = "Observer";
            ArrayList<String> requiredMethods = new ArrayList<String>();
            requiredMethods.add("update");
            if(!containsMethods(classNode, requiredMethods)){
                //System.out.println("Attempting to use observer pattern but doesnt have all the required methods.");
                returnValue = "Failed Observer";
            }
            if (!(checkFields(classNode, "Subject"))) {
                //System.out.println("Attempting to use observer pattern as an Observer but doesnt have a Subject instance.");
                returnValue = "Failed Observer";
            }

        }
        return returnValue;

    }





}
