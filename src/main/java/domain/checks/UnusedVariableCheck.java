package domain.checks;

import domain.Result;
import domain.model.*;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UnusedVariableCheck implements LintCheck{
    public List<Result> runLintCheck(List<ClassModel> classes){
        ArrayList<Result> results = new ArrayList<>();
        ArrayList<String> curr = new ArrayList<>();
         for(ClassModel clas: classes){
            curr = PrintAllVariables(clas);
            for(String message: curr){
                results.add(new Result(clas.getName(), this.getClass().getSimpleName(), message));
            }
         }
         return results;
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
