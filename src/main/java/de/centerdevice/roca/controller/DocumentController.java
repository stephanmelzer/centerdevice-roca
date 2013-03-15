package de.centerdevice.roca.controller;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import de.centerdevice.roca.domain.Documents;
import de.centerdevice.roca.oauth.OAuthAccessToken;
import java.io.IOException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;

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
        Documents docs = mapper.readValue(response.getBody(), Documents.class);

        model.addAttribute("documents", docs);

        return "documentList";
    }
}
