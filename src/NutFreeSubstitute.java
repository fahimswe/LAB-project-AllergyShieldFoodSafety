public class NutFreeSubstitute implements SubstitutionStrategy {
    @Override
    public String findSubstitute(String ingredient) {
        if (ingredient == null || ingredient.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingredient cannot be empty");
        }

        String normalized = ingredient.trim().toLowerCase();
        return switch (normalized) {
            case "peanut butter" -> "Sunflower seed butter";
            case "almonds", "walnuts", "pecans" -> "Pumpkin seeds";
            case "peanuts" -> "Sunflower seeds";
            case "hazelnuts" -> "Oats";
            default -> "No safe substitute known for " + ingredient;
        };
    }
}