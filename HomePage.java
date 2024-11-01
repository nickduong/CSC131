//in progress needs to be updated as we go
package com.example;

import java.util.Scanner;
import java.util.InputMismatchException;

public class HomePage {

    public static void main(String[] args) {
        mongodbOff logOff = new mongodbOff();
        logOff.logOff();
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n   Welcome to the Recipe Management System");
        System.out.println("   ***************************************");

        while (true) {
            System.out.println("\n\n        Proceed with your Commands: ");
            System.out.println("-------------------------------------------------");
            System.out.println("1. Search by Title");
            System.out.println("-------------------------------------------------");
            System.out.println("2. Search by ID"); 
            System.out.println("-------------------------------------------------");
            System.out.println("3. Search by Ingredients");
            System.out.println("-------------------------------------------------");
            System.out.println("4. Search by Cuisine");
            System.out.println("-------------------------------------------------");
            System.out.println("5. Search by Diet");
            System.out.println("-------------------------------------------------");
            System.out.println("6. Update a Recipe");
            System.out.println("-------------------------------------------------");
            System.out.println("7. Delete a Recipe");
            System.out.println("-------------------------------------------------");
            System.out.println("8. Create a Recipe");
            System.out.println("-------------------------------------------------");
            System.out.println("9. View a Saved Recipe");
            System.out.println("-------------------------------------------------");
            System.out.println("10. Search Detailed Recipe by ID");
            System.out.println("-------------------------------------------------");
            System.out.println("11. View Recipes of a Specific Cuisine Locally");
            System.out.println("-------------------------------------------------");
            System.out.println("0. Exit");
            System.out.println("-------------------------------------------------");
            System.out.print("Enter your choice: ");

            try{
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline left-over
                switch (choice) {
                    case 1:
                        System.out.println("\nSearching by Title...");
                        searchByTitle SearchByTitle = new searchByTitle();
                        SearchByTitle.searchTitle();
                        break;
                    case 2:
                        System.out.println("\nSearching by ID...");
                        searchByID SearchByID = new searchByID();
                        SearchByID.searchID();
                        break;
                    case 3:
                        System.out.println("\nSearching by Ingredients...");
                        searchByIngredients SearchByIngredients = new searchByIngredients();
                        SearchByIngredients.searchIngredients();
                        break;
                    case 4:
                        System.out.println("\nSearching by Cuisine...");
                        searchByCuisine SearchByCuisine =  new searchByCuisine();
                        SearchByCuisine.searchCuisine();
                        break;
                    case 5:
                        System.out.println("\nSearching by Diet...");
                        searchByDiet SearchByDiet = new searchByDiet();
                        SearchByDiet.searchDiet();
                        break;
                    case 6:
                        System.out.println("\nUpdating a Recipe...");
                        updateRecipe UpdateRecipe = new updateRecipe();
                        UpdateRecipe.update();
                        break;
                    case 7:
                        System.out.println("\nDeleting a Recipe...");
                        deleteRecipe DeleteRecipe = new deleteRecipe();
                        DeleteRecipe.delete();
                        break;
                    case 8:
                        System.out.println("\nCreating a Recipe...");
                        recipeCreator RecipeCreator = new recipeCreator();
                        RecipeCreator.create();
                        break;
                    case 9:
                        System.out.println("\nViewing a Recipe...");
                        viewRecipes ViewRecipes = new viewRecipes();
                        ViewRecipes.recipeDetails();
                        break;
                    case 10:
                        System.out.println("\nViewing Recipe Details...");
                        detailedViewAPI DetailedViewAPI = new detailedViewAPI();
                        DetailedViewAPI.viewAPIDetails();
                        break;
                    case 11:
                        System.out.println("\nViewing Saved Recipes of Chosen Cuisine...");
                        orderByCuisine OrderByCuisine = new orderByCuisine();
                        OrderByCuisine.viewRecipesByCuisine();
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return; // Exit the application
                    default:
                        System.out.println("Invalid choice, please enter a number between 0 and 11.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a numerical value.");
                scanner.nextLine(); // Consume the invalid input and prompt again.
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
