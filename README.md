# android-string-extractor-plugin [![Build Status](https://travis-ci.org/it-objects/android-string-extractor-plugin.svg?branch=travis-coveralls)](https://travis-ci.org/it-objects/android-string-extractor-plugin) [![Coverage Status](https://coveralls.io/repos/github/it-objects/android-string-extractor-plugin/badge.svg?branch=travis-coveralls)](https://coveralls.io/github/it-objects/android-string-extractor-plugin?branch=travis-coveralls) [![Download](https://api.bintray.com/packages/it-objects/maven/de.ito.gradle.plugin%3Aandroid-string-extractor/images/download.svg) ](https://bintray.com/it-objects/maven/de.ito.gradle.plugin%3Aandroid-string-extractor/_latestVersion)
Gradle plugin which automatically extracts hardcoded strings from Android layouts.

The plugin scans all your flavors and layouts. It automatically extracts detected hardcoded values. Occurrences are replaced with generated references:

```xml
<!-- Before, Layout: example.xml -->
<TextView
  android:id="@+id/textView"
  android:text="Hardcoded value"
  />

<!-- After -->
<TextView
  android:id="@+id/textView"
  android:text="@string/example_textView_text"
  />
```

## Usage
```groovy
buildscript {
  repositories {
    jcenter()
  }
  dependencies {
    ...
    classpath 'de.ito.gradle.plugin:android-string-extractor:<version>'
  }
}

apply plugin: 'android-string-extractor'
```

```shell
$ ./gradlew extractStringsFromLayouts
```

## Contributing
Contributing to this project is appreciated.
Please check the [contribution guidelines](/CONTRIBUTING.md) for more information.
