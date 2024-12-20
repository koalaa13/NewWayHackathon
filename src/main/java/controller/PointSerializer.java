package controller;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import model.Point;

public class PointSerializer extends JsonSerializer<Point> {
    @Override
    public void serialize(Point value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        int[] data = new int[3];
        data[0] = value.coors.get(0);
        data[1] = value.coors.get(1);
        data[2] = value.coors.get(2);
        gen.writeArray(data, 0, 3);
    }
}
