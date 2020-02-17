# MonacoFX
[ ![Download](https://api.bintray.com/packages/miho/MonacoFX/MonacoFX/images/download.svg) ](https://bintray.com/miho/MonacoFX/MonacoFX/_latestVersion)

JavaFX editor node based on the powerful Monaco editor that drives VSCode

<img src="resources/img/screenshot.png">

## Using MonacoFX

Using MonacoFX is straightforward. Just create a MonacoFX node and add it to the scene graph. Here's a an example on how to use MonacoFX with syntax highlighting and code folding for an existing language:

```java
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // create a new monaco editor node
        MonacoFX monacoFX = new MonacoFX();
        StackPane root = new StackPane(monacoFX);

        // set initial text
        monacoFX.getEditor().getDocument().setText(
                "#include <stdio.h>\n" +
                "int main() {\n" +
                "   // printf() displays the string inside quotation\n" +
                "   printf(\"Hello, World!\");\n" +
                "   return 0;\n" +
                "}");

        // use a predefined language like 'c'
        monacoFX.getEditor().setCurrentLanguage("c");
        monacoFX.getEditor().setCurrentTheme("vs-dark");

        // the usual scene & stage setup
        Scene scene = new Scene(root, 800,600);
        primaryStage.setTitle("MonacoFX Demo (running on JDK " + System.getProperty("java.version") + ")");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
```

Visit the [MonacoFX tutorial projects](https://github.com/miho/MonacoFX-Tutorials) and learn more about advanced topics, such as adding custom language support and code folding.


## How To Build The Project

### 1. Dependencies

- JDK >= 11 (tested with JDK 13)
- Internet Connection (other dependencies will be downloaded automatically)
- Optional: IDE with [Gradle](http://www.gradle.org/) support

### 2. Building

#### IDE

To build the project from an IDE do the following:

- open the  [Gradle](http://www.gradle.org/) project
- call the `assemble` Gradle task to build the project

#### Command Line

Building the project from the command line is also possible.

Navigate to the project folder and call the `assemble` [Gradle](http://www.gradle.org/)
task to build the project.

##### Bash (Linux/OS X/Cygwin/other Unix-like OS)

    cd Path/To/MonacoFX
    ./gradlew assemble
    
##### Windows (CMD)

    cd Path\To\MonacoFX
    gradlew assemble

### 3. Running the sample App

#### IDE

To run the project from an IDE do the following:

- open the  [Gradle](http://www.gradle.org/) project
- call the `run` Gradle task to run the project

#### Command Line

Running the project from the command line is also possible.

Navigate to the project folder and call the `run` [Gradle](http://www.gradle.org/)
task to run the project.

##### Bash (Linux/OS X/Cygwin/other Unix-like OS)

    cd Path/To/MonacoFX
    ./gradlew run
    
##### Windows (CMD)

    cd Path\To\MonacoFX
    gradlew run




