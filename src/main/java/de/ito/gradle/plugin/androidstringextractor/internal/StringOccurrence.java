package de.ito.gradle.plugin.androidstringextractor.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

class StringOccurrence {
  private final String id;
  private final String attribute;
  private String value;

  StringOccurrence(String id, String attribute, String value) {
    this.id = id;
    this.attribute = attribute;
    this.value = value;
  }

  boolean hasHardCodedValue() {
    boolean isReference = value.startsWith("@string/");
    boolean isDataBinding = value.startsWith("@{");

    return !(isReference || isDataBinding);
  }

  String getId() {
    return id;
  }

  String getAttribute() {
    return attribute;
  }

  String getValue() {
    return value;
  }

  void replaceHardCodedValueByReference(String key) {
    value = "@string/" + key;
  }

  @Override public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o, false);
  }

  @Override public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

  @Override public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
