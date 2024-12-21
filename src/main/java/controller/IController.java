package controller;

import model.dto.MapInfoDTO;
import model.dto.request.RequestDTO;

public interface IController {
    MapInfoDTO getMapInfo(RequestDTO requestDTO);

    default MapInfoDTO getMapInfo() {
        return getMapInfo(new RequestDTO());
    }
}
