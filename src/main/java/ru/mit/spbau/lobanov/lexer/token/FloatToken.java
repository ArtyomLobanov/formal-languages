package ru.mit.spbau.lobanov.lexer.token;

import java.math.BigInteger;

public class FloatToken extends Token {

    private final double value;

    private FloatToken(int lineIndex, int start, int end, String notation) {
        super(lineIndex, start, end);
        this.value = Double.valueOf(notation);
    }

    public double getValue() {
        return value;
    }

    public static FloatToken create(int lineIndex, int start, int end, String notation) {
        return new FloatToken(lineIndex, start, end, notation);
    }

    @Override
    public String toString() {
        return String.format("FLOAT(value:%s, line:%d, begin:%d, end:%d)",
                Double.toString(value), getLineIndex(), getStart(), getEnd());
    }
}
