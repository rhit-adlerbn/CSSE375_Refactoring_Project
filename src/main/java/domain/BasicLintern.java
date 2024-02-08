package domain;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class BasicLintern {


    protected static boolean containsMethods(ClassNode classNode, ArrayList<String> Methods){
        List<MethodNode> methods = (List<MethodNode>) classNode.methods;
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ArrayList<String> unused = new ArrayList<String>();
        for(MethodNode method: methods){
            map.put(method.name, 1);
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
    protected static boolean implementsInterface(ClassNode classNode, String interfaceSimpleName) {
        for (String implementedInterface : classNode.interfaces) {
            String name = implementedInterface.substring(implementedInterface.indexOf("/")  + 1);
            //System.out.println(name);
            if(interfaceSimpleName.equals(name)){
                return true;
            }
        }
        return false;
    }


    protected static boolean checkFields(ClassNode classNode, String fieldName) {
        List<FieldNode> fields = (List<FieldNode>) classNode.fields;


        for (FieldNode field : fields) {
            Type fieldType;
            if(field.signature == null) {
                fieldType = Type.getType(field.desc);
            } else {
                fieldType = Type.getType(field.signature);
            }
            String name = fieldType.getClassName();


            if(name.contains(fieldName)){
                return true;
            }
        }
        return false;
    }
}
