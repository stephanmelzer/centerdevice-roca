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

    private static final String AUTHORIZE_URL = CenterDeviceOAuthConfig.autorizeUrl;

    @Override
    public String getAccessTokenEndpoint() {
        return CenterDeviceOAuthConfig.tokenEndpoint;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
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
