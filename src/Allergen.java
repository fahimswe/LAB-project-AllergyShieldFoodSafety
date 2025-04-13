import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Allergen implements Serializable {
    private final String name;
    private final String severity;

    public Allergen(String name, String severity) {
        this.name = validateName(name);
        this.severity = validateSeverity(severity);
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Allergen name cannot be empty");
        }
        return name.trim();
    }

    private String validateSeverity(String severity) {
        if (severity == null ||
                !List.of("Low", "Medium", "High").contains(severity)) {
            throw new IllegalArgumentException("Invalid severity level");
        }
        return severity;
    }

    public String getName() {
        return name;
    }

    public String getSeverity() {
        return severity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Allergen allergen = (Allergen) o;
        return name.equalsIgnoreCase(allergen.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name + " (" + severity + ")";
    }
}