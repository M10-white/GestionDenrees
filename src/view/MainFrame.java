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
        JButton btnRemove = new JButton("Supprimer Item");
        JButton btnRefresh = new JButton("Rafraîchir");
        
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemDialog();
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
        
        buttonPanel.add(btnAdd);
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
        JTextField txtDenom = new JTextField();
        JTextField txtDesc = new JTextField();
        JTextField txtQuantite = new JTextField();
        JTextField txtPrix = new JTextField();
        JTextField txtAnnee = new JTextField();
        JTextField txtCepage = new JTextField();
        JTextField txtRegion = new JTextField();
        
        Object[] message = {
            "Dénomination:", txtDenom,
            "Description (rouge/blanc):", txtDesc,
            "Quantité:", txtQuantite,
            "Prix:", txtPrix,
            "Année de production:", txtAnnee,
            "Cépage:", txtCepage,
            "Région:", txtRegion
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
                
                // Créer un nouveau vin avec la date d'ajout actuelle
                model.Vin newVin = new model.Vin(denom, desc, quantite, annee, new Date(), prix, cepage, region);
                contenant.ajouterItem(newVin);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erreur de format dans les champs numériques.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
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
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un item à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
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
