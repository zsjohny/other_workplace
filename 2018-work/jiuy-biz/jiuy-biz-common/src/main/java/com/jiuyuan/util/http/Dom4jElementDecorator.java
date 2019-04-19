package com.jiuyuan.util.http;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;


public class Dom4jElementDecorator {

    private Element element;

    public Dom4jElementDecorator(Element element) {
        this.element = element;
    }

    public String getStringValue() {
        return this.element != null ? this.element.getStringValue() : null;
    }

    public int getIntValue() {
        return getIntValue(0);
    }

    public int getIntValue(int defaultValue) {
        return getIntValue(defaultValue, false);
    }

    public int getIntValue(int defaultValue, boolean tryHard) {
        return NumberUtil.parseInt(getStringValue(), defaultValue, tryHard);
    }

    public long getLongValue() {
        return getLongValue(0);
    }

    public long getLongValue(long defaultValue) {
        return getLongValue(defaultValue, false);
    }

    public long getLongValue(long defaultValue, boolean tryHard) {
        return NumberUtil.parseLong(getStringValue(), defaultValue, tryHard);
    }

    public float getFloatValue() {
        return getFloatValue(0);
    }

    public float getFloatValue(float defaultValue) {
        return getFloatValue(defaultValue, false);
    }

    public float getFloatValue(float defaultValue, boolean tryHard) {
        return NumberUtil.parseFloat(getStringValue(), defaultValue, tryHard);
    }

    public String attributeValue(String attributeName) {
        return this.element != null ? this.element.attributeValue(attributeName) : null;
    }

    public int attributeIntValue(String attributeName) {
        return attributeIntValue(attributeName, 0);
    }

    public int attributeIntValue(String attributeName, int defaultValue) {
        return attributeIntValue(attributeName, defaultValue, false);
    }

    public int attributeIntValue(String attributeName, int defaultValue, boolean tryHard) {
        return NumberUtil.parseInt(attributeValue(attributeName), defaultValue, tryHard);
    }

    public long attributeLongValue(String attributeName) {
        return attributeLongValue(attributeName, 0);
    }

    public long attributeLongValue(String attributeName, long defaultValue) {
        return attributeLongValue(attributeName, defaultValue, false);
    }

    public long attributeLongValue(String attributeName, long defaultValue, boolean tryHard) {
        return NumberUtil.parseLong(attributeValue(attributeName), defaultValue, tryHard);
    }

    public float attributeFloatValue(String attributeName) {
        return attributeFloatValue(attributeName, 0);
    }

    public float attributeFloatValue(String attributeName, float defaultValue) {
        return attributeFloatValue(attributeName, defaultValue, false);
    }

    public float attributeFloatValue(String attributeName, float defaultValue, boolean tryHard) {
        return NumberUtil.parseFloat(attributeValue(attributeName), defaultValue, tryHard);
    }

    public Dom4jElementDecorator getChild(String childName) {
        return getChild(childName, false);
    }

    public Dom4jElementDecorator getChild(String childName, boolean forceCreate) {
        Element element = this.element.element(childName);
        if (element != null || forceCreate) {
            return new Dom4jElementDecorator(element);
        } else {
            return null;
        }
    }

    public List<Dom4jElementDecorator> getChildren(String childName) {
        List<Dom4jElementDecorator> decorators = new ArrayList<Dom4jElementDecorator>();
        List<Element> children = elements(childName);
        for (Element child : children) {
            decorators.add(new Dom4jElementDecorator(child));
        }
        return decorators;
    }

    public List<Element> elements(String name) {
        @SuppressWarnings("unchecked")
        List<Element> elements = element.elements(name);
        return elements;
    }

    public Element element(String name) {
        return element.element(name);
    }
}
