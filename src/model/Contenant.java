package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un contenant (ex : cave ou frigo) qui stocke des items.
 */
public class Contenant {
    private int contenanceMaximale;
    private double temperature;
    private double humidite;
    private List<Item> items;
    
    // Liste d'observateurs pour notifier les changements
    private List<ContenantObserver> observers;
    
    /**
     * Constructeur pour le contenant.
     *
     * @param contenanceMaximale capacité maximale d'items
     * @param temperature température du contenant
     * @param humidite humidité du contenant
     */
    public Contenant(int contenanceMaximale, double temperature, double humidite) {
        this.contenanceMaximale = contenanceMaximale;
        this.temperature = temperature;
        this.humidite = humidite;
        this.items = new ArrayList<>();
        this.observers = new ArrayList<>();
    }
    
    /**
     * Ajoute un observateur.
     *
     * @param observer l'observateur à ajouter
     */
    public void addObserver(ContenantObserver observer) {
        observers.add(observer);
    }
    
    /**
     * Supprime un observateur.
     *
     * @param observer l'observateur à supprimer
     */
    public void removeObserver(ContenantObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Notifie tous les observateurs d'un changement.
     */
    private void notifyObservers() {
        for (ContenantObserver observer : observers) {
            observer.update();
        }
    }
    
    /**
     * Ajoute un item dans le contenant.
     *
     * @param item l'item à ajouter
     */
    public void ajouterItem(Item item) {
        if (items.size() < contenanceMaximale) {
            items.add(item);
            System.out.println("Item ajouté : " + item.getDenomination());
            notifyObservers();
        } else {
            System.out.println("Contenant plein, impossible d'ajouter l'item.");
        }
    }
    
    /**
     * Supprime un item du contenant.
     *
     * @param item l'item à supprimer
     */
    public void supprimerItem(Item item) {
        if (items.remove(item)) {
            System.out.println("Item supprimé : " + item.getDenomination());
            notifyObservers();
        } else {
            System.out.println("Item non trouvé.");
        }
    }
    
    /**
     * Affiche la liste des items présents dans le contenant.
     */
    public void afficherItems() {
        System.out.println("Items dans le contenant :");
        for (Item item : items) {
            item.afficher();
            System.out.println("-------------------------");
        }
    }
    
    /**
     * Calcule la valeur totale des items dans le contenant.
     *
     * @return valeur totale
     */
    public double getValeurTotale() {
        double total = 0;
        for (Item item : items) {
            total += item.getPrix() * item.getQuantite();
        }
        return total;
    }
    
    /**
     * Calcule le prix moyen des items dans le contenant.
     *
     * @return prix moyen
     */
    public double getPrixMoyen() {
        if(items.isEmpty()) return 0;
        double total = 0;
        int count = 0;
        for (Item item : items) {
            total += item.getPrix();
            count++;
        }
        return total / count;
    }
    
    /**
     * Retourne la liste des items.
     *
     * @return liste des items
     */
    public List<Item> getItems() {
        return items;
    }
    
    /**
     * Fournit des suggestions d'accords mets-vins en fonction des types de vins présents.
     *
     * @return liste de suggestions
     */
    public List<String> getPairingSuggestions() {
        List<String> suggestions = new ArrayList<>();
        for (Item item : items) {
            if(item instanceof Vin) {
                Vin vin = (Vin) item;
                // Exemple simple : suggère un accord en fonction du type de vin
                if(vin.getDescription().toLowerCase().contains("rouge")) {
                    suggestions.add("Accord: Steak et vin rouge (" + vin.getDenomination() + ")");
                } else if(vin.getDescription().toLowerCase().contains("blanc")) {
                    suggestions.add("Accord: Poisson et vin blanc (" + vin.getDenomination() + ")");
                }
            }
        }
        return suggestions;
    }
}
