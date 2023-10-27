package servicecourse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class URLFactory {
    /**
     * @return a mock URL, wrapped in an optional because the constructor is throwable, and we don't
     * want null in the signature. Simply unpack with .orElseThrow :)
     */
    public static Optional<URL> newUrl() {
        return newUrlWithDomain("mock");
    }

    /**
     * Wrapped in an optional because the constructor is throwable, and we don't want null in the
     * signature. Use in your test with .orElseThrow :)
     *
     * @return https://domain.com
     */
    public static Optional<URL> newUrlWithDomain(String domain) {
        try {
            return Optional.of(new URL("https://" + domain + ".com"));
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }
}
