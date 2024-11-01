package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

public class orderByCuisine {
    private static final String MONGODB_URI = "mongodb+srv://nick:csus@csc131.tct5wqu.mongodb.net/";

    public List<String> getRecipeNamesByCuisine(String cuisineType) {
        List<String> recipeNames = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(MONGODB_URI)) {
            MongoCollection<Document> collection = mongoClient.getDatabase("recipeProject").getCollection("spoonacularRecipes");

            cuisineType = cuisineType.toLowerCase();
            FindIterable<Document> result = collection.find();

            for (Document recipe : result) {
                String cuisines = recipe.getString("cuisines");
                if (cuisines != null) {
                    String[] cuisineArray = cuisines.split(", ");
                    for (String cuisine : cuisineArray) {
                        if (cuisine.toLowerCase().contains(cuisineType)) {
                            recipeNames.add(recipe.getString("title"));

                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving recipe names of that cuisine: " + e.getMessage());
        }

        return recipeNames;
    }

    public void viewRecipesByCuisine() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter cuisine type: ");
        String cuisineType = scanner.nextLine().trim();

        List<String> recipeNames = getRecipeNamesByCuisine(cuisineType);

        if (!recipeNames.isEmpty()) {
            int index = 1;
            System.out.println("\n\nCuisine Type: " + cuisineType);
            System.out.println("---------------------------");
            for (String recipeName : recipeNames) {
                System.out.println(index + ". " + recipeName);
                System.out.println("-----------------------------------------");
                index++;
            }
        } else {
            System.out.println("No recipes found for cuisine '" + cuisineType + "'.");
        }
    }
}
