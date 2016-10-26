package de.ito.gradle.plugin.androidstringextractor.internal;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

class StringValues {
  private final Map<String, String> values;
  private boolean hasChanged;

  StringValues() {
    values = new LinkedHashMap<>();
  }
  
  StringValues(Map<String, String> values){
    this.values = values;
  }

  Map<String, String> getValues() {
    return values;
  }

  void put(String key, String value) {
    values.put(key, value);
    hasChanged = true;
  }

  public boolean hasChanged() {
    return hasChanged;
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
