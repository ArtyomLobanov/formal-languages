package ru.spbau.mit.ast

import java.io.PrintStream

class ASTPrinter(private val output: PrintStream) : ASTVisitor {
    private var level = 0

    private fun write(text: String) {
        output.println("\t".repeat(level) + text)
    }

    override fun visitFile(file: File) {
        write("File(line=${file.line}, block=")
        level++
        file.block.visit(this)
        level--
        write(")")
    }

    override fun visitDoubleStatement(statement: DoubleStatement) {
        write("DoubleStatement(line=${statement.line}, ")
        write("first=")
        level++
        statement.first.visit(this)
        level--
        write("right=")
        level++
        statement.second.visit(this)
        level--
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
        level--
        write(")")
    }

    override fun visitFunctionDefinition(expression: FunctionDefinition) {
        write("FunctionDefinition(line=${expression.line}, name=${expression.name}, arguments: " +
                expression.arguments.joinToString(separator = ", ") + ")")
    }

    override fun visitWhileStatement(expression: WhileStatement) {
        write("WhileStatement(line=${expression.line}, ")
        write("condition=")
        level++
        expression.condition.visit(this)
        write("body=")
        expression.body.visit(this)
        level--
        write(")")
    }

    override fun visitIfStatement(expression: IfStatement) {
        write("IfStatement(line=${expression.line}, ")
        write("condition=")
        level++
        expression.condition.visit(this)
        write("body=")
        expression.body.visit(this)
        if (expression.elseBody !== null) {
            write("else_body=")
            expression.elseBody.visit(this)
        }
        level--
        write(")")
    }

    override fun visitAssignmentStatement(expression: AssignmentStatement) {
        write("AssignmentStatement(line=${expression.line}, variable=${expression.name},")
        write("expression=")
        level++
        expression.expression.visit(this)
        level--
        write(")")
    }

    override fun visitReadStatement(expression: ReadStatement) {
        write("ReadStatement(line=${expression.line}, variable=${expression.identifier})")
    }

    override fun visitWriteStatement(expression: WriteStatement) {
        write("WriteStatement(line=${expression.line}, variable=${expression.identifier})")
    }

}