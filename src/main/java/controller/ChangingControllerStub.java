package controller;

import model.Point;
import model.dto.MapInfoDTO;
import model.dto.request.RequestDTO;

public class ChangingControllerStub extends StaticFileController {
    private int offset = 0;

    private void addOffsetToPoint(Point p) {
        p.coors.set(0, p.coors.get(0) + offset);
    }

    @Override
    public MapInfoDTO getMapInfo(RequestDTO requestDTO) {
        MapInfoDTO res = super.getMapInfo(requestDTO);
        for (var p : res.food) {
            addOffsetToPoint(p.c);
        }
        for (var p : res.fences) {
            addOffsetToPoint(p);
        }
        for (var s : res.enemies) {
            for (var p : s.geometry) {
                addOffsetToPoint(p);
            }
        }
        for (var s : res.snakes) {
            for (var p : s.geometry) {
                addOffsetToPoint(p);
            }
        }
        offset++;
        return res;
    }
}
