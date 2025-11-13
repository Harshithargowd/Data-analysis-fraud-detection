import javax.swing.*;
import java.awt.*;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard - Fraud Detection");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel chartContainer = new JPanel();
        chartContainer.setLayout(new GridLayout(0,2));
        
        chartContainer.setLayout(new GridLayout(0,2));
        chartContainer.add(ChartUtils.createFraudChart());
        chartContainer.add(ChartUtils.createTopFraudAccountsChart());
        chartContainer.add(ChartUtils.createFraudByTypePieChart());
        chartContainer.add(ChartUtils.createFlaggedVsActualChart());
        chartContainer.add(ChartUtils.createTransactionAmountHistogram());
        chartContainer.add(ChartUtils.createCategoryWiseAmountChart());
        
        JScrollPane scrollPane = new JScrollPane(chartContainer);
        scrollPane.setPreferredSize(new Dimension(1000,800));
        add(scrollPane);
        getContentPane().add(chartContainer);
        add(chartContainer);
        chartContainer.revalidate();
        chartContainer.repaint();
        
    }
}
