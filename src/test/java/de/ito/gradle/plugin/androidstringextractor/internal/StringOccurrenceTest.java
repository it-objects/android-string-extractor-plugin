package de.ito.gradle.plugin.androidstringextractor.internal;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StringOccurrenceTest {
  @Test public void when_hasHardCodedValue_should_returnTrue() throws Exception {
    StringOccurrence occurrence = new StringOccurrence("id", "text", "value");

    boolean actual = occurrence.hasHardCodedValue();

    assertThat(actual, is(true));
  }

  @Test public void when_hasReference_should_returnFalse() throws Exception {
    StringOccurrence occurrence = new StringOccurrence("id2", "hint", "@string/value2");

    boolean actual = occurrence.hasHardCodedValue();

    assertThat(actual, is(false));
  }

  @Test public void when_replaceHardCodedValueByReference_should_containReference()
      throws Exception {
    StringOccurrence occurrence = new StringOccurrence("id", "text", "value");

    occurrence.replaceHardCodedValueByReference("reference");

    assertThat(occurrence.getValue(), equalTo("@string/reference"));
  }
}