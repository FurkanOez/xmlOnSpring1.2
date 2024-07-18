package com.github.fukanoez.xmlonspring1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Attribute {


    String attrName;
    String attrValue;
    String attrUUID;
    String attrId;

    public String getAttrUUID() {return attrUUID;}
    public void setAttrUUID(String attrUUID) {this.attrUUID = attrUUID;}
    public String getAttrId() {return attrId;}
    public void setAttrId(String attrId) {this.attrId = attrId;}

    public String getAttrName() {
        return attrName;
    }
    public String getAttrValue() {
        return attrValue;
    }
    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }
    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }


    @Override
    public String toString() {
        return "Attribute{" +
                "attrId='" + attrId + '\'' +
                ", attrUUID='" + attrUUID + '\'' +
                ", attrValue='" + attrValue + '\'' +
                ", attrName='" + attrName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(attrName, attribute.attrName) &&
                Objects.equals(attrValue, attribute.attrValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attrName, attrValue);
    }
}
