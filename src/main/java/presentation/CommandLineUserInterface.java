package presentation;

import datasource.ASMAdapter;
import domain.checks.*;
import domain.model.ClassModel;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CommandLineUserInterface {

    static void runLinter() throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to input a string
        System.out.print("Please enter the file path to package: ");

        // Read the input string provided by the user
        String filePath = scanner.nextLine();

        List<ClassModel> classes = new ArrayList<>();
        classes = ASMAdapter.parseASM(filePath);

        // Show user all available lint checks
        HashMap<Integer, LintCheck> checks = new HashMap<Integer, LintCheck>();
        checks.put(2, new TemplateCheck());
        checks.put(3, new OCPCheck());
        checks.put(4, new InterfaceCheck());
        checks.put(5, new CouplingCheck());
        checks.put(6, new NamingConvCheck());
        checks.put(7, new ObserverPatternCheck());
        checks.put(8, new ProgramToInterfaceCheck());
        checks.put(9, new UnusedVariableCheck());
        checks.put(10, new SingletonCheck());
        checks.put(11, new PrincipleLeastKnowledgeCheck());
        checks.put(12, new PrivateVarCheck());
        checks.put(13, new StrategyCheck());


        System.out.println("\n\nBelow are the numbers associated with each lint check\n" +
                "------------------------------------------------------------");
        System.out.println("1 = Run All Checks");
        System.out.println(checks + "\n");

        System.out.print("Please enter the numbers of the checks you want to perform (separate with spaces): ");
        String checkNumber = scanner.nextLine();
        List<Integer> checkCommands = convertInput(checkNumber);
        System.out.println(checkCommands);
        if(checkCommands.get(0) == 1) {
            checkCommands = new ArrayList<>();
            checkCommands.addAll(checks.keySet());
        }
        System.out.println(classes);
//C:\Users\chungea\IdeaProjects\project-202420-s3-team15-202420\src\test\resources\CouplingTests
        System.out.println(checks);
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
    private static List<Integer> convertInput(String s) throws IOException {
        try {
            return Arrays.stream(s.split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.out.println("Illegal input. Restarting...");
            runLinter();
        }
        return null;
    }
}
