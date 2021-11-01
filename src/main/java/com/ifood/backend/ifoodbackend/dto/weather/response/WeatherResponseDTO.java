package com.ifood.backend.ifoodbackend.dto.weather.response;

public class WeatherResponseDTO {

    private MainDTO main;
    private Sys sys;

    public MainDTO getMain() {
        return main;
    }

    public void setMain(MainDTO main) {
        this.main = main;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

}