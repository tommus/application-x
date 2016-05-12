package com.todev.appx.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
public class DateTimeSerializer extends JsonSerializer<DateTime> {
    public static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final DateTimeFormatter ISO_8601 = DateTimeFormat.forPattern(ISO_8601_FORMAT);

    /**
     * Serializes Joda's {@link DateTime} to ISO 8601-formatted {@link String}.
     * @param value a {@link DateTime} that will be serialized.
     * @param gen a {@link JsonGenerator} that will be used to serialize data.
     * @param serializers a {@link SerializerProvider} that will be used to serialize data.
     * @throws IOException
     */
    @Override
    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(ISO_8601.print(value));
    }
}
