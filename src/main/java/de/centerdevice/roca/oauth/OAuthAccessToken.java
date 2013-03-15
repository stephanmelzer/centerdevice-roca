package de.centerdevice.roca.oauth;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;

public class OAuthAccessToken {

    private String accessToken;

    public String getAccessToken() {
        if (accessToken != null) {
            return accessToken;
        }
        
        // if no access token was aquired before accessing protected resources,
        // use the static one. Only for development/testing purposes!
        return CenterDeviceOAuthConfig.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
