package servicecourse.services.models;

public class ModelId {
    /**
     * De-obfuscates a {@code String} model ID. Currently, just parses it.
     *
     * @param id the model ID encoded as a String
     * @return the model ID
     * @throws NumberFormatException if the model ID isn't a {@code Long} encoded as a String
     */
    public static Long deserialize(String id) {
        try {
            return Long.parseLong(id);
        } catch (Exception e) {
            throw new NumberFormatException("Invalid model ID");
        }
    }

    /**
     * Obfuscates a {@code Long} model ID. Currently, just turns it into a {@code String} with no
     * extra steps.
     *
     * @param id the model ID
     * @return the obfuscated model ID as a String
     */
    public static String serialize(Long id) {
        return id.toString();
    }
}
