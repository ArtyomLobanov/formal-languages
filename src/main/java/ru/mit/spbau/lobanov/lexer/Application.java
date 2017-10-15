package ru.mit.spbau.lobanov.lexer;

import ru.mit.spbau.lobanov.lexer.token.Token;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Application {
    public static void main(String[] args) throws IOException {
//            Main.generate(new File("C:/Git workspace/lexer/src/main/resources/lexer.jflex"));
        if (args.length != 1) {
            System.out.println("One argument expected: path to file with L-code");
            return;
        }
        final List<Token> tokenList;
        try {
            tokenList = LexerUtils.parse(Paths.get(args[0]));
        } catch (Exception e) {
            System.out.println("Something went wrong:");
            System.out.println("     " + e.getMessage());
            return;
        }
        System.out.println("----Code successfully parsed----");
        LexerUtils.writeTokens(tokenList, System.out);
    }
}
