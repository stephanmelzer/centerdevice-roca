package de.centerdevice.roca.centerdevice;

import de.centerdevice.roca.domain.Document;
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
        HttpResponse responseStub = new HttpResponse();
        responseStub.setStatusCode(200);

        InputStream documentsStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/documents.json");
        responseStub.setBodyInputStream(documentsStreamStub);

        return responseStub;
    }

    @Override
    public HttpResponse getDocumentRaw(String uuid) {
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
        HttpResponse responseStub = new HttpResponse();
        responseStub.setStatusCode(204);

        return responseStub;
    }

    @Override
    public HttpResponse getAllGroupsRaw() {
        HttpResponse responseStub = new HttpResponse();
        responseStub.setStatusCode(200);

        InputStream jsonStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/groups.json");
        responseStub.setBodyInputStream(jsonStreamStub);

        return responseStub;
    }
}
