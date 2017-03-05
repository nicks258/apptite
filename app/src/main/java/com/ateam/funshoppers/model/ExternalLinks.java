package com.ateam.funshoppers.model;

/**
 * Created by Home on 12/2/2016.
 */

public class ExternalLinks {
    private String Website;
    private String Facebook;
    private String Twitter;
    private String Instagram;

    public ExternalLinks(String website, String facebook, String twitter, String instagram) {
        Website = website;
        Facebook = facebook;
        Twitter = twitter;
        Instagram = instagram;
    }

    public String getWebsite() {
        return Website;
    }

    public String getTwitter() {
        return Twitter;
    }

    public String getFacebook() {
        return Facebook;
    }

    public String getInstagram() {
        return Instagram;
    }
}
