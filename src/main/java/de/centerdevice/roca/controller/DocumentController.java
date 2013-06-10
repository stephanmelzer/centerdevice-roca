package de.centerdevice.roca.controller;

import de.centerdevice.roca.centerdevice.HttpMessage;
import de.centerdevice.roca.centerdevice.HttpMessage;
import de.centerdevice.roca.domain.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DocumentController extends CenterDeviceController {

    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public String getDocuments(HttpServletRequest httpServletRequest, Model model) {
        if (centerdevice.isLoggedIn() == false) {
            return "welcome";
        }

        try {
            List<Document> documents = centerdevice.getDocuments(getSearchQuery(httpServletRequest));
            model.addAttribute("documents", documents);
        } catch (IOException ex) {
            Logger.getLogger(DocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "documentList";
    }

    @RequestMapping(value = "/documents", method = RequestMethod.GET, headers = {"Accept=application/json"})
    public void getDocumentsAsJson(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        HttpMessage centerDeviceResponse = centerdevice.getDocumentsRaw(getSearchQuery(httpServletRequest));

        setHttpStatusCode(httpServletResponse, centerDeviceResponse);
        setHttpHeaders(httpServletResponse, centerDeviceResponse);
        copyStream(httpServletResponse.getOutputStream(), centerDeviceResponse.getBodyInputStream());

        centerDeviceResponse.getBodyInputStream().close();
    }

    @RequestMapping(value = "/document/{documentId}", method = RequestMethod.GET)
    public void downloadDocument(HttpServletResponse httpServletResponse, @PathVariable String documentId) throws IOException {
        HttpMessage centerDeviceResponse = centerdevice.getDocumentRaw(documentId);

        setHttpStatusCode(httpServletResponse, centerDeviceResponse);
        setHttpHeaders(httpServletResponse, centerDeviceResponse);
        copyStream(httpServletResponse.getOutputStream(), centerDeviceResponse.getBodyInputStream());

        centerDeviceResponse.getBodyInputStream().close();
    }

    // Checks if the search query string is null (empty).
    // If the search query string is null, the method creates an empty string,
    // otherwise returns the search query string.
    private String getSearchQuery(HttpServletRequest httpServletRequest) {
        String searchQuery = httpServletRequest.getQueryString();
        searchQuery = (searchQuery == null) ? "" : searchQuery;

        return searchQuery;
    }

    @RequestMapping(value = "/documents/json", method = RequestMethod.POST)
    public String uploadFile(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestParam(value = "redirect", required = false) String redirect) throws IOException, ServletException {
        InputStream inputStream = httpServletRequest.getInputStream();
        HttpMessage clientRequest = new HttpMessage();
        clientRequest.setBodyInputStream(inputStream);
        String contentTypeHeader = "Content-Type";
        clientRequest.setHeader(contentTypeHeader, httpServletRequest.getHeader(contentTypeHeader));

        HttpMessage centerdeviceResponse = centerdevice.uploadDocumentRaw(clientRequest);
        httpServletResponse.setStatus(centerdeviceResponse.getStatusCode());

        // only the single page application calls this method.
        // Through a normal browser invoked form post request,
        // the SPA is redirected to itself using the Origin header.
        // TODO: In the future the SPA should make an XHR not a normal form post request.
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/documents", method = RequestMethod.POST)
    public String uploadFileJson(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        InputStream inputStream = httpServletRequest.getInputStream();
        HttpMessage clientRequest = new HttpMessage();
        clientRequest.setBodyInputStream(inputStream);
        String contentTypeHeader = "Content-Type";
        clientRequest.setHeader(contentTypeHeader, httpServletRequest.getHeader(contentTypeHeader));

        centerdevice.uploadDocumentRaw(clientRequest);

        return "redirect:documents";
    }
}
