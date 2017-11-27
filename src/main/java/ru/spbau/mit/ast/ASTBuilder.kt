@file:Suppress("MemberVisibilityCanPrivate")

package ru.spbau.mit.ast

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStream
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
            expression.functionCall() !== null ->
                visitFunctionCall(expression.functionCall())
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
        val arguments = callStatement.arguments().expression()
                .map(this::visitExpression)
        return FunctionCall(name, arguments, callStatement.start.line)
    }

    fun visitFile(file: FileContext): File {
        return File(visitBlock(file.block()), file.start.line)
    }

    fun visitBlock(block: BlockContext): Block {
        val statements = block.statement().map(this::visitStatement)
        return Block(statements, block.start.line)
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
            statement.returnStatement() !== null ->
                visitReturnStatement(statement.returnStatement())
            statement.readStatement() != null ->
                visitReadStatement(statement.readStatement())
            statement.writeStatement() != null ->
                visitWriteStatement(statement.writeStatement())
            else -> throw SyntaxException(statement.start.line)
        }
    }

    fun visitReturnStatement(statement: ReturnStatementContext): ReturnStatement {
        val expression = visitExpression(statement.expression())
        return ReturnStatement(expression, statement.start.line)
    }

    fun visitIfStatement(statement: IfStatementContext): IfStatement {
        val condition = visitLogicExpression(statement.logicExpression())
        val body = visitBlock(statement.blockWithBraces(0).block())
        val elseBody = when (statement.blockWithBraces(1)?.block()) {
            null -> null
            else -> visitBlock(statement.blockWithBraces(1).block())
        }
        return IfStatement(condition, body, elseBody, statement.start.line)
    }

    fun visitWhileStatement(loop: WhileStatementContext): WhileStatement {
        val condition = visitLogicExpression(loop.logicExpression())
        val body = visitBlock(loop.blockWithBraces().block())
        return WhileStatement(condition, body, loop.start.line)
    }

    fun visitFunctionDefinition(definition: DefinitionStatmentContext): FunctionDefinition {
        val name = definition.IDENTIFIER().symbol.text
        val argumentsNames = definition.parameterNames().IDENTIFIER()
                .map { it -> it.symbol.text }
                .toList()
        val body = visitBlock(definition.blockWithBraces().block())
        return FunctionDefinition(name, body, argumentsNames, definition.start.line)
    }

    fun visitReadStatement(statement: ReadStatementContext): ReadStatement {
        val identifier = IdentifierExpression(statement.IDENTIFIER().symbol.text, statement.IDENTIFIER().symbol.line)
        return ReadStatement(identifier, statement.start.line)
    }

    fun visitWriteStatement(statement: WriteStatementContext): WriteStatement {
        val identifier = IdentifierExpression(statement.IDENTIFIER().symbol.text, statement.IDENTIFIER().symbol.line)
        return WriteStatement(identifier, statement.start.line)
    }
}