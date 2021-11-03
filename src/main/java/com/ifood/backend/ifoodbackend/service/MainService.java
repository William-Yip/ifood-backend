package com.ifood.backend.ifoodbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifood.backend.ifoodbackend.InvalidParameterException;
import com.ifood.backend.ifoodbackend.dto.CityQuery;
import com.ifood.backend.ifoodbackend.dto.spotify.response.SpotifySearchResponseDTO;
import com.ifood.backend.ifoodbackend.dto.weather.response.WeatherResponseDTO;
import com.ifood.backend.ifoodbackend.validator.CityQueryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Service
public class MainService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;

    private final String SPOTIFY_SEARCH_BASE_URL = "https://api.spotify.com/v1/recommendations?seed_artists=4NHQUGzhtTLFvgF5SZesLK&seed_tracks=0c6xIDDpzE81m2q797ordA&seed_genres={genre}&market={market}&limit=10";
    private final String API_ID = "b77e07f479efe92156376a8b07640ced";
    private final String WEATHER_BASE_URL = String.format("http://api.openweathermap.org/data/2.5/weather?appid=%s&units=metric", API_ID);

    @Autowired
    CityQueryValidator validator;

    public List<String> findTrackByCity(CityQuery city) throws Exception {

        validateCityQuery(city);

        try {

            String uri = WEATHER_BASE_URL + buildWeatherArgs(city);

            WeatherResponseDTO response = restTemplate.getForEntity(uri, WeatherResponseDTO.class).getBody();

            Double temperature = response.getMain().getTemp();
            String country = response.getSys().getCountry();

            String genre = getGenreByTemperature(temperature);

            return findTracksByGenre(genre, country);
        }

        catch (HttpClientErrorException ex) {

            String error = ex.getResponseBodyAsString();

            Message message = null;

            try {
                message = objectMapper.readValue(error, Message.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new Exception("Internal server error");
            }

            InvalidParameterException invalidParameterException = new InvalidParameterException(message.getMessage());
            invalidParameterException.setStatus(ex.getStatusCode());

            throw invalidParameterException;
        }

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

    private void validateCityQuery(CityQuery city) {

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(city, city.getClass().getSimpleName().toLowerCase() );

        validator.validate(city, errors);

        if (errors.hasErrors())
            throw new InvalidParameterException(errors);

    }

}

class Message {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}