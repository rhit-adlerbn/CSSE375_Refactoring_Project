package presentation;

import domain.LintCheck;
import domain.TemplateCheck;

import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to input a string
        System.out.print("Please enter the file path to package: ");

        // Read the input string provided by the user
        String filePath = scanner.nextLine();

        // TODO: generate .class files from given package

        // Show user all available lint checks
        HashMap<Integer, LintCheck> checks = new HashMap<Integer, LintCheck>();
        checks.put(1, new TemplateCheck());
        System.out.println("\n\nBelow are the numbers associated with each lint check\n" +
                "------------------------------------------------------------");
        System.out.println(checks + "\n");

        System.out.print("Please enter the number of the check you want to perform: ");
        String checkNumber = scanner.nextLine();

        // TODO: run specified checks

        scanner.close();

    }
}
