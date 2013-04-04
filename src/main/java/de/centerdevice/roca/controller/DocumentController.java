package de.centerdevice.roca.controller;

import de.centerdevice.roca.centerdevice.CenterDeviceService;
import de.centerdevice.roca.centerdevice.HttpResponse;
import de.centerdevice.roca.domain.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DocumentController {

    @Autowired
    private CenterDeviceService centerdevice;

    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public String getAllDocuments(Model model) {
        if (centerdevice.isLoggedIn() == false) {
            return "welcome";
        }

        try {
            List<Document> documents = centerdevice.getAllDocuments();
            model.addAttribute("documents", documents);
        } catch (IOException ex) {
            Logger.getLogger(DocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "documentList";
    }

    @RequestMapping(value = "/documents", method = RequestMethod.GET, headers = {"Accept=application/json"})
    public void getAllDocumentsAsJson(HttpServletResponse httpServletResponse) throws IOException {
        HttpResponse centerDeviceResponse = centerdevice.getAllDocumentsRaw();

        setHttpStatusCode(httpServletResponse, centerDeviceResponse);
        setHttpHeaders(httpServletResponse, centerDeviceResponse);
        copyStream(httpServletResponse.getOutputStream(), centerDeviceResponse.getBodyInputStream());

        httpServletResponse.getOutputStream().flush();
        centerDeviceResponse.getBodyInputStream().close();

    }

    @RequestMapping(value = "/document/{documentId}", method = RequestMethod.GET)
    public void downloadDocument(HttpServletResponse httpServletResponse, @PathVariable String documentId) throws IOException {
        HttpResponse centerDeviceResponse = centerdevice.getDocumentRaw(documentId);

        setHttpStatusCode(httpServletResponse, centerDeviceResponse);
        setHttpHeaders(httpServletResponse, centerDeviceResponse);
        copyStream(httpServletResponse.getOutputStream(), centerDeviceResponse.getBodyInputStream());

        httpServletResponse.getOutputStream().flush();
        centerDeviceResponse.getBodyInputStream().close();
    }

    private void copyStream(OutputStream output, InputStream input) throws IOException {
        byte[] buffer = new byte[16384];
        int bytesRead;

        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void setHttpStatusCode(HttpServletResponse httpServletResponse, HttpResponse centerDeviceResponse) {
        httpServletResponse.setStatus(centerDeviceResponse.getStatusCode());
    }

    private void setHttpHeaders(HttpServletResponse httpServletResponse, HttpResponse centerDeviceResponse) {
        Map<String, String> headers = centerDeviceResponse.getHeaders();
        for (String header : headers.keySet()) {
            if (httpServletResponse.containsHeader(header) == false) {
                httpServletResponse.addHeader(header, headers.get(header));
            }
        }
    }
}
