package servicecourse.services.models;

public class ModelId {
    public static Long deserialize(String id) {
        try {
            return Long.parseLong(id);
        } catch (Exception e) {
            throw new NumberFormatException("Invalid model ID");
        }
    }

    public static String serialize(Long id) {
        return id.toString();
    }
}
