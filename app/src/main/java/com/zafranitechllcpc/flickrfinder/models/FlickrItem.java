package com.zafranitechllcpc.flickrfinder.models;

public class FlickrItem {
    private final String TAG = getClass().getSimpleName();
    private int id;
    private int farmId;
    private int serverId;
    private String secret;
    private String title;
    private String smallUrl;
    private String bigUrl;

    public FlickrItem() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFarmId() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getBigUrl() {
        return bigUrl;
    }

    public void setBigUrl(String bigUrl) {
        this.bigUrl = bigUrl;
    }

    @Override
    public String toString() {
        return "FlickrItem{" +
                "id=" + id +
                ", farmId=" + farmId +
                ", serverId=" + serverId +
                ", secret='" + secret + '\'' +
                ", title='" + title + '\'' +
                ", smallUrl='" + smallUrl + '\'' +
                ", bigUrl='" + bigUrl + '\'' +
                '}';
    }
}
