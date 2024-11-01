package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.List;

public class searchByIngredients {

    private static final String API_KEY = "41c2b73f2580458ea8e845483f07dbee";

    public void searchIngredients() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ingredients separated by commas (e.g., tomato,egg):");
        
        int page = 1;
        boolean continueSearch = true;
        while (continueSearch) {
            String ingredients = scanner.nextLine().trim().replaceAll("\\s+", ""); //read clean user input
            
            int resultsPerPage = 10;
            int offset = (page - 1) * resultsPerPage;
            String requestURL = String.format(
                "https://api.spoonacular.com/recipes/complexSearch?apiKey=%s&includeIngredients=%s&number=%d&offset=%d&addRecipeInformation=true&fillIngredients=true&addRecipeInstructions=true&addRecipeNutrition=true",
                API_KEY, ingredients, resultsPerPage, offset);

        HttpClient client = HttpClient.newHttpClient(); // Create a new HTTP client instance
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .GET() // Build the HTTP request with GET method
                .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {  // Check if the HTTP response is successful.
                    String jsonResponse = response.body();

                    // Process the JSON response using a custom parser.
                    if (jsonResponse != null) {
                        List<String> recipes = recipeJsonParser.parseRecipes(jsonResponse); 
                        // Process the JSON response
                        if (!recipes.isEmpty()) {
                            recipeInteraction.handleRecipeSavingAndViewing(scanner, recipes.toArray(new String[0]), new recipeSaver()); // Handle recipe interaction.
                            System.out.println("\n\nDo you want to fetch more recipes? (yes/no)");
                            String answer = scanner.nextLine();
                            if ("yes".equalsIgnoreCase(answer)) {  // If user wants more recipes, increment the page number
                                page++;
                            } else {
                                continueSearch = false;
                            }
                        } else {
                            System.out.println("No recipes found with these ingredients. Try different ingredients.");
                            continueSearch = false;
                        }
                    }
                } else {
                    System.out.println("Failed to fetch recipes: HTTP error code : " + response.statusCode());  // Handle failed HTTP response
                    continueSearch = false;
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("An error occurred while requesting recipes: " + e.getMessage());  // Handle failed HTTP request exceptions
                continueSearch = false;
            }
        }
    }
}

