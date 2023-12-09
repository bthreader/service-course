package servicecourse.services.bikes;

public class BikeId {
    /**
     * De-obfuscates a {@code String} bike ID. Currently, just parses it.
     *
     * @param id the bike ID encoded as a String
     * @return the bike ID
     * @throws NumberFormatException if the model ID isn't a {@code Long} encoded as a String
     */
    public static Long deserialize(String id) {
        try {
            return Long.parseLong(id);
        } catch (Exception e) {
            throw new NumberFormatException("Invalid bike ID");
        }
    }

    /**
     * Obfuscates a {@code Long} bike ID. Currently, just turns it into a {@code String} with no
     * extra steps.
     *
     * @param id the bike ID
     * @return the obfuscated bike ID as a String
     */
    public static String serialize(Long id) {
        return id.toString();
    }
}
