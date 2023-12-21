package servicecourse.utils;

public class Base64Factory {
    /**
     * @return a string containing invalid Base64 characters
     */
    public static String invalidBase64String() {
        return "!!!!";
    }

    /**
     * @return "mock" encoded in Base64
     */
    public static String stringEncodedAsBase64String() {
        return "bW9jaw==";
    }

    /**
     * @return 1L encoded in Base64
     */
    public static String longEncodedAsBase64String() {
        return "MQ==";
    }
}
