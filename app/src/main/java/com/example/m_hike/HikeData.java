package com.example.m_hike;

public class HikeData {
    private int id;
    private String name;
    private String location;
    private String date;
    private String parkingAvailable;
    private String lengthOfHike;
    private String difficultLevel;
    private String description;

    public HikeData(int id, String name, String location, String date, String parkingAvailable, String lengthOfHike, String difficultLevel, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parkingAvailable = parkingAvailable;
        this.lengthOfHike = lengthOfHike;
        this.difficultLevel = difficultLevel;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParkingAvailable() {
        return parkingAvailable;
    }

    public void setParkingAvailable(String parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
    }

    public String getLengthOfHike() {
        return lengthOfHike;
    }

    public void setLengthOfHike(String lengthOfHike) {
        this.lengthOfHike = lengthOfHike;
    }

    public String getDifficultLevel() {
        return difficultLevel;
    }

    public void setDifficultLevel(String difficultLevel) {
        this.difficultLevel = difficultLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
