package com.github.fukanoez.xmlonspring1.service;

import com.github.fukanoez.xmlonspring1.model.Attribute;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.*;
import java.util.*;

@Service
public class LoadDataService {

    private final DefaultErrorAttributes errorAttributes;

    public LoadDataService(DefaultErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    public HashSet<Attribute> returnData() {
        StringBuilder sb1 = new StringBuilder();
        Attribute attribute = new Attribute();
        List<Attribute> list = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        HashSet<Attribute> set1 = new HashSet<>();


        //arraylist attr tutan yeri

        try {
            File xmlFile = new File("/path/to/your/file");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);

            document.getDocumentElement().normalize();
            Element docEl = document.getDocumentElement();
            Node childNode = docEl.getFirstChild();
            printElements(docEl, sb1, list, map1, set1);


            return set1;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }





        private static void printElements (Node node, StringBuilder
        sb1, List < Attribute > list, Map < String, String > map1, HashSet < Attribute > set1){
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                NamedNodeMap attributes = node.getAttributes();
                if (attributes != null) {
                    for (int i = 0; i < attributes.getLength(); i++) {
                        Node attr = attributes.item(i);
                        sb1.append("    Attribute: ").append(attr.getNodeName()).append(" = ").append(attr.getNodeValue());

                        Attribute attribute1 = new Attribute();
                        attribute1.setAttrName(attr.getNodeName());
                        attribute1.setAttrValue(attr.getNodeValue());
                        list.add(attribute1);
                        map1.put(attr.getNodeName(), attr.getNodeValue());
                        set1.add(attribute1);

                    }
                }

                NodeList children = node.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    printElements(child, sb1, list, map1, set1);
                }

                String content = node.getTextContent().trim();
                if (!content.isEmpty() && children.getLength() == 1 && children.item(0).getNodeType() == Node.TEXT_NODE) {
                    sb1.append("    Attribute: ").append(node.getNodeName()).append(" = ").append(content);
                    Attribute attribute1 = new Attribute();
                    attribute1.setAttrName(node.getNodeName());
                    attribute1.setAttrValue(content);
                    list.add(attribute1);
                    map1.put(node.getNodeName(), content);
                    set1.add(attribute1);

                }
            }
        }
    }








// /loadData  Controller olustur ve bu datalari
//string builder nasil calisir bak
//print yerine string builder yap




//attr diye yeni bir class = ok
//attr icinde  name ve value adinda iki field = ok
//bir tane attr classini tutan bir array list = ok
//attr class icin getter setter = ok
//StringBuilder appenleri bir obje haline getir. (örnek: objeadi = sb1.append("    Attribute: ").append(attr.getNodeName()).append(" = ").append(attr.getNodeValue());)
//attr classina toString yaz = ok


//duckdb tablo olustur
//4 tane string field = ok
//1. field UUID olacak bunu generate et(java ile) = ok
//2.field attrName = ok
//3.field attrValue = ok
//4.field attrID = ok
//for döngüsüyle listenin icinde kosup, attrleri dbye ekle.
