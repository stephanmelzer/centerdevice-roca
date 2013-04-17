package de.centerdevice.roca.centerdevice;

import de.centerdevice.roca.domain.Document;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;
import de.centerdevice.roca.domain.User;
import de.centerdevice.roca.oauth.OAuthAccessToken;
import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;

public class CenterDeviceServiceImpl implements CenterDeviceService {

    @Autowired
    private OAuthService service;
    @Autowired
    private OAuthAccessToken accessToken;

    @Override
    public List<Document> getDocuments(String searchQuery) throws IOException {
        HttpResponse response = getDocumentsRaw(searchQuery);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonNode rootNode = mapper.readValue(response.getBodyAsString(), JsonNode.class);
        JsonNode documentNodes = rootNode.get("documents");

        List<Document> documents = mapper.readValue(documentNodes, new TypeReference<List<Document>>() {
        });

        return documents;
    }

    @Override
    public HttpResponse getDocumentsRaw(String searchQuery) {
        OAuthRequest request = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[0] + "?" + searchQuery);
        return getResource(request);
    }

    @Override
    public HttpResponse getDocumentRaw(String uuid) {
        OAuthRequest centerDeviceRequest = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[1] + uuid);
        return getResource(centerDeviceRequest);
    }

    private HttpResponse getResource(OAuthRequest centerDeviceRequest) {
        Token token = new Token(accessToken.getAccessToken(), CenterDeviceOAuthConfig.apiSecret);
        service.signRequest(token, centerDeviceRequest);
        Response centerDeviceResponse = centerDeviceRequest.send();

        //create HttpResponse object
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(centerDeviceResponse.getCode());
        httpResponse.setHeaders(centerDeviceResponse.getHeaders());
        httpResponse.setBodyInputStream(centerDeviceResponse.getStream());

        return httpResponse;
    }

    @Override
    public String getAuthorizationUrl() {
        return service.getAuthorizationUrl(null);
    }

    @Override
    public void login(String code) {
        Token token = service.getAccessToken(null, new Verifier(code));
        accessToken.setAccessToken(token.getToken());
    }

    @Override
    public void logout() {
        accessToken.setAccessToken("");
    }

    @Override
    public boolean isLoggedIn() {
        if (accessToken.getAccessToken().equals("")) {
            return false;
        }
        return true;
    }

    @Override
    public HttpResponse joinGroupRaw(String groupId) {
        OAuthRequest centerDeviceRequest = new OAuthRequest(Verb.POST, CenterDeviceOAuthConfig.protectedResourceUrl[2] + groupId);

        centerDeviceRequest.addHeader("Content-Type", "application/json; charset=UTF-8");
        centerDeviceRequest.addPayload("{'action' : 'join-group'}");

        return getResource(centerDeviceRequest);
    }

    @Override
    public HttpResponse getAllGroupsRaw() {
        OAuthRequest centerDeviceRequest = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[3]);
        return getResource(centerDeviceRequest);
    }

    @Override
    public User getUserInformation(String searchQuery) throws IOException {
        HttpResponse response = getUserInformationRaw(searchQuery);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        User user = mapper.readValue(response.getBodyAsString(), User.class);

        return user;
    }

    @Override
    public HttpResponse getUserInformationRaw(String searchQuery) {
        OAuthRequest request = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[4] + "?" + searchQuery);
        return getResource(request);
    }
}
