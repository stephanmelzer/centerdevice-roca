package de.centerdevice.roca.controller;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import de.centerdevice.roca.domain.DocumentList;
import de.centerdevice.roca.oauth.OAuthAccessToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DocumentController {

    private static final String PROTECTED_RESOURCE_URL = CenterDeviceOAuthConfig.protectedResourceUrl[0];
    @Autowired
    private OAuthService service;
    @Autowired
    private OAuthAccessToken token;

    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public String getAllDocuments(Model model) throws IOException {
        if (isNotLoggedIn()) {
            return "redirect:/login";
        }

        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        Response response = getResource(request);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DocumentList docs = mapper.readValue(response.getBody(), DocumentList.class);

        model.addAttribute("documents", docs);

        return "documentList";
    }

    @RequestMapping(value = "/documents", method = RequestMethod.GET, headers = {"Accept=application/json"})
    @ResponseBody
    public String getAllDocumentsAsJson(Model model, HttpServletResponse httpServletResponse) throws IOException {
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        Response response = getResource(request);
        httpServletResponse.setStatus(response.getCode());
        
        return response.getBody();
    }

    @RequestMapping(value = "/document/{documentId}", method = RequestMethod.GET)
    public void downloadDocument(HttpServletResponse response, @PathVariable String documentId) throws IOException {
        OAuthRequest request = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[1] + documentId);

        Token accessToken = new Token(token.getAccessToken(), CenterDeviceOAuthConfig.apiSecret);
        service.signRequest(accessToken, request);
        Response centerdeviceResponse = request.send();

        response.setStatus(centerdeviceResponse.getCode());
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

    private Response getResource(OAuthRequest request) {
        Token accessToken = new Token(token.getAccessToken(), CenterDeviceOAuthConfig.apiSecret);
        service.signRequest(accessToken, request);
        Response response = request.send();

        return response;
    }

    private boolean isNotLoggedIn() {
        if (token.getAccessToken().equals("")) {
            return true;
        }
        return false;
    }
}
