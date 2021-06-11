package com.example.application.pocketbook;

public class putPDF {
    public String name1;
    public String url;

    public putPDF() {
    }

    public putPDF(String name1, String url) {
        this.name1 = name1;
        this.url = url;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
