package model;

import java.util.Date;

/**
 * Classe représentant un vin dans la cave.
 * Elle hérite de la classe abstraite Item et ajoute des attributs spécifiques aux vins.
 *
 * <p>Les attributs spécifiques incluent le cépage et la région de production.</p>
 */
public class Vin extends Item {
    /** Le cépage utilisé dans le vin. */
    private String cepage;
    /** La région d'origine du vin. */
    private String region;
    
    /**
     * Constructeur pour créer un objet Vin.
     *
     * @param denomination le nom du vin
     * @param description la description du vin (ex : rouge, blanc)
     * @param quantite la quantité disponible
     * @param anneeProduction l'année de production
     * @param dateAjout la date d'ajout du vin
     * @param prix le prix du vin
     * @param cepage le cépage du vin
     * @param region la région d'origine du vin
     */
    public Vin(String denomination, String description, int quantite, int anneeProduction, Date dateAjout, double prix, String cepage, String region) {
        super(denomination, description, quantite, anneeProduction, dateAjout, prix);
        this.cepage = cepage;
        this.region = region;
    }
    
    /**
     * Retourne le cépage du vin.
     * @return le cépage
     */
    public String getCepage() { return cepage; }
    public void setCepage(String cepage) { this.cepage = cepage; }
    
    /**
     * Retourne la région d'origine du vin.
     * @return la région
     */
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    
    /**
     * Affiche les informations complètes du vin, incluant les attributs spécifiques.
     */
    @Override
    public void afficher() {
        super.afficher();
        System.out.println("Cépage : " + cepage);
        System.out.println("Région : " + region);
    }
}
