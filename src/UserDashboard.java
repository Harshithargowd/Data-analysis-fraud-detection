import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UserDashboard extends JFrame {
    public UserDashboard() {
        setTitle("User Dashboard - " );
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea transactionsArea = new JTextArea();
        transactionsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transactionsArea);
        add(scrollPane, BorderLayout.CENTER);

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions WHERE nameOrig")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactionsArea.append("Step: " + rs.getInt("step") +
                        ", Amount: " + rs.getDouble("amount") +
                        ", To: " + rs.getString("nameDest") +
                        ", Fraud: " + rs.getBoolean("isFraud") + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}