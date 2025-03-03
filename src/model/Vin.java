package model;

import java.util.Date;

/**
 * Classe représentant un vin dans la cave.
 */
public class Vin extends Item {
    // Attributs spécifiques au vin
    private String cepage;
    private String region;
    
    /**
     * Constructeur pour un vin.
     *
     * @param denomination nom du vin
     * @param description description (ex : rouge, blanc, etc.)
     * @param quantite quantité disponible
     * @param anneeProduction année de production
     * @param dateAjout date d'ajout dans la cave
     * @param prix prix du vin
     * @param cepage cépage du vin
     * @param region région d'origine
     */
    public Vin(String denomination, String description, int quantite, int anneeProduction, Date dateAjout, double prix, String cepage, String region) {
        super(denomination, description, quantite, anneeProduction, dateAjout, prix);
        this.cepage = cepage;
        this.region = region;
    }
    
    public String getCepage() { return cepage; }
    public void setCepage(String cepage) { this.cepage = cepage; }
    
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    
    /**
     * Affiche les informations complètes du vin.
     */
    @Override
    public void afficher() {
        super.afficher();
        System.out.println("Cépage : " + cepage);
        System.out.println("Région : " + region);
    }
}
