package de.centerdevice.roca.oauth;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;

public class CenterDeviceProvider extends DefaultApi20 {

    private CenterDeviceOAuthConfig config;

    public CenterDeviceProvider() {
        super();
        config = new CenterDeviceOAuthConfig();
    }

    @Override
    public String getAccessTokenEndpoint() {
        return config.getTokenEndpoint();
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig authConfig) {
        return String.format(config.getAuthorizeUrl(),
                authConfig.getApiKey(),
                OAuthEncoder.encode(authConfig.getCallback()));
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new CenterDeviceServiceImpl(this, config);
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}
