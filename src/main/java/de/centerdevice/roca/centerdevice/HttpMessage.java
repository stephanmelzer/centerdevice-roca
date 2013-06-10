package de.centerdevice.roca.centerdevice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpMessage {

    private int statusCode;
    protected Map<String, String> headers;
    protected InputStream bodyInputStream;

    public HttpMessage() {
        this.bodyInputStream = new ByteArrayInputStream("Default InputStream".getBytes());
        this.headers = new HashMap<>();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public InputStream getBodyInputStream() {
        return bodyInputStream;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBodyInputStream(InputStream bodyInputStream) {
        this.bodyInputStream = bodyInputStream;
    }

    public String getBodyAsString() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[16384];
        int bytesRead;
        while ((bytesRead = bodyInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        return outputStream.toString();
    }
}