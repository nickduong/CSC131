package com.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Scanner;

public class deleteRecipe {
    // MongoDB connection URI
    private static final String MONGODB_URI = "mongodb+srv://nick:csus@csc131.tct5wqu.mongodb.net/";

    public void delete(){
        try {
            viewRecipes ViewRecipes = new viewRecipes();
            ViewRecipes.recipeDetails();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the title of the recipe you wish to delete:");
            String title = scanner.nextLine();

            deleteDocument(title);
        }catch(Exception e){
            System.err.println("An error occurred while deleting the recipe: " + e.getMessage());
        }
    }

    // Method to delete a recipe document from MongoDB
    private static void deleteDocument(String title) {
        try (MongoClient mongoClient = MongoClients.create(MONGODB_URI)) {
            // Connect to MongoDB and access the specified database and collection
            MongoDatabase database = mongoClient.getDatabase("recipeProject");
            MongoCollection<Document> collection = database.getCollection("spoonacularRecipes");

            // Create a query to find the document by the lowercase title
            Document query = new Document("title", new Document("$regex", "^" + title + "$").append("$options", "i"));

            // Attempt to delete the document with the given title
            if (collection.deleteOne(query).getDeletedCount() > 0) {
                // Print success message if document was deleted
                System.out.println("Recipe deleted successfully.");
            } else {
                // Print message if no matching document was found
                System.out.println("No matching recipe found to delete.");
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the deletion process
            System.err.println("An error occurred while deleting the recipe: " + e.getMessage());
        }
    }
}
