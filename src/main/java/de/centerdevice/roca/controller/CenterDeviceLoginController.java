package de.centerdevice.roca.controller;

import de.centerdevice.roca.oauth.OAuthAccessToken;
import javax.servlet.http.HttpServletRequest;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Scope(value = "session")
public class CenterDeviceLoginController {

    private static Token EMPTY_TOKEN = null;
    @Autowired
    private OAuthService service;
    @Autowired
    private OAuthAccessToken accessToken;
    private String externalRedirectUrl;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String redirectToCenterDeviceLogin() {
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);

        return "redirect:" + authorizationUrl;
    }

    //TODO: optional params and remove the above method?
    @RequestMapping(value = "/login", method = RequestMethod.GET, params = "redirect")
    public String externalRedirectToCenterDeviceLogin(@RequestParam("redirect") String redirect) {
        externalRedirectUrl = redirect;
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);

        return "redirect:" + authorizationUrl;
    }

    @RequestMapping(value = "/login", params = "code")
    public String loggedIn(@RequestParam("code") String code) {
        Token token = service.getAccessToken(EMPTY_TOKEN, new Verifier(code));
        accessToken.setAccessToken(token.getToken());

        if (externalRedirectUrl != null) {
            return "redirect:" + externalRedirectUrl;
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/logout")
    public String logoutRoca(HttpServletRequest request) {
        logout(request);
        return "welcome";
    }

    @RequestMapping(value = "/logout", headers = "Accept=application/json")
    @ResponseBody
    public void logoutSpa(HttpServletRequest request) {
        logout(request);
    }

    private void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
