package domain.checks;

import domain.model.ClassModel;
import domain.model.FieldModel;
import domain.model.MethodModel;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CouplingCheck implements LintCheck{

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
        for (String needed: unused){
            System.out.println("Missing Method: " + needed);
        }
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
            msgs.add(checkCoupling(clas));
        }
        return msgs;
    }




    public String checkCoupling(ClassModel classNode){
        double couplingScore = 0;
        List<FieldModel> fields =  classNode.getFields();
        HashMap<String, Integer> Primary =  new HashMap<String, Integer>();
        Primary.put("String", 1);
        Primary.put("int", 1);
        Primary.put("char", 1);
        Primary.put("long", 1);
        Primary.put("double", 1);
        for (FieldModel field : fields) {
            Type fieldType;
            if(field.getSignature() == null) {
                fieldType = Type.getType(field.getDesc());
            } else {
                fieldType = Type.getType(field.getSignature());
            }
            String name = fieldType.getClassName();
            if(!Primary.containsKey(name)){
                couplingScore*=1.4;
                couplingScore++;

            }



        }
        return ""+couplingScore;
    }



    public static String getAccess(int acces){
        switch (acces){
            case (1):
                return "+";
            case (2):
                return "-";

        }
        return "-";
    }
}
