package eu.mihosoft.monacofx;

public class Folding {
    public final int start;
    public final int end;
    public final FoldingKind kind;

    public Folding(int start, int end, String kind) {
        this.start = start;
        this.end = end;
        this.kind = new FoldingKind(kind);
    }

    public Folding(int start, int end) {
        this.start = start;
        this.end = end;
        this.kind = new FoldingKind(null);
    }

    public static class FoldingKind {
        public final String value;

        public FoldingKind(String value) {
            this.value = value;
        }

        public FoldingKind() {
            this.value = "default";
        }
    }

}

