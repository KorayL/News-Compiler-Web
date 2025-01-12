package news_compiler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for handy utils shared across all the API tests
 */
public class TestUtils {
    /** GSON object for use in serialization */
    public static final Gson gson = new GsonBuilder()
                                        .registerTypeAdapter(OffsetDateTime.class, new timeAdapter())
                                        .create();

    /**
     * Uses Google's GSON parser to serialize a Java object to JSON. Useful for
     * creating JSON representations of our objects when calling API methods.
     *
     * @param obj to serialize to JSON
     * @return JSON string associated with the object
     */
    public static String asJsonString(final Object obj) {
        return gson.toJson(obj);
    }
}

/**
 * An adapter class used to make objects containing <code>offsetDateTime</code> accessible to
 * <code>Gson</code>.
 * <p>
 * It's a response to: "com.google.gson.JsonIOException: Failed making field
 * 'java.time.LocalDateTime#date' accessible; either increase its visibility or write a custom
 * TypeAdapter for its declaring type."
 */
class timeAdapter extends TypeAdapter<OffsetDateTime> {

    /** Format for reading date/time from JSON */
    private static final DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public void write(JsonWriter jsonWriter, OffsetDateTime offsetDateTime) throws IOException {
        jsonWriter.value(offsetDateTime.format(format));
    }

    @Override
    public OffsetDateTime read(JsonReader jsonReader) throws IOException {
        return OffsetDateTime.parse(jsonReader.nextString(), format);
    }
}
