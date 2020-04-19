package de.telekom.dogsapp.model;

import com.google.gson.annotations.SerializedName;

public class DogBreed {
    @SerializedName("id")
    public String breedId;

    @SerializedName("name")
    public String dogBreed;

    @SerializedName("life_span")
    public String lifeSpan;

    @SerializedName("breed_group")
    public String breedGroup;

    @SerializedName("bred_for")
    public String bredFor;

    // temperament is exactly the name in the list, that is why we can leave it like this@SerializedName("id")
    public String temperament;

    @SerializedName("url")
    public String imageUrl;

    public int uuid;

    public DogBreed(String breedId, String dogBreed, String lifeSpan, String breedGroup, String bredFor, String temperament, String imageUrl) {
        this.breedId = breedId;
        this.dogBreed = dogBreed;
        this.lifeSpan = lifeSpan;
        this.breedGroup = breedGroup;
        this.bredFor = bredFor;
        this.temperament = temperament;
        this.imageUrl = imageUrl;
    }
}
