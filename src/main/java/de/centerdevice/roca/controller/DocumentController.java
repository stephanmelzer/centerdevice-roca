package de.centerdevice.roca.controller;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import de.centerdevice.roca.domain.DocumentList;
import de.centerdevice.roca.oauth.OAuthAccessToken;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DocumentController {

    private static final String PROTECTED_RESOURCE_URL = CenterDeviceOAuthConfig.protectedResourceUrl[0];
    @Autowired
    private OAuthService service;
    @Autowired
    private OAuthAccessToken token;

    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public String getAllDocuments(Model model) throws IOException {
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);

        Token accessToken = new Token(token.getAccessToken(), CenterDeviceOAuthConfig.apiSecret);
        service.signRequest(accessToken, request);
        Response response = request.send();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DocumentList docs = mapper.readValue(response.getBody(), DocumentList.class);

        model.addAttribute("documents", docs);

        return "documentList";
    }

    @RequestMapping(value = "/document/{documentId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String documentId) throws IOException {
        OAuthRequest request = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[1] + documentId);

        Token accessToken = new Token(token.getAccessToken(), CenterDeviceOAuthConfig.apiSecret);
        service.signRequest(accessToken, request);
        Response response = request.send();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", response.getHeader("Content-Disposition"));
        headers.add("Content-Type", response.getHeader("Content-Type"));
        headers.add("Content-Length", response.getHeader("Content-Length"));

        InputStream stream = response.getStream();
        byte[] body = convertInputStreamToByteArray(stream);

        return new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);
    }

    private byte[] convertInputStreamToByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }
}
