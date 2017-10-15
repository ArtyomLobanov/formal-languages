package ru.mit.spbau.lobanov.lexer.token;

import java.math.BigInteger;

public class IntegerToken extends Token {

    private final BigInteger value;

    private IntegerToken(int lineIndex, int start, int end, String notation) {
        super(lineIndex, start, end);
        this.value = new BigInteger(notation);
    }

    public BigInteger getValue() {
        return value;
    }

    public static IntegerToken create(int lineIndex, int start, int end, String notation) {
        return new IntegerToken(lineIndex, start, end, notation);
    }

    @Override
    public String toString() {
        return String.format("INT(value:%s, line:%d, begin:%d, end:%d)",
                value.toString(), getLineIndex(), getStart(), getEnd());
    }
}
