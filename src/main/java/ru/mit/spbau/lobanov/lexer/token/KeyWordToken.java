package ru.mit.spbau.lobanov.lexer.token;

public class KeyWordToken extends Token {
    public enum KeyWordType {
        IF, THEN, ELSE, WHILE, DO, READ, WRITE, BEGIN, END
    }

    private final KeyWordType type;

    private KeyWordToken(int lineIndex, int start, int end, String notation) {
        super(lineIndex, start, end);
        this.type = KeyWordType.valueOf(notation.toUpperCase());
    }

    public KeyWordType getType() {
        return type;
    }

    public static KeyWordToken create(int lineIndex, int start, int end, String notation) {
        return new KeyWordToken(lineIndex, start, end, notation);
    }

    @Override
    public String toString() {
        return String.format("KW_%s(line:%d, begin:%d, end:%d)", type.name(), getLineIndex(), getStart(), getEnd());
    }
}
