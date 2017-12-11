@file:Suppress("MemberVisibilityCanPrivate")

package ru.spbau.mit.ast

interface ASTVisitor {
    fun visitFile(file: File)
    fun visitDoubleStatement(statement: DoubleStatement)
    fun visitComparingLogicExpression(expression: ComparingLogicExpression)
    fun visitLogicalBinaryExpression(expression: LogicalBinaryExpression)
    fun visitIdentifierExpression(identifier: IdentifierExpression)
    fun visitLiteralExpression(literal: LiteralExpression)
    fun visitArithmeticBinaryExpression(expression: ArithmeticBinaryExpression)
    fun visitFunctionCall(expression: FunctionCall)
    fun visitFunctionDefinition(expression: FunctionDefinition)
    fun visitWhileStatement(expression: WhileStatement)
    fun visitIfStatement(expression: IfStatement)
    fun visitAssignmentStatement(expression: AssignmentStatement)
    fun visitReadStatement(expression: ReadStatement)
    fun visitWriteStatement(expression: WriteStatement)
}

data class ASTree(val root: ASTNode)

sealed class ASTNode {
    abstract val line: Int
    abstract fun visit(visitor: ASTVisitor)
}

sealed class Statement : ASTNode()
sealed class PrimaryStatement : Statement()

data class DoubleStatement(
        val first: PrimaryStatement,
        val second: Statement,
        override val line: Int) : Statement() {

    override fun visit(visitor: ASTVisitor) {
        visitor.visitDoubleStatement(this)
    }
}

data class File(val block: Statement, override val line: Int) : ASTNode() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitFile(this)
    }
}

sealed class Expression : PrimaryStatement()
sealed class LogicExpression : Expression()
sealed class ArithmeticExpression : Expression()

data class ComparingLogicExpression(
        val left: ArithmeticExpression,
        val right: ArithmeticExpression,
        val operator: ComparingOperator,
        override val line: Int) : LogicExpression() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitComparingLogicExpression(this)
    }
}

data class LogicalBinaryExpression(
        val left: LogicExpression,
        val right: LogicExpression,
        val operator: LogicBinaryOperator,
        override val line: Int) : LogicExpression() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitLogicalBinaryExpression(this)
    }
}

data class IdentifierExpression(val name: String, override val line: Int) : ArithmeticExpression() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitIdentifierExpression(this)
    }
}

data class LiteralExpression(val value: Int, override val line: Int) : ArithmeticExpression() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitLiteralExpression(this)
    }
}

data class ArithmeticBinaryExpression(
        val left: ArithmeticExpression,
        val right: ArithmeticExpression,
        val operator: ArithmeticBinaryOperator,
        override val line: Int) : ArithmeticExpression() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitArithmeticBinaryExpression(this)
    }
}

data class FunctionCall(
        val function: String,
        val arguments: List<IdentifierExpression>,
        override val line: Int) : PrimaryStatement() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitFunctionCall(this)
    }
}

data class FunctionDefinition(
        val name: String,
        val body: Statement,
        val arguments: List<String>,
        override val line: Int) : PrimaryStatement() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitFunctionDefinition(this)
    }
}

data class WhileStatement(
        val condition: Expression,
        val body: Statement,
        override val line: Int) : PrimaryStatement() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitWhileStatement(this)
    }
}

data class IfStatement(
        val condition: Expression,
        val body: Statement,
        val elseBody: Statement?,
        override val line: Int) : PrimaryStatement() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitIfStatement(this)
    }

}

data class AssignmentStatement(
        val name: String,
        val expression: ArithmeticExpression,
        override val line: Int) : PrimaryStatement() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitAssignmentStatement(this)
    }

}

data class ReadStatement(val identifier: IdentifierExpression, override val line: Int) : PrimaryStatement() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitReadStatement(this)
    }
}

data class WriteStatement(val identifier: IdentifierExpression, override val line: Int) : PrimaryStatement() {
    override fun visit(visitor: ASTVisitor) {
        visitor.visitWriteStatement(this)
    }
}


