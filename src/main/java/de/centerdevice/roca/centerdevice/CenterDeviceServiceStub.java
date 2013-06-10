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
    private HttpMessage httpResponse;

    public void setHttpMessage(HttpMessage httpResponse) {
        this.httpResponse = httpResponse;
    }

    public void setAccessToken(OAuthAccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public List<Document> getDocuments(String searchQuery) throws IOException {
        HttpMessage response = getDocumentsRaw(searchQuery);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonNode rootNode = mapper.readValue(response.getBodyAsString(), JsonNode.class);
        JsonNode documentNodes = rootNode.get("documents");

        List<Document> documents = mapper.readValue(documentNodes, new TypeReference<List<Document>>() {
        });

        return documents;
    }

    @Override
    public HttpMessage getDocumentsRaw(String searchQuery) {
        HttpMessage responseStub = new HttpMessage();
        responseStub.setStatusCode(200);

        InputStream documentsStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/documents.json");
        responseStub.setBodyInputStream(documentsStreamStub);

        return responseStub;
    }

    @Override
    public HttpMessage getDocumentRaw(String uuid) {
        if (this.httpResponse != null) {
            return this.httpResponse;
        }

        InputStream documentFileStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/documentFileStub.txt");

        HttpMessage responseStub = new HttpMessage();
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
    public HttpMessage joinGroupRaw(String groupId) {
        if (this.httpResponse != null) {
            return this.httpResponse;
        }

        HttpMessage responseStub = new HttpMessage();
        responseStub.setStatusCode(204);

        return responseStub;
    }

    @Override
    public HttpMessage getAllGroupsRaw() {
        if (this.httpResponse != null) {
            return this.httpResponse;
        }

        HttpMessage responseStub = new HttpMessage();
        responseStub.setStatusCode(200);
        responseStub.setHeader("Content-Type", "application/json");

        InputStream jsonStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/groups.json");
        responseStub.setBodyInputStream(jsonStreamStub);

        return responseStub;
    }

    @Override
    public User getUserInformation(String searchQuery) throws IOException {
        HttpMessage response = getUserInformationRaw(searchQuery);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        User user = mapper.readValue(response.getBodyAsString(), User.class);

        return user;
    }

    @Override
    public HttpMessage getUserInformationRaw(String searchQuery) {
        if (this.httpResponse != null) {
            return this.httpResponse;
        }

        HttpMessage responseStub = new HttpMessage();
        responseStub.setStatusCode(200);
        responseStub.setHeader("Content-Type", "application/json");

        InputStream jsonStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/user-information.json");
        responseStub.setBodyInputStream(jsonStreamStub);

        return responseStub;
    }

    @Override
    public HttpMessage uploadDocumentRaw(HttpMessage clientRequest) {
        HttpMessage responseStub = new HttpMessage();
        responseStub.setStatusCode(201);
        responseStub.setHeader("Location", "centerdevice.de/document/123-abc");

        InputStream jsonStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/fileUploadSuccess.json");
        responseStub.setBodyInputStream(jsonStreamStub);

        return responseStub;
    }

    @Override
    public HttpMessage getDocumentAsFlash(String documentId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
