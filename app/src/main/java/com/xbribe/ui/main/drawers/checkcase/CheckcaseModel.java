package com.xbribe.ui.main.drawers.checkcase;

public class CheckcaseModel
{
    private String  crimeimage;
   private String name_organization;
    private String case_desc;
    private String caseprocess;
    private String casedetail;


    public CheckcaseModel(String crimeimage, String name_organization, String case_desc, String caseprocess, String casedetail) {
        this.crimeimage = crimeimage;
        this.name_organization = name_organization;
        this.case_desc = case_desc;
        this.caseprocess = caseprocess;
        this.casedetail = casedetail;
    }

    public String getCrimeimage() {
        return crimeimage;
    }

    public void setCrimeimage(String crimeimage) {
        this.crimeimage = crimeimage;
    }

    public String getName_organization() {
        return name_organization;
    }

    public void setName_organization(String name_organization) {
        this.name_organization = name_organization;
    }

    public String getCase_desc() {
        return case_desc;
    }

    public void setCase_desc(String case_desc) {
        this.case_desc = case_desc;
    }

    public String getCaseprocess() {
        return caseprocess;
    }

    public void setCaseprocess(String caseprocess) {
        this.caseprocess = caseprocess;
    }

    public String getCasedetail() {
        return casedetail;
    }

    public void setCasedetail(String casedetail) {
        this.casedetail = casedetail;
    }
}
