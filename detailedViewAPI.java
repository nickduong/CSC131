package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class detailedViewAPI {
    private static final String API_KEY = "42c073de1b0e477089808c29c9c27139";

    public void viewAPIDetails() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Enter recipe ID:");
            String id = scanner.nextLine().trim();
            String requestURL = String.format("https://api.spoonacular.com/recipes/%s/information?apiKey=%s&includeNutrition=true", id, API_KEY);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(requestURL)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                showRecipeDetails(response.body());
            } else {
                System.out.println("Failed to fetch recipe details with status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("An error occurred while requesting recipes: " + e.getMessage());
        }
    }

    private void showRecipeDetails(String jsonData) {
        Gson gson = new Gson();
        JsonObject recipeObject = gson.fromJson(jsonData, JsonObject.class);

        System.out.println("\nRecipe Details:");
        System.out.println("____________________");
        System.out.println("\nTitle: " + getValue(recipeObject, "title"));
        System.out.println("\nImage: " + getValue(recipeObject, "image"));
        System.out.println("\nID: " + getValue(recipeObject, "id"));
        System.out.println("\nServings: " + getValue(recipeObject, "servings"));
        System.out.println("\nReady in: " + getValue(recipeObject, "readyInMinutes") + " minutes");
        System.out.println("\nSummary: " + cleanHtml(getValue(recipeObject, "summary")));
        System.out.println("\nDiets: " + arrayToString(recipeObject.getAsJsonArray("diets")));
        System.out.println("\nCuisines: " + arrayToString(recipeObject.getAsJsonArray("cuisines")));
        System.out.println("\nSpoonacular Score: " + getValue(recipeObject, "spoonacularScore"));
        System.out.println("\nDish Types: " + arrayToString(recipeObject.getAsJsonArray("dishTypes")));
        System.out.println("\nIngredients: \n" + getIngredients(recipeObject.getAsJsonArray("extendedIngredients")));
        System.out.println("Instructions: \n" + getInstructions(recipeObject.getAsJsonArray("analyzedInstructions")));
        
        // Adding Nutrition Details
        JsonObject nutrition = recipeObject.getAsJsonObject("nutrition");
        JsonArray nutrients = nutrition.getAsJsonArray("nutrients");
        System.out.println("\nNutrition Details:");
        for (JsonElement nutrientElement : nutrients) {
            JsonObject nutrient = nutrientElement.getAsJsonObject();
            String name = nutrient.get("name").getAsString();
            double amount = nutrient.get("amount").getAsDouble();
            String unit = nutrient.get("unit").getAsString();
            System.out.println("- " + name + ": " + amount + " " + unit);
        }
    }

    private String getValue(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsString();
        }
        return "Not available";
    }

    private String cleanHtml(String htmlString) {
        return htmlString.replaceAll("<[^>]*>", "");
    }

    private String arrayToString(JsonArray jsonArray) {
        if (jsonArray == null || jsonArray.size() == 0) {
            return "Not available";
        }
        StringBuilder result = new StringBuilder();
        for (JsonElement element : jsonArray) {
            result.append(element.getAsString()).append(", ");
        }
        return result.length() > 0 ? result.substring(0, result.length() - 2) : "Not available";
    }

    private String getIngredients(JsonArray ingredients) {
        if (ingredients == null || ingredients.size() == 0) {
            return "Not available";
        }
        StringBuilder ingredientList = new StringBuilder();
        for (JsonElement element : ingredients) {
            JsonObject ingredient = element.getAsJsonObject();
            String detail = ingredient.get("original").getAsString();
            ingredientList.append("- ").append(detail).append("\n");
        }
        return ingredientList.toString();
    }

    private String getInstructions(JsonArray instructionsArray) {
        if (instructionsArray == null || instructionsArray.size() == 0) {
            return "Not available";
        }
        StringBuilder instructions = new StringBuilder();
        for (JsonElement instructionSet : instructionsArray) {
            JsonObject instructionObject = instructionSet.getAsJsonObject();
            JsonArray steps = instructionObject.getAsJsonArray("steps");
            for (JsonElement stepElement : steps) {
                JsonObject step = stepElement.getAsJsonObject();
                instructions.append("- Step ").append(step.get("number").getAsInt()).append(": ").append(step.get("step").getAsString()).append("\n");
            }
        }
        return instructions.toString();
    }
}
