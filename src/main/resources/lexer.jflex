package ru.mit.spbau.lobanov.lexer;

import ru.mit.spbau.lobanov.lexer.token.*;

%%

%class LLexer

%unicode

%line
%column

%type Token

%{
    private interface TokenCreator {
        Token create(int lineIndex, int begin, int end, String notation);
    }

    private Token create(TokenCreator creator) {
        return creator.create(yyline, yycolumn, yycolumn + yytext().length(), yytext());
    }
%}

Space = (\r | \n | \r\n | \s | \f | \t | " ")*
KeyWord = if | then | else | while | do | read | write | begin | end
Operator = "+" | "−" | "∗" | "/" "%" | "==" | "!=" | ">" | ">=" | "<" | "<=" | "&&" | "||" | "(" | ")" | ";"
Comment = \/\/[^\n\r]*
Integer = ([0-9]|[1-9](_[0-9])*[0-9])
Digits = ([0-9]|[0-9](_[0-9])*[0-9])
ExponentPart = (e|E) ("+"|"-")? {Digits}
Float = {Digits} "." {Digits}? {ExponentPart}? f? | "." {Digits} {ExponentPart}? f? | {Digits} {ExponentPart} f? | {Digits} {ExponentPart} f?
Identifier = [a-z_]([a-z] | \d | _)*


%%

{Space} {break;}

{Comment} {return create(CommentToken::create);}

{KeyWord} {return create(KeyWordToken::create);}

{Operator} {return create(OperatorToken::create);}

{Float} {return create(FloatToken::create);}

{Integer} {return create(IntegerToken::create);}

{Identifier} {return create(IdentifierToken::create);}

[^] { throw new RuntimeException("Cant parse token: \"" + yytext() + "\""); }