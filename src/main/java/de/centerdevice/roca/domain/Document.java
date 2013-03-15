package de.centerdevice.roca.domain;

import java.util.ArrayList;
import java.util.Date;

public class Document {

    private String id;
    private String name;
    private Object owner;
    private Date uploaddate;
    private String version;
    private Object uploader;
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

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }

    public Date getUploaddate() {
        return uploaddate;
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

    public Object getUploader() {
        return uploader;
    }

    public void setUploader(Object uploader) {
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
