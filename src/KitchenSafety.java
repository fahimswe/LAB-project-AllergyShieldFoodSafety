import java.io.Serializable;
import java.util.Objects;

public class KitchenSafety implements Serializable {
    private String riskLevel;

    public KitchenSafety(String riskLevel) {
        setRiskLevel(riskLevel);
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        if (riskLevel == null || riskLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Risk level cannot be empty");
        }
        this.riskLevel = riskLevel.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KitchenSafety that = (KitchenSafety) o;
        return riskLevel.equalsIgnoreCase(that.riskLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(riskLevel.toLowerCase());
    }
}