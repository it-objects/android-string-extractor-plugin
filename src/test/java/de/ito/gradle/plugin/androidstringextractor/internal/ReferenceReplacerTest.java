package de.ito.gradle.plugin.androidstringextractor.internal;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ReferenceReplacerTest {
  private ReferenceReplacer referenceReplacer;

  @Before public void setUp() throws Exception {
    this.referenceReplacer = new ReferenceReplacer();
  }

  @Test public void when_replaceHardCodedValues_should_replaceHardcodedValues() throws Exception {
    StringOccurrence string = new StringOccurrence("id", "text", "value");
    List<StringOccurrence> strings = new ArrayList<>();
    strings.add(string);

    Document document = Util.createEmptyDocument();
    Element textView = document.createElement("TextView");
    textView.setAttribute("xmlns:android", "http://schemas.android.com/apk/res/android");
    textView.setAttribute("android:text", "hardcoded text");
    textView.setAttribute("android:id", "@+id/id");
    document.appendChild(textView);

    referenceReplacer.replaceHardCodedValues(document, strings);

    assertThat(textView.getAttribute("android:text"), equalTo(string.getValue()));
  }
}