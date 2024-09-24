package com.github.fukanoez.xmlonspring1.controller;


import com.github.fukanoez.xmlonspring1.model.Attribute;
import com.github.fukanoez.xmlonspring1.service.UuidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@RestController
public class SearchController {
    private final UuidService uuidService;

    @Autowired
    public SearchController(UuidService uuidService) {
        this.uuidService = uuidService;
    }

//    @GetMapping("/search")
//    public String search(@RequestParam String key, @RequestParam String value) {
//
//        return "Searched for key: " + key + " and value: " + value;
//    }

    @GetMapping("/viewData")
    public HashSet<Attribute> viewData(@RequestParam(value = "folderPath", defaultValue = "/path/to/your/file")String folderPath) {
        File folder = new File(folderPath);

        System.out.println("Folder exists: " + folder.exists());
        System.out.println("Is directory: " + folder.isDirectory());

        if(!folder.exists() || folder.isDirectory()) {
            throw new IllegalArgumentException("Folder does not exist or is not a folder" + folderPath);
        }
        return uuidService.viewData(folder);
    }

    @GetMapping("/search")
    public List<Attribute> search(@RequestParam(required = false) String key, @RequestParam(required = false) String value) {
        if(value != null && key != null){
            return uuidService.searchByKeyAndValue(key,value);
        }else if(key != null){
            return uuidService.searchByKey(key);
        }else{
            return Collections.emptyList();
        }
    }
}
