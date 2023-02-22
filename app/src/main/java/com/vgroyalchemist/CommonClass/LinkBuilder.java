package com.vgroyalchemist.CommonClass;

import java.net.URLEncoder;

public class LinkBuilder {

//              String builtLink = new LinkBuilder().setDomain("example.page.link")
//            .setLink("your link goes here")
//            .setSd(socialTagDesc)
//            .setSt(socialTagTitle)
//            .setSi("social image link here")
//            .setApn("android app pkg")
//            .setAmv("android min version")
//            .setIbi("ios app pkg")
//            .setImv("ios app min version")
//            .setIsi("iosid number").build();


    private String domain;
    private String link;
    private String apn;
    private String amv;

    private String ibi;
    private String imv;
    private String isi;

    private String st;
    private String sd;
    private String si;

    public String getURLEncode(String input) {

        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return input;
    }


    public LinkBuilder setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public LinkBuilder setLink(String link) {
        this.link = getURLEncode(link);
        return this;
    }

    public LinkBuilder setApn(String apn) {
        this.apn = apn;
        return this;
    }

    public LinkBuilder setAmv(String amv) {
        this.amv = amv;
        return this;
    }

    public LinkBuilder setIbi(String ibi) {
        this.ibi = ibi;
        return this;
    }

    public LinkBuilder setImv(String imv) {
        this.imv = imv;
        return this;
    }

    public LinkBuilder setIsi(String isi) {
        this.isi = isi;
        return this;
    }

    public LinkBuilder setSt(String st) {
        this.st = getURLEncode(st);
        return this;
    }

    public LinkBuilder setSd(String sd) {
        this.sd = getURLEncode(sd);
        ;
        return this;
    }

    public LinkBuilder setSi(String si) {
        this.si = getURLEncode(si);
        ;
        return this;
    }

    public String build() {
        return String.format("https://%s/?link=%s&apn=%s&amv=%s&ibi=%s&imv=%s&isi=%s&st=%s&sd=%s&si=%s"
                , domain, link, apn, amv, ibi, imv, isi, st, sd, si);
    }
}