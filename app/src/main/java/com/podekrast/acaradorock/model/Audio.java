package com.podekrast.acaradorock.model;

import java.io.Serializable;

public class Audio implements Serializable {

    private String name;
    private String id;
    private String programName;
    private String url;

    public void addAudio(String id, String programName, String name, String url) {
        this.setId(id);
        this.setProgramName(programName);
        this.setName(name);
        this.setUrl(url);
    }

    /*----------------------------------------*/

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

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
}