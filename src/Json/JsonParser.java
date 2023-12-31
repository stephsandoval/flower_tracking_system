/* Stephanie Sandoval - Caso 02
 * JsonParser - Reads the json file and stores the information
 * Singleton pattern to avoid reading multiple times
 */

package Json;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;


public class JsonParser {
    
    private static JsonParser instance;
    private JsonArray jsonSpecies, jsonStatus;
    private JsonObject jsonTypes;
    private HashMap <String, ArrayList<String>> types;
    private ArrayList <String> species, status;

    private JsonParser (){
        types = new HashMap<>();
        loadJson ();
        species = toArray(jsonSpecies);
        status = toArray(jsonStatus);
        createMap();
    }

    public static synchronized JsonParser getInstance (){
        // only allow one instance of this object
        // this avoids having to read the file multiple times throught the program
        if (instance == null){
            instance = new JsonParser();
        }
        return instance;
    }

    private void loadJson (){
        String filePath = "C:\\Users\\Stephanie\\OneDrive - Estudiantes ITCR\\Semestre II\\Programación Orientada a Objetos\\Caso 02\\Flower Tracking System\\src\\Json\\configuracion.json";
        try (JsonReader reader = Json.createReader(new FileReader(filePath))){
            JsonObject jsonParser = reader.readObject();

            jsonSpecies = jsonParser.getJsonArray("species");
            jsonStatus = jsonParser.getJsonArray("status");
            jsonTypes = jsonParser.getJsonObject("type");

        } catch (Exception e){}
    }

    private ArrayList<String> toArray (JsonArray jsonArray){
        int beginning = 1;
        if (jsonArray == null){
            return null;
        }
        ArrayList<String> array = new ArrayList<>();
        for (JsonValue element : jsonArray){
            String data = element.toString();
            array.add(data.substring(beginning, data.length()-1));
        }
        return array;
    }

    private void createMap (){
        for (String flower : species){
            ArrayList<String> elements = new ArrayList<>();
            elements = toArray(jsonTypes.getJsonArray(flower));
            if (elements != null){
                // make sure the flower species has some type entries in the json
                types.put(flower, elements);
            }
        }
    }

    public ArrayList<String> getSpecies (){
        return this.species;
    }

    public ArrayList<String> getStatus (){
        return this.status;
    }

    public ArrayList<String> getType (String species){
        if (types.containsKey(species)){
            return types.get(species);
        }
        return null;
    }
}