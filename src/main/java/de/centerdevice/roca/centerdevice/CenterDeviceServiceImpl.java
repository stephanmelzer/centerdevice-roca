package de.centerdevice.roca.centerdevice;

import de.centerdevice.roca.domain.Document;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;
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
    public List<Document> getAllDocuments() throws IOException {
        HttpResponse response = getAllDocumentsRaw();
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        JsonNode rootNode = mapper.readValue(response.getBodyAsString(), JsonNode.class);
        JsonNode documentNodes = rootNode.get("documents");
        
        List<Document> documents = mapper.readValue(documentNodes, new TypeReference<List<Document>>() {
        });
        
        return documents;
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
}
