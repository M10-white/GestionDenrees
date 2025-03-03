package main;

import model.Contenant;
import view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Lanceur de l'interface graphique avec Look and Feel amélioré.
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
        
        // Lancement de l'interface graphique dans le thread Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame(contenant);
                frame.setVisible(true);
            }
        });
    }
}
