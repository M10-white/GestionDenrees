package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un contenant (ex : cave ou frigo) qui stocke des items.
 * <p>
 * Ce contenant gère une collection d'objets de type {@link Item} et permet de :
 * <ul>
 *   <li>Ajouter et supprimer des items.</li>
 *   <li>Calculer la valeur totale et le prix moyen des items.</li>
 *   <li>Fournir des suggestions d'accords mets-vins pour les vins présents.</li>
 *   <li>Notifier les observateurs (implémentant {@link ContenantObserver}) lors des changements.</li>
 * </ul>
 * </p>
 *
 * @author 
 */
public class Contenant {
    /** Capacité maximale du contenant (nombre maximum d'items). */
    private int contenanceMaximale;
    /** Température du contenant. */
    private double temperature;
    /** Humidité du contenant. */
    private double humidite;
    /** Liste des items stockés dans le contenant. */
    private List<Item> items;
    /** Liste des observateurs à notifier lors des modifications du contenant. */
    private List<ContenantObserver> observers;
    
    /**
     * Constructeur pour créer un contenant.
     *
     * @param contenanceMaximale capacité maximale d'items
     * @param temperature      température du contenant
     * @param humidite         humidité du contenant
     */
    public Contenant(int contenanceMaximale, double temperature, double humidite) {
        this.contenanceMaximale = contenanceMaximale;
        this.temperature = temperature;
        this.humidite = humidite;
        this.items = new ArrayList<>();
        this.observers = new ArrayList<>();
    }
    
    /**
     * Ajoute un observateur qui sera notifié des changements dans le contenant.
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
     * Notifie tous les observateurs qu'une modification a eu lieu dans le contenant.
     */
    private void notifyObservers() {
        for (ContenantObserver observer : observers) {
            observer.update();
        }
    }
    
    /**
     * Ajoute un item dans le contenant si la capacité maximale n'est pas dépassée.
     * Notifie ensuite les observateurs.
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
     * Notifie ensuite les observateurs.
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
     * Affiche, sur la console, la liste de tous les items présents dans le contenant.
     */
    public void afficherItems() {
        System.out.println("Items dans le contenant :");
        for (Item item : items) {
            item.afficher();
            System.out.println("-------------------------");
        }
    }
    
    /**
     * Calcule la valeur totale de tous les items dans le contenant.
     * La valeur totale est obtenue en multipliant le prix de chaque item par sa quantité.
     *
     * @return la valeur totale
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
     * @return le prix moyen, ou 0 si le contenant est vide
     */
    public double getPrixMoyen() {
        if (items.isEmpty()) {
            return 0;
        }
        double total = 0;
        for (Item item : items) {
            total += item.getPrix();
        }
        return total / items.size();
    }
    
    /**
     * Retourne la liste des items présents dans le contenant.
     *
     * @return la liste des items
     */
    public List<Item> getItems() {
        return items;
    }
    
    /**
     * Fournit des suggestions d'accords mets-vins en fonction du type de vins présents.
     * Pour chaque vin, si la description contient "rouge", suggère un accord pour un steak.
     * Si elle contient "blanc", suggère un accord pour un poisson.
     *
     * @return une liste de suggestions d'accords
     */
    public List<String> getPairingSuggestions() {
        List<String> suggestions = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Vin) {
                Vin vin = (Vin) item;
                if (vin.getDescription().toLowerCase().contains("rouge")) {
                    suggestions.add("Accord: Steak et vin rouge (" + vin.getDenomination() + ")");
                } else if (vin.getDescription().toLowerCase().contains("blanc")) {
                    suggestions.add("Accord: Poisson et vin blanc (" + vin.getDenomination() + ")");
                }
            }
        }
        return suggestions;
    }
}
