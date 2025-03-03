package view;

import model.Contenant;
import model.ContenantObserver;
import model.Item;
import model.Vin;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
     * Constructeur qui initialise l'interface et enregistre l'observateur.
     *
     * @param contenant le contenant à gérer.
     */
    public MainFrame(Contenant contenant) {
        this.contenant = contenant;
        this.contenant.addObserver(this);
        initUI();
        update(); // Mise à jour initiale
    }

    /**
     * Initialise l'interface utilisateur.
     */
    private void initUI() {
        setTitle("Gestion de Denrées");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Titre stylisé
        JLabel lblTitle = new JLabel("<html><center><h1>Cave à vin</h1></center></html>");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(50, 50, 150));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Création du tableau avec une colonne Image en première position
        String[] columnNames = {"Image", "Dénomination", "Description", "Quantité", "Prix", "Année", "Date d'ajout"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(60); // Hauteur adaptée pour afficher les images
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // Affectation d'un renderer personnalisé pour la colonne Image (indice 0)
        table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());

        JScrollPane tableScroll = new JScrollPane(table);
        mainPanel.add(tableScroll, BorderLayout.CENTER);

        // Ajout d'un MouseListener pour détecter le double-clic sur une ligne
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-clic
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Récupère la dénomination depuis la deuxième colonne (indice 1)
                        String denomination = (String) tableModel.getValueAt(selectedRow, 1);
                        Item selectedItem = null;
                        for (Item item : contenant.getItems()) {
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

        // Panel des statistiques et suggestions
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

        // Panel des boutons d'action
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Ajouter du vin");
        JButton btnModify = new JButton("Modifier du vin");
        JButton btnRemove = new JButton("Supprimer du vin");
        JButton btnRefresh = new JButton("Rafraîchir");

        btnAdd.addActionListener(e -> addItemDialog());
        btnModify.addActionListener(e -> modifySelectedItem());
        btnRemove.addActionListener(e -> removeSelectedItem());
        btnRefresh.addActionListener(e -> update());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnModify);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnRefresh);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Panel regroupant les statistiques et les boutons, placé en bas (SOUTH)
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(statsPanel);
        southPanel.add(buttonPanel);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Affiche une boîte de dialogue pour ajouter un nouvel item (ici, un Vin).
     */
    private void addItemDialog() {
        // Champs de base
        JTextField txtDenom = new JTextField();
        JTextField txtDesc = new JTextField();
        JTextField txtQuantite = new JTextField();
        JTextField txtPrix = new JTextField();
        JTextField txtAnnee = new JTextField();
        JTextField txtCepage = new JTextField();
        JTextField txtRegion = new JTextField();
        JTextField txtDLC = new JTextField();
        
        // Champ pour l'image et bouton pour choisir l'image
        JTextField txtImage = new JTextField();
        JButton btnChoisirImage = new JButton("Choisir Image");
        btnChoisirImage.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                txtImage.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        
        // Nouveaux champs
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
            btnChoisirImage,
            "Position dans le contenant:", txtPosition,
            "Phase de vieillissement:", txtPhase,
            "Note/score:", txtNote
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Ajouter un vin", JOptionPane.OK_CANCEL_OPTION);
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
                
                // Création d'un Vin avec la date d'ajout actuelle
                Vin newVin = new Vin(denom, desc, quantite, annee, new Date(), prix, cepage, region);
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
     * Permet de modifier l'item sélectionné dans le tableau.
     */
    private void modifySelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un vin à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Récupérer la dénomination depuis la colonne "Dénomination" (indice 1)
        String denomination = (String) tableModel.getValueAt(selectedRow, 1);
        Item itemToModify = null;
        for (Item item : contenant.getItems()) {
            if (item.getDenomination().equals(denomination)) {
                itemToModify = item;
                break;
            }
        }
        if (itemToModify == null) {
            JOptionPane.showMessageDialog(this, "Vin introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
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
            JButton btnChoisirImage = new JButton("Choisir Image");
            btnChoisirImage.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                int opt = chooser.showOpenDialog(this);
                if (opt == JFileChooser.APPROVE_OPTION) {
                    txtImage.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            });
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
                btnChoisirImage,
                "Position dans le contenant:", txtPosition,
                "Phase de vieillissement:", txtPhase,
                "Note/score:", txtNote
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Modifier le vin", JOptionPane.OK_CANCEL_OPTION);
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
            JOptionPane.showMessageDialog(this, "Modification non supportée pour ce vin.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Supprime l'item sélectionné dans le tableau.
     */
    private void removeSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String denomination = (String) tableModel.getValueAt(selectedRow, 1);
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
     * Affiche toutes les informations de l'item dans un dialogue, incluant l'image.
     *
     * @param item l'item dont on souhaite afficher les détails.
     */
    private void showItemDetails(Item item) {
        // Panneau regroupant texte et image
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        StringBuilder details = new StringBuilder();
        details.append("Dénomination : ").append(item.getDenomination()).append("\n");
        details.append("Description : ").append(item.getDescription()).append("\n");
        details.append("Quantité : ").append(item.getQuantite()).append("\n");
        details.append("Prix : ").append(item.getPrix()).append("\n");
        details.append("Année de production : ").append(item.getAnneeProduction()).append("\n");
        details.append("Date d'ajout : ").append(new SimpleDateFormat("dd/MM/yyyy").format(item.getDateAjout())).append("\n");
        details.append("DLC : ").append(item.getDlc()).append("\n");
        details.append("Position : ").append(item.getPosition()).append("\n");
        details.append("Phase de vieillissement : ").append(item.getPhaseVieillissement()).append("\n");
        details.append("Note/Score : ").append(item.getNote()).append("\n");
        if (item instanceof Vin) {
            Vin vin = (Vin) item;
            details.append("Cépage : ").append(vin.getCepage()).append("\n");
            details.append("Région : ").append(vin.getRegion()).append("\n");
        }
        detailsArea.setText(details.toString());

        // Affichage de l'image dans un JLabel redimensionné
        ImageIcon icon = new ImageIcon(item.getImage());
        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        JLabel imgLabel = new JLabel(icon);
        imgLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        panel.add(imgLabel, BorderLayout.EAST);

        JOptionPane.showMessageDialog(this, panel, "Détails du vin", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Met à jour le tableau et les statistiques en affichant les items du contenant.
     */
    @Override
    public void update() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Item item : contenant.getItems()) {
            Object[] row = {
                item.getImage(),            // Colonne Image
                item.getDenomination(),
                item.getDescription(),
                item.getQuantite(),
                item.getPrix(),
                item.getAnneeProduction(),
                sdf.format(item.getDateAjout())
            };
            tableModel.addRow(row);
        }
        lblTotalValue.setText("Valeur Totale: " + contenant.getValeurTotale());
        lblAveragePrice.setText("Prix Moyen: " + contenant.getPrixMoyen());
        List<String> suggestions = contenant.getPairingSuggestions();
        StringBuilder sb = new StringBuilder();
        for (String s : suggestions) {
            sb.append(s).append("\n");
        }
        txtPairings.setText(sb.toString());
    }

    /**
     * Renderer personnalisé pour afficher une image dans une cellule de tableau.
     */
    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            if (value != null) {
                ImageIcon icon = new ImageIcon((String) value);
                Image img = icon.getImage();
                Image scaled = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
            }
            return label;
        }
    }
}
