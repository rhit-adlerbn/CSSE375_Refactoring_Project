package presentation;

import javax.swing.*;

import domain.Result;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GraphicsUserInterface extends UserInterface {
    private static final int FRAME_WIDTH = 2100;
    private static final int FRAME_HEIGHT = 500;
    private static final CountDownLatch latch = new CountDownLatch(1);
    private static JFrame mainPage;
    private static JTextField filePath;
    private static JTextField checkNums;
    private static JTextArea resultText;
    private static JScrollPane scrollPane;
  
    private static JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel();

        resultText = new JTextArea(0, 0);
        resultText.setEditable(false);
        resultText.setLineWrap(true);
        resultText.setPreferredSize(new Dimension(500, 500));

        scrollPane = new JScrollPane(resultText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        return resultsPanel;
    }
 
    private static JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JLabel info = new JLabel("Please enter the numbers of the checks you want to perform (separate with spaces): ");
        filePath = new JTextField("Enter File Path");
        checkNums = new JTextField("Enter numbers for checks");

        inputPanel.add(filePath);
        inputPanel.add(info);
        inputPanel.add(checkNums);

        return inputPanel;
    }

    private static JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();

        StringBuilder optionsBuilder = new StringBuilder("<html>Available lint check options:<br>");
        for (String option : getChecks()) {
            optionsBuilder.append(option).append("<br>");
        }
        optionsBuilder.append("</html>");
        JLabel checkInfo = new JLabel(optionsBuilder.toString());

        infoPanel.add(checkInfo, BorderLayout.EAST);

        return infoPanel;
    }

    private static JPanel createButtonPanel() {
        JButton runLinter = createRunButton();
        JButton useCommandLine = new JButton("Use Command Line");
        JPanel buttonPanel = new JPanel();

        buttonPanel.add(runLinter);
        buttonPanel.add(useCommandLine);

        useCommandLine.addActionListener(e -> {
            mainPage.dispose();
            try {
                CommandLineUserInterface UI = new CommandLineUserInterface();
                UI.runLinter();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return buttonPanel;
    }

    private static JButton createRunButton() {
        JButton runLinter = new JButton("Run Lint Checks");

        runLinter.addActionListener(e -> {
            resultText.removeAll();
            latch.countDown();
        });

        return runLinter;
    }

    private static JFrame createMainPage() {
        JFrame frame = new JFrame();

        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setTitle("Linter");
        frame.setLayout(new GridLayout(0, 2));

        return frame;
    }
    
    @Override
    void startDisplay() {
        mainPage = createMainPage();
       
        mainPage.add(createInfoPanel());
        mainPage.add(createResultsPanel());
        mainPage.add(createInputPanel());
        mainPage.add(createButtonPanel());

        mainPage.setVisible(true);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    String getFilePath() {
        return filePath.getText();
    }
    @Override
    String getCheckToRun() {
        return checkNums.getText();
    }
    @Override
    void displayResults(List<Result> results) {
        for (Result result : results) {
            resultText.append(result.toString() + "\n");
        }
        scrollPane.revalidate();
        scrollPane.validate();
        mainPage.repaint();
        mainPage.revalidate();
        mainPage.validate();
    }
    @Override
    void displayChecks() {  
        // Do nothing, JSwing handles display
    }
    @Override
    void close() {
        // Do nothing, user can close window
    }

}
