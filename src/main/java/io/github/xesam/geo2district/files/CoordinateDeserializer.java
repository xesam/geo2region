package io.github.xesam.geo2district.files;

import com.google.gson.*;
import io.github.xesam.gis.core.Coordinate;

import java.lang.reflect.Type;

/**
 * @author xesamguo@gmail.com
 */
class CoordinateDeserializer implements JsonDeserializer<Coordinate> {

    @Override
    public Coordinate deserialize(final JsonElement jsonElement, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        JsonArray jPoint = jsonElement.getAsJsonArray();
        return new Coordinate(jPoint.get(0).getAsDouble(), jPoint.get(1).getAsDouble());
    }
}
