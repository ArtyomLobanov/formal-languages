package ru.spbau.mit

import org.antlr.v4.runtime.CharStreams
import org.junit.Test
import ru.spbau.mit.ast.*
import kotlin.test.assertEquals

class ParsingTests {
    @Test
    fun functionDefinition() {
        val tree = ASTBuilder.buildAST(CharStreams.fromString(
                """
                 define foo(a, b) {
                     a+b;
                 }

                """.trimIndent()
        )).root
        val expectedTree = File(
                Block(
                        listOf(FunctionDefinition("foo",
                                Block(
                                        listOf(
                                                ArithmeticBinaryExpression(
                                                        IdentifierExpression("a", 2),
                                                        IdentifierExpression("b", 2),
                                                        ArithmeticBinaryOperator.Plus,
                                                        2
                                                )
                                        ),
                                        2
                                ),
                                listOf("a", "b"),
                                1)
                        ),
                        1),
                1
        )
        assertEquals(expectedTree, tree)
    }

    @Test
    fun functionCall() {
        val tree = ASTBuilder.buildAST(CharStreams.fromString(
                """
                       //nothing

                        foo();
                        foo(c);
                        foo(a, b);
                """
        )).root
        val expectedTree = File(
                Block(
                        listOf(
                                FunctionCall("foo",
                                        listOf(),
                                        4
                                ),
                                FunctionCall("foo",
                                        listOf(
                                                IdentifierExpression("c", 5)
                                        ),
                                        5
                                ),
                                FunctionCall("foo",
                                        listOf(
                                                IdentifierExpression("a", 6),
                                                IdentifierExpression("b", 6)
                                        ),
                                        6
                                )
                        ),
                        4
                ),
                4)
        assertEquals(expectedTree, tree)
    }

    @Test
    fun whileCycle() {
        val tree = ASTBuilder.buildAST(CharStreams.fromString(
                """  while(a > b) {
                            a := 3;"
                        }
                """
        )).root
        val expectedTree = File(
                Block(
                        listOf(
                                WhileStatement(
                                        ComparingLogicExpression(
                                                IdentifierExpression("a", 1),
                                                IdentifierExpression("b", 1),
                                                ComparingOperator.Greater,
                                                1
                                        ),
                                        Block(
                                                listOf(
                                                        AssignmentStatement(
                                                                "a",
                                                                LiteralExpression(3, 2),
                                                                2
                                                        )
                                                ),
                                                2
                                        ),
                                        1
                                )
                        ),
                        1
                ),
                1)
        assertEquals(expectedTree, tree)
    }

    @Test
    fun ifStatement() {
        val tree = ASTBuilder.buildAST(CharStreams.fromString(
                """
                    if (a >= b) {
                        foo();
                    }
                    if (c == d) {
                        lout <<  x;
                    } else {
                        lin>>  x;
                    }
                """
        )).root
        val expectedTree = File(
                Block(
                        listOf(
                                IfStatement(
                                        ComparingLogicExpression(
                                                IdentifierExpression("a", 2),
                                                IdentifierExpression("b", 2),
                                                ComparingOperator.GreaterOrEqual,
                                                2
                                        ),
                                        Block(
                                                listOf(
                                                        FunctionCall("foo", listOf(), 3)
                                                ),
                                                3
                                        ),
                                        null,
                                        2
                                ),
                                IfStatement(
                                        ComparingLogicExpression(
                                                IdentifierExpression("c", 5),
                                                IdentifierExpression("d", 5),
                                                ComparingOperator.Equal,
                                                5
                                        ),
                                        Block(
                                                listOf(
                                                        WriteStatement(IdentifierExpression("x", 6), 6)
                                                ),
                                                6
                                        ),
                                        Block(
                                                listOf(
                                                        ReadStatement(IdentifierExpression("x", 8), 8)
                                                ),
                                                8
                                        ),
                                        5
                                )
                        ),
                        2
                ),
                2)
        assertEquals(expectedTree, tree)
    }

    @Test
    fun priorityTest() {
        val tree = ASTBuilder.buildAST(CharStreams.fromString(
                """
                 2-2-2;
                 3*4-5-8*9;
                 3*(4-5);
                 a == b || c == d && k == p;
                """.trimIndent()
        )).root
        val expectedTree = File(
                Block(
                        listOf(
                                ArithmeticBinaryExpression(
                                        ArithmeticBinaryExpression(
                                                LiteralExpression(2, 1),
                                                LiteralExpression(2, 1),
                                                ArithmeticBinaryOperator.Minus,
                                                1
                                        ),
                                        LiteralExpression(2, 1),
                                        ArithmeticBinaryOperator.Minus,
                                        1
                                ),
                                ArithmeticBinaryExpression(
                                        ArithmeticBinaryExpression(
                                                ArithmeticBinaryExpression(
                                                        LiteralExpression(3, 2),
                                                        LiteralExpression(4, 2),
                                                        ArithmeticBinaryOperator.Multiply,
                                                        2
                                                ),
                                                LiteralExpression(5, 2),
                                                ArithmeticBinaryOperator.Minus,
                                                2
                                        ),
                                        ArithmeticBinaryExpression(
                                                LiteralExpression(8, 2),
                                                LiteralExpression(9, 2),
                                                ArithmeticBinaryOperator.Multiply,
                                                2
                                        ),
                                        ArithmeticBinaryOperator.Minus,
                                        2
                                ),
                                ArithmeticBinaryExpression(
                                        LiteralExpression(3, 3),
                                        ArithmeticBinaryExpression(
                                                LiteralExpression(4, 3),
                                                LiteralExpression(5, 3),
                                                ArithmeticBinaryOperator.Minus,
                                                3
                                        ),
                                        ArithmeticBinaryOperator.Multiply,
                                        3
                                ),
                                LogicalBinaryExpression(
                                        ComparingLogicExpression(
                                                IdentifierExpression("a", 4),
                                                IdentifierExpression("b", 4),
                                                ComparingOperator.Equal,
                                                4
                                        ),
                                        LogicalBinaryExpression(
                                                ComparingLogicExpression(
                                                        IdentifierExpression("c", 4),
                                                        IdentifierExpression("d", 4),
                                                        ComparingOperator.Equal,
                                                        4
                                                ),
                                                ComparingLogicExpression(
                                                        IdentifierExpression("k", 4),
                                                        IdentifierExpression("p", 4),
                                                        ComparingOperator.Equal,
                                                        4
                                                ),
                                                LogicBinaryOperator.And,
                                                4
                                        ),
                                        LogicBinaryOperator.Or,
                                        4
                                )
                        ),
                        1),
                1
        )
        assertEquals(expectedTree, tree)
    }
}
