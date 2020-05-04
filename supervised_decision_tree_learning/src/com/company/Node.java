package com.company;

import com.company.enums.Attribute;

import java.util.ArrayList;

public class Node {
  private Node parent;
  private boolean isLeaf;
  private Attribute attribute;
  private String attributeDetail;
  private boolean value; // leaf
  private ArrayList<Node> children;

  Node(boolean isLeaf) {
    if (isLeaf == false) {
      this.children = new ArrayList<>();
    }
    this.isLeaf = isLeaf;
  }

  public void setAttributeDetail(String detail) {
    this.attributeDetail = detail;
  }

  @Override
  public String toString() {
    if (this.isLeaf) {
      final String _val = (value) ? "Yes" : "No";
      String nodeName = "Leaf";
      return nodeName + "{" + "attribute=" + attributeDetail + ", WillWait=" + _val + "}";
    } else {
      String nodeName = "Node";
      if (this.parent == null) {
        nodeName = "\nRoot";
      }
      return nodeName + "(" + "{" + "attribute=" + attribute + ",\n {children=" + children + "}"+")";
    }
  }

  public void addChild(Node child) {
    child.parent = this;
    this.children.add(child);
  }

  public void setAttribute(Attribute attribute) {
    this.attribute = attribute;
  }

  public Attribute getAttribute() {
    return attribute;
  }

  public ArrayList<Node> getChildren() {
    return children;
  }

  public boolean isLeaf() {
    return isLeaf;
  }

  public void setValue(boolean value) {
    this.value = value;
  }

  public boolean getValue() {
    return this.value;
  }
}
