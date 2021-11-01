package com.ifood.backend.ifoodbackend.dto.spotify.response;

import java.util.List;

public class SpotifySearchResponseDTO {

    private List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

}