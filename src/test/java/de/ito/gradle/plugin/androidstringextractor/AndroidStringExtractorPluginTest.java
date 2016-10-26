package de.ito.gradle.plugin.androidstringextractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static de.ito.gradle.plugin.androidstringextractor.AndroidStringExtractorPlugin.TASK_NAME;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AndroidStringExtractorPluginTest {
  @Rule public final TemporaryFolder testProjectDir = new TemporaryFolder();

  @Test public void test() throws Exception {
    setUpTestProject();

    BuildResult result = GradleRunner.create()
        .withProjectDir(testProjectDir.getRoot())
        .withPluginClasspath()
        .withArguments(TASK_NAME, "--stacktrace")
        .build();

    assertThat(result.task(":" + TASK_NAME).getOutcome(), equalTo(SUCCESS));
  }

  private void setUpTestProject() throws IOException {
    createBuildFile();
    createFlavors();
  }

  private void createBuildFile() throws IOException {
    File buildFile = testProjectDir.newFile("build.gradle");
    writeFile(buildFile, "plugins { id 'android-string-extractor' }");
  }

  private void createFlavors() throws IOException {
    File flavorPath = new File(testProjectDir.getRoot(), "/src/flavor");
    flavorPath.mkdirs();

    createFlavor(flavorPath);

    File testPath = new File(testProjectDir.getRoot(), "/src/test");
    testPath.mkdirs();
  }

  private void createFlavor(File flavorPath) throws IOException {
    createStringValues(flavorPath);
    createLayouts(flavorPath);
  }

  private void createStringValues(File flavorPath) throws IOException {
    File valuesPath = new File(flavorPath, "/res/values/");
    valuesPath.mkdirs();

    File stringValuesFile = new File(valuesPath, "string_layouts.xml");
    stringValuesFile.createNewFile();

    writeFile(stringValuesFile,
        "<resources>" + "<string name=\"id\">value</string>" + "</resources>");
  }

  private void createLayouts(File flavorPath) throws IOException {
    File layoutPath = new File(flavorPath, "/res/layout/");
    layoutPath.mkdirs();

    createLayout(layoutPath);
  }

  private void createLayout(File layoutPath) throws IOException {
    File layoutFile = new File(layoutPath, "layout.xml");

    String linearLayout =
        "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\">%s\n%s</LinearLayout>";
    String referencedTextView =
        "<TextView android:id=\"@+id/textView1\" android:text=\"@string/layout_textView1_text\" />";
    String hardcodedTextView =
        "<TextView android:id=\"@+id/textView2\" android:text=\"hardcoded text\" />";

    writeFile(layoutFile, String.format(linearLayout, referencedTextView, hardcodedTextView));
  }

  private void writeFile(File destination, String content) throws IOException {
    BufferedWriter output = null;
    try {
      output = new BufferedWriter(new FileWriter(destination));
      output.write(content);
    } finally {
      if (output != null) {
        output.close();
      }
    }
  }
}
