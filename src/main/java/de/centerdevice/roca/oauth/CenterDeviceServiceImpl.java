package de.centerdevice.roca.oauth;

import java.nio.charset.Charset;
import org.apache.commons.codec.binary.Base64;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;

/**
 *
 * @author stephan
 */
public class CenterDeviceServiceImpl extends OAuth20ServiceImpl {

    private final DefaultApi20 api;
    private final OAuthConfig config;

    public CenterDeviceServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
        this.api = api;
        this.config = config;
    }

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        String clientCredentials = config.getApiKey() + ":" + config.getApiSecret();
        //base64 endcoding...
        Base64 base64Encoder = new Base64(true);
        clientCredentials = base64Encoder.encodeToString(clientCredentials.getBytes(Charset.forName("UTF-8")));
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addHeader("Authorization", "Basic " + clientCredentials);

        request.addBodyParameter("grant_type", "authorization_code");
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        if (config.hasScope()) {
            request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
        }
        Response response = request.send();
        System.out.println(request.toString());
        System.out.println(request.getBodyContents());
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    @Override
    public void signRequest(Token accessToken, OAuthRequest request) {
        request.addHeader("Authorization", "Bearer " + accessToken.getToken());
    }
}
