package com.github.fukanoez.xmlonspring1.controller;

import com.github.fukanoez.xmlonspring1.model.Attribute;
import com.github.fukanoez.xmlonspring1.service.LoadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RestController

public class LoadDataContoller {

    @Autowired
    private final LoadDataService getloadDataService1;

    public LoadDataContoller(LoadDataService getloadDataService1) {
        this.getloadDataService1 = getloadDataService1;
    }

    @GetMapping("/Data")
    public HashSet<Attribute> getData(){
        return this.getloadDataService1.returnData();
    }
}

