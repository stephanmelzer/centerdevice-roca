package de.centerdevice.roca.centerdevice;

import de.centerdevice.roca.domain.Document;
import java.io.IOException;
import java.util.List;

public interface CenterDeviceService {

    List<Document> getAllDocuments() throws IOException;

    HttpResponse getAllDocumentsRaw();

    HttpResponse getDocumentRaw(String uuid);
}
