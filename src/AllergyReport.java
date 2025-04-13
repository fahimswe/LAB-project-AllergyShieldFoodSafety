import java.util.List;
import java.util.stream.Collectors;

public class AllergyReport {
    public String generateBasicReport() {
        return "Basic allergy warning: Always check ingredients carefully.";
    }

    public String generateDetailedReport(Recipe recipe, UserProfile user) {
        if (recipe == null || user == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        StringBuilder report = new StringBuilder();
        report.append("=== Allergy Safety Report ===\n");
        report.append("For user: ").append(user.getName()).append("\n");
        report.append("Recipe: ").append(recipe.getName()).append("\n\n");

        List<String> unsafeIngredients = recipe.getIngredients().stream()
                .filter(ingredient -> user.getAllergies().stream()
                        .anyMatch(allergy -> allergy.equalsIgnoreCase(ingredient)))
                .collect(Collectors.toList());

        if (unsafeIngredients.isEmpty()) {
            report.append("✅ This recipe appears safe for your allergies!\n");
        } else {
            report.append("⚠️ WARNING: Contains these allergens:\n");
            unsafeIngredients.forEach(ing -> report.append("- ").append(ing).append("\n"));

            report.append("\nSuggested substitutions:\n");
            SubstitutionStrategy strategy = new NutFreeSubstitute();
            unsafeIngredients.forEach(ing ->
                    report.append(ing).append(" → ")
                            .append(strategy.findSubstitute(ing)).append("\n"));
        }

        report.append("\nKitchen safety: ").append(recipe.getSafety().getRiskLevel());
        return report.toString();
    }
}