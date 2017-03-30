import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.lang.StringBuilder;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Vector;

/*
I declare that the attached assignment is my own work in accordance
with Seneca Academic Policy. No part of this assignment has been
copied manually or electronically from any other source (including web
sites) or distributed to other students.
Name : Nathan Lo
Student ID: 013491154
*/
public class NathanA2 {
    private JPanel mainPanel;
    private JComboBox characterLimitComboBox;
    private JCheckBox sequenceInGroupsOfTenCheckBox;
    private JRadioButton uppercaseRadioButton;
    private JRadioButton lowercaseRadioButton;
    private JButton processSequenceButton;
    private JButton resetButton;
    private JTextArea metricsTextArea;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTabbedPane tabbedPane1;
    private JButton confirmQueryButton;
    private JPanel buttonPanel;
    private JScrollPane tableScrollPane;
    private JFrame resetFrame; //JFrame to generate reset dialogue panel
    private JFrame errorFrame; //JFrame to generate error dialogue panel
    private ButtonGroup tableNameGroup = new ButtonGroup();
    private JRadioButton tableNameButtons = new JRadioButton();
    String databaseURI = "jdbc:sqlite:C:\\Users\\Nathan Lo\\Desktop\\NathanA3.db";
    String username = "";
    String password = "";
    String displayTables = "select name from sqlite_master where type = 'table';"; //fix all tables calling


