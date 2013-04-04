package de.centerdevice.roca.centerdevice;

import de.centerdevice.roca.domain.Document;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;
import de.centerdevice.roca.domain.DocumentList;
import de.centerdevice.roca.oauth.OAuthAccessToken;
import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;

public class CenterDeviceServiceImpl implements CenterDeviceService {

    @Autowired
    private OAuthService service;
    @Autowired
    private OAuthAccessToken token;

    @Override
    public List<Document> getAllDocuments() throws IOException {
        HttpResponse response = getAllDocumentsRaw();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //TODO: remove dependency on DocumentList. Find a way to parse only document objects.
        //List<Document> docs = mapper.readValue(response.getBody(), new TypeReference<List<Document>>(){});
        DocumentList docs = mapper.readValue(response.getBodyAsString(), DocumentList.class);

        return docs.getDocuments();
    }

    @Override
    public HttpResponse getAllDocumentsRaw() {
        OAuthRequest request = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[0]);
        return getResource(request);
    }

    @Override
    public HttpResponse getDocumentRaw(String uuid) {
        OAuthRequest centerDeviceRequest = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[1] + uuid);
        return getResource(centerDeviceRequest);
    }

    private HttpResponse getResource(OAuthRequest centerDeviceRequest) {
        Token accessToken = new Token(token.getAccessToken(), CenterDeviceOAuthConfig.apiSecret);
        service.signRequest(accessToken, centerDeviceRequest);
        Response centerDeviceResponse = centerDeviceRequest.send();

        //create HttpResponse object
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(centerDeviceResponse.getCode());
        httpResponse.setHeaders(centerDeviceResponse.getHeaders());
        httpResponse.setBodyInputStream(centerDeviceResponse.getStream());

        return httpResponse;
    }
}
