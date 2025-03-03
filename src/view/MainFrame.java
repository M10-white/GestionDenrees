package view;

import model.Contenant;
import model.Item;
import model.Vin;
import model.ContenantObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Fenêtre principale de l'interface graphique pour la gestion des denrées.
 */
public class MainFrame extends JFrame implements ContenantObserver {
    private Contenant contenant;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblTotalValue;
    private JLabel lblAveragePrice;
    private JTextArea txtPairings;
    
    /**
     * Constructeur de MainFrame.
     *
     * @param contenant le contenant à gérer
     */
    public MainFrame(Contenant contenant) {
        this.contenant = contenant;
        this.contenant.addObserver(this);
        initUI();
        update(); // Mise à jour initiale
    }
    
    /**
     * Affiche toutes les informations de l'item dans une boîte de dialogue.
     *
     * @param item l'item dont on souhaite afficher les détails
     */
    private void showItemDetails(model.Item item) {
        StringBuilder details = new StringBuilder();
        details.append("Dénomination : ").append(item.getDenomination()).append("\n");
        details.append("Description : ").append(item.getDescription()).append("\n");
        details.append("Quantité : ").append(item.getQuantite()).append("\n");
        details.append("Prix : ").append(item.getPrix()).append("\n");
        details.append("Année de production : ").append(item.getAnneeProduction()).append("\n");
        details.append("Date d'ajout : ").append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(item.getDateAjout())).append("\n");
        details.append("DLC : ").append(item.getDlc()).append("\n");
        details.append("Image : ").append(item.getImage()).append("\n");
        details.append("Position dans le contenant : ").append(item.getPosition()).append("\n");
        details.append("Phase de vieillissement : ").append(item.getPhaseVieillissement()).append("\n");
        details.append("Note/Score : ").append(item.getNote()).append("\n");
        
        // Si l'item est de type Vin, ajouter les attributs spécifiques
        if (item instanceof model.Vin) {
            model.Vin vin = (model.Vin) item;
            details.append("Cépage : ").append(vin.getCepage()).append("\n");
            details.append("Région : ").append(vin.getRegion()).append("\n");
        }
        
