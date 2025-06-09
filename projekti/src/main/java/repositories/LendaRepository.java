package repositories;

import database.DBConnector;
import models.*;
import models.dto.create.CreateLenda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LendaRepository {
    public int getLendaIdByName(String emri) {
        String sql = "SELECT id FROM lenda WHERE LOWER(emri) = LOWER(?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emri.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean shto(CreateLenda l) {
        String sql = "INSERT INTO lenda (emri, drejtimi_id, perioda_id, mesuesi_id) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, l.getEmri());
            ps.setInt(2, l.getDrejtimi());
            ps.setInt(3, l.getPerioda());
            ps.setInt(4, l.getMesuesi());
            return ps.executeUpdate() > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean fshij(int id) {
        String sql = "DELETE FROM lenda WHERE id=?";
        try (Connection con = DBConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Lenda> gjejTeGjitha() {
        List<Lenda> lendet = new ArrayList<>();

        String query = """
            SELECT l.*, 
                   d.emri AS drejtimi_emri,
                   p.emri AS perioda_emri,
                   m.emri AS mesuesi_emri, 
                   m.mbiemri AS mesuesi_mbiemri
            FROM lenda l
            JOIN drejtimi d ON l.drejtimi_id = d.id
            JOIN perioda p ON l.perioda_id = p.id
            JOIN mesuesi m ON l.mesuesi_id = m.id
        """;

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Perioda perioda = new Perioda(rs.getInt("perioda_id"), rs.getString("perioda_emri"), null, null);
                Mesuesi mesuesi = new Mesuesi(rs.getInt("mesuesi_id"), rs.getString("mesuesi_emri"), rs.getString("mesuesi_mbiemri"), null,null);
                Drejtimi drejtimi = new Drejtimi(rs.getInt("drejtimi_id"), rs.getString("drejtimi_emri"));

                Lenda lenda=new Lenda(rs.getInt("id"), rs.getString("emri"), drejtimi, perioda, mesuesi);
                lendet.add(lenda);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lendet;
    }
}