# Projet de Gestion de Denrées – Cave à Vin

Ce projet Java est une application de gestion de denrées, axée ici sur la gestion d'une cave à vin. Il illustre les principes de la Programmation Orientée Objet (POO) et intègre plusieurs fonctionnalités avancées, telles que :

- L'ajout, la modification et la suppression d'items (vins) via une interface graphique Swing.
- L'affichage d'images associées aux items dans le tableau de bord et dans une fenêtre de détails accessible par double-clic.
- La persistance des données grâce à un système de stockage en JSON.
- L'utilisation de design patterns (par exemple, Observer) pour synchroniser l'interface avec le modèle de données.

## Table des matières
- [Aperçu](#aperçu)
- [Fonctionnalités](#fonctionnalités)
- [Structure du Projet](#structure-du-projet)
- [Installation et Configuration](#installation-et-configuration)
- [Compilation et Exécution](#compilation-et-exécution)
- [Utilisation](#utilisation)
- [Extensions et Améliorations](#extensions-et-améliorations)
- [Crédits](#crédits)

## Aperçu

L'application permet de gérer une cave à vin avec :

- **Ajout d'un vin** : Saisie de tous les attributs (dénomination, description, quantité, année de production, prix, DLC, image, position dans le contenant, phase de vieillissement, note/score, cépage et région).
- **Modification** : Modification d'un vin existant via un dialogue pré-rempli.
- **Suppression** : Suppression d'un vin sélectionné.
- **Affichage** : Affichage dans un tableau des informations principales, avec une colonne dédiée aux images. Un double-clic sur une ligne ouvre un dialogue détaillé incluant l'image et toutes les informations du vin.
- **Persistance** : Sauvegarde des données dans un fichier JSON, permettant de restaurer les données lors du prochain lancement.

## Fonctionnalités

### Interface Graphique :
- L'interface est réalisée avec Swing, en utilisant le Look and Feel Nimbus pour une apparence moderne.
- Les composants incluent un tableau avec un renderer personnalisé pour afficher les images, des boutons pour les opérations CRUD, et une zone de statistiques.

### Gestion des Denrées :
- La classe abstraite `Item` définit les attributs communs, et la classe `Vin` étend `Item` pour ajouter des attributs spécifiques aux vins (cépage, région).
- La classe `Contenant` gère une collection d'items et calcule des statistiques (valeur totale, prix moyen).

### Persistance JSON :
- Un DAO (Data Access Object) sous forme de `JsonItemDAO` permet de lire, écrire, mettre à jour et supprimer les items dans un fichier JSON (`items.json`), sans utiliser de bibliothèque externe (même si l'utilisation d'une bibliothèque comme Gson peut être envisagée pour plus de robustesse).

### Design Patterns :
- Le pattern Observer est implémenté via l'interface `ContenantObserver` afin de notifier automatiquement l'interface graphique lors des modifications du contenant.

## Structure du Projet

L'organisation du projet est la suivante :

```
GestionDenrees/
├── src/
│   ├── main/
│   │   ├── Launcher.java         // Lancement en mode console (pour tests)
│   │   └── GUILauncher.java      // Lancement de l'interface graphique
│   │
│   ├── model/
│   │   ├── Dessinable.java       // Interface pour les objets dessinables (méthode getImage())
│   │   ├── Item.java             // Classe abstraite d'un item (attributs communs + nouveaux attributs)
│   │   ├── Vin.java              // Classe concrète représentant un vin, avec attributs spécifiques
│   │   ├── Contenant.java        // Classe gérant la collection d'items et les statistiques
│   │   └── ContenantObserver.java // Interface du pattern Observer pour notifier les mises à jour
│   │
│   ├── view/
│   │   └── MainFrame.java        // Interface graphique principale (tableau, dialogues, etc.)
│   │
│   └── dao/
│       └── JsonItemDAO.java      // Classe DAO pour la persistance des items dans un fichier JSON
│
├── images/                       // Dossier de ressources contenant les images (ex: chateau-margaux.png)
└── items.json                    // Fichier JSON de persistance (sera créé/sauvegardé automatiquement)
```

## Installation et Configuration

### Environnement de développement :
- Installez le JDK (Java Development Kit) version 8 ou ultérieure.
- Utilisez Eclipse, IntelliJ IDEA ou un autre IDE de votre choix pour importer le projet.

### Structure du projet :
- Le code source se trouve dans le dossier `src/` et est organisé en packages : `main`, `model`, `view`, `dao`.
- Le dossier `images/` doit contenir les fichiers image utilisés pour représenter les vins.
- Vérifiez que les chemins d'accès aux images dans les données JSON sont corrects par rapport à la structure de votre projet.

### Dépendances :
- Ce projet n'utilise pas de bibliothèque externe pour la gestion du JSON, car la sérialisation/désérialisation est faite manuellement.
- Si vous souhaitez utiliser une bibliothèque (comme Gson), placez le JAR correspondant dans le dossier `lib/` et ajoutez-le au classpath.

## Compilation et Exécution

### En utilisant Eclipse :
1. **Importer le projet** :
   - Sélectionnez `File > Import > Existing Projects into Workspace` et choisissez le dossier racine du projet.
2. **Vérifier le Build Path** :
   - Assurez-vous que tous les dossiers sources sont bien ajoutés et que, si nécessaire, le dossier `lib/` contenant les JAR est inclus.
3. **Exécuter l'application** :
   - Pour lancer l'interface graphique, exécutez la classe `GUILauncher` (située dans le package `main`).

### En ligne de commande :
1. **Compiler** :
   - Dans le dossier racine du projet, compilez les sources :
   ```bash
   javac -d bin src/main/*.java src/model/*.java src/view/*.java src/dao/*.java
   ```
2. **Exécuter** :
   - Lancez l'application avec :
   ```bash
   java -cp bin main.GUILauncher
   ```

## Utilisation

### Interface Graphique :
L'interface principale affiche une table listant tous les vins avec leurs images, nom, description, quantité, etc.

- **Ajouter un vin** : Cliquez sur "Ajouter du vin" et remplissez le formulaire. Un bouton "Choisir Image" permet de sélectionner un fichier image via un JFileChooser.
- **Modifier un vin** : Sélectionnez une ligne, cliquez sur "Modifier du vin". Le formulaire s'ouvrira avec les données pré-remplies.
- **Supprimer un vin** : Sélectionnez une ligne et cliquez sur "Supprimer du vin".
- **Détails** : Double-cliquez sur une ligne pour afficher une fenêtre de détails avec l'image en grand et toutes les informations du vin.
- **Rafraîchir** : Cliquez sur "Rafraîchir" pour recharger les données en mémoire.

### Persistance :
Toute modification (ajout, modification, suppression) est enregistrée dans le fichier `items.json`. Au démarrage, ce fichier est lu pour recharger les données persistantes.

## Crédits

- **Auteur** : Brahim Chaouki (CHKWEBDEV)
- **Formation** : Bachelor 3 EPSI DEVIA FS
