package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import domain.Result;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private static File selectedFile = null;
    private static List<String> checksToRun;

    private static JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel();

        // Set up outer panel
        resultsPanel.setLayout(new BorderLayout());

        // Set up inner panel
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        resultsPanel.add(innerPanel, BorderLayout.CENTER);

        // Set up text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridBagLayout());
        innerPanel.add(textPanel);

        // Set up text panel constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        // Set up text panel
        resultText = new JTextArea(0, 0);
        resultText.setEditable(false);
        resultText.setLineWrap(true);

        // Set up scroll pane
        scrollPane = new JScrollPane(resultText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textPanel.add(scrollPane, gbc);

        // Add button panel
        innerPanel.add(createButtonPanel());

        return resultsPanel;
    }

    private static JPanel createInputPanel() {
        // Create container panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        // Create top file selector panel
        JPanel fileChooserPanel = new JPanel();
        GridBagLayout fileChooserLayout = new GridBagLayout();
        fileChooserPanel.setLayout(fileChooserLayout);

        // Add the label
        JPanel labelPanel = new JPanel();
        labelPanel.add(new JLabel("Select a project to check"));
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.fill = GridBagConstraints.BOTH;
        fileChooserPanel.add(labelPanel, gbcCenter);

        // Add the button and accompanying text
        JPanel buttonPanel = getFileSelectorPanel();
        gbcCenter.gridy = 1;
        fileChooserPanel.add(buttonPanel, gbcCenter);

        // Add the panel to the container
        infoPanel.add(fileChooserPanel);

        // Create the bottom checkbox panel
        JPanel checksPanel = new JPanel();
        checksPanel.setLayout(new GridBagLayout());

        // Add the label
        JPanel labelPanel2 = new JPanel();
        labelPanel2.add(new JLabel("Select checks to run"));
        gbcCenter.gridy = 0;
        checksPanel.add(labelPanel2, gbcCenter);

        // Add the checkboxes
        JPanel checkboxPanel = getCheckboxPanel();
        gbcCenter.gridy = 1;
        checksPanel.add(checkboxPanel, gbcCenter);

        // Add the panel to the container
        infoPanel.add(checksPanel);
        return infoPanel;
    }

    /**
     *
     * @return a JPanel with a checkbox selection for the listed checks
     */
    private static JPanel getCheckboxPanel() {
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checksToRun = new ArrayList<>();
        for (int i=0; i<getChecks().size(); i++) {
            JCheckBox b1 = new JCheckBox(getChecks().get(i));
            int index = i;
            b1.addItemListener(
                    e -> {
                        String toModify = "" + (index + 1);
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            checksToRun.add(toModify);
                        } else {
                            checksToRun.remove(toModify);
                        }
                    }
            );
            checkboxPanel.add(b1);
        }
        return checkboxPanel;
    }

    /**
     *
     * @return a JPanel with an attached fileSelector and the accompanying text
     */
    private static JPanel getFileSelectorPanel() {
        JPanel buttonPanel = new JPanel();
        JLabel text = new JLabel("No project selected");
        JButton button = new JButton("Test");
        button.addActionListener(
                e -> {
                    JFileChooser fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnVal = fc.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fc.getSelectedFile();
                        text.setText(selectedFile.getAbsolutePath());
                    }
                }
        );
        buttonPanel.add(text);
        buttonPanel.add(button);
        return buttonPanel;
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
            if (selectedFile == null) {
                JOptionPane.showMessageDialog(mainPage, "Please select a project to check!",
                        "Linter", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (checksToRun.isEmpty()) {
                JOptionPane.showMessageDialog(mainPage, "Please select the checks to run!",
                        "Linter", JOptionPane.ERROR_MESSAGE);
                return;
            }
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

        mainPage.add(createInputPanel());
        mainPage.add(createResultsPanel());

        mainPage.setVisible(true);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    String getFilePath() {
        return selectedFile.getAbsolutePath();
    }
    @Override
    String getCheckToRun() {
        StringBuilder result = new StringBuilder();
        for (String s: checksToRun) {
            result.append(s).append(" ");
        }
        return result.toString();
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