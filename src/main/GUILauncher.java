package main;

import dao.ItemDAO;
import model.Contenant;
import model.Item;
import view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.List;

/**
 * Lanceur de l'interface graphique.
 *
 * <p>Cette classe configure le Look and Feel Nimbus, charge les données persistantes depuis le fichier JSON
 * et lance l'interface graphique principale.</p>
 */
public class GUILauncher {
    public static void main(String[] args) {
        // Configuration du Look and Feel Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Nimbus Look and Feel non disponible, utilisation du Look and Feel par défaut.");
        }
        
        // Création d'un contenant avec des paramètres d'exemple
        Contenant contenant = new Contenant(20, 12.0, 70.0);
        
        // Chargement des items depuis le fichier JSON et ajout dans le contenant
        try {
            ItemDAO jsonDao = new ItemDAO();
            List<Item> items = jsonDao.getAllItems();
            for (Item item : items) {
                contenant.ajouterItem(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Lancement de l'interface graphique dans le thread Swing
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(contenant);
            frame.setVisible(true);
        });
    }
}
