package controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import model.Point;

public class PointDeserializer extends StdDeserializer<Point> {
    protected PointDeserializer() {
        super(Point.class);
    }

    @Override
    public Point deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return new Point(p.getCodec().readValue(p, new TypeReference<List<Integer>>() {}));
    }
}
