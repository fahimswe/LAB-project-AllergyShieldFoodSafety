import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Recipe implements Serializable {
    private final String name;
    private final List<String> ingredients;
    private final KitchenSafety safety;

    public Recipe(String name, List<String> ingredients) {
        this.name = validateName(name);
        this.ingredients = validateIngredients(ingredients);
        this.safety = new KitchenSafety("Low");
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe name cannot be empty");
        }
        return name.trim();
    }

    private List<String> validateIngredients(List<String> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalArgumentException("Ingredients list cannot be empty");
        }
        return ingredients.stream()
                .map(String::trim)
                .filter(ing -> !ing.isEmpty())
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return List.copyOf(ingredients);
    }

    public KitchenSafety getSafety() {
        return safety;
    }

    public boolean containsAllergen(List<String> userAllergies) {
        return userAllergies.stream()
                .anyMatch(allergy -> ingredients.stream()
                        .anyMatch(ingredient ->
                                ingredient.equalsIgnoreCase(allergy)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return name.equalsIgnoreCase(recipe.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return "Recipe: " + name + "\nIngredients: " + ingredients +
                "\nSafety: " + safety.getRiskLevel();
    }
}