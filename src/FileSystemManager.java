import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileSystemManager {
    private static final String DATA_DIR = "allergy_data";
    private static final String USER_FILE = DATA_DIR + "/users.ser";
    private static final String RECIPE_FILE = DATA_DIR + "/recipes.ser";
    private static final String ALLERGEN_FILE = DATA_DIR + "/allergens.ser";

    private static final Object userLock = new Object();
    private static final Object recipeLock = new Object();
    private static final Object allergenLock = new Object();

    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create data directory: " + e.getMessage());
        }
    }

    private static <T> void saveData(String filename, Object lock, List<T> data) {
        synchronized (lock) {
            Path tempPath = Paths.get(filename + ".tmp");
            Path finalPath = Paths.get(filename);

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    Files.newOutputStream(tempPath))) {
                oos.writeObject(new ArrayList<>(data));
                Files.move(tempPath, finalPath,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                System.err.println("Error saving " + filename + ": " + e.getMessage());
                try {
                    Files.deleteIfExists(tempPath);
                } catch (IOException ex) {
                    System.err.println("Failed to clean up temp file: " + ex.getMessage());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> loadData(String filename, Object lock) {
        synchronized (lock) {
            Path path = Paths.get(filename);
            if (!Files.exists(path) || !Files.isRegularFile(path)) {
                return new ArrayList<>();
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    Files.newInputStream(path))) {
                return (List<T>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading " + filename + ": " + e.getMessage());
                return new ArrayList<>();
            }
        }
    }

    public static void saveUsers(List<UserProfile> users) {
        saveData(USER_FILE, userLock, users);
    }

    public static List<UserProfile> loadUsers() {
        return loadData(USER_FILE, userLock);
    }

    public static void saveRecipes(List<Recipe> recipes) {
        saveData(RECIPE_FILE, recipeLock, recipes);
    }

    public static List<Recipe> loadRecipes() {
        return loadData(RECIPE_FILE, recipeLock);
    }

    public static void saveAllergens(List<Allergen> allergens) {
        saveData(ALLERGEN_FILE, allergenLock, allergens);
    }

    public static List<Allergen> loadAllergens() {
        return loadData(ALLERGEN_FILE, allergenLock);
    }
}