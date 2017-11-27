package ru.spbau.mit

import org.antlr.v4.gui.TreeViewer
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.spbau.mit.ast.ASTBuilder
import ru.spbau.mit.ast.ASTPrinter
import ru.spbau.mit.ast.InterpreterException
import ru.spbau.mit.parser.LLanguageLexer
import ru.spbau.mit.parser.LLanguageParser
import javax.swing.JFrame
import javax.swing.JPanel


fun main(args: Array<String>) {
    if (args.size != 2) {
        print("Exactly two argument expected (path to file and flag - show tree or not)!")
        return
    }
    try {
        val tree = ASTBuilder.buildAST(CharStreams.fromFileName(args[0]))
        val visitor = ASTPrinter(System.out)
        tree.root.visit(visitor)
        val flag = args[1].toBoolean()
        if (flag) {
            showTree(args[0])
        }
    } catch (e: Exception) {
        print("Something went wrong:\n")
        print("Message: ${e.message}\n")
        if (e is InterpreterException) {
            print("At line: ${e.line}\n")
        }
    }
}

fun showTree(filename: String) {
    val lexer = LLanguageLexer(CharStreams.fromFileName(filename))
    val parser = LLanguageParser(BufferedTokenStream(lexer))
    parser.buildParseTree = true
    val frame = JFrame("Antlr AST")
    val panel = JPanel()
    val viewer = TreeViewer(parser.ruleNames.asList(), parser.file())
    viewer.scale = 1.5
    panel.add(viewer)
    frame.add(panel)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setSize(1000, 1000)
    frame.isVisible = true
}
