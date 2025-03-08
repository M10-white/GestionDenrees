package dao;

import model.Item;
import model.Vin;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Classe DAO pour la persistance des items via un fichier JSON.
 *
 * <p>Cette implémentation gère la sérialisation et la désérialisation
 * des items dans un fichier JSON sans utiliser de bibliothèque externe.</p>
 */
public class ItemDAO {
    /** Chemin du fichier JSON de persistance. */
    private static final String FILE_PATH = "items.json";

    /**
     * Récupère la liste des items depuis le fichier JSON.
     *
     * @return une liste d'items
     */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return items;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString().trim();
            // Traitement simplifié pour un tableau JSON
            if (json.startsWith("[") && json.endsWith("]")) {
                json = json.substring(1, json.length() - 1).trim();
                if (json.isEmpty()) {
                    return items;
                }
                String[] objectStrings = json.split("\\},\\s*\\{");
                for (int i = 0; i < objectStrings.length; i++) {
                    String objStr = objectStrings[i];
                    if (!objStr.startsWith("{")) {
                        objStr = "{" + objStr;
                    }
                    if (!objStr.endsWith("}")) {
                        objStr = objStr + "}";
                    }
                    Item item = parseItem(objStr);
                    if (item != null) {
                        items.add(item);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Sérialise la liste des items en chaîne JSON.
     *
     * @param items la liste des items à sérialiser
     * @return la chaîne JSON représentant les items
     */
    private String serializeItems(List<Item> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < items.size(); i++) {
            sb.append(serializeItem(items.get(i)));
            if (i < items.size() - 1) {
                sb.append(",\n");
            }
        }
        sb.append("\n]");
        return sb.toString();
    }

    /**
     * Sérialise un item en une chaîne JSON.
     * Pour cet exemple, on suppose que l'item est un Vin.
     *
     * @param item l'item à sérialiser
     * @return la chaîne JSON représentant l'item
     */
    private String serializeItem(Item item) {
        StringBuilder sb = new StringBuilder();
        sb.append("  {");
        sb.append("\"denomination\":\"").append(item.getDenomination()).append("\",");
        sb.append("\"description\":\"").append(item.getDescription()).append("\",");
        sb.append("\"quantite\":").append(item.getQuantite()).append(",");
        sb.append("\"anneeProduction\":").append(item.getAnneeProduction()).append(",");
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(item.getDateAjout());
        sb.append("\"dateAjout\":\"").append(dateStr).append("\",");
        sb.append("\"prix\":").append(item.getPrix()).append(",");
        sb.append("\"dlc\":\"").append(item.getDlc() != null ? item.getDlc() : "").append("\",");
        sb.append("\"image\":\"").append(item.getImage() != null ? item.getImage() : "").append("\",");
        sb.append("\"position\":\"").append(item.getPosition() != null ? item.getPosition() : "").append("\",");
        sb.append("\"phaseVieillissement\":\"").append(item.getPhaseVieillissement() != null ? item.getPhaseVieillissement() : "").append("\",");
        sb.append("\"note\":").append(item.getNote()).append(",");
        if (item instanceof Vin) {
            Vin vin = (Vin) item;
            sb.append("\"cepage\":\"").append(vin.getCepage() != null ? vin.getCepage() : "").append("\",");
            sb.append("\"region\":\"").append(vin.getRegion() != null ? vin.getRegion() : "").append("\"");
        } else {
            sb.append("\"cepage\":\"\",");
            sb.append("\"region\":\"\"");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Sauvegarde la liste complète des items dans le fichier JSON.
     *
     * @param items la liste des items à sauvegarder
     */
    private void saveAllItems(List<Item> items) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write(serializeItems(items));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse une chaîne JSON représentant un item.
     *
     * @param jsonObject la chaîne JSON de l'item
     * @return l'item parsé, ou null en cas d'erreur
     */
    private Item parseItem(String jsonObject) {
        jsonObject = jsonObject.trim();
        if (jsonObject.startsWith("{") && jsonObject.endsWith("}")) {
            jsonObject = jsonObject.substring(1, jsonObject.length()-1);
        }
        String[] fields = jsonObject.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        Map<String, String> map = new HashMap<>();
        for (String field : fields) {
            String[] parts = field.split(":", 2);
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                if (key.startsWith("\"") && key.endsWith("\"")) {
                    key = key.substring(1, key.length()-1);
                }
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length()-1);
                }
                map.put(key, value);
            }
        }
        try {
            String denomination = map.get("denomination");
            String description = map.get("description");
            int quantite = Integer.parseInt(map.get("quantite"));
            int anneeProduction = Integer.parseInt(map.get("anneeProduction"));
            String dateStr = map.get("dateAjout");
            Date dateAjout = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            double prix = Double.parseDouble(map.get("prix"));
            String dlc = map.get("dlc");
            String image = map.get("image");
            String position = map.get("position");
            String phaseVieillissement = map.get("phaseVieillissement");
            double note = Double.parseDouble(map.get("note"));
            String cepage = map.get("cepage");
            String region = map.get("region");
            Vin vin = new Vin(denomination, description, quantite, anneeProduction, dateAjout, prix, cepage, region);
            vin.setDlc(dlc);
            vin.setImage(image);
            vin.setPosition(position);
            vin.setPhaseVieillissement(phaseVieillissement);
            vin.setNote(note);
            return vin;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Insère un nouvel item dans le fichier JSON.
     *
     * @param item l'item à insérer
     */
    public void insertItem(Item item) {
        List<Item> items = getAllItems();
        items.add(item);
        saveAllItems(items);
    }

    /**
     * Met à jour un item existant dans le fichier JSON.
     * L'item est identifié par sa dénomination.
     *
     * @param item l'item à mettre à jour
     */
    public void updateItem(Item item) {
        List<Item> items = getAllItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getDenomination().equals(item.getDenomination())) {
                items.set(i, item);
                break;
            }
        }
        saveAllItems(items);
    }

    /**
     * Supprime un item du fichier JSON.
     * L'item est identifié par sa dénomination.
     *
     * @param item l'item à supprimer
     */
    public void deleteItem(Item item) {
        List<Item> items = getAllItems();
        items.removeIf(i -> i.getDenomination().equals(item.getDenomination()));
        saveAllItems(items);
    }
}
