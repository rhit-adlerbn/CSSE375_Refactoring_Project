package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class linter {


        String[] fieldForAnalysisByThisProgram = new String[1];

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
            // TODO: Learn how to create separate Run Configurations so you can run
            // 		 your code on different programs without changing the code each time.
            //		 Otherwise, you will just see your program runs without any output.
            for (String className : args) {
                // One way to read in a Java class with ASM:
                // Step 1. ASM's ClassReader does the heavy lifting of parsing the compiled Java class.
                ClassReader reader = new ClassReader(className);

                // Step 2. ClassNode is just a data container for the parsed class
                ClassNode classNode = new ClassNode();

                // Step 3. Tell the Reader to parse the specified class and store its data in our ClassNode.
                // EXPAND_FRAMES means: I want my code to work. (Always pass this flag.)
                reader.accept(classNode, ClassReader.EXPAND_FRAMES);
                ClassModel classModel = new ClassModel(classNode);

                // Now we can navigate the classNode and look for things we are interested in.
//                printClass(classNode);
                LintCheck interfaceCheck = new InterfaceCheck();
                LintCheck templateCheck = new TemplateCheck();
                LintCheck OCPCheck = new OCPCheck();

                List<ClassModel> classes = new ArrayList<>();
                classes.add(classModel);
                System.out.println(OCPCheck.runLintCheck(classes));
                System.out.println(interfaceCheck.runLintCheck(classes));
                System.out.println(templateCheck.runLintCheck(classes));
            }
        }

        private static void printClass(ClassNode classNode) {
            System.out.println("Class's Internal JVM name: " + classNode.name);
            System.out.println("User-friendly name: "
                    + Type.getObjectType(classNode.name).getClassName());
            System.out.println("public? "
                    + ((classNode.access & Opcodes.ACC_PUBLIC) != 0));
            System.out.println("Extends: " + classNode.superName);
            System.out.println("Implements: " + classNode.interfaces);
            // TODO: how do I write a lint check to tell if this class has a bad name?
        }

        private static String checkInterfaceImplementation(ClassNode classNode) throws IOException {
            List<MethodNode> classMethods = classNode.methods;
            if(classNode.interfaces.isEmpty()){
                return "Interface Check is not applicable here.";
            }
            String theInterface = classNode.interfaces.get(0);

            ClassReader reader = new ClassReader(theInterface);


            ClassNode interfaceNode = new ClassNode();
            //get methods for the interface by defining a interface node
            reader.accept(interfaceNode, ClassReader.EXPAND_FRAMES);
            List<MethodNode> interfaceMethods = interfaceNode.methods;

            Set<String> classMethodNames = new HashSet<>();
            Set<String> interfaceMethodNames = new HashSet<>();

            for(MethodNode method: classMethods){
                classMethodNames.add(method.name);
            }

            for(MethodNode method: interfaceMethods){
                interfaceMethodNames.add(method.name);
            }

            boolean allMethodsImplemented = classMethodNames.containsAll(interfaceMethodNames);
            if(!allMethodsImplemented){
                Set<String> missingMethods = new HashSet<>(interfaceMethodNames);
                missingMethods.removeAll(classMethodNames);

                return "Fail. All methods in interface are not implemented. Missing these methods: " + missingMethods;
            }else {

                return "Success. All methods implemented from interface.";
            }


        }

        private static String checkTemplate(ClassNode classNode) throws IOException {
            List<MethodNode> classMethods = classNode.methods;
            String abstractClass = classNode.superName;
            if(!abstractClass.equals("Abstraction")){
                return "Does not implement template method.";
            }
            List<String> requiredMethods = new ArrayList<>();
            requiredMethods.add("stepIfImplDifferBySubclassM1");
            requiredMethods.add("stepIfImplDifferBySubclassM2");
            Set<String> classMethodNames = new HashSet<>();


            for(MethodNode method: classMethods){
                classMethodNames.add(method.name);
            }

            ClassReader reader = new ClassReader(abstractClass);
            ClassNode abstractNode = new ClassNode();
            //get methods for the abstract by defining an abstract node
            reader.accept(abstractNode, ClassReader.EXPAND_FRAMES);
            List<MethodNode> abstractMethods = abstractNode.methods;
            List<String> requiredAbstractMethods = new ArrayList<>();
            requiredAbstractMethods.add("stepIfImplDifferBySubclassM1");
            requiredAbstractMethods.add("stepIfImplDifferBySubclassM2");
            requiredAbstractMethods.add("runAlgorithm");
            requiredAbstractMethods.add("stepIfImplCommonToAllSubclass");
            requiredAbstractMethods.add("hookMethod");

            Set<String> abstractMethodNames = new HashSet<>();
            for(MethodNode method: abstractMethods){
                abstractMethodNames.add(method.name);
            }


            if(!classMethodNames.containsAll(requiredMethods)){

                return "Does not implement template pattern.";
            }
            if(!abstractMethodNames.containsAll(requiredAbstractMethods)){
                return "Does not implement template pattern";
            }


            return "Correctly implements Template pattern";
        }

        private static String OCPCheck(ClassNode classNode){
            int access = classNode.access;
            List<MethodNode> classMethods = classNode.methods;
            for(MethodNode method: classMethods){
                if((method.access & Opcodes.ACC_FINAL) != 0){
                    return "Methods are final, so not open for extension. Potential violation of OCP.";
                }
            }

//            for (FieldNode field : classNode.fields) {
//                if ((field.access & (Opcodes.ACC_FINAL)) != 0) {
//                    return "Methods are final, not open for mod
//                }
//            }
            if ((access & Opcodes.ACC_FINAL) != 0) {
                return "Class is final, so not open for extension. Potential violation of OCP.";
            }
           return "OCP is held up.";
        }

    }


