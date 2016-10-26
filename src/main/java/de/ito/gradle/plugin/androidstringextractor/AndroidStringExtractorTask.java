package de.ito.gradle.plugin.androidstringextractor;

import de.ito.gradle.plugin.androidstringextractor.internal.AndroidProjectFactory;
import de.ito.gradle.plugin.androidstringextractor.internal.LayoutStringExtractor;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class AndroidStringExtractorTask extends DefaultTask {

  private final LayoutStringExtractor layoutStringExtractor;

  public AndroidStringExtractorTask() {
    layoutStringExtractor = new LayoutStringExtractor(new AndroidProjectFactory());
  }

  @TaskAction
  public void extractStringsFromLayouts() throws Exception {
    String projectPath = getProject().getProjectDir().getPath();
    layoutStringExtractor.extract(projectPath);
  }
}