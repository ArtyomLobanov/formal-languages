package ru.spbau.mit.ast

sealed class InterpreterException(message: String, val line: Int) : RuntimeException(message)

class SyntaxException(line: Int)
    : InterpreterException("Syntax exception! Failed to parse code!", line)
