package ru.mit.spbau.lobanov.lexer.token;

import java.util.Arrays;

public class OperatorToken extends Token {
    public enum OperatorTokenType {
        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVIDE("/"),
        MOD("%"),
        SET(":="),
        EQUAL("=="),
        NOT_EQUAL("!="),
        GREATER(">"),
        GREATER_OR_EQUAL(">="),
        LOWER("<"),
        LOWER_OR_EQUAL("<="),
        AND("&&"),
        OR("||"),
        LEFT_BRACKET("("),
        RIGHT_BRACKET(")"),
        SEMICOLON(";");

        public final String notation;

        OperatorTokenType(String notation) {
            this.notation = notation;
        }

        @Override
        public String toString() {
            return notation;
        }
    }

    private final OperatorTokenType type;

    private OperatorToken(int lineIndex, int start, int end, String notation) {
        super(lineIndex, start, end);
        this.type = Arrays.stream(OperatorTokenType.values())
                .filter(type -> type.notation.equals(notation))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Unexpected operator: " + notation));
    }

    public OperatorTokenType getType() {
        return type;
    }

    public static OperatorToken create(int lineIndex, int start, int end, String notation) {
        return new OperatorToken(lineIndex, start, end, notation);
    }

    @Override
    public String toString() {
        return String.format("OP_%s(line:%d, begin:%d, end:%d)", type.name(), getLineIndex(), getStart(), getEnd());
    }
}