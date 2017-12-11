package ru.spbau.mit

import org.antlr.v4.runtime.CharStreams
import ru.spbau.mit.ast.ASTBuilder
import ru.spbau.mit.ast.ASTPrinter
import ru.spbau.mit.ast.InterpreterException


fun main(args: Array<String>) {
    if (args.size != 1) {
        print("Exactly one argument expected (path to file)!")
        return
    }
    try {
        val tree = ASTBuilder.buildAST(CharStreams.fromFileName(args[0]))
        val visitor = ASTPrinter(System.out)
        tree.root.visit(visitor)
    } catch (e: Exception) {
        print("Something went wrong:\n")
        print("Message: ${e.message}\n")
        if (e is InterpreterException) {
            print("At line: ${e.line}\n")
        }
    }
}

