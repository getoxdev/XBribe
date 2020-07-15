package com.xbribe.ui.main.drawers.notification;

public class NotificationModel
{
    String organisation_name;
    String organisation_ministry;
    String organisationdepartmnt;
    String case_id;
    String user_id;
    String notification_msg;

    public NotificationModel(String organisation_name, String organisation_ministry, String organisationdepartmnt, String case_id, String user_id, String notification_msg) {
        this.organisation_name = organisation_name;
        this.organisation_ministry = organisation_ministry;
        this.organisationdepartmnt = organisationdepartmnt;
        this.case_id = case_id;
        this.user_id = user_id;
        this.notification_msg = notification_msg;
    }

    public String getOrganisation_name() {
        return organisation_name;
    }

    public void setOrganisation_name(String organisation_name) {
        this.organisation_name = organisation_name;
    }

    public String getOrganisation_ministry() {
        return organisation_ministry;
    }

    public void setOrganisation_ministry(String organisation_ministry) {
        this.organisation_ministry = organisation_ministry;
    }

    public String getOrganisationdepartmnt() {
        return organisationdepartmnt;
    }

    public void setOrganisationdepartmnt(String organisationdepartmnt) {
        this.organisationdepartmnt = organisationdepartmnt;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNotification_msg() {
        return notification_msg;
    }

    public void setNotification_msg(String notification_msg) {
        this.notification_msg = notification_msg;

    }
}
