package de.centerdevice.roca.centerdevice;

import de.centerdevice.roca.domain.Document;
import de.centerdevice.roca.domain.User;
import de.centerdevice.roca.oauth.OAuthAccessToken;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.ServletContext;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;

public class CenterDeviceServiceStub implements CenterDeviceService {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private OAuthAccessToken accessToken;
    private HttpResponse httpResponse;

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public void setAccessToken(OAuthAccessToken accessToken) {
        this.accessToken = accessToken;
    }

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
        HttpResponse responseStub = new HttpResponse();
        responseStub.setStatusCode(200);

        InputStream documentsStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/documents.json");
        responseStub.setBodyInputStream(documentsStreamStub);

        return responseStub;
    }

    @Override
    public HttpResponse getDocumentRaw(String uuid) {
        if (this.httpResponse != null) {
            return this.httpResponse;
        }

        InputStream documentFileStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/documentFileStub.txt");

        HttpResponse responseStub = new HttpResponse();
        responseStub.setStatusCode(200);
        responseStub.setHeader("Content-Type", "text/plain; charset=utf-8");
        responseStub.setHeader("Content-Disposition", "attachment; filename=\"documentFileStub.txt\"");
        responseStub.setHeader("Content-Length", "20");
        responseStub.setBodyInputStream(documentFileStreamStub);

        return responseStub;
    }

    @Override
    public String getAuthorizationUrl() {
        return "http://roca.local:8080/centerdevice-roca/login?code=testCode";
    }

    @Override
    public void login(String code) {
        accessToken = new OAuthAccessToken();

        accessToken.setAccessToken("testToken");
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
        if (this.httpResponse != null) {
            return this.httpResponse;
        }

        HttpResponse responseStub = new HttpResponse();
        responseStub.setStatusCode(204);

        return responseStub;
    }

    @Override
    public HttpResponse getAllGroupsRaw() {
        if (this.httpResponse != null) {
            return this.httpResponse;
        }

        HttpResponse responseStub = new HttpResponse();
        responseStub.setStatusCode(200);
        responseStub.setHeader("Content-Type", "application/json");

        InputStream jsonStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/groups.json");
        responseStub.setBodyInputStream(jsonStreamStub);

        return responseStub;
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
        if (this.httpResponse != null) {
            return this.httpResponse;
        }

        HttpResponse responseStub = new HttpResponse();
        responseStub.setStatusCode(200);
        responseStub.setHeader("Content-Type", "application/json");

        InputStream jsonStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/user-information.json");
        responseStub.setBodyInputStream(jsonStreamStub);

        return responseStub;
    }
}
