package de.centerdevice.roca.centerdevice;

import de.centerdevice.roca.domain.Document;
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

    String getAuthorizationUrl();

    // Groups
    public HttpResponse getAllGroupsRaw();

    public HttpResponse joinGroupRaw(String groupId);
}
