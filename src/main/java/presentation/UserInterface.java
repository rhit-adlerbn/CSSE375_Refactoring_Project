package presentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import datasource.ASMAdapter;
import datasource.FileOutput;
import domain.Result;
import domain.checks.CouplingCheck;
import domain.checks.InterfaceCheck;
import domain.checks.LintCheck;
import domain.checks.NamingConvCheck;
import domain.checks.OCPCheck;
import domain.checks.ObserverPatternCheck;
import domain.checks.PrincipleLeastKnowledgeCheck;
import domain.checks.PrivateVarCheck;
import domain.checks.SingletonCheck;
import domain.checks.StrategyCheck;
import domain.checks.TemplateCheck;
import domain.checks.UnusedVariableCheck;
import domain.model.ClassModel;

/**
 * Class acts as a UI template
 * Handles domain access, implmenters are reponsible for display
 */
public abstract class UserInterface {
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

        List<Result> results = runChecks(checkCommands,classes);
        
        
        displayResults(results);

        FileOutput.saveResults(results);

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
    private List<Result> runChecks(List<Integer> checkCommands, List<ClassModel> classes) {
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
    private List<ClassModel> getClassesFromFile(String filePath){
        
        return new ASMAdapter().parseASM(filePath);
    }

     /**
     * Converts an input string to a list of Integers
     * Restriction: Numbers in string must be space-separated
     * @param s
     * @return
     */
    private List<Integer> convertInput(String s) throws IOException {
        try {
            String[] keys = s.split("\\s+");
            return Arrays.stream(keys).map(Integer::parseInt).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.out.println("Illegal input. Restarting...");
            runLinter();
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
