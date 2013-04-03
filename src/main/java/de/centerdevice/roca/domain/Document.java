package de.centerdevice.roca.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.io.FileUtils;

public class Document {

    private String id;
    private String name;
    private User owner;
    private Date uploaddate;
    private String version;
    private User uploader;
    private String filename;
    private int size;
    private Date versiondate;
    private String mimetype;
    private String title;
    private String author;
    private int numpages;
    private Object representation;
    private ArrayList<Object> groupListData;
    private ArrayList<Object> sharerData;
    private ArrayList<Object> commentData;
    private ArrayList<Object> versionData;
    private ArrayList<Object> linkData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getUploaddate() {
        return uploaddate;
    }

    public String getHtml5Uploaddate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
        return df.format(uploaddate);
    }

    public String getFormatedUploaddate() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return df.format(uploaddate);
    }

    public void setUploaddate(Date uploaddate) {
        this.uploaddate = uploaddate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getSize() {
        return size;
    }

    public String getFormatedSize() {
        return FileUtils.byteCountToDisplaySize(size);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Date getVersiondate() {
        return versiondate;
    }

    public void setVersiondate(Date versiondate) {
        this.versiondate = versiondate;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumpages() {
        return numpages;
    }

    public void setNumpages(int numpages) {
        this.numpages = numpages;
    }

    public Object getRepresentation() {
        return representation;
    }

    public void setRepresentation(Object representation) {
        this.representation = representation;
    }

    public ArrayList<Object> getGroupListData() {
        return groupListData;
    }

    public void setGroupListData(ArrayList<Object> groupListData) {
        this.groupListData = groupListData;
    }

    public ArrayList<Object> getSharerData() {
        return sharerData;
    }

    public void setSharerData(ArrayList<Object> sharerData) {
        this.sharerData = sharerData;
    }

    public ArrayList<Object> getCommentData() {
        return commentData;
    }

    public void setCommentData(ArrayList<Object> commentData) {
        this.commentData = commentData;
    }

    public ArrayList<Object> getVersionData() {
        return versionData;
    }

    public void setVersionData(ArrayList<Object> versionData) {
        this.versionData = versionData;
    }

    public ArrayList<Object> getLinkData() {
        return linkData;
    }

    public void setLinkData(ArrayList<Object> linkData) {
        this.linkData = linkData;
    }
}
