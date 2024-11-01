package com.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import java.util.Scanner;

public class updateRecipe {
    // MongoDB connection URI
    private static final String MONGODB_URI = "mongodb+srv://nick:csus@csc131.tct5wqu.mongodb.net/";

    public void update() {
        try {
            viewRecipes ViewRecipes = new viewRecipes();
            ViewRecipes.recipeDetails();

            Scanner scanner = new Scanner(System.in);
            System.out.println("\nEnter the title of the recipe you'd like to update:");
            String title = scanner.nextLine();
            System.out.println("\nEnter the field you want to update:\n(title/image/servings/id/summary/diets/cuisines/spoonacularScore/dishTypes/ingredients/instructions/nutrition/readyInMinutes)");
            String field = scanner.nextLine();
            System.out.println("\nEnter the new value for the field:");
            String value = scanner.nextLine();

            updateDocument(title, field, value);
        }catch(Exception e){
            System.err.println("An error occured while updating a Recipe.");
        }
    }

    // Method to update a recipe document in MongoDB
    private static void updateDocument(String title, String field, String value) {
        try (MongoClient mongoClient = MongoClients.create(MONGODB_URI)) {
            // Connect to MongoDB and access the specified database and collection
            MongoDatabase database = mongoClient.getDatabase("recipeProject");
            MongoCollection<Document> collection = database.getCollection("spoonacularRecipes");

            // Define query to find the document with the given title
            Document query = new Document("title", new Document("$regex", "^" + title + "$").append("$options", "i"));
            
            // Define update to set the specified field to the new value
            Document update = new Document("$set", new Document(field, value));
            
            // Configure options for the update operation
            UpdateOptions options = new UpdateOptions().upsert(false);

            // Perform the update operation and check if document was modified
            if (collection.updateOne(query, update, options).getModifiedCount() > 0) {
                // Print success message if document was modified
                System.out.println("Recipe updated successfully.");
            } else {
                // Print message if no matching document was found
                System.out.println("No matching recipe found to update.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred while updating the recipe: " + e.getMessage());
        }
    }
}
