package main;

import model.Contenant;
import model.Vin;
import java.util.Date;

/**
 * Lanceur de l'application en mode console.
 *
 * <p>Cette classe sert à tester les fonctionnalités en ligne de commande sans interface graphique.</p>
 */
public class Launcher {
    public static void main(String[] args) {
        // Création d'un contenant avec une capacité maximale de 10 items
        Contenant cave = new Contenant(10, 12.0, 70.0);
        
        // Création d'un vin d'exemple
        Vin vinRouge = new Vin("Château Margaux", "Vin rouge de Bordeaux", 5, 2015, new Date(), 150.0, "Cabernet Sauvignon, Merlot", "Bordeaux, France");
        
        // Ajout du vin dans le contenant
        cave.ajouterItem(vinRouge);
        
        // Affichage des items dans le contenant
        cave.afficherItems();
        
        // Affichage des statistiques
        System.out.println("Valeur Totale: " + cave.getValeurTotale());
        System.out.println("Prix Moyen: " + cave.getPrixMoyen());
    }
}
