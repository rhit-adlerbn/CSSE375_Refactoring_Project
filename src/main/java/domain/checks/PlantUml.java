package domain.checks;

import domain.Result;
import domain.model.ClassModel;
import domain.model.FieldModel;
import domain.model.LocalVarModel;
import domain.model.MethodModel;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlantUml implements LintCheck {
    public List<Result> runLintCheck(List<ClassModel> classes){
        String testName = this.getClass().getSimpleName();
        ArrayList<Result> results = new ArrayList<>();
        for(ClassModel clas: classes){
            results.add(new Result(clas.getName(), testName, CreateUML(clas)));
        }
        return results;
    }

    public String CreateUML(ClassModel classNode){
        String uml = "";
        String names = classNode.getName().substring(classNode.getName().lastIndexOf("/") + 1);
        uml += "+class " + names + "{\n";
        //List<MethodNode> methods = (List<MethodNode>) classNode.methods;
        String depend = "";
        List<MethodModel> methods = classNode.getMethods();
        List<FieldModel> fields = classNode.getFields();
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
            name = name.substring(name.lastIndexOf(".") + 1);
            //System.out.println("\n" + name +"\n");
            if(name.contains("Array")){
                for(String key: Primary.keySet()){
                    if(name.contains(key)){
                        uml += getAccess(field.getAccess()) + field.getName() +  ": List<" + key + ">" + "\n" ;
                    }
                }
            }
            if(Primary.containsKey(name)){
                uml += getAccess(field.getAccess()) + field.getName() + ": " + name + "\n" ;
            }else{
                depend += names + "-->" + name +"\n";
            }

        }
        for(MethodModel method: methods){
            if(!method.getName().equals("<init>")) {
                uml += getAccess(method.isPrivate()) + method.getName() + "(";
                List<LocalVarModel> params = method.getLocalVars();
                for (LocalVarModel param : params) {
                    if(!param.getName().equals("this")) {
                        uml += param.getName() + ",";
                    }
                }
                uml = uml.substring(0, uml.length()-1);
                uml += "):" + method.getReturnType() + "\n";
            }

        }

        uml+="}\n";
        uml += depend;
        return uml;
    }


    public static String getAccess(boolean acces){
        if(acces)
            return "-";
        return "+";
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
