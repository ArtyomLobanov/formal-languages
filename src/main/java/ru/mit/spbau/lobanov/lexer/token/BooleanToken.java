package ru.mit.spbau.lobanov.lexer.token;

public class BooleanToken extends Token {

    private final boolean value;

    private BooleanToken(int lineIndex, int start, int end, String notation) {
        super(lineIndex, start, end);
        this.value = Boolean.valueOf(notation);
    }

    public boolean getValue() {
        return value;
    }

    public static BooleanToken create(int lineIndex, int start, int end, String notation) {
        return new BooleanToken(lineIndex, start, end, notation);
    }

    @Override
    public String toString() {
        return String.format("BOOL(value:%s, line:%d, begin:%d, end:%d)",
                Boolean.toString(value), getLineIndex(), getStart(), getEnd());
    }
}
