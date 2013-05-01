package de.centerdevice.roca.oauth;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class OAuthAccessToken {

    private String accessToken;
    @Autowired
    private CenterDeviceOAuthConfig config;

    public OAuthAccessToken() {
        this.accessToken = "";
    }

    public String getAccessToken() {
        if (config.getAccessToken() != null) {
            //Only for development/testing purposes!
            return config.getAccessToken();
        }

        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
