package de.centerdevice.roca.centerdevice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private int statusCode;
    private Map<String, String> headers;
    private InputStream bodyInputStream;

    public HttpResponse() {
        this.headers = new HashMap<String, String>();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public InputStream getBodyInputStream() {
        return bodyInputStream;
    }

    public void setBodyInputStream(InputStream bodyInputStream) {
        this.bodyInputStream = bodyInputStream;
    }

    public String getBodyAsString() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[16384];
        int bytesRead;

        while ((bytesRead = bodyInputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        return baos.toString();
    }
}
