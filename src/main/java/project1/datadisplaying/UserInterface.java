package project1.datadisplaying;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class UserInterface {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField inputField;
    private JButton submitButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserInterface window = new UserInterface();
            window.frame.setVisible(true);
        });
    }

    public UserInterface() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Application Menu");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setMargin(new Insets(20, 20, 20, 20));
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        inputField = new JTextField(10);
        inputPanel.add(inputField);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> processInput());

        inputPanel.add(submitButton);

        panel.add(inputPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);

        // Initial message
        updateTextArea("""
            Nice to meet you. What are you thinking of?
            1. Getting Spiderum users' profile in CSV format
            2. Viewing Spiderum users' data on Airtable
            3. Viewing gender statistical chart from collected data
            4. Viewing social account statistic from collected data
            5. Viewing score distribution bar chart
            6. Exit
            """);

        inputField.requestFocus();
    }

    private void processInput() {
        String option = inputField.getText().trim();

        switch (option) {
            case "1":
                updateTextArea("--Getting Spiderum users' profile in CSV format--\n");
                displayFileContent("SInfor.csv");
                //openFile("SInfor.csv");
                break;
            case "2":
                openWebpage("https://airtable.com/app6ftijp1MtmwT5O/shrLEr0ARNpnapAr7");
                break;
            case "3":
                openFile("gender_statistics_chart.png");
                break;
            case "4":
                openFile("social_account_status.png");
                break;
            case "5":
                openFile("score_distribution.png");
                break;
            case "6":
                updateTextArea("Good bye! Have a nice day\n");
                System.exit(0);
                break;
            default:
                updateTextArea("""
                        !!!WARNING: INVALID INPUT!
                        "PLEASE ENTER AN INTEGER FROM 1 TO 6!
                        """);
        }

        inputField.setText("");
        inputField.requestFocus();
    }

    private void updateTextArea(String message) {
        textArea.append(message);
    }

    private void openFile(String fileName) {
        try {
            Desktop.getDesktop().open(new java.io.File(fileName));
        } catch (IOException e) {
            updateTextArea("Error: File not found\n");
        }
    }

    private void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            updateTextArea("Error: Could not open webpage\n");
        }
    }

    private void displayFileContent(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException e) {
            updateTextArea("Error: Failed to read file " + fileName + "\n");
        }
    }
}




