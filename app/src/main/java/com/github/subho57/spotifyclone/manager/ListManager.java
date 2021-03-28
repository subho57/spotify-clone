package com.github.subho57.spotifyclone.manager;

import com.github.subho57.spotifyclone.model.AlbumNew;
import com.github.subho57.spotifyclone.model.ArtistSearch;
import com.github.subho57.spotifyclone.model.Music;
import com.github.subho57.spotifyclone.model.SimplePlaylist;
import com.github.subho57.spotifyclone.model.TopArtist;
import com.github.subho57.spotifyclone.model.TopTrack;

import java.util.ArrayList;

/**
 * Created by subho57
 */

public class ListManager {

    private static ListManager listManager;

    private ArrayList<Music> trackLists;
    private ArrayList<ArtistSearch> artistSearches;
    private ArrayList<AlbumNew> albumNewArrayList;
    private ArrayList<TopArtist> topArtists;
    private ArrayList<TopTrack> topTracks;
    private ArrayList<SimplePlaylist> simplePlaylists;

    private String current_playing_title;
    private String current_playing_album;

    private ListManager() {
        trackLists = new ArrayList<>();
        artistSearches = new ArrayList<>();
        albumNewArrayList = new ArrayList<>();
        topArtists = new ArrayList<>();
        topTracks = new ArrayList<>();
        simplePlaylists = new ArrayList<>();

        current_playing_title = null;
        current_playing_album = null;
    }

    public static ListManager getInstance() {
        if (listManager == null) {
            listManager = new ListManager();
        }

        return listManager;
    }

    public ArrayList<SimplePlaylist> getSimplePlaylists() {
        return simplePlaylists;
    }

    public void addSimpleList(SimplePlaylist simple) {
        simplePlaylists.add(simple);
    }

    public ArrayList<TopTrack> getTopTracks() {
        return topTracks;
    }

    public void addTopTrack(TopTrack track) {

        topTracks.add(track);
    }

    public void addTopArtist(TopArtist artist) {
        topArtists.add(artist);
    }

    public ArrayList<TopArtist> getTopArtists() {
        return topArtists;
    }

    public void addNewAlbum(AlbumNew albumNew) {
        albumNewArrayList.add(albumNew);
    }

    public ArrayList<AlbumNew> getAlbumNewArrayList() {
        return albumNewArrayList;
    }

    public ArrayList<ArtistSearch> getArtists() {
        return artistSearches;
    }

    public void addArtist(ArtistSearch search) {

        ArtistSearch found = null;

        for (ArtistSearch artistSearch : artistSearches) {
            if (artistSearch.getName().equals(search.getName())) {
                found = artistSearch;
            }
        }

        if (found != null)
            artistSearches.remove(found);

        artistSearches.add(0, search);
    }

    public ArrayList<Music> getTrackLists() {
        return trackLists;
    }

    public void addTrack(Music music) {
        trackLists.add(music);
    }

    public void clearList() {
        trackLists.clear();
    }

    public Music findCurrentMusic(String title, String album) {
        for (Music m : trackLists) {
            if (m.getTitle().equals(title) && m.getAlbum().equals(album)) {
                return m;
            }
        }

        return null;
    }

    public String getCurrent_playing_title() {
        return current_playing_title;
    }

    public void setCurrent_playing_title(String current_playing_title) {
        this.current_playing_title = current_playing_title;
    }

    public String getCurrent_playing_album() {
        return current_playing_album;
    }

    public void setCurrent_playing_album(String current_playing_album) {
        this.current_playing_album = current_playing_album;
    }
}
