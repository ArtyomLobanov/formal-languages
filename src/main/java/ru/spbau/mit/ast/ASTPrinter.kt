package ru.spbau.mit.ast

import java.io.PrintStream

class ASTPrinter(val output: PrintStream) : ASTVisitor {
    private var level = 0

    fun write(text: String) {
        output.println("\t".repeat(level) + text)
    }

    override fun visitFile(file: File) {
        write("File(line=${file.line}, block=")
        level++;
        file.block.visit(this)
        level--;
        write(")")
    }

    override fun visitBlock(block: Block) {
        write("Block(line=${block.line}, statements:")
        level++
        block.statements.forEach({ it.visit(this) })
        level--;
        write(")")
    }

    override fun visitComparingLogicExpression(expression: ComparingLogicExpression) {
        write("ComparingLogicExpression(line=${expression.line}, operator=${expression.operator},")
        write("left=")
        level++
        expression.left.visit(this)
        level--
        write("right=")
        level++
        expression.right.visit(this)
        level--
        write(")")
    }

    override fun visitLogicalBinaryExpression(expression: LogicalBinaryExpression) {
        write("LogicalBinaryExpression(line=${expression.line}, operator=${expression.operator},")
        write("left=")
        level++
        expression.left.visit(this)
        level--
        write("right=")
        level++
        expression.right.visit(this)
        level--
        write(")")
    }

    override fun visitIdentifierExpression(identifier: IdentifierExpression) {
        write("Identifier(line=${identifier.line}, name=${identifier.name}")
    }

    override fun visitLiteralExpression(literal: LiteralExpression) {
        write("Literal(line=${literal.line}, value=${literal.value})")
    }

    override fun visitArithmeticBinaryExpression(expression: ArithmeticBinaryExpression) {
        write("ArithmeticBinaryExpression(line=${expression.line}, operator=${expression.operator},")
        write("left=")
        level++
        expression.left.visit(this)
        level--
        write("right=")
        level++
        expression.right.visit(this)
        level--
        write(")")
    }

    override fun visitFunctionCall(expression: FunctionCall) {
        write("FunctionCall(line=${expression.line}, name=${expression.function}, arguments:")
        level++
        expression.arguments.forEach({ it.visit(this) })
        level--;
        write(")")
    }

    override fun visitFunctionDefinition(expression: FunctionDefinition) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitWhileStatement(expression: WhileStatement) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitIfStatement(expression: IfStatement) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitAssignmentStatement(expression: AssignmentStatement) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitReturnStatement(expression: ReturnStatement) {
        write("ReturnStatement(line=${expression.line}, expression=")
        level++
        expression.expression.visit(this)
        level--;
        write(")")
    }

    override fun visitReadStatement(expression: ReadStatement) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitWriteStatement(expression: WriteStatement) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}