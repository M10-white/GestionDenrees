package model;

/**
 * Interface pour implémenter l'observateur du contenant.
 * Les classes qui souhaitent être notifiées lors d'une mise à jour du contenant doivent implémenter cette interface.
 */
public interface ContenantObserver {
    /**
     * Méthode appelée lors d'une mise à jour du contenant.
     */
    void update();
}
