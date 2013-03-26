package de.centerdevice.roca.controller;

import de.centerdevice.roca.oauth.OAuthAccessToken;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private OAuthAccessToken token;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        if (token.getAccessToken().equals("")) {
            //no token aquired, so not logged in
            return "welcome";
        }

        //return normal, logged in view
        return "redirect:/documents";
    }
}
