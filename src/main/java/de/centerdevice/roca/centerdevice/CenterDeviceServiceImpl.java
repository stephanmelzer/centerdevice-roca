package de.centerdevice.roca.centerdevice;

import com.sun.xml.internal.messaging.saaj.util.TeeInputStream;
import de.centerdevice.roca.domain.Document;

import de.centerdevice.roca.config.CenterDeviceOAuthConfig;
import de.centerdevice.roca.domain.User;
import de.centerdevice.roca.oauth.OAuthAccessToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;

public class CenterDeviceServiceImpl implements CenterDeviceService {

    @Autowired
    private OAuthService service;
    @Autowired
    private OAuthAccessToken accessToken;

    @Override
    public List<Document> getDocuments(String searchQuery) throws IOException {
        HttpResponse response = getDocumentsRaw(searchQuery);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonNode rootNode = mapper.readValue(response.getBodyAsString(), JsonNode.class);
        JsonNode documentNodes = rootNode.get("documents");

        List<Document> documents = new LinkedList<>();
        if (documentNodes != null) {
            documents = mapper.readValue(documentNodes, new TypeReference<List<Document>>() {
            });
        }

        return documents;
    }

    @Override
    public HttpResponse getDocumentsRaw(String searchQuery) {
        OAuthRequest request = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[0] + "?" + searchQuery);
        return getResource(request);
    }

    @Override
    public HttpResponse getDocumentRaw(String uuid) {
        OAuthRequest centerDeviceRequest = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[1] + uuid);
        return getResource(centerDeviceRequest);
    }

    private HttpResponse getResource(OAuthRequest centerDeviceRequest) {
        Token token = new Token(accessToken.getAccessToken(), CenterDeviceOAuthConfig.apiSecret);
        service.signRequest(token, centerDeviceRequest);
        Response centerDeviceResponse = centerDeviceRequest.send();

        //create HttpResponse object
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(centerDeviceResponse.getCode());
        httpResponse.setHeaders(centerDeviceResponse.getHeaders());
        httpResponse.setBodyInputStream(centerDeviceResponse.getStream());

        return httpResponse;
    }

    @Override
    public String getAuthorizationUrl() {
        return service.getAuthorizationUrl(null);
    }

    @Override
    public void login(String code) {
        Token token = service.getAccessToken(null, new Verifier(code));
        accessToken.setAccessToken(token.getToken());
    }

    @Override
    public void logout() {
        accessToken.setAccessToken("");
    }

    @Override
    public boolean isLoggedIn() {
        if (accessToken.getAccessToken().equals("")) {
            return false;
        }
        return true;
    }

    @Override
    public HttpResponse joinGroupRaw(String groupId) {
        OAuthRequest centerDeviceRequest = new OAuthRequest(Verb.POST, CenterDeviceOAuthConfig.protectedResourceUrl[2] + groupId);

        centerDeviceRequest.addHeader("Content-Type", "application/json; charset=UTF-8");
        centerDeviceRequest.addPayload("{'action' : 'join-group'}");

        return getResource(centerDeviceRequest);
    }

    @Override
    public HttpResponse getAllGroupsRaw() {
        OAuthRequest centerDeviceRequest = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[3]);
        return getResource(centerDeviceRequest);
    }

    @Override
    public HttpResponse uploadDocumentRaw(HttpRequest clientRequest) {
        HttpResponse centerdeviceResponse = new HttpResponse();
        try {
            URL url = new URL(CenterDeviceOAuthConfig.protectedResourceUrl[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Authorization", "Bearer " + accessToken.getAccessToken());
            connection.setRequestProperty("Content-Type", clientRequest.getHeaders().get("Content-Type"));
            connection.setDoOutput(true);
            connection.connect();

            //get boundary
            String contentType = clientRequest.getHeaders().get("Content-Type");
            String[] contentTypeValues = contentType.split(";");
            String boundary = "";

            for (String value : contentTypeValues) {
                if (value.contains("boundary")) {
                    boundary = value.split("=")[1];
                    break;
                }
            }
            //write existing content to outputstream and count bytes for filesize

            OutputStream outputStream = connection.getOutputStream();

            int countedBytes = countBytes(clientRequest.getBodyInputStream(), outputStream, boundary);

            //write meta data (temporay solution)
            String contentDispositionHeader = "Content-Disposition: form-data; name=\"metadata\"\r\n";
            String contentTypeHeader = "Content-Type: application/json\r\n\r\n";
            String metaDataJson = "{\"metadata\":{\"document\":{\"size\":" + countedBytes + "}}}\r\n";

            outputStream.write(("\r\n--" + boundary).getBytes());
            outputStream.write("\r\n".getBytes());
            outputStream.write(contentDispositionHeader.getBytes());
            outputStream.write(contentTypeHeader.getBytes());
            outputStream.write(metaDataJson.getBytes());
            outputStream.write(("--" + boundary + "--").getBytes());
            outputStream.write("\r\n".getBytes());

            String responseMessage = connection.getResponseMessage();
            centerdeviceResponse.setStatusCode(connection.getResponseCode());
            centerdeviceResponse.setBodyInputStream(connection.getErrorStream());
        } catch (MalformedURLException ex) {
            Logger.getLogger(CenterDeviceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CenterDeviceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return centerdeviceResponse;
    }


    @Override
    public User getUserInformation(String searchQuery) throws IOException {
        HttpResponse response = getUserInformationRaw(searchQuery);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        User user = mapper.readValue(response.getBodyAsString(), User.class);

        return user;
    }

    @Override
    public HttpResponse getUserInformationRaw(String searchQuery) {
        OAuthRequest request = new OAuthRequest(Verb.GET, CenterDeviceOAuthConfig.protectedResourceUrl[4] + "?" + searchQuery);
        return getResource(request);
    }

    public int countBytes(InputStream inputStream, OutputStream outputStream, String boundaryString) throws IOException {
        InputStream tis = (new TeeInputStream(inputStream, outputStream));

        //looking for first boundary

        int readByte;
        int boundaryIndex = 0;
        boolean isBoundary = false;
        byte[] boundary = ("--" + boundaryString).getBytes();;

        //check for starting boundary otherwise through exception, because boundary musst come first?!

        while ((readByte = tis.read()) != -1) {
            // check if the current byte is the same as in the boundary array
            // and the index length is not bigger than the boundary array length
            if (boundaryIndex < boundary.length && boundary[boundaryIndex] == readByte) {
                boundaryIndex++;
                if (boundaryIndex == (boundary.length)) {
                    isBoundary = true;
                    break;
                }
            }
        }

        // here comes the header...
        byte[] headerEnd = "\r\n\r\n".getBytes();
        boolean isHeaderEnd = false;
        boundaryIndex = 0;
        while ((readByte = tis.read()) != -1) {
            if (boundaryIndex < headerEnd.length && headerEnd[boundaryIndex] == readByte) {
                boundaryIndex++;
                if (boundaryIndex == (headerEnd.length)) {
                    isHeaderEnd = true;
                    break;
                }
            } else {
                //the current byte does not match
                boundaryIndex = 0;
            }
        }


        //now the content
        boolean isContent = false;
        int countedBytes = 0;
        boundaryIndex = 0;

        boolean countCurrentByte = false;

        byte[] endBoundary = ("\r\n--" + boundaryString).getBytes();

        while ((readByte = tis.read()) != -1) {
            if (boundaryIndex < endBoundary.length) {


                if (boundaryIndex > 0 && endBoundary[boundaryIndex] != readByte) {
                    countCurrentByte = true;
                }

                if (endBoundary[boundaryIndex] != readByte) {
                    //reset index and try again
                    //the current byte does not match
                    countedBytes = boundaryIndex > 0 ? countedBytes + boundaryIndex : countedBytes + 1;
                    boundaryIndex = 0;

                }

                //check if current part of boundary is the same as the current read byte.
                if (endBoundary[boundaryIndex] == readByte) {
                    boundaryIndex++;

                    //check if the boundary index is at the end of the boundary array.
                    if (boundaryIndex == (endBoundary.length)) {
                        isContent = true;
                        break;
                    }

                } else if (countCurrentByte) {
                    countedBytes++;
                    countCurrentByte = false;
                }
            }
        }

        return countedBytes;
    }
}
