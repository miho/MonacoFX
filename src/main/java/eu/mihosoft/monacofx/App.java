/*
 * MIT License
 *
 * Copyright (c) 2020 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eu.mihosoft.monacofx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // create new monaco editor node
        MonacoFX monacoFX1 = new MonacoFX();
        MonacoFX monacoFX2 = new MonacoFX();
        StackPane root = new StackPane(new HBox(monacoFX1, monacoFX2));

        // set initial text
        monacoFX1.getEditor().getDocument().setText("[error: test]\n[info: test]\n[custom data]\n");
        monacoFX2.getEditor().getDocument().setText(
                "#include <stdio.h>\n" +
                "int main() {\n" +
                "   // printf() displays the string inside quotation\n" +
                "   printf(\"Hello, World!\");\n" +
                "   return 0;\n" +
                "}");

        // custom language support
        LanguageSupport myLang = new LanguageSupport() {
            @Override
            public String getName() {
                return "mylang";
            }

            @Override
            public FoldingProvider getFoldingProvider() {
                return editor1 ->
                        new Folding[]{
                            new Folding(5, 7),
                            new Folding(13, 17),
                            new Folding(19, 21)
                        };
            }

            @Override
            public MonarchSyntaxHighlighter getMonarchSyntaxHighlighter() {
                return () ->
                " 	tokenizer: {\n"+
                " 		root: [\n"+
                "           [/\\[error.*/, \"custom-error\"],\n"+
                " 			[/\\[notice.*/, \"custom-notice\"],\n"+
                " 			[/\\[info.*/, \"custom-info\"],\n"+
                " 			[/\\[[a-zA-Z 0-9:]+\\]/, \"custom-date\"],\n"+
                " 		],\n"+
                " 	}\n";
            }
        };

        // register custom language
        monacoFX1.getEditor().registerLanguage(myLang);

        // define a theme for the language
        EditorTheme theme = new EditorTheme("mylangTheme","vs",false,
            new Rule("custom-info","808080"),
            new Rule("custom-error", "ff0000",  null, null, null, "bold"),
            new Rule("custom-date", "20ff00")
        );

        // register the theme
        monacoFX1.getEditor().registerTheme(theme);

        // tell monaco to use our custom language and theme
        monacoFX1.getEditor().setCurrentLanguage("mylang");
        monacoFX1.getEditor().setCurrentTheme("mylangTheme");

        // or use a predefined language like 'c'
        monacoFX2.getEditor().setCurrentLanguage("c");
        monacoFX2.getEditor().setCurrentTheme("vs-dark");

        // the usual scene & stage setup
        Scene scene = new Scene(root, 800,600);
        primaryStage.setTitle("MonacoFX Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
