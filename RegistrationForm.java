import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class RegistrationForm extends JFrame implements ActionListener {
    JTextField nameField, mobileField, addressField;
    JRadioButton male, female;
    JComboBox<String> dobDay, dobMonth, dobYear;
    JCheckBox terms;
    JButton submit, reset;
    JTextArea displayArea;

    public RegistrationForm() {
        setTitle("Registration Form");
        setSize(700, 500);
        setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 100, 20);
        nameField = new JTextField();
        nameField.setBounds(120, 20, 200, 20);
        add(nameLabel);
        add(nameField);

        JLabel mobileLabel = new JLabel("Mobile:");
        mobileLabel.setBounds(20, 60, 100, 20);
        mobileField = new JTextField();
        mobileField.setBounds(120, 60, 200, 20);
        add(mobileLabel);
        add(mobileField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(20, 100, 100, 20);
        male = new JRadioButton("Male");
        female = new JRadioButton("Female");
        male.setBounds(120, 100, 70, 20);
        female.setBounds(200, 100, 80, 20);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);
        add(genderLabel);
        add(male);
        add(female);

        JLabel dobLabel = new JLabel("DOB:");
        dobLabel.setBounds(20, 140, 100, 20);
        dobDay = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        dobMonth = new JComboBox<>(new String[]{"Jan", "Feb", "Mar", "Apr", "May"});
        dobYear = new JComboBox<>(new String[]{"2000", "2001", "2002", "2003"});
        dobDay.setBounds(120, 140, 50, 20);
        dobMonth.setBounds(180, 140, 60, 20);
        dobYear.setBounds(250, 140, 70, 20);
        add(dobLabel);
        add(dobDay);
        add(dobMonth);
        add(dobYear);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(20, 180, 100, 20);
        addressField = new JTextField();
        addressField.setBounds(120, 180, 200, 50);
        add(addressLabel);
        add(addressField);

        terms = new JCheckBox("Accept Terms And Conditions");
        terms.setBounds(50, 250, 300, 20);
        add(terms);

        submit = new JButton("Submit");
        submit.setBounds(80, 300, 100, 30);
        reset = new JButton("Reset");
        reset.setBounds(200, 300, 100, 30);
        add(submit);
        add(reset);

        displayArea = new JTextArea();
        displayArea.setBounds(350, 20, 300, 300);
        displayArea.setEditable(false);
        add(displayArea);

        submit.addActionListener(this);
        reset.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            saveData();
        } else if (e.getSource() == reset) {
            nameField.setText("");
            mobileField.setText("");
            addressField.setText("");
            male.setSelected(false);
            female.setSelected(false);
            terms.setSelected(false);
            displayArea.setText("");
        }
    }

    private void saveData() {
        String name = nameField.getText();
        String mobile = mobileField.getText();
        String gender = male.isSelected() ? "Male" : "Female";
        String dob = dobYear.getSelectedItem() + "-" + dobMonth.getSelectedItem() + "-" + dobDay.getSelectedItem();
        String address = addressField.getText();

        if (!terms.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please accept the terms and conditions.");
            return;
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration", "root", "");
            String query = "INSERT INTO registrations (name, mobile, gender, dob, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, mobile);
            pst.setString(3, gender);
            pst.setString(4, dob);
            pst.setString(5, address);
            pst.executeUpdate();
            displayArea.setText("Submitted Data:\nName: " + name + "\nMobile: " + mobile + "\nGender: " + gender + "\nDOB: " + dob + "\nAddress: " + address);
            JOptionPane.showMessageDialog(this, "Data saved successfully!");
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data.");
        }
    }

    public static void main(String[] args) {
        new RegistrationForm();
    }
}
