package com.github.subho57.spotifyclone.model;

import android.graphics.Bitmap;

/**
 * Created by subho57
 */

public class ArtistSearch {

    private String name;
    private Bitmap image;

    public ArtistSearch(String name, Bitmap img) {
        this.name = name;
        this.image = img;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }
}
