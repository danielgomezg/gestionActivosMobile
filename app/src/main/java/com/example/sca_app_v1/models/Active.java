package com.example.sca_app_v1.models;

public class Active {

    private Integer id;
    private String bar_code;
    private String comment;
    private String acquisition_date;
    private String accounting_document;
    private String accounting_record_number;
    private String name_in_charge_active;
    private String rut_in_charge_active;
    private String serie;
    private String model;
    private String state;
    private String creation_date;
    private Integer removed;
    private Integer office_id;
    private Integer article_id;

    public Active(Integer id, String bar_code, String comment, String acquisition_date, String accounting_document, String accounting_record_number, String name_in_charge_active, String rut_in_charge_active, String serie, String model, String state, String creation_date, Integer removed, Integer office_id, Integer article_id) {
        this.id = id;
        this.bar_code = bar_code;
        this.comment = comment;
        this.acquisition_date = acquisition_date;
        this.accounting_document = accounting_document;
        this.accounting_record_number = accounting_record_number;
        this.name_in_charge_active = name_in_charge_active;
        this.rut_in_charge_active = rut_in_charge_active;
        this.serie = serie;
        this.model = model;
        this.state = state;
        this.creation_date = creation_date;
        this.removed = removed;
        this.office_id = office_id;
        this.article_id = article_id;
    }

    public Active(JSONObject active) throws JSONException {
        this.id = (int) active.getInt("id");
        this.bar_code = active.getString("bar_code");
        this.comment = active.getString("comment");
        this.acquisition_date = active.getString("acquisition_date");
        this.accounting_document = active.getString("accounting_document");
        this.accounting_record_number = active.getString("accounting_record_number");
        this.name_in_charge_active = active.getString("name_in_charge_active");
        this.rut_in_charge_active = active.getString("rut_in_charge_active");
        this.serie = active.getString("serie");
        this.model = active.getString("model");
        this.state = active.getString("state");
        this.creation_date = active.getString("creation_date");
        this.removed = (int) active.getInt("removed");
        this.office_id = active.getInt("office_id");
        this.article_id = (int) active.getInt("article_id");
    }

    public Integer getId() {
        return id;
    }

    public String getBar_code() {
        return bar_code;
    }

    public String getComment() {
        return comment;
    }

    public String getAcquisition_date() {
        return acquisition_date;
    }

    public String getAccounting_document() {
        return accounting_document;
    }

    public String getAccounting_record_number() {
        return accounting_record_number;
    }

    public String getName_in_charge_active() {
        return name_in_charge_active;
    }

    public String getRut_in_charge_active() {
        return rut_in_charge_active;
    }

    public String getSerie() {
        return serie;
    }

    public String getModel() {
        return model;
    }

    public String getState() {
        return state;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public Integer getRemoved() {
        return removed;
    }

    public Integer getOffice_id() {
        return office_id;
    }

    public Integer getArticle_id() {
        return article_id;
    }

}
