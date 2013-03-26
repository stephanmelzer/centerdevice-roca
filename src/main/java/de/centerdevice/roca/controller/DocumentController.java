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
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
    public void downloadDocument(HttpServletResponse response, @PathVariable String documentId) throws IOException {
        OAuthRequest request = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[1] + documentId);

        Token accessToken = new Token(token.getAccessToken(), CenterDeviceOAuthConfig.apiSecret);
        service.signRequest(accessToken, request);
        Response centerdeviceResponse = request.send();

        response.addHeader("Content-Disposition", centerdeviceResponse.getHeader("Content-Disposition"));
        response.addHeader("Content-Type", centerdeviceResponse.getHeader("Content-Type"));
        response.addHeader("Content-Length", centerdeviceResponse.getHeader("Content-Length"));

        InputStream centerdeviceStream = centerdeviceResponse.getStream();
        copyStream(centerdeviceStream, response.getOutputStream());
    }

    private void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[16384];
        int bytesRead;

        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
}
