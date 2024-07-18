package com.github.fukanoez.xmlonspring1.controller;

import com.github.fukanoez.xmlonspring1.model.Attribute;
import com.github.fukanoez.xmlonspring1.service.UuidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashSet;

@RestController
public class UuidController {

    private final UuidService uuidService;

    @Autowired
    public UuidController(UuidService uuidService) {
        this.uuidService = uuidService;
    }



    @GetMapping("/saveData")
    public String saveData(@RequestParam("folderPath") String folderPath){
        File folder = new File(folderPath);
        HashSet<Attribute> attributes1 = uuidService.viewData(folder);
        return "Successfully saved data";
    }
}




















//XML dosyasindaki verileri key ve value seklinde web arayüzünde bastirmak icin bir kod yazdim.
//
//
//Duck db de yeni bir database olusturdum ve database in icine key ve value adinda iki alan ekledim bu alanlar xml dosyasindaki verileri tutmak icin.
//daha önceki veriler icin UUID ve CorrelationsID tutan alan ekledim, bunlari eklemek icin yeni controller ve service sinifi olusturdum.
//DuckDB de yeni bir databse ve onun icinde tabela olusturmak icin yeni bir controller class ve servie class  olusturdum. Verileri tabelaya eklerken  bir cok sorunla karsilastim, sorunlar üzerine calistim. Bu sorunu hallettikten sonra