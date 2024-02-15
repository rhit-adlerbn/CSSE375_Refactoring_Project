package presentation;

import datasource.ASMAdapter;
import domain.ClassModel;
import domain.LintCheck;
import domain.TemplateCheck;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to input a string
        System.out.print("Please enter the file path to package: ");

        // Read the input string provided by the user
        String filePath = scanner.nextLine();

        // TODO: generate .class files from given package (need to merge Adapter_and_Model branch)

        List<ClassModel> classes = new ArrayList<>();
        classes = ASMAdapter.parseASM(filePath);

        // Show user all available lint checks
        HashMap<Integer, LintCheck> checks = new HashMap<Integer, LintCheck>();
        checks.put(1, new TemplateCheck());
        // TODO: add all checks to hashmap

        System.out.println("\n\nBelow are the numbers associated with each lint check\n" +
                "------------------------------------------------------------");
        System.out.println(checks + "\n");

        System.out.print("Please enter the numbers of the checks you want to perform (separate with spaces): ");
        String checkNumber = scanner.nextLine();
        List<Integer> checkCommands = convertInput(checkNumber);

        // TODO: add option to run all checks

        for(Integer i : checkCommands) {
            List<String> output = checks.get(i).runLintCheck(classes);
            for(String s : output) System.out.println(s);
        }




        scanner.close();

    }

    /**
     * Converts an input string to a list of Integers
     * Restriction: Numbers in string must be space-separated
     * @param s
     * @return
     */
    private static List<Integer> convertInput(String s) {
        return Arrays.stream(s.split("\\s+"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
