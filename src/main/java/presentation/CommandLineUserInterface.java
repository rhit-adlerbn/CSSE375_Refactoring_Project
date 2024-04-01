package presentation;

import datasource.ASMAdapter;
import domain.Result;
import domain.checks.*;
import domain.model.ClassModel;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CommandLineUserInterface extends UserInterface{
    static Scanner scanner = new Scanner(System.in);
    
    @Override
        void startDisplay() {
            System.out.println("Welcome to the Lint Check Program \n");
        }

    @Override
    String getFilePath() {
        System.out.print("Please enter the file path to package: ");
        return scanner.nextLine();
    }

    @Override
    String getCheckToRun() {
        System.out.print("Please enter the numbers of the checks you want to perform (separate with spaces): ");
        return scanner.nextLine();
    }

    @Override
    void displayChecks() {
        System.out.println("\n\nBelow are the numbers associated with each lint check\n" +
                "------------------------------------------------------------");
        System.out.println("1 = Run All Checks");
        for(String option : getChecks()){
            System.out.println(option);
        };
    }

    @Override
    void displayResults(List<Result> results) {
        for(Result result : results) {
            System.out.println(result.toString());
        }
    }

    @Override
    void close() {
        scanner.close();
        System.exit(0);
    }
}
