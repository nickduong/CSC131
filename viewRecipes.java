package com.example;

import java.util.Scanner;
import java.util.List;
import org.bson.Document;

public class viewRecipes {
    Scanner scanner = new Scanner(System.in);

    public void recipeDetails() {
        recipeSaver recipeSaver = new recipeSaver();
        List<Document> savedRecipes = recipeSaver.getSavedRecipes();

        if (!savedRecipes.isEmpty()) {
            System.out.println("\n\nYour list of saved recipes:");
            System.out.println("------------------------------------------------------------------");
            int index = 1;
            for (Document savedRecipe : savedRecipes) {
                System.out.println(index + ": " + safeGetString(savedRecipe, "title", "No title available"));
                System.out.println("   - ID: " + safeGetString(savedRecipe, "id", "No ID available"));
                System.out.println("   - Cuisine(s): " + safeGetString(savedRecipe, "cuisines", "Not available"));
                System.out.println("   - Diet(s): " + safeGetString(savedRecipe, "diets", "Not available"));
                System.out.println("   - Dish Type(s): " + safeGetString(savedRecipe, "dishTypes", "Not available"));
                System.out.println("------------------------------------------------------------------");
                index++;
            }
        } else {
            System.out.println("No saved recipes found.");
            return;
        }

        int optionNum;
        while (true) {
            System.out.println("\nEnter the number of a recipe for detailed view (or '0' to go back):");
            try {
                optionNum = scanner.nextInt();
                scanner.nextLine();

                if (optionNum >= 1 && optionNum <= savedRecipes.size()) {
                    Document recipeDetails = savedRecipes.get(optionNum - 1);
                    displayDetails(recipeDetails);
                    break; // Exit the loop if a valid option is selected
                } else if (optionNum == 0) {
                    System.out.println("Returning...");
                    return;
                } else {
                    System.out.println("ERROR: Invalid choice selected. Please enter a valid number.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: Invalid input. Please enter a valid number.");
                scanner.next(); // Clear scanner wrong input
            }
        }
    }

    private void displayDetails(Document recipeDetails) {
        System.out.println("\nTitle: " + safeGetString(recipeDetails, "title", "No title available"));
        System.out.println("\nImage: " + safeGetString(recipeDetails, "image", "No image available"));
        System.out.println("\nID Number: " + safeGetString(recipeDetails, "id", "0"));
        System.out.println("\nSummary: " + safeGetString(recipeDetails, "summary", "No summary available"));
        System.out.println("\nServings: " + safeGetString(recipeDetails, "servings", "No servings information"));
        System.out.println("\nRating: " + safeGetDouble(recipeDetails, "spoonacularScore", 0.0));
        System.out.println("\nDiet(s): " + safeGetString(recipeDetails, "diets", "Not available"));
        System.out.println("\nCuisine(s): " + safeGetString(recipeDetails, "cuisines", "Not available"));
        System.out.println("\nIngredients: \n" + formatListItems(safeGetString(recipeDetails, "ingredients", "No ingredients listed")));
        System.out.println("\nInstructions: \n" + formatInstructions(safeGetString(recipeDetails, "instructions", "No instructions provided")));
        System.out.println("\nNutrients:\n" + formatNutrients(safeGetString(recipeDetails, "nutrition", "No nutrition information")));
    }
    
    private String formatNutrients(String nutrients) {
        if (nutrients.equals("No nutrition information")) {
            return nutrients;
        }
        String[] items = nutrients.split(", ");
        StringBuilder formatted = new StringBuilder();
        for (String item : items) {
            formatted.append("- ").append(item).append("\n");
        }
        return formatted.toString();
    }
    
    private String safeGetString(Document document, String key, String defaultValue) {
        Object value = document.get(key);
        return value != null ? value.toString() : defaultValue;
    }

    private double safeGetDouble(Document document, String key, double defaultValue) {
        Object value = document.get(key);
        if ((value instanceof Number)) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }

    private String formatListItems(String text) {
        String[] items = text.split("; ");
        StringBuilder formatted = new StringBuilder();
        for (String item : items) {
            formatted.append("- ").append(item).append("\n");
        }
        return formatted.toString();
    }

    private String formatInstructions(String instructions) {
        String[] lines = instructions.split("\n");  // Split instructions into lines assuming each step is on a new line.
        StringBuilder formatted = new StringBuilder();
        for (String line : lines) {
            if (line.trim().startsWith("Step")) {  // Check if the line starts with "Step"
                formatted.append("- ").append(line).append("\n");
            } else {
                formatted.append(line).append("\n");
            }
        }
        return formatted.toString();
    }
}    

