


import jdk.jfr.Description;

import javax.lang.model.element.Name;
import javax.print.attribute.standard.MediaSize;
import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.*;
        import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.SplittableRandom;

abstract class Person{
    private String name;
    private String email;
    private String phone;


    Person(String name, String email, String phone){
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public abstract void displayDetail();
}
class Customer extends Person{
    private double balance;
    private String description;


    Customer(String name, String email, String phone){
        super(name, email, phone);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void addBalance(double amount, String description){
        this.balance +=amount;
        this.description +="\nAmount added "+amount+" ( "+description+" ) ";
    }
    public void deductBalance(double amount, String description){
        if(this.balance>amount){
            this.balance -=amount;
            this.description +="\nAmount deducted "+amount+" ( "+description+" ) ";
            JOptionPane.showMessageDialog(null,"Amount removed Succesfully");

        }
        else           JOptionPane.showMessageDialog(null,"Error! You can't take more amount then you gave", "Error", JOptionPane.ERROR_MESSAGE);

    }
    public String[] Transaction(){
        return this.description.split(", ");
    }
    public void displayDetail(){
        System.out.println("Name is "+getName()+"\n Email is "+getEmail()+"\n Phone number is "+getPhone());
    }

}
abstract class PersonManager{
    public ArrayList<Customer> customers = new ArrayList<>();
    public void addCustomer(Customer customer, String name){
        for(Customer customer1 : customers){
            if(customer1.getName().equals(name)){
                JOptionPane.showMessageDialog(null, "Person with this name already exist");
                return;
            }
        }
        customers.add(customer);
        JOptionPane.showMessageDialog(null, "Customer Added Successfully");
    }

    public abstract void addPersonDetail(String name, String email, String phone);
}
class CustomerManager extends PersonManager{
    public void addPersonDetail(String name, String email, String phone){
        Customer customer = new Customer(name, email, phone);
        addCustomer(customer, name);
    }
}
class TransactionManager {

    public PersonManager personManager;
    public TransactionManager(PersonManager personManager) {
        this.personManager = personManager;
    }
    Scanner scanner = new Scanner(System.in);

