package de.ito.gradle.plugin.androidstringextractor;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Class containing the parameters of the plugin.
 */
class AndroidStringExtractorPluginExtension {

  String targetFolder;

  public AndroidStringExtractorPluginExtension() {
  }

  public String getTargetFolder() {
    return targetFolder;
  }

  public void setTargetFolder(String targetFolder) {
    this.targetFolder = targetFolder;
  }
}

public class AndroidStringExtractorPlugin implements Plugin<Project> {

  static final String TASK_NAME = "extractStringsFromLayouts";

  @Override
  public void apply(Project target) {
    AndroidStringExtractorPluginExtension extractionProperties =
        target.getExtensions()
            .create("stringExtractionProperties", AndroidStringExtractorPluginExtension.class);
    AndroidStringExtractorTask androidStringExtractorTask =
        target.getTasks().create(TASK_NAME, AndroidStringExtractorTask.class);
    androidStringExtractorTask.doFirst(
        task -> ((AndroidStringExtractorTask) task).setOtherProjectDir(
            extractionProperties.targetFolder));
  }
}
