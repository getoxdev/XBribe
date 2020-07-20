package com.xbribe.ui.main.drawers.drafts;

public class DraftModel
{
    String ministry;
    String address;
    String pincode;
    String city;
    String deparment;
    String organisation_name;
    String description;


    public DraftModel(String ministry, String address, String pincode, String city, String deparment, String organisation_name, String description) {
        this.ministry = ministry;
        this.address = address;
        this.pincode = pincode;
        this.city = city;
        this.deparment = deparment;
        this.organisation_name = organisation_name;
        this.description = description;
    }

    public String getMinistry() {
        return ministry;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDeparment() {
        return deparment;
    }

    public void setDeparment(String deparment) {
        this.deparment = deparment;
    }

    public String getOrganisation_name() {
        return organisation_name;
    }

    public void setOrganisation_name(String organisation_name) {
        this.organisation_name = organisation_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
