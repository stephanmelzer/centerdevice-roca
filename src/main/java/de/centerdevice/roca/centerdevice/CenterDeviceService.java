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

    HttpMessage getDocumentsRaw(String searchQuery);

    HttpMessage getDocumentRaw(String uuid);

    String getAuthorizationUrl();

    HttpMessage uploadDocumentRaw(HttpMessage clientRequest);

    // Groups
    HttpMessage getAllGroupsRaw();

    HttpMessage joinGroupRaw(String groupId);

    // User information
    User getUserInformation(String searchQuery) throws IOException;

    HttpMessage getUserInformationRaw(String searchQuery);
}
