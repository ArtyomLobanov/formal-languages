package ru.mit.spbau.lobanov.lexer;

import ru.mit.spbau.lobanov.lexer.token.Token;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public final class LexerUtils {
    private LexerUtils() {
    }

    public static List<Token> parse(Path path) throws IOException {
        try (FileReader reader = new FileReader(path.toFile())) {
            final LLexer lexer = new LLexer(reader);
            final List<Token> tokens = new ArrayList<>();
            Token token = lexer.yylex();
            while (token != null) {
                tokens.add(token);
                System.out.println(token);
                token = lexer.yylex();
            }
            return tokens;
        }
    }

    public static void writeTokens(List<Token> tokens, PrintStream out) {
        tokens.forEach(out::println);
    }
}

