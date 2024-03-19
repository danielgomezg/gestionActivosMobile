package com.example.sca_app_v1.home_app.company;

public class CompanyItem {
    private String id;
    private String name;

    public CompanyItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Esto se usará para mostrar el nombre en el AutoCompleteTextView
    @Override
    public String toString() {
        return name;
    }
}
