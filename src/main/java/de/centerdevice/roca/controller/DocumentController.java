package de.centerdevice.roca.controller;

import de.centerdevice.roca.centerdevice.HttpResponse;
import de.centerdevice.roca.domain.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;

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
        HttpResponse centerDeviceResponse = centerdevice.getDocumentsRaw(getSearchQuery(httpServletRequest));

        setHttpStatusCode(httpServletResponse, centerDeviceResponse);
        setHttpHeaders(httpServletResponse, centerDeviceResponse);
        copyStream(httpServletResponse.getOutputStream(), centerDeviceResponse.getBodyInputStream());

        centerDeviceResponse.getBodyInputStream().close();
    }

    @RequestMapping(value = "/document/{documentId}", method = RequestMethod.GET)
    public void downloadDocument(HttpServletResponse httpServletResponse, @PathVariable String documentId) throws IOException {
        HttpResponse centerDeviceResponse = centerdevice.getDocumentRaw(documentId);

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
}
