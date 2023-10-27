package servicecourse.services.bikes;

public class BikeId {
    public static Long deserialize(String id) {
        try {
            return Long.parseLong(id);
        } catch (Exception e) {
            throw new NumberFormatException("Invalid bike ID");
        }
    }

    public static String serialize(Long id) {
        return id.toString();
    }
}
