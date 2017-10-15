package ru.mit.spbau.lobanov.lexer.token;

public abstract class Token {
    private final int lineIndex;
    private final int start;
    private final int end;


    protected Token(int lineIndex, int start, int end) {
        this.lineIndex = lineIndex;
        this.start = start;
        this.end = end;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
