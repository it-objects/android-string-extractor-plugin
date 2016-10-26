package de.ito.gradle.plugin.androidstringextractor;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AndroidStringExtractorPlugin implements Plugin<Project> {

  static final String TASK_NAME = "extractStringsFromLayouts";

  @Override
  public void apply(Project target) {
    target.getTasks().create(TASK_NAME, AndroidStringExtractorTask.class);
  }
}
