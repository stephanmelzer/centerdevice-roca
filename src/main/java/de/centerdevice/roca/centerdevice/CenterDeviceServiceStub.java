package de.centerdevice.roca.centerdevice;

import de.centerdevice.roca.domain.Document;
import de.centerdevice.roca.oauth.OAuthAccessToken;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
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

        InputStream centerDeviceInputStreamStub = IOUtils.toInputStream("{\"hits\":90,\"documents\":[{\"uploaddate\":\"2012-11-07T09:39:00.374Z\",\"title\":\"Module 02- Routers and Routing\",\"version\":1,\"author\":\"dell\",\"representations\":{\"pdf\":\"yes\",\"swf\":\"yes\",\"fulltext\":\"yes\",\"png\":\"yes\",\"jpg\":\"no\"},\"mimetype\":\"application\\/pdf\",\"owner\":{\"email\":\"ansgar.schulte@codecentric.de\",\"first-name\":\"Ansgar\",\"last-name\":\"Schulte\",\"name\":\"Ansgar Schulte\"},\"id\":\"5416b6c5-ac3b-4849-a422-51ec8978fef3\",\"size\":898179,\"uploader\":{\"email\":\"ansgar.schulte@codecentric.de\",\"first-name\":\"Ansgar\",\"last-name\":\"Schulte\",\"name\":\"Ansgar Schulte\"},\"filename\":\"01-01 Basics Lab-3.pdf\",\"versiondate\":\"2012-11-07T09:39:00.374Z\",\"sharer-data\":{\"groups\":[{\"id\":\"126fdd2b-ba22-44a4-bb83-cb8be048c363\",\"role\":\"2\"}]},\"tag-data\":[\"Mule\",\"MuleSoft\",\"Schulung\",\"Training\"],\"numpages\":15},{\"uploaddate\":\"2012-11-05T09:08:22.256Z\",\"title\":\"\",\"version\":1,\"author\":\"AskSunday\",\"representations\":{\"pdf\":\"yes\",\"swf\":\"yes\",\"fulltext\":\"yes\",\"png\":\"yes\",\"jpg\":\"no\"},\"mimetype\":\"application\\/pdf\",\"owner\":{\"email\":\"ansgar.schulte@codecentric.de\",\"first-name\":\"Ansgar\",\"last-name\":\"Schulte\",\"name\":\"Ansgar Schulte\"},\"id\":\"10f9283a-1e44-4aed-aaaf-e8ad999c1455\",\"size\":249184,\"uploader\":{\"email\":\"ansgar.schulte@codecentric.de\",\"first-name\":\"Ansgar\",\"last-name\":\"Schulte\",\"name\":\"Ansgar Schulte\"},\"filename\":\"01-01 Basics.pdf\",\"versiondate\":\"2012-11-05T09:08:22.256Z\",\"sharer-data\":{\"groups\":[{\"id\":\"126fdd2b-ba22-44a4-bb83-cb8be048c363\",\"role\":\"2\"}]},\"tag-data\":[\"Mule\",\"MuleSoft\",\"Schulung\",\"Training\"],\"numpages\":6},{\"uploaddate\":\"2012-11-05T09:08:27.354Z\",\"title\":\"Module 02- Routers and Routing\",\"version\":1,\"author\":\"dell\",\"representations\":{\"pdf\":\"yes\",\"swf\":\"yes\",\"fulltext\":\"yes\",\"png\":\"yes\",\"jpg\":\"no\"},\"mimetype\":\"application\\/pdf\",\"owner\":{\"email\":\"ansgar.schulte@codecentric.de\",\"first-name\":\"Ansgar\",\"last-name\":\"Schulte\",\"name\":\"Ansgar Schulte\"},\"id\":\"04191b17-eb28-4592-8244-aae47c2f8d63\",\"size\":731212,\"uploader\":{\"email\":\"ansgar.schulte@codecentric.de\",\"first-name\":\"Ansgar\",\"last-name\":\"Schulte\",\"name\":\"Ansgar Schulte\"},\"filename\":\"01-02 Basics Lab.pdf\",\"versiondate\":\"2012-11-05T09:08:27.354Z\",\"sharer-data\":{\"groups\":[{\"id\":\"126fdd2b-ba22-44a4-bb83-cb8be048c363\",\"role\":\"2\"}]},\"tag-data\":[\"Mule\",\"MuleSoft\",\"Schulung\",\"Training\"],\"numpages\":14},{\"uploaddate\":\"2012-11-05T09:08:35.115Z\",\"title\":\"\",\"version\":1,\"author\":\"AskSunday\",\"representations\":{\"pdf\":\"yes\",\"swf\":\"yes\",\"fulltext\":\"yes\",\"png\":\"yes\",\"jpg\":\"no\"},\"mimetype\":\"application\\/pdf\",\"owner\":{\"email\":\"ansgar.schulte@codecentric.de\",\"first-name\":\"Ansgar\",\"last-name\":\"Schulte\",\"name\":\"Ansgar Schulte\"},\"id\":\"4189cf69-b2d7-46aa-be1d-5bca31f81a1e\",\"size\":679415,\"uploader\":{\"email\":\"ansgar.schulte@codecentric.de\",\"first-name\":\"Ansgar\",\"last-name\":\"Schulte\",\"name\":\"Ansgar Schulte\"},\"filename\":\"02-01 Unit Tests Manual.pdf\",\"versiondate\":\"2012-11-05T09:08:35.115Z\",\"sharer-data\":{\"groups\":[{\"id\":\"126fdd2b-ba22-44a4-bb83-cb8be048c363\",\"role\":\"2\"}]},\"tag-data\":[\"Mule\",\"MuleSoft\",\"Schulung\",\"Training\"],\"numpages\":13},{\"uploaddate\":\"2012-11-07T09:39:02.482Z\",\"title\":\"Module 02- Routers and Routing\",\"version\":1,\"author\":\"dell\",\"representations\":{\"pdf\":\"yes\",\"swf\":\"yes\",\"fulltext\":\"yes\",\"png\":\"yes\",\"jpg\":\"no\"},\"mimetype\":\"application\\/pdf\",\"owner\":{\"email\":\"ansgar.schulte@codecentric.de\",\"first-name\":\"Ansgar\",\"last-name\":\"Schulte\",\"name\":\"Ansgar Schulte\"},\"id\":\"ab45f5af-b8e6-479d-ba75-d2ba8eedd2ce\",\"size\":428050,\"uploader\":{\"email\":\"ansgar.schulte@codecentric.de\",\"first-name\":\"Ansgar\",\"last-name\":\"Schulte\",\"name\":\"Ansgar Schulte\"},\"filename\":\"02-01 Unit Tests PPT.pdf\",\"versiondate\":\"2012-11-07T09:39:02.482Z\",\"sharer-data\":{\"groups\":[{\"id\":\"126fdd2b-ba22-44a4-bb83-cb8be048c363\",\"role\":\"2\"}]},\"tag-data\":[\"Mule\",\"MuleSoft\",\"Schulung\",\"Training\"],\"numpages\":10}]}");
        responseStub.setBodyInputStream(centerDeviceInputStreamStub);

        return responseStub;
    }

    @Override
    public HttpResponse getDocumentRaw(String uuid) {
        InputStream documentFileStubStream = servletContext.getResourceAsStream("/WEB-INF/stubs/documentFileStub.txt");

        HttpResponse responseStub = new HttpResponse();
        responseStub.setStatusCode(200);
        responseStub.setHeader("Content-Type", "text/plain; charset=utf-8");
        responseStub.setHeader("Content-Disposition", "attachment; filename=\"documentFileStub.txt\"");
        responseStub.setHeader("Content-Length", "20");
        responseStub.setBodyInputStream(documentFileStubStream);

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
}
