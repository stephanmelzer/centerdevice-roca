package de.centerdevice.roca.controller;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import de.centerdevice.roca.domain.Documents;
import de.centerdevice.roca.oauth.CenterDeviceProvider;
import java.io.IOException;
import java.util.Scanner;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 *
 * @author stephan
 */
@Controller
public class DocumentController {

    private static final String NETWORK_NAME = "CenterDevice";
    private static final String PROTECTED_RESOURCE_URL = CenterDeviceOAuthConfig.protectedResourceUrl[0];

    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public String getAllDocuments(Model model) throws IOException {
        Token EMPTY_TOKEN = null;
        String apiKey = CenterDeviceOAuthConfig.apiKey;
        String apiSecret = CenterDeviceOAuthConfig.apiSecret;
        // Replace these with your own api key and secret
        OAuthService service = new ServiceBuilder()
                .provider(CenterDeviceProvider.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback(CenterDeviceOAuthConfig.callbackUrl)
                .debugStream(System.out)
                .debug()
                .build();
        Scanner in = new Scanner(System.in);
        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
//        System.out.println("Fetching the Authorization URL...");
//        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
//        System.out.println("Got the Authorization URL!");
//        System.out.println("Now go and authorize Scribe here:");
//        System.out.println(authorizationUrl);
//        System.out.println("And paste the authorization code here");
//        System.out.print(">>");
//        Verifier verifier = new Verifier(in.nextLine());
//        System.out.println();
//
//
//        // Trade the Request Token and Verfier for the Access Token
//        System.out.println("Trading the Request Token for an Access Token...");
//        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
//        System.out.println("Got the Access Token!");
//        System.out.println("(if your curious it looks like this: " + accessToken + " )");
//        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);

        Token accessToken = new Token(CenterDeviceOAuthConfig.accessToken, apiSecret);
        service.signRequest(accessToken, request);

        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Documents docs = mapper.readValue(response.getBody(), Documents.class);
        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with Scribe! :)");

        model.addAttribute("documents", docs);

        return "documentList";
    }
}
