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

        // Display the input string back to the user
        System.out.println("You entered: " + filePath);


//        // Prompt the user to input a string
//        System.out.print("Please enter the check you want to perform: ");
//
//        // Read the input string provided by the user
//        String checkString = scanner.nextLine();
//
//        // Display the input string back to the user
//        System.out.println("You entered: " + checkString);

        HashMap<Integer, LintCheck> checks = new HashMap<Integer, LintCheck>();
        LintCheck template = new TemplateCheck();
        checks.put(1, template);

        System.out.println("1 = " + checks.get(1).toString());
        System.out.print("Please enter the number of the check you want to perform: ");
        String checkNumber = scanner.nextLine();
        System.out.println("You entered: " + checkNumber);

        scanner.close();

    }
}
