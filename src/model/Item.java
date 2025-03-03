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
    
    // Nouveaux attributs
    private String dlc;
    private String image; // Permet de stocker un chemin d'image personnalisé
    private String position; // Position dans le contenant
    private String phaseVieillissement;
    private double note; // Note/score

    /**
     * Constructeur de base pour un item.
     *
     * @param denomination nom de l'item
     * @param description description de l'item
     * @param quantite quantité disponible
     * @param anneeProduction année de production
     * @param dateAjout date d'ajout
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
    
    // Getters et setters pour les nouveaux attributs
    public String getDlc() { return dlc; }
    public void setDlc(String dlc) { this.dlc = dlc; }
    
    /**
     * Si un chemin d'image personnalisé a été défini, il sera utilisé ; sinon, on génère un chemin par défaut.
     */
    @Override
    public String getImage() {
        if (image != null && !image.isEmpty())
            return image;
        return "images" + File.separator + getClass().getSimpleName() + ".png";
    }
    public void setImage(String image) { this.image = image; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getPhaseVieillissement() { return phaseVieillissement; }
    public void setPhaseVieillissement(String phaseVieillissement) { this.phaseVieillissement = phaseVieillissement; }
    
    public double getNote() { return note; }
    public void setNote(double note) { this.note = note; }
    
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
        if (dlc != null && !dlc.isEmpty()) System.out.println("DLC : " + dlc);
        if (position != null && !position.isEmpty()) System.out.println("Position : " + position);
        if (phaseVieillissement != null && !phaseVieillissement.isEmpty()) System.out.println("Phase de vieillissement : " + phaseVieillissement);
        if (note != 0) System.out.println("Note/Score : " + note);
    }
}
