package de.centerdevice.roca.controller;

import de.centerdevice.roca.oauth.OAuthAccessToken;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CenterDeviceLoginController {

    private static Token EMPTY_TOKEN = null;
    @Autowired
    private OAuthService service;
    @Autowired
    private OAuthAccessToken t;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String redirectToCenterDeviceLogin() {
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);

        return "redirect:" + authorizationUrl;
    }

    @RequestMapping(value = "/login", params = "code")
    public String loggedIn(@RequestParam("code") String accessToken) {
        Token token = service.getAccessToken(EMPTY_TOKEN, new Verifier(accessToken));
        t.setAccessToken(token.getToken());

        System.out.println(t.getAccessToken());

        return "redirect:/";
    }
}
