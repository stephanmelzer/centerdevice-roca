package de.centerdevice.roca.controller;

import de.centerdevice.roca.centerdevice.CenterDeviceService;
import de.centerdevice.roca.centerdevice.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CenterDeviceController {

    @Autowired
    protected CenterDeviceService centerdevice;

    protected void copyStream(OutputStream output, InputStream input) throws IOException {
        byte[] buffer = new byte[16384];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    protected void setHttpHeaders(HttpServletResponse httpServletResponse, HttpResponse centerDeviceResponse) {
        Map<String, String> headers = centerDeviceResponse.getHeaders();
        for (String header : headers.keySet()) {
            if (header != null && httpServletResponse.containsHeader(header) == false) {
                httpServletResponse.setHeader(header, headers.get(header));
            }
        }
    }

    protected void setHttpStatusCode(HttpServletResponse httpServletResponse, HttpResponse centerDeviceResponse) {
        httpServletResponse.setStatus(centerDeviceResponse.getStatusCode());
    }
}
