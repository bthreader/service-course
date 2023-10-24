package servicecourse.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class URLFactory {
    /**
     * @return a mock URL, wrapped in an optional because the constructor is throwable, and we don't
     * want null in the signature. Simply unpack with .orElseThrow :)
     */
    public static Optional<URL> newUrl() {
        try {
            return Optional.of(new URL("https://hello.com"));
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }
}
