package com.ifood.backend.ifoodbackend.service;

import com.ifood.backend.ifoodbackend.dto.CityQuery;
import com.ifood.backend.ifoodbackend.dto.spotify.response.SpotifySearchResponseDTO;
import com.ifood.backend.ifoodbackend.dto.weather.response.WeatherResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Service
public class MainService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;

    private final String SPOTIFY_SEARCH_BASE_URL = "https://api.spotify.com/v1/recommendations?seed_artists=4NHQUGzhtTLFvgF5SZesLK&seed_tracks=0c6xIDDpzE81m2q797ordA&seed_genres={genre}&market={market}&limit=10";
    private final String API_ID = "b77e07f479efe92156376a8b07640ced";
    private final String WEATHER_BASE_URL = String.format("http://api.openweathermap.org/data/2.5/weather?appid=%s&units=metric", API_ID);

    public List<String> findTrackByCity(CityQuery city) {

        // TODO
        //  - validate here CityQuery

        try {

            String uri = WEATHER_BASE_URL + buildWeatherArgs(city);

            WeatherResponseDTO response = restTemplate.exchange(uri, HttpMethod.GET, null, WeatherResponseDTO.class).getBody();

            Double temperature = response.getMain().getTemp();
            String country = response.getSys().getCountry();

            String genre = getGenreByTemperature(temperature);

            return findTracksByGenre(genre, country);
        }

        catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }

        return new ArrayList<>();
    }

    private String buildWeatherArgs(CityQuery city) {

        StringBuilder sb = new StringBuilder();

        if (!StringUtils.isEmpty(city.getName()))
            sb.append(format("&q=%s", city.getName()));

        else {
            if (Objects.nonNull(city.getLat()) && Objects.nonNull(city.getLon()))
                sb.append(format("&lat=%s&lon=%s", city.getLat().toString(), city.getLon().toString()));
        }

        return sb.toString();
    }

    private String getGenreByTemperature(Double temperature) {

        if (temperature > 30)
            return "party";

        else if (temperature >= 15 && temperature <= 30)
            return "pop";

        else if (temperature >= 10 && temperature <= 14)
            return "rock";

        else
            return "classical";

    }

    private List<String> findTracksByGenre (String genre, String country) {

        SpotifySearchResponseDTO response = oAuth2RestTemplate.getForEntity(SPOTIFY_SEARCH_BASE_URL, SpotifySearchResponseDTO.class, genre, country).getBody();

        ArrayList<String> tracks = new ArrayList<>();

        response.getTracks().forEach(track -> {
            tracks.add(track.getName());
        });

        return tracks;
    }

}