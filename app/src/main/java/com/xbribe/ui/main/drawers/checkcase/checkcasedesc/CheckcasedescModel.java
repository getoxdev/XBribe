package com.xbribe.ui.main.drawers.checkcase.checkcasedesc;

import com.xbribe.ui.main.drawers.checkcase.CheckcaseModel;

import java.util.List;

public class CheckcasedescModel
{
    private List<CheckcaseModel>  casedetails;
    private String  location;
    private int imagesno;
    private int audiono;
    private int videono;

    public CheckcasedescModel(List<CheckcaseModel> casedetails, String location, int imagesno, int audiono, int videono) {
        this.casedetails = casedetails;
        this.location = location;
        this.imagesno = imagesno;
        this.audiono = audiono;
        this.videono = videono;
    }

    public List<CheckcaseModel> getCasedetails()
    {
        return casedetails;
    }

    public void setCasedetails(List<CheckcaseModel> casedetails) {
        this.casedetails = casedetails;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImagesno() {
        return imagesno;
    }

    public void setImagesno(int imagesno) {
        this.imagesno = imagesno;
    }

    public int getAudiono() {
        return audiono;
    }

    public void setAudiono(int audiono) {
        this.audiono = audiono;
    }

    public int getVideono() {
        return videono;
    }

    public void setVideono(int videono) {
        this.videono = videono;
    }
}
