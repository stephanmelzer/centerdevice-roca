package de.centerdevice.roca.centerdevice;

import de.centerdevice.roca.domain.Document;
import de.centerdevice.roca.domain.User;
import java.io.IOException;
import java.util.List;

public interface CenterDeviceService {

    //Login
    void login(String code);

    void logout();

    boolean isLoggedIn();

    // Documents
    List<Document> getDocuments(String searchQuery) throws IOException;

    HttpResponse getDocumentsRaw(String searchQuery);

    HttpResponse getDocumentRaw(String uuid);

    HttpResponse getDocumentAsFlash(String uuid);

    String getAuthorizationUrl();

    HttpResponse uploadDocumentRaw(HttpRequest clientRequest);

    // Groups
    HttpResponse getAllGroupsRaw();

    HttpResponse joinGroupRaw(String groupId);

    // User information
    User getUserInformation(String searchQuery) throws IOException;

    HttpResponse getUserInformationRaw(String searchQuery);
}
