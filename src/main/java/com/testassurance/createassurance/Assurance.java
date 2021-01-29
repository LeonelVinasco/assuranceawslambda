/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testassurance.createassurance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 *
 * @author leo
 */
public class Assurance implements AssuranceInterface {
    private String assuranceName;
    private String id;
    private Double value;

    public Assurance(String json) {
        Gson gson = new Gson();
        Assurance request = gson.fromJson(json, Assurance.class);
        this.assuranceName = request.getAssuranceName();
        this.id = request.getId();
        this.value = request.getValue();
    }
    @Override
    public String getAssuranceName() {
        return assuranceName;
    }
    @Override
    public void setAssuranceName(String assuranceName) {
        this.assuranceName = assuranceName;
    }
    @Override
    public String getId() {
        return id;
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public Double getValue() {
        return value;
    }
    @Override
    public void setValue(Double value) {
        this.value = value;
    }
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
