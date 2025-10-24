package sms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StudentPanel extends JPanel {
    private final JTextField idField = new JTextField(10);
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTable table = new JTable();

    public StudentPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Top form
        var form = new JPanel(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END;
        form.add(new JLabel("Student ID:"), gbc);
        gbc.gridy++; form.add(new JLabel("Name:"), gbc);
        gbc.gridy++; form.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_START;
        form.add(idField, gbc);
        gbc.gridy++; form.add(nameField, gbc);
        gbc.gridy++; form.add(emailField, gbc);

        var btnAdd = new JButton("Add Student");
        var btnUpdate = new JButton("Update Student");
        btnAdd.addActionListener(this::onAdd);
        btnUpdate.addActionListener(this::onUpdate);

        var btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.add(btnAdd); btns.add(btnUpdate);

        var top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btns, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Table in center
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // When selecting a row, populate fields
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idField.setText(table.getValueAt(row, 0).toString());
                nameField.setText(table.getValueAt(row, 1).toString());
                emailField.setText(table.getValueAt(row, 2).toString());
            }
        });
    }

    private void onAdd(ActionEvent e) {
        var id = idField.getText().trim();
        var name = nameField.getText().trim();
        var email = emailField.getText().trim();
        if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
            UiUtils.error(this, "All fields are required.");
            return;
        }
        boolean ok = DataStore.addStudent(new Student(id, name, email));
        if (!ok) {
            UiUtils.error(this, "Student ID already exists.");
            return;
        }
        UiUtils.info(this, "Student added.");
        refreshTable();
        clear();
    }

    private void onUpdate(ActionEvent e) {
        var id = idField.getText().trim();
        var name = nameField.getText().trim();
        var email = emailField.getText().trim();
        if (id.isEmpty()) {
            UiUtils.error(this, "Select an existing student from the table, or enter an ID.");
            return;
        }
        var s = new Student(id, name, email);
        boolean ok = DataStore.updateStudent(s);
        if (!ok) {
            UiUtils.error(this, "Student not found.");
            return;
        }
        UiUtils.info(this, "Student updated.");
        refreshTable();
        clear();
    }

    private void refreshTable() {
        DefaultTableModel model = UiUtils.makeStudentTableModel(DataStore.getStudents());
        table.setModel(model);
    }

    private void clear() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
    }
}
