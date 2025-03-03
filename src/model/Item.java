package model;

import java.io.File;
import java.util.Date;

/**
 * Classe abstraite représentant un item dans le système de gestion des denrées.
 */
public abstract class Item implements Dessinable {
    // Attributs de base
    private String denomination;
    private String description;
    private int quantite;
    private int anneeProduction;
    private Date dateAjout;
    private double prix;
    
    // Attributs supplémentaires
    private String dlc; // DLC ou DLUO
    private String position; // Position dans le contenant
    private String phaseVieillissement;
    private String annotations;
    private double note;
    private String caracteristiquesGustatives;
    private String drinkingWindow; // Période conseillée pour consommer l'item
    
    /**
     * Constructeur de base pour un item.
     *
     * @param denomination nom de l'item
     * @param description description (ex : rouge, blanc, champagne, etc.)
     * @param quantite quantité disponible
     * @param anneeProduction année de production
     * @param dateAjout date d'ajout dans le système
     * @param prix prix de l'item
     */
    public Item(String denomination, String description, int quantite, int anneeProduction, Date dateAjout, double prix) {
        this.denomination = denomination;
        this.description = description;
        this.quantite = quantite;
        this.anneeProduction = anneeProduction;
        this.dateAjout = dateAjout;
        this.prix = prix;
    }
    
    // Getters et setters pour les attributs de base
    public String getDenomination() { return denomination; }
    public void setDenomination(String denomination) { this.denomination = denomination; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    public int getAnneeProduction() { return anneeProduction; }
    public void setAnneeProduction(int anneeProduction) { this.anneeProduction = anneeProduction; }
    
    public Date getDateAjout() { return dateAjout; }
    public void setDateAjout(Date dateAjout) { this.dateAjout = dateAjout; }
    
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    
    // Getters et setters pour les attributs supplémentaires
    public String getDlc() { return dlc; }
    public void setDlc(String dlc) { this.dlc = dlc; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getPhaseVieillissement() { return phaseVieillissement; }
    public void setPhaseVieillissement(String phaseVieillissement) { this.phaseVieillissement = phaseVieillissement; }
    
    public String getAnnotations() { return annotations; }
    public void setAnnotations(String annotations) { this.annotations = annotations; }
    
    public double getNote() { return note; }
    public void setNote(double note) { this.note = note; }
    
    public String getCaracteristiquesGustatives() { return caracteristiquesGustatives; }
    public void setCaracteristiquesGustatives(String caracteristiquesGustatives) { this.caracteristiquesGustatives = caracteristiquesGustatives; }
    
    public String getDrinkingWindow() { return drinkingWindow; }
    public void setDrinkingWindow(String drinkingWindow) { this.drinkingWindow = drinkingWindow; }
    
    /**
     * Retourne le chemin de l'image associée à cet item.
     *
     * @return chemin de l'image basé sur le nom de la classe
     */
    @Override
    public String getImage() {
        return "images" + File.separator + getClass().getSimpleName() + ".png";
    }
    
    /**
     * Affiche les informations de l'item dans la console.
     */
    public void afficher() {
        System.out.println("Dénomination : " + denomination);
        System.out.println("Description : " + description);
        System.out.println("Quantité : " + quantite);
        System.out.println("Année de production : " + anneeProduction);
        System.out.println("Date d'ajout : " + dateAjout);
        System.out.println("Prix : " + prix);
        if(dlc != null) System.out.println("DLC/DLUO : " + dlc);
        if(position != null) System.out.println("Position : " + position);
        if(phaseVieillissement != null) System.out.println("Phase de vieillissement : " + phaseVieillissement);
        if(annotations != null) System.out.println("Annotations : " + annotations);
        if(note != 0) System.out.println("Note/Score : " + note);
        if(caracteristiquesGustatives != null) System.out.println("Caractéristiques gustatives : " + caracteristiquesGustatives);
        if(drinkingWindow != null) System.out.println("Drinking Window : " + drinkingWindow);
    }
}
