import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryManagementSystem extends JFrame {

    JTextField txtId, txtName, txtDept, txtBook, txtIssue, txtReturn;
    DefaultTableModel model;
    JTable table;

    String url="jdbc:mysql://localhost:3306/library_system";
    String user="root";
    String password="aparna@cse";

    public LibraryManagementSystem(){

        setTitle("Library Management System");
        setSize(900,500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6,2,5,5));
        panel.setPreferredSize(new Dimension(300,250));

        txtId = new JTextField();
        txtName = new JTextField();
        txtDept = new JTextField();
        txtBook = new JTextField();
        txtIssue = new JTextField();
        txtReturn = new JTextField();

        panel.add(new JLabel("Student ID"));
        panel.add(txtId);

        panel.add(new JLabel("Student Name"));
        panel.add(txtName);

        panel.add(new JLabel("Department"));
        panel.add(txtDept);

        panel.add(new JLabel("Book Name"));
        panel.add(txtBook);

        panel.add(new JLabel("Issue Date (YYYY-MM-DD)"));
        panel.add(txtIssue);

        panel.add(new JLabel("Return Date (YYYY-MM-DD)"));
        panel.add(txtReturn);

        add(panel,BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();

        JButton issueBtn = new JButton("Issue Book");
        JButton returnBtn = new JButton("Return Book");
        JButton viewBtn = new JButton("View Records");

        buttonPanel.add(issueBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.add(viewBtn);

        add(buttonPanel,BorderLayout.SOUTH);

        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Dept");
        model.addColumn("Book");
        model.addColumn("Issue Date");
        model.addColumn("Return Date");
        model.addColumn("Fine");
        model.addColumn("Status");

        table = new JTable(model);
        add(new JScrollPane(table),BorderLayout.CENTER);

        issueBtn.addActionListener(e -> issueBook());
        returnBtn.addActionListener(e -> returnBook());
        viewBtn.addActionListener(e -> viewRecords());

        setVisible(true);
    }

    void issueBook(){

        try{

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(url,user,password);

            String sql="insert into library_books values(?,?,?,?,?,?,?,?)";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1,Integer.parseInt(txtId.getText()));
            pst.setString(2,txtName.getText());
            pst.setString(3,txtDept.getText());
            pst.setString(4,txtBook.getText());
            pst.setDate(5,Date.valueOf(txtIssue.getText()));
            pst.setDate(6,null);
            pst.setInt(7,0);
            pst.setString(8,"Issued");

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,"Book Issued");

            con.close();

        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,e);
        }
    }

    void returnBook(){

        try{

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(url,user,password);

            int id = Integer.parseInt(txtId.getText());

            LocalDate issue = LocalDate.parse(txtIssue.getText());
            LocalDate ret = LocalDate.parse(txtReturn.getText());

            long days = ChronoUnit.DAYS.between(issue,ret);

            int fine = 0;

            if(days>7)
                fine=(int)(days-7);

            String sql="update library_books set return_date=?, fine=?, status=? where student_id=?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setDate(1,Date.valueOf(ret));
            pst.setInt(2,fine);
            pst.setString(3,"Book Returned");
            pst.setInt(4,id);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,"Book Returned. Fine = ₹"+fine);

            con.close();

        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,e);
        }
    }

    void viewRecords(){

        try{

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(url,user,password);

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("select * from library_books");

            model.setRowCount(0);

            while(rs.next()){

                model.addRow(new Object[]{
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getString("department"),
                        rs.getString("book_name"),
                        rs.getDate("issue_date"),
                        rs.getDate("return_date"),
                        rs.getInt("fine"),
                        rs.getString("status")
                });

            }

            con.close();

        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,e);
        }
    }

    public static void main(String[] args){

        new LibraryManagementSystem();

    }
}