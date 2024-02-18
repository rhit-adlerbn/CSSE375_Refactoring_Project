package domain;

import datasource.ASMAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GrammarCheck extends BasicLintern{



    /**
     * Reads in a list of Java Classes and prints fun facts about them.
     *
     * For more information, read: https://asm.ow2.io/asm4-guide.pdf
     *
     * @param args
     *            : the names of the classes, separated by spaces. For example:
     *            java example.MyFirstLinter java.lang.String
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException {

        for (String FilePath : args) {

            ArrayList<ClassModel> classes =  ASMAdapter.parseASM(FilePath);



            for (ClassModel clas: classes) {
                PrintAllVariables(clas);
            }
        }
    }









    public static void PrintAllVariables(ClassModel classNode){

        List<MethodModel> methods = classNode.getMethods();
        for (MethodModel method : methods) {
            printVariables(method);
        }
    }
    public static void printVariables(MethodModel methodNode){
        InstructionModel instructions = methodNode.getInstructions();
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        HashMap<Integer, String> variables = new HashMap<Integer, String>();


        Type[] argumentTypes = Type.getArgumentTypes(methodNode.getDesc());
        for (int i = 0; i < argumentTypes.length; i++) {
            map.put(i, 1);
            variables.put(i, argumentTypes[i].getClassName());
        }

        for (LocalVarModel localVar : methodNode.getLocalVars()) {
            variables.put(localVar.getIndex(), localVar.getName());

        }

        for (int i = 0; i < instructions.getSize(); i++) {
            AbstractInsModel insn = instructions.get(i);
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
            if(map.get(var) == 1){
                System.out.println("Unused Variable "+ "\""+variables.get(var)+"\"." + " In method " + methodNode.getName());
            }

        }
    }





}