        JOptionPane.showMessageDialog(this, details.toString(), "Détails de l'item", JOptionPane.INFORMATION_MESSAGE);
    }

    
    /**
     * Initialise l'interface utilisateur avec un style amélioré.
     */
    private void initUI() {
        setTitle("Gestion de Denrées");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel pour le titre stylisé
        JLabel lblTitle = new JLabel("<html><center><h1>Cave à vin</h1></center></html>");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(50, 50, 150));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Layout principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);
        
        // Ajout du titre en haut
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Panel pour la table des items (centre)
        String[] columnNames = {"Dénomination", "Description", "Quantité", "Prix", "Année", "Date d'ajout"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // rendre la table non éditable
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        JScrollPane tableScroll = new JScrollPane(table);
        mainPanel.add(tableScroll, BorderLayout.CENTER);
        
        // Panel pour les statistiques et suggestions
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        lblTotalValue = new JLabel("Valeur Totale: ");
        lblTotalValue.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblAveragePrice = new JLabel("Prix Moyen: ");
        lblAveragePrice.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtPairings = new JTextArea();
        txtPairings.setEditable(false);
        txtPairings.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtPairings.setBorder(BorderFactory.createTitledBorder("Suggestions d'accords"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statsPanel.add(lblTotalValue);
        statsPanel.add(lblAveragePrice);
        statsPanel.add(new JScrollPane(txtPairings));
        
        // Panel pour les boutons d'action
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Ajouter Item");
        JButton btnModify = new JButton("Modifier Item");
        JButton btnRemove = new JButton("Supprimer Item");
        JButton btnRefresh = new JButton("Rafraîchir");
        
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemDialog();
            }
        });
        
        btnModify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifySelectedItem();
            }
        });
        
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedItem();
            }
        });
        
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Détection d'un double-clic
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Récupérer la dénomination (première colonne)
                        String denomination = (String) tableModel.getValueAt(selectedRow, 0);
                        model.Item selectedItem = null;
                        for (model.Item item : contenant.getItems()) {
                            if (item.getDenomination().equals(denomination)) {
                                selectedItem = item;
                                break;
                            }
                        }
                        if (selectedItem != null) {
                            showItemDetails(selectedItem);
                        }
                    }
                }
            }
        });
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnModify);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnRefresh);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Regrouper statsPanel et buttonPanel dans un conteneur commun
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(statsPanel);
        southPanel.add(buttonPanel);
        
        mainPanel.add(southPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Affiche une boîte de dialogue pour ajouter un nouvel item (ici, un vin).
     */
    private void addItemDialog() {
        // Champs de base pour un vin
        JTextField txtDenom = new JTextField();
        JTextField txtDesc = new JTextField();
        JTextField txtQuantite = new JTextField();
        JTextField txtPrix = new JTextField();
        JTextField txtAnnee = new JTextField();
        JTextField txtCepage = new JTextField();
        JTextField txtRegion = new JTextField();
        // Nouveaux champs
        JTextField txtDLC = new JTextField();
        JTextField txtImage = new JTextField();
        JTextField txtPosition = new JTextField();
        JTextField txtPhase = new JTextField();
        JTextField txtNote = new JTextField();
        
        Object[] message = {
            "Dénomination:", txtDenom,
            "Description (rouge/blanc):", txtDesc,
            "Quantité:", txtQuantite,
            "Prix:", txtPrix,
            "Année de production:", txtAnnee,
            "Cépage:", txtCepage,
            "Région:", txtRegion,
            "DLC:", txtDLC,
            "Image (chemin):", txtImage,
            "Position dans le contenant:", txtPosition,
            "Phase de vieillissement:", txtPhase,
            "Note/score:", txtNote
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Ajouter un Vin", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String denom = txtDenom.getText();
                String desc = txtDesc.getText();
                int quantite = Integer.parseInt(txtQuantite.getText());
                double prix = Double.parseDouble(txtPrix.getText());
                int annee = Integer.parseInt(txtAnnee.getText());
                String cepage = txtCepage.getText();
                String region = txtRegion.getText();
                String dlc = txtDLC.getText();
                String imagePath = txtImage.getText();
                String position = txtPosition.getText();
                String phase = txtPhase.getText();
                double note = Double.parseDouble(txtNote.getText());
                
                // Création d'un vin avec les informations de base
                model.Vin newVin = new model.Vin(denom, desc, quantite, annee, new Date(), prix, cepage, region);
                // Affectation des nouveaux attributs
                newVin.setDlc(dlc);
                newVin.setImage(imagePath);
                newVin.setPosition(position);
                newVin.setPhaseVieillissement(phase);
                newVin.setNote(note);
                
                contenant.ajouterItem(newVin);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erreur de format dans les champs numériques.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Permet de modifier l'item sélectionné dans la table.
     */
    private void modifySelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un item à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Récupérer la dénomination de l'item sélectionné
        String denomination = (String) tableModel.getValueAt(selectedRow, 0);
        Item itemToModify = null;
        for (Item item : contenant.getItems()) {
            if (item.getDenomination().equals(denomination)) {
                itemToModify = item;
                break;
            }
        }
        if (itemToModify == null) {
            JOptionPane.showMessageDialog(this, "Item introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Modification pour les vins uniquement
        if (itemToModify instanceof Vin) {
            Vin vin = (Vin) itemToModify;
            JTextField txtDenom = new JTextField(vin.getDenomination());
            JTextField txtDesc = new JTextField(vin.getDescription());
            JTextField txtQuantite = new JTextField(String.valueOf(vin.getQuantite()));
            JTextField txtPrix = new JTextField(String.valueOf(vin.getPrix()));
            JTextField txtAnnee = new JTextField(String.valueOf(vin.getAnneeProduction()));
            JTextField txtCepage = new JTextField(vin.getCepage());
            JTextField txtRegion = new JTextField(vin.getRegion());
            JTextField txtDLC = new JTextField(vin.getDlc());
            JTextField txtImage = new JTextField(vin.getImage());
            JTextField txtPosition = new JTextField(vin.getPosition());
            JTextField txtPhase = new JTextField(vin.getPhaseVieillissement());
            JTextField txtNote = new JTextField(String.valueOf(vin.getNote()));
            
            Object[] message = {
                "Dénomination:", txtDenom,
                "Description (rouge/blanc):", txtDesc,
                "Quantité:", txtQuantite,
                "Prix:", txtPrix,
                "Année de production:", txtAnnee,
                "Cépage:", txtCepage,
                "Région:", txtRegion,
                "DLC:", txtDLC,
                "Image (chemin):", txtImage,
                "Position dans le contenant:", txtPosition,
                "Phase de vieillissement:", txtPhase,
                "Note/score:", txtNote
            };
            
            int option = JOptionPane.showConfirmDialog(this, message, "Modifier le Vin", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    vin.setDenomination(txtDenom.getText());
                    vin.setDescription(txtDesc.getText());
                    vin.setQuantite(Integer.parseInt(txtQuantite.getText()));
                    vin.setPrix(Double.parseDouble(txtPrix.getText()));
                    vin.setAnneeProduction(Integer.parseInt(txtAnnee.getText()));
                    vin.setCepage(txtCepage.getText());
                    vin.setRegion(txtRegion.getText());
                    vin.setDlc(txtDLC.getText());
                    vin.setImage(txtImage.getText());
                    vin.setPosition(txtPosition.getText());
                    vin.setPhaseVieillissement(txtPhase.getText());
                    vin.setNote(Double.parseDouble(txtNote.getText()));
                    update();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur de format dans les champs numériques.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Modification non supportée pour cet item.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Supprime l'item sélectionné dans la table.
     */
    private void removeSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Récupérer la dénomination de l'item sélectionné
            String denomination = (String) tableModel.getValueAt(selectedRow, 0);
            Item toRemove = null;
            for (Item item : contenant.getItems()) {
                if (item.getDenomination().equals(denomination)) {
                    toRemove = item;
                    break;
                }
            }
            if (toRemove != null) {
                contenant.supprimerItem(toRemove);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un vin à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Met à jour la table des items et les statistiques.
     */
    @Override
    public void update() {
        // Mise à jour de la table
        tableModel.setRowCount(0); // vider la table
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Item item : contenant.getItems()) {
            Object[] row = {item.getDenomination(), item.getDescription(), item.getQuantite(), item.getPrix(), item.getAnneeProduction(), sdf.format(item.getDateAjout())};
            tableModel.addRow(row);
        }
        // Mise à jour des statistiques
        lblTotalValue.setText("Valeur Totale: " + contenant.getValeurTotale());
        lblAveragePrice.setText("Prix Moyen: " + contenant.getPrixMoyen());
        
        // Mise à jour des suggestions d'accords
        List<String> suggestions = contenant.getPairingSuggestions();
        StringBuilder sb = new StringBuilder();
        for (String s : suggestions) {
            sb.append(s).append("\n");
        }
        txtPairings.setText(sb.toString());
    }
}
