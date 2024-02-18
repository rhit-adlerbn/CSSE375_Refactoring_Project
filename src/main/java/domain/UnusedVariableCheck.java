package domain;

import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UnusedVariableCheck implements LintCheck{








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
         ArrayList<String> msgs = new ArrayList<String >();
        ArrayList<String> curr = new ArrayList<String >();
         for(ClassModel clas: classes){
            curr = PrintAllVariables(clas);
            for(String message: curr){
                msgs.add(message);
            }

         }
         return msgs;
    }



    public ArrayList<String> PrintAllVariables(ClassModel classNode){
        ArrayList<String> msgs = new ArrayList<String>();
        List<MethodModel> methods = classNode.getMethods();
        ArrayList<String> curr = new ArrayList<String>();
        for (MethodModel method : methods) {
            if(!method.getName().equals("<init>")) {
                curr = printVariables(method);
                for(String message: curr){
                    msgs.add(message);
                }
            }
        }
        return msgs;

    }
    public ArrayList<String> printVariables(MethodModel methodNode){
        InstructionModel instructions = methodNode.getInstructions();
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        HashMap<Integer, String> variables = new HashMap<Integer, String>();

        ArrayList<String> msgs = new ArrayList<String>();
        Type[] argumentTypes = Type.getArgumentTypes(methodNode.getDesc());
        for (int i = 0; i < argumentTypes.length; i++) {
            map.put(i, 1);
            variables.put(i, argumentTypes[i].getClassName());
        }

        for (LocalVarModel localVar : methodNode.getLocalVars()) {
            variables.put(localVar.getIndex(), localVar.getName());

        }

        for (int i = 0; i < instructions.getSize(); i++) {
            AbstractInsnModel insn = instructions.get(i);
            //System.out.println(insn.getOpcode());
            if (insn.getType() == 2) {

                VarInsModel varInsn = insn.getVar();
               // System.out.println("Variable name: " + variables.get(varInsn.var)+" " + varInsn.getOpcode());
                if(map.containsKey(varInsn.getVar())){
                    if(varInsn.getOpCode() != 54) {
                        map.put(varInsn.getVar(), 2);
                    }
                } else {
                    map.put(varInsn.getVar(), 1);
                }
            }

        }



        for(Integer var: map.keySet()){
            if(!variables.get(var).equals("this")) {
                if (map.get(var) == 1) {
                    msgs.add("Unused Variable " + "\"" + variables.get(var) + "\"." + " In method " + methodNode.getName());

                }
            }

        }
        if(msgs.isEmpty()){
            msgs.add("No unused Vars");
        }
        return msgs;

    }





}
