package ru.mit.spbau.lobanov.lexer.token;

public class CommentToken extends Token {

    private final String text;

    public CommentToken(int lineIndex, int start, int end, String text) {
        super(lineIndex, start, end);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static CommentToken create(int lineIndex, int start, int end, String notation) {
        return new CommentToken(lineIndex, start, end, notation);
    }

    @Override
    public String toString() {
        return String.format("COMMENT(text:%s, line:%d, begin:%d, end:%d)", text, getLineIndex(), getStart(), getEnd());
    }
}
