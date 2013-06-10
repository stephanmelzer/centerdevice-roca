package de.centerdevice.roca.controller;

import de.centerdevice.roca.centerdevice.HttpMessage;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController extends CenterDeviceController {

    @RequestMapping(value = "/user", method = RequestMethod.GET, headers = {"Accept=application/json"})
    public void getUserInformation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        HttpMessage centerDeviceResponse = centerdevice.getUserInformationRaw(getSearchQuery(httpServletRequest));

        setHttpStatusCode(httpServletResponse, centerDeviceResponse);
        setHttpHeaders(httpServletResponse, centerDeviceResponse);
        copyStream(httpServletResponse.getOutputStream(), centerDeviceResponse.getBodyInputStream());

        centerDeviceResponse.getBodyInputStream().close();
    }

    private String getSearchQuery(HttpServletRequest httpServletRequest) {
        String searchQuery = httpServletRequest.getQueryString();
        searchQuery = (searchQuery == null) ? "" : searchQuery;

        return searchQuery;
    }
}
