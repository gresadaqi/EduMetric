package repositories;

import database.DBConnector;
import java.sql.*;

public class PeriodaRepository {
    public static Integer getPeriodaIdByName(String emri) {
        String sql = "SELECT id FROM perioda WHERE TRIM(emri) = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            System.out.println("🔍 Po kërkoj periudhën me emri: " + emri);

            stmt.setString(1, emri.trim());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}