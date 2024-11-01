package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class searchByTitle {
    private static final String API_KEY = "41c2b73f2580458ea8e845483f07dbee";  

    public void searchTitle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your query: ");
        String findQuery = scanner.nextLine();

        int page = 1;
        boolean continueSearch = true;
        while (continueSearch) { //loop til user decides to stop fetching recipes 
            String jsonResponse = fetchRecipes(findQuery, page);
            if (jsonResponse != null) { //ensures api response is not null
                List<String> recipes = recipeJsonParser.parseRecipes(jsonResponse);
                if (!recipes.isEmpty()) {
                    recipeInteraction.handleRecipeSavingAndViewing(scanner, recipes.toArray(new String[0]), new recipeSaver()); //handles user interaction for recipe saving and viewing
                    System.out.println("\n\nDo you want to fetch more recipes? (yes/no)");
                    String answer = scanner.nextLine();
                    if ("yes".equalsIgnoreCase(answer)) {
                        page++;
                    } else {
                        continueSearch = false;
                    }
                } else {
                    System.out.println("Limited Results.");
                    continueSearch = false;
                }
            } else {
                continueSearch = false;
            }
        }
    }

    //method to fetch recipes using HTTP requests
    private String fetchRecipes(String query, int page) {
        int resultsPerPage = 10; 
        int offset = (page - 1) * resultsPerPage;

        //constructs the URL
        String urlString = "https://api.spoonacular.com/recipes/complexSearch?query=" + query
            + "&apiKey=" + API_KEY + "&number=" + resultsPerPage + "&offset=" + offset
            + "&addRecipeInformation=true&fillIngredients=true&addRecipeInstructions=true&addRecipeNutrition=true";
        //checks if connection to API is working 
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); //use get method
            conn.connect(); //connects to server

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) { //checks if connection is successful
                System.out.println("Failed to fetch recipes: HTTP error code : " + responseCode);
                return null;
            }
            
            // Reads the response from the input stream, building it into a single string
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line); //Appends each line of the response to the StringBuilder
            }
            reader.close();
            return response.toString();
        } catch (Exception e) {
            System.err.println("An error occurred while searching for recipes: " + e.getMessage());
            return null; // Returns null if an error occurs during the connection
        }
    }
}
