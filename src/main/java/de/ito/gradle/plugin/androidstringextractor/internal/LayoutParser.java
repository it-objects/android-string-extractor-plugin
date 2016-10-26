package de.ito.gradle.plugin.androidstringextractor.internal;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class LayoutParser {
  private boolean ignoreMissingId = true;

  void setIgnoreMissingId(boolean ignoreMissingId) {
    this.ignoreMissingId = ignoreMissingId;
  }

  List<StringOccurrence> parse(Document document) {
    List<StringOccurrence> stringOccurrences = new ArrayList<>();
    processNodes(stringOccurrences, document.getChildNodes());

    return stringOccurrences;
  }

  private void processNodes(List<StringOccurrence> stringOccurrences, NodeList nodes)
      throws IllegalStateException {
    for (int i = 0; i < nodes.getLength(); i++) {
      processNode(stringOccurrences, nodes.item(i));
    }
  }

  private void processNode(List<StringOccurrence> stringOccurrences, Node node) {
    if (node.hasChildNodes()) processNodes(stringOccurrences, node.getChildNodes());

    if (!node.hasAttributes()) return;

    NamedNodeMap attributes = node.getAttributes();
    Node idAttribute = attributes.getNamedItem("android:id");
    Node textAttribute = attributes.getNamedItem("android:text");
    Node hintAttribute = attributes.getNamedItem("android:hint");

    if (!ignoreMissingId) validateNode(idAttribute, textAttribute, hintAttribute);
    
    if (idAttribute == null || (textAttribute == null && hintAttribute == null)) return;
    
    String id = stripIdPrefix(idAttribute.getNodeValue());
    if (textAttribute != null) {
      if (!isDataBinding(textAttribute.getNodeValue())) {
        stringOccurrences.add(
            new StringOccurrence(id, "text", textAttribute.getNodeValue()));
      }
    }
    if (hintAttribute != null) {
      if (!isDataBinding(hintAttribute.getNodeValue())) {
        stringOccurrences.add(
            new StringOccurrence(id, "hint", hintAttribute.getNodeValue()));
      }
    }
  }

  private void validateNode(Node idAttribute, Node textAttribute, Node hintAttribute) {
    if (idAttribute == null || idAttribute.getNodeValue() == null) {
      if (textAttribute != null) {
        throw new IllegalStateException(
            String.format("No id specified for %s", textAttribute.getNodeValue()));
      } else {
        throw new IllegalStateException(
            String.format("No id specified for %s", hintAttribute.getNodeValue()));
      }
    }
  }

  private String stripIdPrefix(String id) {
    if (id.startsWith("@+id/")) return id.substring(5);
    
    return id;
  }

  private boolean isDataBinding(String value) {
    boolean oneWayDataBinding = value.startsWith("@{");
    boolean twoWayDataBinding = value.startsWith("@={");
    
    return oneWayDataBinding || twoWayDataBinding;
  }
}