    public NathanA2() {
        //panel default value initialization
        mainPanel.setPreferredSize(new Dimension(1200, 600));
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setLineWrap(true);
        metricsTextArea.setWrapStyleWord(true);
        metricsTextArea.setLineWrap(true);
        characterLimitComboBox.setSelectedIndex(2);
        lowercaseRadioButton.setSelected(true);
        sequenceInGroupsOfTenCheckBox.setSelected(false);
        processSequenceButton.addActionListener(new processButtonClicked());

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        boolean connected = true;

        //reset button action listener and dialogue generator
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Yes",
                        "No",
                        "Cancel"};
                int n = JOptionPane.showOptionDialog(resetFrame,
                        "Are you sure you want to reset?",
                        "Confirm reset",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[2]);
                if (n == JOptionPane.YES_OPTION) {
                    inputTextArea.setText("");
                    outputTextArea.setText("");
                    metricsTextArea.setText("");
                    characterLimitComboBox.setSelectedIndex(2);
                    lowercaseRadioButton.setSelected(true);
                    sequenceInGroupsOfTenCheckBox.setSelected(false);
                }
            }
        });

        //query selected radioButtons
        try {
            connection = DriverManager.getConnection(databaseURI, username, password);
            statement = connection.createStatement();
            if (connected == true) {
                try {
                    resultSet = statement.executeQuery(displayTables);
                    while (resultSet.next()) {
                        String tableName = resultSet.getString(1);
                        tableNameButtons = new JRadioButton(tableName);
                        tableNameGroup.add(tableNameButtons);
                        buttonPanel.add(tableNameButtons);
                    }
                } catch (SQLException l) {
                    l.printStackTrace();
                }
                //confirm button to display table on JScrollPanel
                confirmQueryButton.addActionListener(new ActionListener() {
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet resultSet = null;
                    boolean connected = false;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            //connect to database
                            connection = DriverManager.getConnection(databaseURI, username, password);
                            //create a Statement that we will use to interact with the database
                            statement = connection.createStatement();
                            String tableName = getSelectedButtonText(tableNameGroup);
                            String queryTableName = "select * from " + tableName + ";";
                            resultSet = statement.executeQuery(queryTableName);
                            connected = true;
                            JTable table = new JTable(buildTableModel(resultSet));
                            tableScrollPane.getViewport().add(table);
                        } catch (SQLException l) {
                            l.printStackTrace();
                            connected = false;
                        }
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //create a Statement that we will use to interact with the database

    }

    //gets selected table names for naming radio buttons in GUI
    public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }

    //function to add space or new line at specific number of characters
    public static String conversionTextAppearance(String text, String insert, int spacing) {
        StringBuilder builder = new StringBuilder(text.length() + insert.length() * (text.length() / spacing) + 1);
        int index = 0;
        String prefix = "";
        while (index < text.length()) {
            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index, Math.min(index + spacing, text.length())));
            index += spacing;
        }
        return builder.toString();
    }

    //character counting function
    public static int baseCounts(String sequence, char nucleotide) {
        int count = 0;
        for (int i = 0; i < sequence.length(); i++) {
            if (sequence.charAt(i) == nucleotide) {
                count++;
            }
        }
        return count;
    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        mainPanel.add(tabbedPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("A2", panel1);
        metricsTextArea = new JTextArea();
        metricsTextArea.setText("");
        panel1.add(metricsTextArea, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 3, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        inputTextArea = new JTextArea();
        inputTextArea.setText("");
        scrollPane1.setViewportView(inputTextArea);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel1.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        outputTextArea = new JTextArea();
        scrollPane2.setViewportView(outputTextArea);
        final JLabel label1 = new JLabel();
        label1.setText("String Metrics");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Post-Processed String");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        processSequenceButton = new JButton();
        processSequenceButton.setText("Process Sequence");
        panel1.add(processSequenceButton, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resetButton = new JButton();
        resetButton.setText("Reset Form");
        panel1.add(resetButton, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(287, 32), null, 0, false));
        characterLimitComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("40");
        defaultComboBoxModel1.addElement("50");
        defaultComboBoxModel1.addElement("60");
        defaultComboBoxModel1.addElement("70");
        characterLimitComboBox.setModel(defaultComboBoxModel1);
        panel1.add(characterLimitComboBox, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sequenceInGroupsOfTenCheckBox = new JCheckBox();
        sequenceInGroupsOfTenCheckBox.setText("Sequence in Groups of 10");
        panel1.add(sequenceInGroupsOfTenCheckBox, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(287, 24), null, 0, false));
        uppercaseRadioButton = new JRadioButton();
        uppercaseRadioButton.setText("Uppercase");
        panel1.add(uppercaseRadioButton, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lowercaseRadioButton = new JRadioButton();
        lowercaseRadioButton.setText("Lowercase");
        panel1.add(lowercaseRadioButton, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(287, 28), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Insert String Here:");
        panel1.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("A3", panel2);
        tableScrollPane = new JScrollPane();
        panel2.add(tableScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        confirmQueryButton = new JButton();
        confirmQueryButton.setText("Confirm Query");
        panel2.add(confirmQueryButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        panel2.add(buttonPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(lowercaseRadioButton);
        buttonGroup.add(uppercaseRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    //action listener for process button
    private class processButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String conversionText = inputTextArea.getText();
            Integer comboBoxSelection = Integer.parseInt((characterLimitComboBox.getSelectedItem()).toString());
            Integer sequenceLength = conversionText.length();
            Integer adenineTotal, thymineTotal, cytosineTotal, guanineTotal;
            Double adeninePercent, thyminePercent, cytosinePercent, guaninePercent;
            //regex to determine if nucleotide letters entered only
            if (!conversionText.matches("^[CAGTcagt]+$")) {
                JOptionPane.showMessageDialog(errorFrame,
                        "Input allows the characters A, C, T, G, a, c, t, or g only.");
            } else {
                //formatting metrics for metricsTextArea
                NumberFormat percentFormat = NumberFormat.getPercentInstance();
                percentFormat.setMinimumFractionDigits(2);
                adenineTotal = baseCounts(conversionText, 'A') + baseCounts(conversionText, 'a');
                thymineTotal = baseCounts(conversionText, 'T') + baseCounts(conversionText, 't');
                ;
                cytosineTotal = baseCounts(conversionText, 'C') + baseCounts(conversionText, 'c');
                guanineTotal = baseCounts(conversionText, 'G') + baseCounts(conversionText, 'g');
                adeninePercent = Double.valueOf(adenineTotal) / Double.valueOf(sequenceLength);
                thyminePercent = Double.valueOf(thymineTotal) / Double.valueOf(sequenceLength);
                guaninePercent = Double.valueOf(guanineTotal) / Double.valueOf(sequenceLength);
                cytosinePercent = Double.valueOf(cytosineTotal) / Double.valueOf(sequenceLength);
                //determining which radio button was selected
                if (uppercaseRadioButton.isSelected()) {
                    conversionText = conversionText.toUpperCase();
                } else if (lowercaseRadioButton.isSelected()) {
                    conversionText = conversionText.toLowerCase();
                }
                //determining if sequenceInGroupsOfTenCheckbox is selected or not
                if (sequenceInGroupsOfTenCheckBox.isSelected()) {
                    conversionText = conversionTextAppearance(conversionText, " ", 10);
                    //compensating for added space characters in character row limit
                    conversionText = conversionTextAppearance(conversionText, "\n", comboBoxSelection + (comboBoxSelection / 10));
                } else {
                    conversionText = conversionTextAppearance(conversionText, "\n", comboBoxSelection);
                }
                //adding text to metricsTextArea
                metricsTextArea.setText("Total Sequence Length: " + sequenceLength.toString() + "\n"
                        + "Total Adenine Counted: " + adenineTotal.toString() + "\n"
                        + "Adenine Percentage: " + percentFormat.format(adeninePercent) + "\n"
                        + "Total Thymine Counted: " + thymineTotal.toString() + "\n"
                        + "Thymine Percentage: " + percentFormat.format(thyminePercent) + "\n"
                        + "Total Cytosine Counted: " + cytosineTotal.toString() + "\n"
                        + "Cytosine Percentage: " + percentFormat.format(cytosinePercent) + "\n"
                        + "Total Guanine Counted: " + guanineTotal.toString() + "\n"
                        + "Guanine Percentage: " + percentFormat.format(guaninePercent) + "\n");
                outputTextArea.setText(conversionText);
            }
        }
    }

    //main function to run GUI
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException l) {
            l.printStackTrace();
            System.out.println("Did you forget to include the JAR file in the project?");
        } finally {
            System.out.println("Loading completed...");
        }
        JFrame frame = new JFrame("NathanA2");
        frame.setContentPane(new NathanA2().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

