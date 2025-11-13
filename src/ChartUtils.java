import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;

import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChartUtils {
    public static ChartPanel createFraudChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT step, SUM(amount) AS total FROM transactions WHERE isFraud = TRUE GROUP BY step")) {

            while (rs.next()) {
                int step = rs.getInt("step");
                double amount = rs.getDouble("total");
                dataset.addValue(amount, "Fraud Amount", Integer.toString(step));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Fraud Amount per Step",
                "Step",
                "Amount",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        return new ChartPanel(chart);
    }
     public static JPanel createTopFraudAccountsChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT nameOrig, COUNT(*) AS fraud_count FROM transactions WHERE isFraud = 1 GROUP BY nameOrig ORDER BY fraud_count DESC LIMIT 5";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dataset.addValue(rs.getInt("fraud_count"), "Frauds", rs.getString("nameOrig"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createBarChart("Top 5 Fraudulent Origin Accounts", "Account", "Fraud Count", dataset, PlotOrientation.VERTICAL, false, true, false);
        return new ChartPanel(chart);
    }
    public static JPanel createFraudByTypePieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT type, COUNT(*) AS total FROM transactions WHERE isFraud = 1 GROUP BY type";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dataset.setValue(rs.getString("type"), rs.getInt("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createPieChart("Fraud by Transaction Type", dataset, true, true, false);
        return new ChartPanel(chart);
    }
    
        public static JPanel createFlaggedVsActualChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT (SELECT COUNT(*) FROM transactions WHERE isFraud = 1) AS frauds, (SELECT COUNT(*) FROM transactions WHERE isFlaggedFraud = 1) AS flagged";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dataset.addValue(rs.getInt("frauds"), "Count", "Actual Fraud");
                dataset.addValue(rs.getInt("flagged"), "Count", "Flagged Fraud");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createBarChart("Flagged vs Actual Fraud", "Type", "Count", dataset);
        return new ChartPanel(chart);
    }
        public static JPanel createTransactionAmountHistogram() {
        List<Double> values = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT amount FROM transactions WHERE isFraud = 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                values.add(rs.getDouble("amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        double[] array = values.stream().mapToDouble(Double::doubleValue).toArray();
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Fraud Amounts", array, 10);
        JFreeChart chart = ChartFactory.createHistogram("Fraud Amount Distribution", "Amount", "Frequency", dataset,PlotOrientation.VERTICAL,true,true,false);
        return new ChartPanel(chart);
    }
        
        public static JPanel createCategoryWiseAmountChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT type, SUM(amount) AS total_amount FROM transactions WHERE isFraud = 1 GROUP BY type";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dataset.addValue(rs.getDouble("total_amount"), "Amount", rs.getString("type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createBarChart("Fraud Amount by Transaction Type", "Transaction Type", "Total Amount", dataset);
        return new ChartPanel(chart);
    }

   
}
