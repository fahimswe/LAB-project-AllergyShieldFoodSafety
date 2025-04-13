import java.util.List;

public class NutFreeRecipe extends Recipe {
    public NutFreeRecipe(String name, List<String> ingredients) {
        super(name, ingredients);
        getSafety().setRiskLevel("Nut-Free Certified");
    }

    @Override
    public boolean containsAllergen(List<String> userAllergies) {
        return userAllergies.stream()
                .anyMatch(allergy -> !allergy.equalsIgnoreCase("nuts") &&
                        getIngredients().stream()
                                .anyMatch(ingredient ->
                                        ingredient.equalsIgnoreCase(allergy)));
    }
}