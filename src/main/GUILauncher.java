package main;

import model.Contenant;
import view.MainFrame;
import javax.swing.SwingUtilities;

/**
 * Lanceur de l'interface graphique.
 */
public class GUILauncher {
    public static void main(String[] args) {
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
