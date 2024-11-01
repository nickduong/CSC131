package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner; 
import java.util.List;

public class searchByID {

    private static final String API_KEY = "41c2b73f2580458ea8e845483f07dbee"; // Your API key

    public void searchID() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the recipe you'd like to find:");
        String recipeId = scanner.nextLine().trim();

        String url = String.format("https://api.spoonacular.com/recipes/%s/information?apiKey=%s&includeNutrition=true", recipeId, API_KEY);

        HttpClient client = HttpClient.newHttpClient(); // Creates a new instance of HttpClient
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build(); // Constructs an HTTP request using the URL

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) { // Checks if the response status code is 200 (OK)
                String jsonResponse = response.body();
                List<String> recipes = recipeJsonParser.parseRecipes("[" + jsonResponse + "]"); // Wrap in array brackets for compatibility
                if (!recipes.isEmpty()) {
                    // Handles viewing and saving of the fetched recipe if any are found.
                    recipeInteraction.handleRecipeSavingAndViewing(scanner, recipes.toArray(new String[0]), new recipeSaver());
                } else {
                    System.out.println("No recipe details found.");
                }
            } else {
                System.out.println("Failed to fetch recipe details with status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching recipe details: " + e.getMessage());
            e.printStackTrace();
        }
    }
}




