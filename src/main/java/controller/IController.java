package controller;

import model.dto.MapInfoDTO;
import model.dto.request.RequestDTO;

public interface IController {
    public MapInfoDTO getMapInfo(RequestDTO requestDTO);
}
