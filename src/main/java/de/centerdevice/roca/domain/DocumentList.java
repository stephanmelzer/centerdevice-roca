package de.centerdevice.roca.domain;

import java.util.ArrayList;

public class DocumentList {

    private int hits;
    private ArrayList<Document> documents;

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<Document> documents) {
        this.documents = documents;
    }
}
