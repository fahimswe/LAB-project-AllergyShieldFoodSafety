import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RecipeManager recipeManager = new RecipeManager();
    private static final AllergyReport reportGenerator = new AllergyReport();

    public static void main(String[] args) {
        initializeSampleData();
        showMainMenu();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== Food Allergy Checker ===");
            System.out.println("1. Check recipe safety");
            System.out.println("2. Add new recipe");
            System.out.println("3. List all recipes");
            System.out.println("4. Manage user profile");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> checkRecipeSafety();
                    case 2 -> addNewRecipe();
                    case 3 -> listAllRecipes();
                    case 4 -> manageUserProfile();
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice! Please enter 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private static void checkRecipeSafety() {
        UserProfile user = getCurrentUser();
        List<Recipe> recipes = recipeManager.getAllRecipes();

        if (recipes.isEmpty()) {
            System.out.println("No recipes available. Please add some first.");
            return;
        }

        System.out.println("\nAvailable Recipes:");
        for (int i = 0; i < recipes.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, recipes.get(i).getName());
        }

        System.out.print("Select recipe to check (0 to cancel): ");
        try {
            int selection = Integer.parseInt(scanner.nextLine()) - 1;
            if (selection == -1) return;
            if (selection >= 0 && selection < recipes.size()) {
                Recipe selectedRecipe = recipes.get(selection);
                System.out.println("\n" + reportGenerator.generateDetailedReport(selectedRecipe, user));
            } else {
                System.out.println("Invalid selection!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }

    private static void addNewRecipe() {
        System.out.print("Enter recipe name: ");
        String name = scanner.nextLine();

        System.out.println("Enter ingredients (comma separated):");
        List<String> ingredients = Arrays.stream(scanner.nextLine().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        System.out.print("Is this nut-free? (y/n): ");
        boolean isNutFree = scanner.nextLine().equalsIgnoreCase("y");

        try {
            Recipe recipe = isNutFree ?
                    new NutFreeRecipe(name, ingredients) :
                    new Recipe(name, ingredients);

            recipeManager.addRecipe(recipe);
            System.out.println("Recipe added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listAllRecipes() {
        List<Recipe> recipes = recipeManager.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes available.");
            return;
        }

        System.out.println("\nAll Recipes:");
        recipes.forEach(recipe -> {
            System.out.println("---------------------");
            System.out.println(recipe);
        });
    }

    private static void manageUserProfile() {
        UserProfile user = getCurrentUser();
        System.out.println("\nCurrent Profile:");
        System.out.println(user);

        System.out.println("\n1. Add allergy");
        System.out.println("2. Remove allergy");
        System.out.println("3. Back to main menu");
        System.out.print("Choose an option: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter allergy to add: ");
                    String allergy = scanner.nextLine();
                    try {
                        user.addAllergy(allergy);
                        saveUser(user);
                        System.out.println("Allergy added successfully!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Enter allergy to remove: ");
                    String allergy = scanner.nextLine();
                    user.removeAllergy(allergy);
                    saveUser(user);
                    System.out.println("Allergy removed successfully!");
                }
                case 3 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }

    private static UserProfile getCurrentUser() {
        List<UserProfile> users = FileSystemManager.loadUsers();
        if (users.isEmpty()) {
            System.out.println("\nNo user profile found. Let's create one!");
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            UserProfile newUser = new UserProfile(name);
            users.add(newUser);
            FileSystemManager.saveUsers(users);
            return newUser;
        }
        return users.get(0);
    }

    private static void saveUser(UserProfile user) {
        List<UserProfile> users = new ArrayList<>();
        users.add(user);
        FileSystemManager.saveUsers(users);
    }

    private static void initializeSampleData() {
        if (FileSystemManager.loadRecipes().isEmpty()) {
            try {
                Recipe r1 = new Recipe("Peanut Butter Cookies",
                        Arrays.asList("flour", "sugar", "peanuts", "eggs"));
                Recipe r2 = new NutFreeRecipe("Oatmeal Raisin Cookies",
                        Arrays.asList("oats", "raisins", "flour", "sugar"));

                recipeManager.addRecipe(r1);
                recipeManager.addRecipe(r2);

                List<Allergen> allergens = Arrays.asList(
                        new Allergen("peanuts", "High"),
                        new Allergen("gluten", "Medium"),
                        new Allergen("dairy", "Medium")
                );
                FileSystemManager.saveAllergens(allergens);
            } catch (IllegalArgumentException e) {
                System.err.println("Failed to initialize sample data: " + e.getMessage());
            }
        }
    }
}