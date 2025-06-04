
import javax.swing.*;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class Main extends JFrame implements ActionListener {
    private JTextField display;
    private double num1, num2, result;
    private String operator;
    private boolean startNewNumber;
    private double memory;
    private DecimalFormat df;

    public Main() {
        df = new DecimalFormat("#.##########");
        initializeCalculator();
    }

    private void initializeCalculator() {
        setTitle("Java Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize variables
        num1 = 0;
        num2 = 0;
        result = 0;
        operator = "";
        startNewNumber = true;
        memory = 0;

        // Create display
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createLoweredBevelBorder());

        // Create button panel
        JPanel buttonPanel = createButtonPanel();

        // Layout
        setLayout(new BorderLayout(10, 10));
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Add padding
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 4, 5, 5));

        // Memory buttons row
        panel.add(createButton("MC", Color.LIGHT_GRAY));
        panel.add(createButton("MR", Color.LIGHT_GRAY));
        panel.add(createButton("M+", Color.LIGHT_GRAY));
        panel.add(createButton("M-", Color.LIGHT_GRAY));

        // First row
        panel.add(createButton("C", Color.RED));
        panel.add(createButton("CE", Color.ORANGE));
        panel.add(createButton("±", Color.LIGHT_GRAY));
        panel.add(createButton("√", Color.LIGHT_GRAY));

        // Second row
        panel.add(createButton("7", Color.WHITE));
        panel.add(createButton("8", Color.WHITE));
        panel.add(createButton("9", Color.WHITE));
        panel.add(createButton("÷", Color.CYAN));

        // Third row
        panel.add(createButton("4", Color.WHITE));
        panel.add(createButton("5", Color.WHITE));
        panel.add(createButton("6", Color.WHITE));
        panel.add(createButton("×", Color.CYAN));

        // Fourth row
        panel.add(createButton("1", Color.WHITE));
        panel.add(createButton("2", Color.WHITE));
        panel.add(createButton("3", Color.WHITE));
        panel.add(createButton("-", Color.CYAN));

        // Fifth row
        panel.add(createButton("0", Color.WHITE));
        panel.add(createButton(".", Color.WHITE));
        panel.add(createButton("=", Color.GREEN));
        panel.add(createButton("+", Color.CYAN));

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(color);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.addActionListener(this);
        button.setFocusPainted(false);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        try {
            switch (command) {
                case "0": case "1": case "2": case "3": case "4":
                case "5": case "6": case "7": case "8": case "9":
                    handleNumber(command);
                    break;
                case ".":
                    handleDecimal();
                    break;
                case "+": case "-": case "×": case "÷":
                    handleOperator(command);
                    break;
                case "=":
                    handleEquals();
                    break;
                case "C":
                    handleClear();
                    break;
                case "CE":
                    handleClearEntry();
                    break;
                case "±":
                    handlePlusMinus();
                    break;
                case "√":
                    handleSquareRoot();
                    break;
                case "MC":
                    memoryClear();
                    break;
                case "MR":
                    memoryRecall();
                    break;
                case "M+":
                    memoryAdd();
                    break;
                case "M-":
                    memorySubtract();
                    break;
            }
        } catch (Exception ex) {
            display.setText("Error");
            startNewNumber = true;
        }
    }

    private void handleNumber(String number) {
        if (startNewNumber) {
            display.setText(number);
            startNewNumber = false;
        } else {
            String currentText = display.getText();
            if (currentText.length() < 15) { // Limit display length
                display.setText(currentText + number);
            }
        }
    }

    private void handleDecimal() {
        String currentText = display.getText();
        if (startNewNumber) {
            display.setText("0.");
            startNewNumber = false;
        } else if (!currentText.contains(".")) {
            display.setText(currentText + ".");
        }
    }

    private void handleOperator(String op) {
        if (!startNewNumber) {
            if (!operator.isEmpty()) {
                handleEquals();
            } else {
                num1 = Double.parseDouble(display.getText());
            }
        }
        operator = op;
        startNewNumber = true;
    }

    private void handleEquals() {
        if (!operator.isEmpty() && !startNewNumber) {
            num2 = Double.parseDouble(display.getText());
            
            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "×":
                    result = num1 * num2;
                    break;
                case "÷":
                    if (num2 != 0) {
                        result = num1 / num2;
                    } else {
                        display.setText("Cannot divide by zero");
                        startNewNumber = true;
                        operator = "";
                        return;
                    }
                    break;
            }
            
            display.setText(df.format(result));
            num1 = result;
            operator = "";
            startNewNumber = true;
        }
    }

    private void handleClear() {
        display.setText("0");
        num1 = 0;
        num2 = 0;
        result = 0;
        operator = "";
        startNewNumber = true;
    }

    private void handleClearEntry() {
        display.setText("0");
        startNewNumber = true;
    }

    private void handlePlusMinus() {
        double current = Double.parseDouble(display.getText());
        current = -current;
        display.setText(df.format(current));
    }

    private void handleSquareRoot() {
        double current = Double.parseDouble(display.getText());
        if (current >= 0) {
            result = Math.sqrt(current);
            display.setText(df.format(result));
            startNewNumber = true;
        } else {
            display.setText("Invalid input");
            startNewNumber = true;
        }
    }

    private void memoryClear() {
        memory = 0;
    }

    private void memoryRecall() {
        display.setText(df.format(memory));
        startNewNumber = true;
    }

    private void memoryAdd() {
        memory += Double.parseDouble(display.getText());
    }

    private void memorySubtract() {
        memory -= Double.parseDouble(display.getText());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new Main().setVisible(true);
        });
    }
}
