package de.ito.gradle.plugin.androidstringextractor.internal;

import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class LayoutParserTest {
  @Rule public TemporaryFolder folder = new TemporaryFolder();

  private LayoutParser layoutParser;

  @Before public void setUp() throws Exception {
    layoutParser = new LayoutParser();
  }

  @Test public void when_parseDocument_should_returnStringOccurrences() throws Exception {
    List<StringOccurrence> expected = createDummyStrings();
    Document document = createDummyDocument();

    List<StringOccurrence> actual = layoutParser.parse(document);

    assertThat(actual, equalTo(expected));
  }

  private List<StringOccurrence> createDummyStrings() {
    StringOccurrence string1 = new StringOccurrence("id1", "text", "text");
    StringOccurrence string2 = new StringOccurrence("id2", "hint", "hint");
    
    return Arrays.asList(string1, string2);
  }

  private Document createDummyDocument() throws ParserConfigurationException {
    Document document = Util.createEmptyDocument();

    Element linearLayout = document.createElement("LinearLayout");
    linearLayout.setAttribute("xmlns:android",
        "http://schemas.android.com/apk/res/android");
    linearLayout.setAttribute("android:orientation", "vertical");
    document.appendChild(linearLayout);
    
    Element textViewWithIdAndText = document.createElement("TextView");
    textViewWithIdAndText.setAttribute("android:text", "text");
    textViewWithIdAndText.setAttribute("android:id", "@+id/id1");
    linearLayout.appendChild(textViewWithIdAndText);

    Element textViewWithIdAndHint = document.createElement("TextView");
    textViewWithIdAndHint.setAttribute("android:hint", "hint");
    textViewWithIdAndHint.setAttribute("android:id", "@+id/id2");
    linearLayout.appendChild(textViewWithIdAndHint);

    Element textViewWithoutIdAndText = document.createElement("TextView");
    textViewWithoutIdAndText.setAttribute("android:text", "text");
    linearLayout.appendChild(textViewWithoutIdAndText);

    Element textViewWithIdButWithoutTextAndHint = document.createElement("TextView");
    textViewWithIdButWithoutTextAndHint.setAttribute("android:id", "@+id/id3");
    linearLayout.appendChild(textViewWithIdButWithoutTextAndHint);
    
    return document;
  }
}