    public void YouGave(String name, double amount, String description){

        for(Customer customer1 :  personManager.customers){
            if(customer1.getName().equals(name)){

                customer1.addBalance(amount, description);
                JOptionPane.showMessageDialog(null,"Amount Added Successfully");
                return;
            }
        }
        JOptionPane.showMessageDialog(null,"Customer Not Found");


    }
    public void YouGot(String name, double amount, String description){

        for(Customer customer1 :  personManager.customers){
            if(customer1.getName().equals(name)){

                customer1.deductBalance(amount, description);

                return;
            }
        }
        JOptionPane.showMessageDialog(null,"Customer Not Found");

    }
    public void viewCustomerTransaction(String name){

        for(Customer customer1 : personManager.customers){
            if(customer1.getName().equals(name)){
                for(String transaction : customer1.Transaction()){
                    if(transaction.length()>0){
//                          System.out.println("Total given to "+customer1.getName()+" is "+customer1.getBalance());
//                        boolean b = customer1.getBalance() > 100000;
                        int interestRate = 0;
                        String interest = null;
                        if(customer1.getBalance()>100000){
                            interestRate = 5;
                            double payAfterInterest = customer1.getBalance()*0.05;
                            double payable = customer1.getBalance() + payAfterInterest;
                            interest = "You will pay "+payable+" after "+interestRate+"% of interest rate";
                        }

                        else {
                            interest = " ";
                        }


                        JOptionPane.showMessageDialog(null,"Total given to "+customer1.getName()+" is "+customer1.getBalance()+"\n Here;s Transaction "+transaction+"\n "+interest);

                    }
                    else JOptionPane.showMessageDialog(null, "Not Transaction record for this customer");
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Customer not Found");


    }
    public void whatsapp(String name, String phoneNumber){
        for(Customer customer1 : personManager.customers){
            if(customer1.getName().equals(name)){
                try{
                    String amount =  JOptionPane.showInputDialog(null, "ENter amount want to request");
                    String message = "Please pay " + amount + " to settle your balance ";

                    String whatsappUrl = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode(message, "UTF-8");
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.browse(new URI(whatsappUrl));
                        System.out.println("Opening WhatsApp link in the default browser.");
                    } else {
                        System.out.println("Desktop is not supported on this system.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Customer not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


public class Main {


    public static void main(String[] args) throws Exception {
        Connection cons = DriverManager.getConnection("jdbc:ucanaccess://C://databaseC//Database1.accdb");

        CustomerManager customerManager = new CustomerManager();
        TransactionManager transactionManager = new TransactionManager(customerManager);

        JFrame loginFrame = new JFrame("SignUp Form");
        JLabel img = new JLabel();
        img.setSize(500, 300);
//        img.setIcon(new ImageIcon("C:\\Users\\Idea tech\\Pictures\\pr.png"));
        JLabel loginlabel = new JLabel("Login to Digital Khata App");
        loginlabel.setBounds(130, 10, 250, 30);
        Font font = new Font("Arial", Font.PLAIN, 20);
        loginlabel.setFont(font);

        JLabel logo = new JLabel();
        logo.setBounds(180, 65, 120, 70);

        logo.setIcon(new ImageIcon("C:\\Users\\Idea tech\\IdeaProjects\\ConnectData\\src\\khatalogo (1).png"));
        JTextField userName = new JTextField();
        userName.setBounds(180, 170, 130, 25);

        JPasswordField password = new JPasswordField();
        password.setBounds(180, 220, 130, 25);


        JLabel Username = new JLabel("UserName");
        Username.setBounds(100, 170, 100, 30);

        JLabel Password = new JLabel("Password");
        Password.setBounds(100, 220, 100, 30);


        loginFrame.add(Username);
        loginFrame.add(img);
        loginFrame.setBackground(Color.red);
        loginFrame.add(logo);

        loginFrame.add(userName);
        loginFrame.add(Password);
        loginFrame.add(password);
        loginFrame.add(loginlabel);


        JButton signUp = new JButton("SignUp");
        signUp.setBounds(180, 270, 100, 30);
        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = userName.getText();
                String pass = password.getText();
                try {
                    Connection con = DriverManager.getConnection("jdbc:ucanaccess://C://databaseC//Database3.accdb");
                    String query = "SELECT * FROM employee WHERE UserName = ? and Password = ?";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setString(1, name);
                    pst.setString(2, pass);
                    ResultSet rs = pst.executeQuery();
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(null, "Incorrect Field Entered ", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(null, "Login Successfully");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JFrame jFrame = new JFrame("Digital Khata");
                JLabel label = new JLabel("Welcome to Digital Khata App");
                Font font = new Font("Arial", Font.PLAIN, 20);
                label.setFont(font);
                label.setBounds(170, 10, 400, 30);

                JButton addcustomerButton = new JButton("Add customer");
                addcustomerButton.setBounds(30, 50, 200, 30);
                Color customColor = new Color(231, 68, 37);
                addcustomerButton.setBackground(customColor);
                addcustomerButton.setForeground(Color.white);
                addcustomerButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame frame = new JFrame();

                        JTextField Name = new JTextField();
                        Name.setBounds(90, 30, 100, 23);
                        JTextField Email = new JTextField();
                        Email.setBounds(90, 60, 100, 23);
                        JTextField Phone = new JTextField();
                        Phone.setBounds(90, 90, 100, 23);

                        JLabel NameLabel = new JLabel("Name");
                        NameLabel.setBounds(20, 30, 70, 20);
                        JLabel EmailLabel = new JLabel("Email");
                        EmailLabel.setBounds(20, 60, 70, 20);
                        JLabel PhoneLabel = new JLabel("Phone");
                        PhoneLabel.setBounds(20, 90, 70, 20);

                        frame.add(NameLabel);
                        frame.add(Name);
                        frame.add(EmailLabel);
                        frame.add(Email);
                        frame.add(PhoneLabel);
                        frame.add(Phone);
                        frame.setLayout(null);
                        frame.setSize(300, 300);
                        frame.setLayout(null);
                        frame.setVisible(true);

                        JButton Submit = new JButton("Submit");
                        Submit.setBounds(40, 140, 100, 25);
                        Submit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(Name.getText().isEmpty() || Email.getText().isEmpty() || Phone.getText().isEmpty()){
                                    JOptionPane.showMessageDialog(null, "Please fill all the feilds", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                                else {
                                    String name = Name.getText();
                                    String email = Email.getText();
                                    String phone = Phone.getText();


                                    customerManager.addPersonDetail(Name.getText(), Email.getText(), Phone.getText());

                                    frame.dispose();
                                    String query = "INSERT INTO Customers(Name, Email, Phone) VALUES ('" + name + "', '" + email + "', '" + phone + "')";
                                    try {
                                        Statement st = cons.createStatement();
                                        st.executeUpdate(query);
                                    } catch (SQLException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }

                            }
                        });
                        frame.add(Submit);
                    }
                });

                JButton YouGaveButton = new JButton("Give Amount");
                YouGaveButton.setBounds(30, 100, 200, 30);
                YouGaveButton.setBackground(customColor);
                YouGaveButton.setForeground(Color.white);
                YouGaveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame frame = new JFrame("Amount You Gave");
                        JTextField Name = new JTextField();
                        Name.setBounds(90, 30, 100, 23);
                        JTextField Amount = new JTextField();
                        Amount.setBounds(90, 60, 100, 23);
                        JTextField Description = new JTextField();
                        Description.setBounds(90, 90, 100, 23);

                        JLabel NameLabel = new JLabel("Name");
                        NameLabel.setBounds(20, 30, 70, 20);
                        JLabel AmountLabel = new JLabel("Amount");
                        AmountLabel.setBounds(20, 60, 70, 20);
                        JLabel DescriptionLabel = new JLabel("Description");
                        DescriptionLabel.setBounds(20, 90, 70, 20);

                        frame.add(NameLabel);
                        frame.add(Name);
                        frame.add(AmountLabel);
                        frame.add(Amount);
                        frame.add(DescriptionLabel);
                        frame.add(Description);

                        JButton Submit = new JButton("Submit");
                        Submit.setBounds(40, 140, 100, 25);
                        Submit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(Name.getText().isEmpty() || Amount.getText().isEmpty()){
                                    JOptionPane.showMessageDialog(null, "Please fill all the feilds", "Error", JOptionPane.ERROR_MESSAGE);

                                }
                                else {
                                    String name = Name.getText();
                                    Double amount = Double.parseDouble(Amount.getText());
                                    String description = Description.getText();
                                    transactionManager.YouGave(Name.getText(), Double.parseDouble(Amount.getText()), Description.getText());
                                    String query = "INSERT INTO Transactions(CustomerName, Amount, Description, Type) VALUES ('" + name + "', '" + amount + "', '" + description + "', 'Added')";
                                    try {
                                        Statement st = cons.createStatement();
                                        st.executeUpdate(query);
                                    } catch (SQLException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    frame.dispose();
                                }

                            }
                        });

                        frame.add(Submit);
                        frame.setLayout(null);
                        frame.setSize(300, 300);
                        frame.setLayout(null);
                        frame.setVisible(true);

                    }
                });

                JButton YouGotButton = new JButton("Return Amount");
                YouGotButton.setBounds(30, 150, 200, 30);
                YouGotButton.setBackground(customColor);
                YouGotButton.setForeground(Color.white);

                YouGotButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame frame = new JFrame("You Got");
                        JTextField Name = new JTextField();
                        Name.setBounds(90, 30, 100, 23);
                        JTextField Amount = new JTextField();
                        Amount.setBounds(90, 60, 100, 23);
                        JTextField Description = new JTextField();
                        Description.setBounds(90, 90, 100, 23);

                        JLabel NameLabel = new JLabel("Name");
                        NameLabel.setBounds(20, 30, 70, 20);
                        JLabel AmountLabel = new JLabel("Amount");
                        AmountLabel.setBounds(20, 60, 70, 20);
                        JLabel DescriptionLabel = new JLabel("Description");
                        DescriptionLabel.setBounds(20, 90, 70, 20);

                        frame.add(NameLabel);
                        frame.add(Name);
                        frame.add(AmountLabel);
                        frame.add(Amount);
                        frame.add(DescriptionLabel);
                        frame.add(Description);

                        JButton Submit = new JButton("Submit");
                        Submit.setBounds(40, 140, 100, 25);

                        Submit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(Name.getText().isEmpty() || Amount.getText().isEmpty()){
                                    JOptionPane.showMessageDialog(null, "Please fill all the feilds", "Error", JOptionPane.ERROR_MESSAGE);

                                }
                                else {
                                    String name = Name.getText();
                                    Double amount = Double.parseDouble(Amount.getText());
                                    String description = Description.getText();
                                    transactionManager.YouGot(Name.getText(), Double.parseDouble(Amount.getText()), Description.getText());
                                    String query = "INSERT INTO Transactions(CustomerName, Amount, Description, Type) VALUES ('" + name + "', '" + amount + "', '" + description + "', 'Deduct')";
                                    try {
                                        Statement st = cons.createStatement();
                                        st.executeUpdate(query);
                                    } catch (SQLException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    frame.dispose();
                                }

                            }
                        });

                        frame.add(Submit);
                        frame.setLayout(null);
                        frame.setSize(300, 300);
                        frame.setLayout(null);
                        frame.setVisible(true);
                    }
                });
                JButton viewCustomerTransaction = new JButton("View Customer Transaction");
                viewCustomerTransaction.setBounds(30, 200, 200, 30);
                viewCustomerTransaction.setBackground(customColor);
                viewCustomerTransaction.setForeground(Color.white);
                viewCustomerTransaction.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame frame = new JFrame("View Customer Transaction");
                        JTextField Name = new JTextField();
                        Name.setBounds(90, 30, 100, 23);
                        JLabel NameLabel = new JLabel("Name");
                        NameLabel.setBounds(20, 30, 70, 20);
                        frame.add(NameLabel);
                        frame.add(Name);
                        JButton Submit = new JButton("Submit");
                        Submit.setBounds(40, 100, 100, 25);
                        Submit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                transactionManager.viewCustomerTransaction(Name.getText());
                                frame.dispose();
                            }
                        });

                        frame.add(Submit);
                        frame.setLayout(null);
                        frame.setSize(300, 300);
                        frame.setLayout(null);
                        frame.setVisible(true);
                    }
                });
                JButton viewCustomerTransactionDAtaBase = new JButton("View DataBase");
                viewCustomerTransactionDAtaBase.setBounds(30, 250, 200, 30);
                viewCustomerTransactionDAtaBase.setBackground(customColor);
                viewCustomerTransactionDAtaBase.setForeground(Color.white);
                viewCustomerTransactionDAtaBase.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame frame = new JFrame("View Customer Transaction on DataBase");
                        JTextField Name = new JTextField();
                        Name.setBounds(90, 30, 100, 23);
                        JLabel NameLabel = new JLabel("Name");
                        NameLabel.setBounds(20, 30, 70, 20);
                        frame.add(NameLabel);
                        frame.add(Name);
                        JButton Submit = new JButton("Submit");
                        Submit.setBounds(40, 100, 100, 25);
                        Submit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String username = Name.getText();
                                String query = "SELECT * FROM Transactions WHERE CustomerName = '" + username + "'";
                                try {
                                    Statement st = cons.createStatement();
                                    ResultSet rs = st.executeQuery(query);
                                    JFrame frame = new JFrame("Viewing  transaction through Database");
                                    JTextArea textArea = new JTextArea(20, 40);
                                    while (rs.next()) {
                                        String description = rs.getString("Description");
                                        Double amount = rs.getDouble("Amount");
                                        String type = rs.getString("Type");
                                        textArea.append("\nDescription : " + description + " ,      Amount : " + amount + " ,       Type : " + type);
                                    }
                                    frame.add(textArea);
                                    frame.pack();
//                                    frame.setSize(500, 500);
//                                    frame.setLayout(null);
                                    frame.setVisible(true);
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                                frame.dispose();
                            }
                        });

                        frame.add(Submit);
                        frame.setLayout(null);
                        frame.setSize(300, 300);
                        frame.setLayout(null);
                        frame.setVisible(true);
                    }
                });
                JButton whatsapp = new JButton("Whatsapp");
                whatsapp.setBounds(30, 300, 200, 30);
                whatsapp.setBackground(customColor);
                whatsapp.setForeground(Color.white);
                whatsapp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {


                        String names = JOptionPane.showInputDialog(null, "Enter Name");

                        String phoneNumber = JOptionPane.showInputDialog(null, "Enter Phone Number");
                        transactionManager.whatsapp(names, phoneNumber);

                    }
                });
                JLabel pic = new JLabel();
                pic.setBounds(300, 0, 220, 420);
                pic.setIcon(new ImageIcon("C:\\Users\\Idea tech\\IdeaProjects\\ConnectData\\src\\khatapic.jpeg"));

                jFrame.add(pic);
                jFrame.add(label);
                jFrame.add(addcustomerButton);
                jFrame.add(YouGaveButton);
                jFrame.add(YouGotButton);
                jFrame.add(viewCustomerTransaction);
                jFrame.add(viewCustomerTransactionDAtaBase);
                jFrame.add(whatsapp);
                jFrame.setSize(600, 550);
                jFrame.setLocationRelativeTo(null);
                jFrame.setLayout(null);
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.setVisible(true);
            }
        });



        loginFrame.add(signUp);

        loginFrame.setSize(500, 400);
        loginFrame.setLayout(null);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);

    }}







