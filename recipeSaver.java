package com.example;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import java.util.ArrayList;

public class recipeSaver {
    private static final String MONGODB_URI = "mongodb+srv://nick:csus@csc131.tct5wqu.mongodb.net/"; // MongoDB URI for connecting to the database

    // Method to save a recipe document into the MongoDB database.
    public void saveRecipe(String title, String imageUrl, int servings, int id, String summary, String diets, String cuisines, float spoonacularScore, String dishTypes, String ingredients, String instructions, String nutrition, int readyInMinutes) {
        try (MongoClient mongoClient = MongoClients.create(MONGODB_URI)) {  // Try-with-resources to ensure the client is closed after use
            MongoDatabase database = mongoClient.getDatabase("recipeProject");
            MongoCollection<Document> collection = database.getCollection("spoonacularRecipes");

            // Creating a new recipe document with provided parameters.
            Document recipe = new Document("title", title).append("image", imageUrl).append("servings", servings).append("id", id)
                    .append("summary", summary).append("diets", diets).append("cuisines", cuisines).append("spoonacularScore", spoonacularScore)
                    .append("dishTypes", dishTypes).append("ingredients", ingredients).append("instructions", instructions)
                    .append("nutrition", nutrition).append("readyInMinutes", readyInMinutes);
            collection.insertOne(recipe);
            System.out.println("\nRecipe saved: " + title + " with " + servings + " servings");
        } catch (Exception e) {
            System.err.println("Error saving recipe: " + e.getMessage());
        }
    }

    // Method to retrieve all saved recipes from the MongoDB database.
    public List<Document> getSavedRecipes() {
        List<Document> savedRecipes = new ArrayList<>(); // List to store retrieved recipes.
        try (MongoClient mongoClient = MongoClients.create(MONGODB_URI)) {
            MongoDatabase database = mongoClient.getDatabase("recipeProject");
            MongoCollection<Document> collection = database.getCollection("spoonacularRecipes");

            for (Document recipe : collection.find()) {
                savedRecipes.add(recipe); // Add each recipe document to the list.
            }
        } catch (Exception e) {
            System.err.println("Error retrieving recipes: " + e.getMessage());
        }
        return savedRecipes; // Return the list of retrieved recipe documents.
    }
}
