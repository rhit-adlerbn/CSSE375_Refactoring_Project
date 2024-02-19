package presentation;

import datasource.ASMAdapter;
import domain.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GraphicsUserInterface {

    private static final int frameWidth = 2100;
    private static final int frameHeight = 1000;
    static void runGraphics(){
        JFrame frame = new JFrame();
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.setTitle("Linter");
        JButton runLinter = new JButton("Run Linter");
        JButton useCommandLine = new JButton("Use Command Line");
        JTextField filePath = new JTextField("Enter File Path");
        JTextField runChecks = new JTextField("Enter number for checks");

        JPanel buttonPanel = new JPanel();
        JPanel infoPanel = new JPanel();
        JPanel resultsPanel = new JPanel();
        JPanel results2Panel = new JPanel();

        buttonPanel.add(runLinter);
        buttonPanel.add(useCommandLine);

        frame.add(buttonPanel, BorderLayout.SOUTH);

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


        JLabel info = new JLabel("\n\nThese are the numbers associated with each lint check\n");
        JLabel runAll = new JLabel("1 = Run All Checks");


        StringBuilder optionsBuilder = new StringBuilder("<html>Available lint check options:<br>");
        for (Integer key : checks.keySet()) {
            optionsBuilder.append(key).append(": ").append(checks.get(key).getClass().getSimpleName()).append("<br>");
        }
        optionsBuilder.append("</html>");
        JLabel checkInfo = new JLabel(optionsBuilder.toString());
        JLabel enter = new JLabel("Please enter the numbers of the checks you want to perform (separate with spaces): ");
        infoPanel.add(info);
        infoPanel.add(runAll);
        infoPanel.add(checkInfo);
        infoPanel.add(enter);
        frame.add(infoPanel, BorderLayout.NORTH);
        resultsPanel.add(filePath);
        resultsPanel.add(runChecks);
        frame.add(resultsPanel, BorderLayout.WEST);
        frame.add(results2Panel, BorderLayout.CENTER);

        useCommandLine.addActionListener(e ->{
            frame.dispose();
            try {
                CommandLineUserInterface.runLinter();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        runLinter.addActionListener(e ->{
            results2Panel.removeAll();
            String filePathText = filePath.getText();
            String numbers = runChecks.getText();

            List<ClassModel> classes = new ArrayList<>();
            classes = ASMAdapter.parseASM(filePathText);

            List<Integer> checkCommands = null;
            try {
                checkCommands = convertInput(numbers);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            if(checkCommands.get(0) == 1) {
                checkCommands = new ArrayList<>();
                checkCommands.addAll(checks.keySet());
            }

            for(Integer i : checkCommands) {
                List<String> output = checks.get(i).runLintCheck(classes);
                for(String s : output){
//                    System.out.println(s);
                    JLabel result = new JLabel(s);
                    results2Panel.add(result);
                }

            }

            frame.repaint();
            frame.revalidate();
            frame.validate();
        });

        frame.setVisible(true);
    }

    private static List<Integer> convertInput(String s) throws IOException {
        try {
            return Arrays.stream(s.split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.out.println("Illegal input. Restarting...");
            runGraphics();
        }
        return null;
    }
}
