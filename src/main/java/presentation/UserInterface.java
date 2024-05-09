package presentation;

import java.io.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import datasource.ASMAdapter;
import datasource.FileOutput;
import domain.Result;
import domain.checks.*;
import domain.model.ClassModel;

/**
 * Class acts as a UI template
 * Handles domain access, implmenters are reponsible for display
 */
public abstract class UserInterface {
    private String resultPath = "files/output" + ".csv";
    private static final String keyPath = "files/key" + ".txt";
    private static final HashMap<Integer, LintCheck> CHECKS;
    static{
        CHECKS = new HashMap<Integer, LintCheck>();
        CHECKS.put(2, new TemplateCheck());
        CHECKS.put(3, new OCPCheck());
        CHECKS.put(4, new InterfaceCheck());
        CHECKS.put(5, new CouplingCheck());
        CHECKS.put(6, new NamingConvCheck());
        CHECKS.put(7, new ObserverPatternCheck());
        CHECKS.put(8, new UnusedVariableCheck());
        CHECKS.put(9, new SingletonCheck());
        CHECKS.put(10, new PrincipleLeastKnowledgeCheck());
        CHECKS.put(11, new PrivateVarCheck());
        CHECKS.put(12, new StrategyCheck());
        CHECKS.put(13, new AccessModifer());

        File keyFile = new File(keyPath);
        try {
            Scanner scanner = new Scanner(keyFile);
            String line = scanner.nextLine();
            if (line.startsWith("sk-")) {
                CHECKS.put(14, new ChatGPTCouplingCheck(line));
                CHECKS.put(15, new ChatGptObserverCheck(line));
                CHECKS.put(16, new ChatGPTSingletonCheck(line));
            }
        } catch (FileNotFoundException e) {
            // We can run without keyfile existing
            System.err.println("No ChatGPT key file found!");
        }
    }
    /**
     * Main Function for running a linting UI
     */
    public void runLinter() throws IOException {

        startDisplay();


        String filePath = getFilePath();
        List<ClassModel> classes = getClassesFromFile(filePath);

        displayChecks();

        String checkNumber = getCheckToRun();
        List<Integer> checkCommands = convertInput(checkNumber);

        List<Result> results = runChecks(checkCommands, classes);


        displayResults(results);

        FileOutput.saveResults(results, resultPath);

        close();

    }

    /**
     * Gets a list of checks to run
     * @return spaced
     */
    abstract String getCheckToRun();

    /**
     * Displays Checks
     * Implemenation Specific
     */
    abstract void displayChecks();

    /**
     * Starts Display
     * Implementaion Specific
     */
    abstract void startDisplay();

    /**
     * Gets a filepath from the user
     * Implementation Specific
     * @return filepath
     */
    abstract String getFilePath();

    /**
     * Displays the results of the linting checks
     * @param results
     */
    abstract void displayResults(List<Result> results);

    /**
     * Runs after displaying results
     * Use to close scanners or I/O and clean up after checks
     */
    abstract void close();

    /**
     * Runs the coressponding cheks
     * @param checkCommands
     * @return
     */
    public static List<Result> runChecks(List<Integer> checkCommands, List<ClassModel> classes) {
        if(checkCommands.get(0) == 1) {
            checkCommands = new ArrayList<>();
            checkCommands.addAll(CHECKS.keySet());
        }

        List<Result> results = new ArrayList<>();
        for(Integer i : checkCommands) {

            results.addAll(CHECKS.get(i).runLintCheck(classes));
        }

        return results;
    }

    /**
     * Gets classes in a directory
     * @param filePath
     * @return
     */
    public static List<ClassModel> getClassesFromFile(String filePath){
        return new ASMAdapter().parseASM(filePath);
    }

    /**
     * Converts an input string to a list of Integers
     * Restriction: Numbers in string must be space-separated
     * @param s
     * @return
     */
    public static List<Integer> convertInput(String s) throws IOException {
        try {
            String[] keys = s.split("\\s+");
            return Arrays.stream(keys).map(Integer::parseInt).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.out.println("Illegal input");
        }
        return null;
    }
    /**
     * Gets a list of LintChecks for displaying
     * @return HashMap of Checks
     */
    protected static List<String> getChecks() {
        List<String> checks = new ArrayList<>();
        checks.add("1 = Run all Checks");
        for(int key : CHECKS.keySet()){
            checks.add(Integer.toString(key) + " = " + CHECKS.get(key).getClass().getSimpleName());
        }
        return checks;
    }
}