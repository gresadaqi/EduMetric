package repositories;

import database.DBConnector;
import models.*;
import models.dto.create.CreateDrejtor;
import models.dto.create.CreateShkolla;
import models.dto.update.UpdateDrejtor;
import models.dto.update.UpdateShkolla;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrejtoriRepository {

    public boolean shtoDrejtor(CreateDrejtor d) {
        String sql = "INSERT INTO drejtor (emri, mbiemri, email, tel, adresa_id, shkolla_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, d.getEmri());
            stmt.setString(2, d.getMbiemri());
            stmt.setString(3, d.getEmail());
            stmt.setString(4, d.getTel());
            stmt.setInt(5, d.getAdresaId());
            stmt.setInt(6, d.getShkollaId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean perditesoDrejtor(UpdateDrejtor drejtori){
        StringBuilder sql = new StringBuilder("UPDATE drejtor SET ");
        List<Object> params = new ArrayList<>();

        if (drejtori.getEmri() != null && !drejtori.getEmri().isBlank()) {
            sql.append("emri = ?, ");
            params.add(drejtori.getEmri());
        }
        if (drejtori.getMbiemri() != null && !drejtori.getMbiemri().isBlank()) {
            sql.append("mbiemri = ?, ");
            params.add(drejtori.getMbiemri());
        }
        if (drejtori.getTel() != null && !drejtori.getTel().isBlank()) {
            sql.append("tel = ?, ");
            params.add(drejtori.getTel());
        }
        if (drejtori.getEmail() != null && !drejtori.getEmail().isBlank()) {
            sql.append("email = ?, ");
            params.add(drejtori.getEmail());
        }
        if (drejtori.getAdresaId() != 0) {
            sql.append("adresa_id = ?, ");
            params.add(drejtori.getAdresaId());
        }
        if (drejtori.getShkollaId() != 0) {
            sql.append("shkolla_id = ?, ");
            params.add(drejtori.getShkollaId());
        }
        if (params.isEmpty()) return false;

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id = ?");
        params.add(drejtori.getId());

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean fshijDrejtor(int id) {
        String sql = "DELETE FROM drejtor WHERE id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public List<Drejtor> gjejTeGjithe() {
        List<Drejtor> drejtoret = new ArrayList<>();
        String query = """
            SELECT d.id, d.emri, d.mbiemri, d.email, d.tel,
            sh.id AS shkolla_id, sh.emri AS shkolla_emri
            FROM drejtor d
            JOIN shkolla sh ON d.shkolla_id = sh.id
        """;
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Shkolla shkolla = new Shkolla(rs.getInt("shkolla_id"), rs.getString("shkolla_emri"), null, null);
                Drejtor drejtori = new Drejtor(
                        rs.getInt("id"),
                        rs.getString("emri"),
                        rs.getString("mbiemri"),
                        rs.getString("email"),
                        rs.getString("tel"),
                        null,
                        shkolla
                );
                drejtoret.add(drejtori);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return drejtoret;
    }
}

