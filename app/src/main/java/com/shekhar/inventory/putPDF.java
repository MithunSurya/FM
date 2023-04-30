package com.shekhar.inventory;

public class putPDF {

    public String doc;
    public String url;

    public putPDF() {
    }

    public putPDF(String doc, String url) {
        this.doc = doc;
        this.url = url;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
