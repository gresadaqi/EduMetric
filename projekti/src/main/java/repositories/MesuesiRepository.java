package repositories;

import database.DBConnector;
import models.Adresa;
import models.Mesuesi;
import models.dto.create.CreateMesuesi;
import models.dto.update.UpdateMesuesi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesuesiRepository {



    public boolean shto(CreateMesuesi m) {
        String sql = "INSERT INTO mesuesi (emri, mbiemri, email, tel, roli, adresa_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.emri);
            ps.setString(2, m.mbiemri);
            ps.setString(3, m.email);
            ps.setString(4, m.tel);
            ps.setString(5, m.roli);
            ps.setInt(6, m.adresaId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace(); return false;
        }
    }

    public boolean perditeso(UpdateMesuesi m) {
        String sql = "UPDATE mesuesi SET emri=?, mbiemri=?, email=?, tel=?, roli=?, adresa_id=? WHERE id=?";
        try (Connection con = DBConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.emri);
            ps.setString(2, m.mbiemri);
            ps.setString(3, m.email);
            ps.setString(4, m.tel);
            ps.setString(5, m.roli);
            ps.setInt(6, m.adresaId);
            ps.setInt(7, m.id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace(); return false;
        }
    }

    public boolean fshij(int id) {
        String sql = "DELETE FROM mesuesi WHERE id=?";
        try (Connection con = DBConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public int merrNumrinEMesuesve() {
        int count = 0;

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM mesuesi")) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // ose log.error(e);
        }

        return count;
    }
    public List<Mesuesi> gjejTeGjithe() {
        List<Mesuesi> mesuesit = new ArrayList<>();
        String query = """
                SELECT m.id, m.emri, m.mbiemri, m.email, m.tel
                FROM mesuesi m
                """;

        try (Connection con = DBConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Mesuesi m = new Mesuesi(
                        rs.getInt("id"),
                        rs.getString("emri"),
                        rs.getString("mbiemri"),
                        rs.getString("email"),
                        rs.getString("tel")
                );
                mesuesit.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mesuesit;
    }
    public int getMesuesiIdByEmail(String email) {
        String sql = "SELECT id FROM mesuesi WHERE email = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}