package servicecourse.repo.common;

import jakarta.persistence.AttributeConverter;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Allows the conversion of a potentially null URL entity attribute to and from a database column
 * which represents the url as a string.
 */
public class URLConverter implements AttributeConverter<URL, String> {
    @Override
    public String convertToDatabaseColumn(URL url) {
        return (url != null) ? url.toString() : null;
    }

    @Override
    public URL convertToEntityAttribute(String url) {
        try {
            return (url != null) ? new URL(url) : null;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format: " + url);
        }
    }
}
