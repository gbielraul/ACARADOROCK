package com.podekrast.acaradorock.model;

import java.io.Serializable;

public class Audio implements Serializable {

    private String programId;
    private String programTitle;
    private String programName;
    private String programDate;
    private String programUrl;

    /*
    public void addAudio(String id, String programName, String title, String programDate, String url) {
        this.setId(id);
        this.setProgramName(programName);
        this.setTitle(title);
        this.setProgramDate(programDate);
        this.setUrl(url);
    }
     */

    /*----------------------------------------*/

    public String getProgramUrl() {
        return programUrl;
    }

    public void setProgramUrl(String programUrl) {
        this.programUrl = programUrl;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getProgramTitle() {
        return programTitle;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public String getProgramDate() {
        return programDate;
    }

    public void setProgramDate(String programDate) {
        this.programDate = programDate;
    }
}