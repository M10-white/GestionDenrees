package model;

/**
 * Interface pour les objets dessinables.
 * Toute classe implémentant cette interface doit fournir une méthode pour obtenir le chemin de l'image associée.
 */
public interface Dessinable {
    /**
     * Retourne le chemin de l'image associée à l'objet.
     *
     * @return le chemin de l'image sous forme de chaîne de caractères
     */
    String getImage();
}
