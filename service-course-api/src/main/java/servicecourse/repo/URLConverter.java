package servicecourse.repo;

import jakarta.persistence.AttributeConverter;

import java.net.MalformedURLException;
import java.net.URL;

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
