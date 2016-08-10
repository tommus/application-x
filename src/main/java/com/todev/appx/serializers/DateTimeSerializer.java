package com.todev.appx.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateTimeSerializer extends JsonSerializer<DateTime> {
  @Override
  public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeString(String.valueOf(value.toDateTime(DateTimeZone.UTC).getMillis()));
  }
}
