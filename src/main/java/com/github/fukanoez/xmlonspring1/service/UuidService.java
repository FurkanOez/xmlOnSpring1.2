package com.github.fukanoez.xmlonspring1.service;

import com.github.fukanoez.xmlonspring1.model.Attribute;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.*;
import java.util.*;


@Service
public class UuidService {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:duckdb:/path/to/your/file");
    }

    public HashSet<Attribute> viewData(File folder) {
        HashSet<Attribute> allAttributes = new HashSet<>();

        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".xml")) {
                    System.out.println("Found XML file: " + file.getName());
                    HashSet<Attribute> attributes = processXMLFile(file);
                    allAttributes.addAll(attributes);
                }
            }
        }
        saveDataToDb(allAttributes);
        return allAttributes;
    }

    private HashSet<Attribute> processXMLFile(File file) {
        HashSet<Attribute> attributes = new HashSet<>();
        try {
            //File xmlFile = new File("/path/to/your/file");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            document.getDocumentElement().normalize();
            Element docEl = document.getDocumentElement();

            StringBuilder sb = new StringBuilder();
            List<Attribute> list = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            HashSet<Attribute> set = new HashSet<>();

            printElements(docEl, sb, list, map, set);
            attributes = set;

            saveDataToDb(attributes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attributes;
    }


    private static void printElements(Node node, StringBuilder sb1, List<Attribute> list, Map<String, String> map1, HashSet<Attribute> set1) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {

            NamedNodeMap attributes = node.getAttributes();
            if (attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node attr = attributes.item(i);
                    sb1.append("    Attribute: ").append(attr.getNodeName()).append(" = ").append(attr.getNodeValue());

                    Attribute attribute1 = new Attribute();
                    attribute1.setAttrName(attr.getNodeName());
                    attribute1.setAttrValue(attr.getNodeValue());
                    attribute1.setAttrUUID(UUID.randomUUID().toString());
                    attribute1.setAttrId("1");
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
                attribute1.setAttrUUID(UUID.randomUUID().toString());
                attribute1.setAttrId("1");
                list.add(attribute1);
                map1.put(node.getNodeName(), content);
                set1.add(attribute1);

            }
        }
    }

    public String saveDataToDb(Set<Attribute> attributes) {
        String result = "";


        Optional<Attribute> correlationsIDOpt = attributes.stream()
                .filter(s -> s.getAttrName().equals("CorrelationsID"))
                .findFirst();

        if (!correlationsIDOpt.isPresent()) {
            return "Error: CorrelationsID not found.";
        }

        Attribute correlationsID = correlationsIDOpt.get();



        try (Connection conn = getConnection();) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS attrs (" +
                    "attrUUID STRING," +
                    "attrName STRING, " +
                    "attrValue STRING, " +
                    "attrId STRING)";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTableSQL);
            }

            String truncate = "TRUNCATE TABLE attrs";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(truncate);
            }

            String insertSQL = "INSERT INTO attrs(attrUUID, attrName, attrValue, attrId) VALUES (?,?,?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

                for (Attribute attributes1 : attributes) {
                    pstmt.setString(1, attributes1.getAttrUUID());
                    pstmt.setString(2, attributes1.getAttrName());
                    pstmt.setString(3, attributes1.getAttrValue());
                    pstmt.setString(4, correlationsID.getAttrValue());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                result = "Data Inserted Successfully";
            }
        } catch (SQLException e) {
            result = "Error " + e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    public List<Attribute> searchByKey(String key) {
        List<Attribute> result = new ArrayList<>();
        String query = "SELECT * FROM attrs WHERE attrName LIKE ?";
                // AND attrValue = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%"  + key+ "%");
            //pstmt.setString(2,value);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Attribute attribute = new Attribute();
                    attribute.setAttrUUID(rs.getString("attrUUID"));
                    attribute.setAttrName(rs.getString("attrName"));
                    attribute.setAttrValue(rs.getString("attrValue"));
                    attribute.setAttrId(rs.getString("attrId"));
                    result.add(attribute);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Attribute> searchByKeyAndValue(String key, String value) {
        List<Attribute> result = new ArrayList<>();
        String query = "SELECT * FROM attrs WHERE attrName LIKE ? AND attrValue LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%"  + key + "%");
            pstmt.setString(2, "%" + value + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Attribute attribute = new Attribute();
                    attribute.setAttrUUID(rs.getString("attrUUID"));
                    attribute.setAttrName(rs.getString("attrName"));
                    attribute.setAttrValue(rs.getString("attrValue"));
                    attribute.setAttrId(rs.getString("attrId"));
                    result.add(attribute);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
