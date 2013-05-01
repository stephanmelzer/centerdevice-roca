package de.centerdevice.roca.config;

public class CenterDeviceOAuthConfig {

    public String getApiKey() {
        return getEnviromentVariable("API_KEY");
    }

    public String getApiSecret() {
        return getEnviromentVariable("API_SECRET");
    }

    public String getCallbackUrl() {
        return getEnviromentVariable("CALLBACK_URL");
    }

    public String getAuthorizeUrl() {
        return getEnviromentVariable("AUTHORIZE_URL");
    }

    public String getTokenEndpoint() {
        return getEnviromentVariable("TOKEN_ENDPOINT");
    }

    public String getBaseUrl() {
        return getEnviromentVariable("BASE_URL");
    }

    public String getAccessToken() {
        return getEnviromentVariable("ACCESS_TOKEN");
    }

    private String getEnviromentVariable(String variableName) {
        String value = System.getenv(variableName);
        return value;
    }
}
