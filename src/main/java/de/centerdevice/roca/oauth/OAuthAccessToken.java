package de.centerdevice.roca.oauth;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;

public class OAuthAccessToken {

    private String accessToken;
    private boolean inDevelopmentMode;

    public OAuthAccessToken() {
        this.accessToken = "";
        this.inDevelopmentMode = false;
    }

    public String getAccessToken() {
        if (inDevelopmentMode) {
            //Only for development/testing purposes!
            return CenterDeviceOAuthConfig.accessToken;
        }

        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isInDevelopmentMode() {
        return inDevelopmentMode;
    }

    public void setInDevelopmentMode(boolean inDevelopmentMode) {
        this.inDevelopmentMode = inDevelopmentMode;
    }
}
