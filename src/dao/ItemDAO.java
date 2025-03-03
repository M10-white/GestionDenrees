package dao;

import model.Item;
import model.Vin;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe DAO pour la persistance des items dans la base de données.
 * Utilise JDBC pour effectuer les opérations CRUD sur une base SQLite.
 */
public class ItemDAO {
    private Connection connection;

    /**
     * Constructeur qui établit la connexion à la base SQLite et crée la table si elle n'existe pas.
     *
     * @throws SQLException en cas d'erreur de connexion ou de création de table.
     */
    public ItemDAO() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:denrees.db");
        createTableIfNotExists();
    }

    /**
     * Crée la table items si elle n'existe pas déjà, en incluant toutes les colonnes nécessaires.
     *
     * @throws SQLException en cas d'erreur lors de l'exécution de la requête.
     */
    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS items ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "denomination TEXT UNIQUE, "
                + "description TEXT, "
                + "quantite INTEGER, "
                + "anneeProduction INTEGER, "
                + "dateAjout TEXT, "
                + "prix REAL, "
                + "dlc TEXT, "
                + "image TEXT, "
                + "position TEXT, "
                + "phaseVieillissement TEXT, "
                + "note REAL, "
                + "cepage TEXT, "
                + "region TEXT"
                + ")";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }

    /**
     * Insère un nouvel item dans la base de données.
     *
     * @param item l'item à insérer.
     * @throws SQLException en cas d'erreur d'exécution.
     */
    public void insertItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (denomination, description, quantite, anneeProduction, dateAjout, prix, dlc, image, position, phaseVieillissement, note, cepage, region) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, item.getDenomination());
        pstmt.setString(2, item.getDescription());
        pstmt.setInt(3, item.getQuantite());
        pstmt.setInt(4, item.getAnneeProduction());
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(item.getDateAjout());
        pstmt.setString(5, dateStr);
        pstmt.setDouble(6, item.getPrix());
        pstmt.setString(7, item.getDlc());
        pstmt.setString(8, item.getImage());
        pstmt.setString(9, item.getPosition());
        pstmt.setString(10, item.getPhaseVieillissement());
        pstmt.setDouble(11, item.getNote());
        if (item instanceof Vin) {
            Vin vin = (Vin) item;
            pstmt.setString(12, vin.getCepage());
            pstmt.setString(13, vin.getRegion());
        } else {
            pstmt.setString(12, null);
            pstmt.setString(13, null);
        }
        pstmt.executeUpdate();
    }

    /**
     * Lit et retourne la liste de tous les items stockés dans la base de données.
     *
     * @return une liste d'items.
     * @throws SQLException en cas d'erreur d'exécution.
     */
    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String denomination = rs.getString("denomination");
            String description = rs.getString("description");
            int quantite = rs.getInt("quantite");
            int anneeProduction = rs.getInt("anneeProduction");
            String dateStr = rs.getString("dateAjout");
            Date dateAjout;
            try {
                dateAjout = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            } catch (Exception e) {
                dateAjout = new Date();
            }
            double prix = rs.getDouble("prix");
            String dlc = rs.getString("dlc");
            String image = rs.getString("image");
            String position = rs.getString("position");
            String phaseVieillissement = rs.getString("phaseVieillissement");
            double note = rs.getDouble("note");
            String cepage = rs.getString("cepage");
            String region = rs.getString("region");
            // Pour cet exemple, nous considérons que si la colonne 'cepage' n'est pas vide, l'item est un Vin.
            if (cepage != null && !cepage.isEmpty()) {
                Vin vin = new Vin(denomination, description, quantite, anneeProduction, dateAjout, prix, cepage, region);
                vin.setDlc(dlc);
                vin.setImage(image);
                vin.setPosition(position);
                vin.setPhaseVieillissement(phaseVieillissement);
                vin.setNote(note);
                items.add(vin);
            }
            // Vous pouvez gérer d'autres types d'items ici.
        }
        return items;
    }

    /**
     * Met à jour les informations d'un item dans la base de données.
     * Utilise la dénomination comme identifiant unique.
     *
     * @param item l'item à mettre à jour.
     * @throws SQLException en cas d'erreur d'exécution.
     */
    public void updateItem(Item item) throws SQLException {
        String sql = "UPDATE items SET description=?, quantite=?, anneeProduction=?, dateAjout=?, prix=?, dlc=?, image=?, position=?, phaseVieillissement=?, note=?, cepage=?, region=? WHERE denomination=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, item.getDescription());
        pstmt.setInt(2, item.getQuantite());
        pstmt.setInt(3, item.getAnneeProduction());
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(item.getDateAjout());
        pstmt.setString(4, dateStr);
        pstmt.setDouble(5, item.getPrix());
        pstmt.setString(6, item.getDlc());
        pstmt.setString(7, item.getImage());
        pstmt.setString(8, item.getPosition());
        pstmt.setString(9, item.getPhaseVieillissement());
        pstmt.setDouble(10, item.getNote());
        if (item instanceof Vin) {
            Vin vin = (Vin) item;
            pstmt.setString(11, vin.getCepage());
            pstmt.setString(12, vin.getRegion());
        } else {
            pstmt.setString(11, null);
            pstmt.setString(12, null);
        }
        pstmt.setString(13, item.getDenomination());
        pstmt.executeUpdate();
    }

    /**
     * Supprime un item de la base de données en utilisant la dénomination comme identifiant.
     *
     * @param item l'item à supprimer.
     * @throws SQLException en cas d'erreur d'exécution.
     */
    public void deleteItem(Item item) throws SQLException {
        String sql = "DELETE FROM items WHERE denomination=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, item.getDenomination());
        pstmt.executeUpdate();
    }
}
