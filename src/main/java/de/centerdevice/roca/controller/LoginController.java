package de.centerdevice.roca.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Scope(value = "session")
public class LoginController extends CenterDeviceController {

    private String externalRedirectUrl;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "redirect", required = false) String redirect) {
        externalRedirectUrl = redirect;
        String authorizationUrl = centerdevice.getAuthorizationUrl();

        return "redirect:" + authorizationUrl;
    }

    @RequestMapping(value = "/login", params = "code")
    public String loggedIn(@RequestParam("code") String code) {
        centerdevice.login(code);

        if (externalRedirectUrl != null) {
            return "redirect:" + externalRedirectUrl;
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/logout")
    public String logoutRoca(HttpServletRequest request) {
        request.getSession().invalidate();
        centerdevice.logout();
        return "welcome";
    }

    @RequestMapping(value = "/logout", headers = "Accept=application/json")
    @ResponseBody
    public void logoutSpa(HttpServletRequest request) {
        request.getSession().invalidate();
        centerdevice.logout();
    }
}
