package ru.mit.spbau.lobanov.lexer.token;

public class IdentifierToken extends Token {

    private final String name;

    private IdentifierToken(int lineIndex, int start, int end, String name) {
        super(lineIndex, start, end);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static IdentifierToken create(int lineIndex, int start, int end, String notation) {
        return new IdentifierToken(lineIndex, start, end, notation);
    }

    @Override
    public String toString() {
        return String.format("IDENTIFIER(name:%s, line:%d, begin:%d, end:%d)",
                name, getLineIndex(), getStart(), getEnd());
    }
}
