package de.centerdevice.roca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author stephan
 */
@Controller
public class CenterDeviceLoginController {

    @RequestMapping(value = "/login")
    public void login() {
    }
}
