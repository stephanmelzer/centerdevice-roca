package de.centerdevice.roca.controller;

import de.centerdevice.roca.centerdevice.HttpMessage;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GroupController extends CenterDeviceController {

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.POST)
    public void joinGroup(HttpServletResponse httpServletResponse, @PathVariable String groupId) throws IOException {
        HttpMessage centerDeviceResponse = centerdevice.joinGroupRaw(groupId);

        setHttpStatusCode(httpServletResponse, centerDeviceResponse);
        setHttpHeaders(httpServletResponse, centerDeviceResponse);
    }

    @RequestMapping(value = "/groups", method = RequestMethod.GET, headers = {"Accept=application/json"})
    public void getAllGroupsAsJson(HttpServletResponse httpServletResponse) throws IOException {
        HttpMessage centerDeviceResponse = centerdevice.getAllGroupsRaw();

        setHttpStatusCode(httpServletResponse, centerDeviceResponse);
        setHttpHeaders(httpServletResponse, centerDeviceResponse);
        copyStream(httpServletResponse.getOutputStream(), centerDeviceResponse.getBodyInputStream());

        centerDeviceResponse.getBodyInputStream().close();
    }
}
