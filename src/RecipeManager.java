import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeManager {
    private final List<Recipe> recipes;

    public RecipeManager() {
        this.recipes = new ArrayList<>(FileSystemManager.loadRecipes());
    }

    public void addRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        recipes.add(recipe);
        FileSystemManager.saveRecipes(recipes);
    }

    public List<Recipe> findSafeRecipes(UserProfile user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return recipes.stream()
                .filter(recipe -> !recipe.containsAllergen(user.getAllergies()))
                .collect(Collectors.toList());
    }

    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipes);
    }

    public boolean removeRecipe(String recipeName) {
        boolean removed = recipes.removeIf(
                recipe -> recipe.getName().equalsIgnoreCase(recipeName));
        if (removed) {
            FileSystemManager.saveRecipes(recipes);
        }
        return removed;
    }
}