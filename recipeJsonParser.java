package com.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

public class recipeJsonParser {
        
    // Parse the JSON response and extract recipe details into a list.
    public static List<String> parseRecipes(String jsonResponse) {
        JsonElement jelement = JsonParser.parseString(jsonResponse);
        List<String> recipes = new ArrayList<>();
        
        // Determine if the response is an object or an array and process accordingly
        if (jelement.isJsonObject()) {
            JsonObject jobject = jelement.getAsJsonObject();
            JsonArray results = jobject.getAsJsonArray("results");  // Used if the response is an object containing an array
            processJsonArray(recipes, results);
        } else if (jelement.isJsonArray()) {
            JsonArray jsonArray = jelement.getAsJsonArray();
            processJsonArray(recipes, jsonArray); // Directly process the JsonArray
        } else {
            System.out.println("No recipes found matching your query.");
        }
        return recipes;
    }
    
    // Process the JsonArray to extract recipe details and add them to the list
    private static void processJsonArray(List<String> recipes, JsonArray jsonArray) {
        if (jsonArray != null && jsonArray.size() > 0) {
            System.out.println("\nRecipes found:");
            System.out.println("--------------");
            int index = 1;
            // Iterate through each element of the JsonArray.
            for (JsonElement element : jsonArray) {
                // Extract various details using safe getter methods.
                JsonObject recipe = element.getAsJsonObject();
                String title = getStringSafe(recipe, "title");
                String image = getStringSafe(recipe, "image");
                int servings = getIntSafe(recipe, "servings");
                int id = getIntSafe(recipe, "id");
                String summary = getStringSafe(recipe, "summary");
                String diets = getArraySafe(recipe, "diets");
                String cuisines = getArraySafe(recipe, "cuisines");
                float spoonacularScore = getFloatSafe(recipe, "spoonacularScore");
                String dishTypes = getArraySafe(recipe, "dishTypes");
                String ingredients = getIngredients(recipe, "extendedIngredients");
                String instructions = getInstructions(recipe, "analyzedInstructions");
                String nutrition = getNutrition(recipe, "nutrition");
                int readyInMinutes = getIntSafe(recipe, "readyInMinutes");
                System.out.println(index + ". " + title + "\n   -ID: " + id + "\n   -" + image + "\n   -" + servings + " servings"+ "\n   -Ready in: " + readyInMinutes + " minutes");
                System.out.println("------------------------------------------------------------------");
                recipes.add(title + " - " + image + " - " + servings + " - " + id + " - " + summary + " - " + diets + " - " + cuisines + " - " + spoonacularScore + " - " + dishTypes + " - " + ingredients + " - " + instructions + " - " + nutrition + " - " + readyInMinutes);
                index++;
            }
        } else {
            System.out.println("\nNo recipes found.");
        }
    }

    // Safely retrieve a String value from a JsonObject, handling potential null or missing values
    private static String getStringSafe(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element != null && !element.isJsonNull()) {
            return element.getAsString();
        }
        return "Not available";
    }

    // Safely retrieve an integer value from a JsonObject
    private static int getIntSafe(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element != null && !element.isJsonNull()) {
            try {
                return element.getAsInt();
            } catch (NumberFormatException e) {
                return 0; // Default value if conversion fails
            }
        }
        return 0; // Default value if not available
    }
    
    // Safely retrieve a float value from a JsonObject, handling potential null or missing values
    private static float getFloatSafe(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element != null && !element.isJsonNull()) {
            try {
                return element.getAsFloat();
            } catch (NumberFormatException e) {
                return 0.0f;
            }
        }
        return 0.0f;
    }

    // Safely retrieve a String representing a list of elements from a JsonArray within a JsonObject
    private static String getArraySafe(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element != null && element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            StringBuilder result = new StringBuilder();
            for (JsonElement je : jsonArray) {
                result.append(je.getAsString()).append(", ");
            }
            if(!result.isEmpty()){
                return result.substring(0, result.length() - 2);
            }
        }
        return "Not available";
    }

    // Extract and format a string containing a list of ingredients from a JsonObject
    private static String getIngredients(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && jsonObject.get(key).isJsonArray()) {
            JsonArray ingredients = jsonObject.getAsJsonArray(key);
            StringBuilder ingredientList = new StringBuilder();
            for (JsonElement element : ingredients) {
                if (element.isJsonObject()) {
                    JsonObject ingredient = element.getAsJsonObject();
                    String ingredientInfo = ingredient.has("original") ? ingredient.get("original").getAsString() : "No ingredient information";
                    ingredientList.append(ingredientInfo).append("; ");
                }
            }
            return ingredientList.toString(); // Format the list of ingredients into a single string
        } else {
            return "No ingredients listed";
        }
    }
    
    // Parse and construct a detailed string containing cooking instructions from a JsonObject
    private static String getInstructions(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && jsonObject.get(key).isJsonArray()) {
            JsonArray instructionsArray = jsonObject.getAsJsonArray(key);
            StringBuilder instructionList = new StringBuilder();
            int stepNumber = 1;

            // Iterate over each instruction object in the array
            for (JsonElement instructionElement : instructionsArray) {
                JsonObject instructionObject = instructionElement.getAsJsonObject();

                // Check if the instruction object contains a "steps" array
                if (instructionObject.has("steps") && instructionObject.get("steps").isJsonArray()) {
                    JsonArray stepsArray = instructionObject.getAsJsonArray("steps");

                    // Iterate over each step in the "steps" array
                    for (JsonElement stepElement : stepsArray) {
                        JsonObject stepObject = stepElement.getAsJsonObject();
                        String step = stepObject.get("step").getAsString();
                        instructionList.append("Step " + stepNumber + ": " + step + "\n");
                        stepNumber++;
                    }
                }
            }

            return instructionList.toString();
        } else {
            return "No instructions listed.";
        }
    }

    // Retrieve and format nutritional information from a JsonObject.
    private static String getNutrition(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && jsonObject.get(key).isJsonObject()) {
            JsonObject nutritionObject = jsonObject.getAsJsonObject(key);
            StringBuilder nutritionList = new StringBuilder();

            // Check if the nutrition object contains a "nutrients" array
            if (nutritionObject.has("nutrients") && nutritionObject.get("nutrients").isJsonArray()) {
                JsonArray nutrientsArray = nutritionObject.getAsJsonArray("nutrients");

                // Iterate over each nutrient in the "nutrients" array
                for (JsonElement nutrientElement : nutrientsArray) {
                    JsonObject nutrientObject = nutrientElement.getAsJsonObject();
                    String name = nutrientObject.get("name").getAsString();
                    double amount = nutrientObject.get("amount").getAsDouble();
                    String unit = nutrientObject.get("unit").getAsString();
                    nutritionList.append(name + ": " + amount + " " + unit + ", ");
                }
            }

            return nutritionList.toString();
        } else {
            return "No nutrients listed.";
        }
    }
}

