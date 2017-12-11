@file:Suppress("MemberVisibilityCanPrivate")

package ru.spbau.mit.ast

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.tree.TerminalNode
import ru.spbau.mit.parser.LLanguageLexer
import ru.spbau.mit.parser.LLanguageParser
import ru.spbau.mit.parser.LLanguageParser.*

object ASTBuilder {

    fun buildAST(stream: CharStream): ASTree {
        val lexer = LLanguageLexer(stream)
        val parser = LLanguageParser(BufferedTokenStream(lexer))
        return ASTree(visitFile(parser.file()))
    }

    fun visitUnitLogicExpression(expression: UnitLogicExpressionContext): LogicExpression {
        return when {
            expression.op !== null && expression.left !== null && expression.right !== null -> {
                val operator = when (expression.op.type) {
                    LLanguageParser.GREATER -> ComparingOperator.Greater
                    LLanguageParser.LESS -> ComparingOperator.Less
                    LLanguageParser.GREATER_OR_EQUAL -> ComparingOperator.GreaterOrEqual
                    LLanguageParser.LESS_OR_EQUAL -> ComparingOperator.LessOrEqual
                    LLanguageParser.EQUAL -> ComparingOperator.Equal
                    LLanguageParser.NOT_EQUAL -> ComparingOperator.NotEqual
                    else -> throw SyntaxException(expression.start.line)
                }
                val left = visitArithmeticExpression(expression.left)
                val right = visitArithmeticExpression(expression.right)
                ComparingLogicExpression(left, right, operator, expression.start.line)
            }
            expression.logicExpression() !== null ->
                visitLogicExpression(expression.logicExpression())
            else -> throw SyntaxException(expression.start.line)
        }
    }

    fun visitLogicExpressionHighPrioity(expression: LogicExpressionHighPrioityContext): LogicExpression {
        return when {
            expression.op !== null && expression.left !== null && expression.right !== null -> {
                val operator = when (expression.op.type) {
                    LLanguageParser.LOGICAL_AND -> LogicBinaryOperator.And
                    LLanguageParser.LOGICAL_OR -> LogicBinaryOperator.Or
                    else -> throw SyntaxException(expression.start.line)
                }
                val left = visitLogicExpressionHighPrioity(expression.left)
                val right = visitUnitLogicExpression(expression.right)
                LogicalBinaryExpression(left, right, operator, expression.start.line)
            }
            expression.unitLogicExpression() !== null ->
                visitUnitLogicExpression(expression.unitLogicExpression())
            else -> throw SyntaxException(expression.start.line)
        }
    }

    fun visitLogicExpression(expression: LogicExpressionContext): LogicExpression {
        return when {
            expression.op !== null && expression.left !== null && expression.right !== null -> {
                val operator = when (expression.op.type) {
                    LLanguageParser.LOGICAL_AND -> LogicBinaryOperator.And
                    LLanguageParser.LOGICAL_OR -> LogicBinaryOperator.Or
                    else -> throw SyntaxException(expression.start.line)
                }
                val left = visitLogicExpression(expression.left)
                val right = visitLogicExpressionHighPrioity(expression.right)
                LogicalBinaryExpression(left, right, operator, expression.start.line)
            }
            expression.logicExpressionHighPrioity() !== null ->
                visitLogicExpressionHighPrioity(expression.logicExpressionHighPrioity())
            else -> throw SyntaxException(expression.start.line)
        }
    }

    fun visitUnitArithmeticExpression(expression: UnitArithmeticExpressionContext): ArithmeticExpression {
        return when {
            expression.IDENTIFIER() !== null ->
                IdentifierExpression(expression.IDENTIFIER().symbol.text, expression.IDENTIFIER().symbol.line)
            expression.INTEGER() !== null ->
                LiteralExpression(Integer.parseInt(expression.INTEGER().symbol.text), expression.INTEGER().symbol.line)
            expression.arithmeticExpression() !== null ->
                visitArithmeticExpression(expression.arithmeticExpression())
            else -> throw SyntaxException(expression.start.line)
        }
    }

    fun visitArithmeticExpressionHighPrioity(expression: ArithmeticExpressionHighPrioityContext):
            ArithmeticExpression {
        return when {
            expression.op !== null && expression.left !== null && expression.right !== null -> {
                val operator = when (expression.op.type) {
                    LLanguageParser.PLUS -> ArithmeticBinaryOperator.Plus
                    LLanguageParser.MINUS -> ArithmeticBinaryOperator.Minus
                    LLanguageParser.MULTIPLY -> ArithmeticBinaryOperator.Multiply
                    LLanguageParser.DIVIDE -> ArithmeticBinaryOperator.Divide
                    LLanguageParser.REMAINDER -> ArithmeticBinaryOperator.Remainder
                    else -> throw SyntaxException(expression.start.line)
                }
                val left = visitArithmeticExpressionHighPrioity(expression.left)
                val right = visitUnitArithmeticExpression(expression.right)
                ArithmeticBinaryExpression(left, right, operator, expression.start.line)
            }
            expression.unitArithmeticExpression() !== null ->
                visitUnitArithmeticExpression(expression.unitArithmeticExpression())
            else -> throw SyntaxException(expression.start.line)
        }
    }

