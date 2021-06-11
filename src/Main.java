import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

import static java.lang.Integer.parseInt;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        JFrame jFrame = new JFrame();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Options");
        menuBar.add(fileMenu);
        jFrame.setJMenuBar(menuBar);

        JMenuItem createEmployeeItem = new JMenuItem("Apply new employee");
        JMenuItem dropEmployeeItem = new JMenuItem("Drop employee");
        JMenuItem saveChangesItem = new JMenuItem("Save changes");
        JMenuItem openItem = new JMenuItem("Open");

        EmployeeModel model = new EmployeeModel();
        JTable jTable = new JTable(model);
        jTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(jTable);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jTable.setAutoCreateRowSorter(true);

        createEmployeeItem.addActionListener(e -> {
            Employee employee = new Employee("", "", 0, 0, null);
            model.addEmployee(employee);
            model.fireTableDataChanged();
        });
        createEmployeeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));

        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.removeChoosableFileFilter(jFileChooser.getAcceptAllFileFilter());
        jFileChooser.setFileFilter(new FileNameExtensionFilter("Files ending in .myExtension", "myExtension"));

        jTable.getDefaultEditor(Integer.class).addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                int i = 0;
                JobPosition jobPosition;
                while (i < jTable.getRowCount()) {
                    try {
                        jobPosition = (JobPosition) jTable.getValueAt(i, 4);
                        if (Integer.parseInt(jTable.getValueAt(i, 3).toString()) < jobPosition.getMinSalary()) {
                            jTable.setValueAt(jobPosition.getMinSalary(), i, 3);
                            JOptionPane.showMessageDialog(null, "Wrong salary range at row:" + i, "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        if (Integer.parseInt(jTable.getValueAt(i, 3).toString()) > jobPosition.getMaxSalary()) {
                            jTable.setValueAt(jobPosition.getMaxSalary(), i, 3);
                            JOptionPane.showMessageDialog(null, "Wrong salary range at row:" + i, "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        jTable.revalidate();
                    } catch (NullPointerException nullPointerException) {
                        JOptionPane.showMessageDialog(null, "Select a job position at row: " + i, "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    i++;
                }
            }

            @Override
            public void editingCanceled(ChangeEvent e) {

            }
        });

        openItem.addActionListener(e -> {
            int code = jFileChooser.showOpenDialog(jFrame);
            if (code == JFileChooser.APPROVE_OPTION) {
                String path = jFileChooser.getSelectedFile().getAbsolutePath();
                File file = new File(path);
                if (jFileChooser.accept(file)) {
                    try {
                        Scanner scannerLineCounter = new Scanner(file);
                        int lineCounter = 0;
                        String employeeData;
                        while (scannerLineCounter.hasNextLine()) {
                            lineCounter++;
                            scannerLineCounter.nextLine();
                        }
                        scannerLineCounter.close();

                        if (model.getRowCount() != 0) {
                            while (model.getRowCount() > 0) {
                                model.removeEmployee(0);
                            }
                            model.fireTableDataChanged();
                        }

                        Scanner scannerRead = new Scanner(file);
                        for (int i = 0; i < lineCounter; i++) {
                            employeeData = scannerRead.nextLine();
                            String[] tmp = employeeData.split(";");
                            String name = tmp[0];
                            String surname = tmp[1];
                            int seniority = parseInt(tmp[2]);
                            int salary = parseInt(tmp[3]);
                            JobPosition position = switch (tmp[4]) {
                                case "IT_Manager" -> JobPosition.IT_Manager;
                                case "Accountant" -> JobPosition.Accountant;
                                case "CEO" -> JobPosition.CEO;
                                case "SQL_Developer" -> JobPosition.SQL_Developer;
                                case "Web_Designer" -> JobPosition.Web_Designer;
                                case "Web_Developer" -> JobPosition.Web_Developer;
                                case "Help_Desk_Worker" -> JobPosition.Help_Desk_Worker;
                                case "Software_Engineer" -> JobPosition.Software_Engineer;
                                case "Information_Security_Analyst" -> JobPosition.Information_Security_Analyst;
                                case "Network_Administrator" -> JobPosition.Network_Administrator;
                                case "Application_Developer" -> JobPosition.Application_Developer;
                                case "Computer_Programmer" -> JobPosition.Computer_Programmer;
                                default -> null;
                            };
                            if (position != null) {
                                if (salary < position.getMinSalary()) {
                                    salary = position.getMinSalary();
                                    JOptionPane.showMessageDialog(null, "Wrong salary range!", "Warning", JOptionPane.WARNING_MESSAGE);
                                    JOptionPane.showMessageDialog(null, "Salary range has been successfully changed!", "Information", JOptionPane.INFORMATION_MESSAGE);
                                }
                                if (salary > position.getMaxSalary()) {
                                    salary = position.getMaxSalary();
                                    JOptionPane.showMessageDialog(null, "Wrong salary range!", "Warning", JOptionPane.WARNING_MESSAGE);
                                    JOptionPane.showMessageDialog(null, "Salary range has been successfully changed!", "Information", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                            Employee employee = new Employee(name, surname, seniority, salary, position);
                            model.addEmployee(employee);
                        }
                        model.fireTableDataChanged();
                    } catch (FileNotFoundException fileNotFoundException) {
                        JOptionPane.showMessageDialog(null, "File does not exist!", "Warning", JOptionPane.WARNING_MESSAGE);
                    } catch (NumberFormatException numberFormatException) {
                        JOptionPane.showMessageDialog(null, "Wrong number format!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong extension!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));

        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dropEmployeeItem.addActionListener(e -> {
            int row = jTable.getSelectedRow();
            if (row != -1) {
                row = jTable.convertRowIndexToModel(row);
                model.removeEmployee(row);
            }
        });
        dropEmployeeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));

        saveChangesItem.addActionListener(e -> {
            int code = jFileChooser.showSaveDialog(jFrame);
            if (code == JFileChooser.APPROVE_OPTION) {
                String path = jFileChooser.getSelectedFile().getAbsolutePath();

                PrintWriter printWriter;
                try {
                    printWriter = new PrintWriter(path);
                    for (int i = 0; i < jTable.getRowCount(); i++) {
                        for (int j = 0; j < 5; j++) {
                            printWriter.print(model.getValueAt(i, j) + ";");
                        }
                        printWriter.print("\r\n");
                    }
                    printWriter.close();
                } catch (FileNotFoundException d) {
                    JOptionPane.showMessageDialog(null, "Wrong path!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        saveChangesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(createEmployeeItem);
        fileMenu.add(openItem);
        fileMenu.add(saveChangesItem);
        fileMenu.add(dropEmployeeItem);

        JComboBox<String> comboBox = new JComboBox<>();
        for (String s : Arrays.asList("IT_Manager", "Accountant", "CEO", "SQL_Developer", "Web_Designer", "Help_Desk_Worker", "Software_Engineer", "Information_Security_Analyst", "Network_Administrator", "Application_Developer", "Computer_Programmer")) {
            comboBox.addItem(s);
        }
        jTable.setDefaultEditor(Enum.class, new DefaultCellEditor(comboBox));

        jTable.getDefaultEditor(model.getColumnClass(4)).addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                int i = 0;
                JobPosition jobPosition;
                while (i < jTable.getRowCount()) {
                    try {
                        jobPosition = (JobPosition) jTable.getValueAt(i, 4);
                        if (Integer.parseInt(jTable.getValueAt(i, 3).toString()) < jobPosition.getMinSalary()) {
                            jTable.setValueAt(jobPosition.getMinSalary(), i, 3);
                            jTable.revalidate();
                            jTable.repaint();
                            JOptionPane.showMessageDialog(null, "Wrong salary range at row:" + i, "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        if (Integer.parseInt(jTable.getValueAt(i, 3).toString()) > jobPosition.getMaxSalary()) {
                            jTable.setValueAt(jobPosition.getMaxSalary(), i, 3);
                            jTable.revalidate();
                            jTable.repaint();
                            JOptionPane.showMessageDialog(null, "Wrong salary range at row:" + i, "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        jTable.revalidate();
                    } catch (NullPointerException nullPointerException) {
                        //JOptionPane.showMessageDialog(null, "Select a job position at row: " + i, "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    i++;
                }
            }

            @Override
            public void editingCanceled(ChangeEvent e) {

            }
        });

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel jPanelComponents = new JPanel(new BorderLayout());
        jPanel.add(jPanelComponents, BorderLayout.PAGE_END);

        JPanel jPanelFilter = new JPanel(new BorderLayout());
        jPanel.add(jPanelComponents, BorderLayout.PAGE_END);

        JLabel jLabelFilter = new JLabel("Filter Text:");
        jPanelFilter.add(jLabelFilter, BorderLayout.WEST);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable.getModel());
        jTable.setRowSorter(sorter);

        JTextField textFieldFilter = new JTextField();
        textFieldFilter.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                filterText(jTable, textFieldFilter, sorter);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                filterText(jTable, textFieldFilter, sorter);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                filterText(jTable, textFieldFilter, sorter);
            }
        });
        jPanelFilter.add(textFieldFilter, BorderLayout.CENTER);

        jFrame.setSize(500, 500);
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

        JPanel jPanelSearch = new JPanel(new BorderLayout());
        JButton searchButtonHigher = new JButton("Search higher salary");
        JButton searchButtonLower = new JButton("Search lower salary");
        NumberFormat numberFormat = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(numberFormat);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        JFormattedTextField searchTextField = new JFormattedTextField(formatter);

        searchButtonHigher.addActionListener(new ActionListener() {
            private boolean filter = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchTextField.getText().isEmpty()) {
                    filter = false;
                    JOptionPane.showMessageDialog(null, "Input is null!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                if (filter) {
                    sorter.setRowFilter(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, Integer.parseInt(searchTextField.getValue().toString()), 3));
                } else {
                    sorter.setRowFilter(null);
                }
                filter = !filter;
            }
        });
        searchButtonLower.addActionListener(new ActionListener() {
            private boolean filter = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchTextField.getText().isEmpty()) {
                    filter = false;
                    JOptionPane.showMessageDialog(null, "Input is null!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                if (filter) {
                    sorter.setRowFilter(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, Integer.parseInt(searchTextField.getValue().toString()), 3));
                } else {
                    sorter.setRowFilter(null);
                }
                filter = !filter;
            }
        });

        jPanelSearch.add(searchTextField, BorderLayout.CENTER);
        jPanelSearch.add(searchButtonHigher, BorderLayout.EAST);
        jPanelSearch.add(searchButtonLower, BorderLayout.WEST);

        jPanelComponents.add(jPanelFilter, BorderLayout.NORTH);
        jPanelComponents.add(jPanelSearch, BorderLayout.SOUTH);
    }

    public static void filterText(JTable jTable, JTextField filterText, TableRowSorter<TableModel> sorter) {
        String text = filterText.getText();
        try {
            if (null == text || text.length() < 1) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter(filterText.getText()));
            }
            jTable.clearSelection();
        } catch (PatternSyntaxException patternSyntaxException) {
            sorter.setRowFilter(null);
            filterText.setText(null);
            JOptionPane.showMessageDialog(null, "PatternSyntaxException", "Warning!", JOptionPane.WARNING_MESSAGE);
        }
    }
}