package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

public class searchByDiet {

    private static final String API_KEY = "42c073de1b0e477089808c29c9c27139"; // API Key included as requested
    private static final List<String> supportedDiets = Arrays.asList(
            "gluten-free", "ketogenic", "vegetarian", "lacto-vegetarian", 
            "ovo-vegetarian", "vegan", "pescetarian", "paleo", 
            "primal", "low-fodmap", "whole30");
    
    public void searchDiet() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Here is a list of supported diets: \n- Gluten-Free \n- Ketogenic \n- Vegetarian \n- Lacto-Vegetarian \n- Ovo-Vegetarian \n- Vegan \n- Pescetarian \n- Paleo \n- Primal \n- Low-FODMAP \n- Whole30\n");
        System.out.println("Enter your diet:");
        String diet = scanner.nextLine().toLowerCase();

        //check user input
        if (!supportedDiets.contains(diet)) {
            System.out.println("The entered diet is not supported.");
            return; // Exit the method if diet is not supported
        }
        
        int page = 1;
        int resultsPerPage = 10;
        boolean continueSearch = true;
        while (continueSearch) {
            int offset = (page - 1) * resultsPerPage;
            String requestURL = String.format(
                    "https://api.spoonacular.com/recipes/complexSearch?apiKey=%s&diet=%s&number=%d&offset=%d&addRecipeInformation=true&fillIngredients=true&addRecipeInstructions=true&addRecipeNutrition=true",
                    API_KEY, diet, resultsPerPage, offset);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .GET()
                .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    String jsonResponse = response.body();
                    if (jsonResponse != null) {
                        List<String> recipes = recipeJsonParser.parseRecipes(jsonResponse);
                        if (!recipes.isEmpty()) {
                            recipeInteraction.handleRecipeSavingAndViewing(scanner, recipes.toArray(new String[0]), new recipeSaver());
                            System.out.println("\n\nDo you want to fetch more recipes? (yes/no)");
                            String answer = scanner.nextLine();
                            if ("yes".equalsIgnoreCase(answer)) {
                                page++;
                            } else {
                                continueSearch = false;
                            }
                        } else {
                            System.out.println("No recipes found matching your query.");
                            continueSearch = false;
                        }
                    }
                } else {
                    System.out.println("Failed to fetch recipes: HTTP error code : " + response.statusCode());
                    continueSearch = false;
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("An error occurred while requesting recipes: " + e.getMessage());
                continueSearch = false;
            }
        }
    }
}
