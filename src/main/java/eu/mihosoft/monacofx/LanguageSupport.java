package eu.mihosoft.monacofx;

public interface LanguageSupport {

    String getName();

    default FoldingProvider getFoldingProvider(){return null;};

    default MonarchSyntaxHighlighter getMonarchSyntaxHighlighter(){return null;};

}
