package ru.spbau.mit.ast

enum class ComparingOperator {
    Greater,
    Less,
    GreaterOrEqual,
    LessOrEqual,
    Equal,
    NotEqual
}

enum class LogicBinaryOperator {
    Or,
    And
}

enum class ArithmeticBinaryOperator{
    Plus,
    Minus,
    Multiply,
    Divide,
    Remainder}
