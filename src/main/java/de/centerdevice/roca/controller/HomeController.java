package de.centerdevice.roca.controller;

import de.centerdevice.roca.centerdevice.CenterDeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private CenterDeviceService centerDevice;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        if (centerDevice.isLoggedIn() == false) {
            return "welcome";
        }

        //return normal, logged in view
        return "redirect:/documents";
    }
}
