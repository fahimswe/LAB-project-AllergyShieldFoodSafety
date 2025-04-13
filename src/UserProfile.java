import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserProfile implements Serializable {
    private final String name;
    private final List<String> allergies;

    public UserProfile(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
        this.allergies = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getAllergies() {
        return new ArrayList<>(allergies);
    }

    public void addAllergy(String allergy) {
        String normalized = validateAllergy(allergy);
        if (!allergies.contains(normalized)) {
            allergies.add(normalized);
        }
    }

    public void removeAllergy(String allergy) {
        allergies.remove(validateAllergy(allergy));
    }

    private String validateAllergy(String allergy) {
        if (allergy == null || allergy.trim().isEmpty()) {
            throw new IllegalArgumentException("Allergy cannot be empty");
        }
        return allergy.trim().toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return "User: " + name + ", Allergies: " + allergies;
    }
}