package model;

import java.io.File;
import java.util.Date;

/**
 * Classe abstraite représentant un item dans le système de gestion des denrées.
 * Cette classe regroupe les attributs communs à tous les items et implémente l'interface Dessinable.
 *
 * <p>Les attributs incluent : la dénomination, la description, la quantité, l'année de production,
 * la date d'ajout, le prix, ainsi que des attributs supplémentaires (DLC, image, position, phase de vieillissement, note).</p>
 *
 * @author 
 */
public abstract class Item implements Dessinable {
    /** Le nom de l'item. */
    private String denomination;
    /** La description de l'item. */
    private String description;
    /** La quantité disponible. */
    private int quantite;
    /** L'année de production de l'item. */
    private int anneeProduction;
    /** La date d'ajout de l'item. */
    private Date dateAjout;
    /** Le prix de l'item. */
    private double prix;
    
    // Attributs supplémentaires
    /** La date limite de consommation (DLC ou DLUO). */
    private String dlc;
    /** Le chemin de l'image associée à l'item. */
    private String image;
    /** La position de l'item dans le contenant. */
    private String position;
    /** La phase de vieillissement de l'item. */
    private String phaseVieillissement;
    /** La note ou le score de l'item. */
    private double note;

    /**
     * Constructeur de base pour créer un item.
     *
     * @param denomination le nom de l'item
     * @param description la description de l'item
     * @param quantite la quantité disponible
     * @param anneeProduction l'année de production
     * @param dateAjout la date d'ajout
     * @param prix le prix de l'item
     */
    public Item(String denomination, String description, int quantite, int anneeProduction, Date dateAjout, double prix) {
        this.denomination = denomination;
        this.description = description;
        this.quantite = quantite;
        this.anneeProduction = anneeProduction;
        this.dateAjout = dateAjout;
        this.prix = prix;
    }

    // Getters et setters avec leur Javadoc
    /**
     * Retourne la dénomination de l'item.
     * @return la dénomination
     */
    public String getDenomination() { return denomination; }
    public void setDenomination(String denomination) { this.denomination = denomination; }
    
    /**
     * Retourne la description de l'item.
     * @return la description
     */
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    /**
     * Retourne la quantité disponible.
     * @return la quantité
     */
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    /**
     * Retourne l'année de production.
     * @return l'année de production
     */
    public int getAnneeProduction() { return anneeProduction; }
    public void setAnneeProduction(int anneeProduction) { this.anneeProduction = anneeProduction; }
    
    /**
     * Retourne la date d'ajout de l'item.
     * @return la date d'ajout
     */
    public Date getDateAjout() { return dateAjout; }
    public void setDateAjout(Date dateAjout) { this.dateAjout = dateAjout; }
    
    /**
     * Retourne le prix de l'item.
     * @return le prix
     */
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    
    // Getters et setters pour les nouveaux attributs
    public String getDlc() { return dlc; }
    public void setDlc(String dlc) { this.dlc = dlc; }
    
    /**
     * Retourne le chemin de l'image associé à cet item.
     * Si aucun chemin personnalisé n'est défini, renvoie le chemin par défaut.
     *
     * @return le chemin de l'image
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
     * Affiche les informations de base de l'item dans la console.
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