    fun visitArithmeticExpression(expression: ArithmeticExpressionContext):
            ArithmeticExpression {
        return when {
            expression.op !== null && expression.left !== null && expression.right !== null -> {
                val operator = when (expression.op.type) {
                    LLanguageParser.PLUS -> ArithmeticBinaryOperator.Plus
                    LLanguageParser.MINUS -> ArithmeticBinaryOperator.Minus
                    LLanguageParser.MULTIPLY -> ArithmeticBinaryOperator.Multiply
                    LLanguageParser.DIVIDE -> ArithmeticBinaryOperator.Divide
                    LLanguageParser.REMAINDER -> ArithmeticBinaryOperator.Remainder
                    else -> throw SyntaxException(expression.start.line)
                }
                val left = visitArithmeticExpression(expression.left)
                val right = visitArithmeticExpressionHighPrioity(expression.right)
                ArithmeticBinaryExpression(left, right, operator, expression.start.line)
            }
            expression.arithmeticExpressionHighPrioity() !== null ->
                visitArithmeticExpressionHighPrioity(expression.arithmeticExpressionHighPrioity())
            else -> throw SyntaxException(expression.start.line)
        }
    }

    fun visitExpression(expression: ExpressionContext): Expression {
        return when {
            expression.logicExpression() !== null ->
                visitLogicExpression(expression.logicExpression())
            expression.arithmeticExpression() !== null ->
                visitArithmeticExpression(expression.arithmeticExpression())
            else -> throw SyntaxException(expression.start.line)
        }
    }

    fun visitFunctionCall(callStatement: FunctionCallContext): FunctionCall {
        val name = callStatement.IDENTIFIER().symbol.text
        val arguments = callStatement.arguments().IDENTIFIER()
                .map(this::visitIdentifierExpression)
        return FunctionCall(name, arguments, callStatement.start.line)
    }

    fun visitFile(file: FileContext): File {
        return File(visitStatement(file.statement()), file.start.line)
    }

    fun visitDoubleStatement(doubleStatement: DoubleStatementContext): DoubleStatement {
        return DoubleStatement(
                visitPrimaryStatement(doubleStatement.primaryStatement()),
                visitStatement(doubleStatement.statement()),
                doubleStatement.start.line
                )
    }

    fun visitAssignmentStatement(expression: AssignmentStatementContext): AssignmentStatement {
        return when {
            expression.arithmeticExpression() != null && expression.IDENTIFIER() !== null ->
                AssignmentStatement(expression.IDENTIFIER().symbol.text,
                        visitArithmeticExpression(expression.arithmeticExpression()), expression.start.line)
            else -> throw SyntaxException(expression.start.line)
        }
    }

    fun visitStatement(statement: StatementContext): Statement {
        return when {
            statement.primaryStatement() !== null -> visitPrimaryStatement(statement.primaryStatement())
            statement.doubleStatement() !== null -> visitDoubleStatement(statement.doubleStatement())
            else -> throw SyntaxException(statement.start.line)
        }
    }

    fun visitPrimaryStatement(statement: PrimaryStatementContext): PrimaryStatement {
        return when {
            statement.assignmentStatement() !== null ->
                visitAssignmentStatement(statement.assignmentStatement())
            statement.expressionStatement() !== null ->
                visitExpression(statement.expressionStatement().expression())
            statement.definitionStatment() !== null ->
                visitFunctionDefinition(statement.definitionStatment())
            statement.ifStatement() !== null ->
                visitIfStatement(statement.ifStatement())
            statement.assignmentStatement() !== null ->
                visitAssignmentStatement(statement.assignmentStatement())
            statement.whileStatement() !== null ->
                visitWhileStatement(statement.whileStatement())
            statement.readStatement() != null ->
                visitReadStatement(statement.readStatement())
            statement.writeStatement() != null ->
                visitWriteStatement(statement.writeStatement())
            statement.functionCall() != null ->
                visitFunctionCall(statement.functionCall())
            else -> throw SyntaxException(statement.start.line)
        }
    }

    fun visitIfStatement(statement: IfStatementContext): IfStatement {
        val condition = visitExpression(statement.expression())
        val body = visitStatement(statement.blockWithBraces(0).statement())
        val elseBody = when (statement.blockWithBraces(1)?.statement()) {
            null -> null
            else -> visitStatement(statement.blockWithBraces(1).statement())
        }
        return IfStatement(condition, body, elseBody, statement.start.line)
    }

    fun visitWhileStatement(loop: WhileStatementContext): WhileStatement {
        val condition = visitExpression(loop.expression())
        val body = visitStatement(loop.blockWithBraces().statement())
        return WhileStatement(condition, body, loop.start.line)
    }

    fun visitFunctionDefinition(definition: DefinitionStatmentContext): FunctionDefinition {
        val name = definition.IDENTIFIER().symbol.text
        val argumentsNames = definition.parameterNames().IDENTIFIER()
                .map { it -> it.symbol.text }
                .toList()
        val body = visitStatement(definition.blockWithBraces().statement())
        return FunctionDefinition(name, body, argumentsNames, definition.start.line)
    }

    fun visitReadStatement(statement: ReadStatementContext): ReadStatement {
        return ReadStatement(visitIdentifierExpression(statement.IDENTIFIER()), statement.start.line)
    }

    fun visitWriteStatement(statement: WriteStatementContext): WriteStatement {
        return WriteStatement(visitIdentifierExpression(statement.IDENTIFIER()), statement.start.line)
    }

    fun visitIdentifierExpression(identifier: TerminalNode): IdentifierExpression {
        return IdentifierExpression(identifier.symbol.text, identifier.symbol.line)
    }
}