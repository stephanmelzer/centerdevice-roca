package de.centerdevice.roca.controller;

import de.centerdevice.roca.centerdevice.CenterDeviceService;
import de.centerdevice.roca.centerdevice.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
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
        output.flush();
    }

    protected void setHttpHeaders(HttpServletResponse httpServletResponse, HttpResponse centerDeviceResponse) {
        String[] allowedHeaders = {"Content-Encoding",
            "Content-Language",
            "Content-Length",
            "Content-MD5",
            "Content-Disposition",
            "Content-Type"};
        Map<String, String> centerDeviceResponseHeaders = centerDeviceResponse.getHeaders();
        Set<String> set = centerDeviceResponseHeaders.keySet();
        String[] centerDeviceHeaders = set.toArray(new String[set.size()]);

        //compare if centerDevice contains allowed headers
        for (String allowedHeader : allowedHeaders) {
            allowedHeader = allowedHeader.toLowerCase();
            for (int i = 0; i < centerDeviceHeaders.length; i++) {
                if (centerDeviceHeaders[i] != null && centerDeviceHeaders[i].trim().toLowerCase().equals(allowedHeader)) {
                    String header = centerDeviceHeaders[i];
                    String headerValue = centerDeviceResponseHeaders.get(centerDeviceHeaders[i]);
                    httpServletResponse.setHeader(header, headerValue);
                }
            }
        }
    }

    protected void setHttpStatusCode(HttpServletResponse httpServletResponse, HttpResponse centerDeviceResponse) {
        httpServletResponse.setStatus(centerDeviceResponse.getStatusCode());
    }
}
