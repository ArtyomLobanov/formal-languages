grammar LLanguage;

file : statement;
blockWithBraces : '{' statement '}';

statement
    : primaryStatement
    | doubleStatement
    ;

primaryStatement
    : definitionStatment
    | expressionStatement
    | whileStatement
    | ifStatement
    | functionCall
    | assignmentStatement
    | writeStatement
    | readStatement
    ;

writeStatement : 'lout' '<<' IDENTIFIER LINE_SEPARATOR;
readStatement : 'lin' '>>' IDENTIFIER LINE_SEPARATOR;
definitionStatment : 'define' IDENTIFIER '(' parameterNames ')' blockWithBraces;
parameterNames : (IDENTIFIER (',' IDENTIFIER)*)?;
whileStatement : 'while' '(' expression ')' blockWithBraces;
ifStatement : 'if' '(' expression ')' blockWithBraces ('else' blockWithBraces)?;
assignmentStatement : IDENTIFIER ':=' arithmeticExpression LINE_SEPARATOR;
expressionStatement : expression LINE_SEPARATOR;
doubleStatement : primaryStatement statement;

functionCall : IDENTIFIER '(' arguments ')' LINE_SEPARATOR;
arguments : (IDENTIFIER (',' IDENTIFIER)*)?;

unitLogicExpression
    : '(' logicExpression ')'
    | left =arithmeticExpression op = (GREATER | LESS | GREATER_OR_EQUAL | LESS_OR_EQUAL) right = arithmeticExpression
    | left =arithmeticExpression op = (EQUAL | NOT_EQUAL) right = arithmeticExpression;

logicExpressionHighPrioity
    : unitLogicExpression
    | left = logicExpressionHighPrioity op = LOGICAL_AND right = unitLogicExpression;

logicExpression
    : logicExpressionHighPrioity
    | left = logicExpression op = LOGICAL_OR right = logicExpressionHighPrioity;

unitArithmeticExpression
    : '(' arithmeticExpression ')'
    | IDENTIFIER
    | INTEGER;

arithmeticExpressionHighPrioity
    : unitArithmeticExpression
    | left = arithmeticExpressionHighPrioity op = (MULTIPLY | DIVIDE | REMAINDER) right = unitArithmeticExpression;

arithmeticExpression
    : arithmeticExpressionHighPrioity
    | left = arithmeticExpression op = (PLUS | MINUS) right = arithmeticExpressionHighPrioity;

expression
    : logicExpression
    | arithmeticExpression;

LINE_SEPARATOR : ';';
MULTIPLY : '*';
DIVIDE : '/';
REMAINDER : '%';
PLUS : '+';
MINUS : '-';
GREATER : '>';
LESS : '<';
GREATER_OR_EQUAL : '>=';
LESS_OR_EQUAL : '<=';
EQUAL : '==';
NOT_EQUAL : '!=';
LOGICAL_OR : '||';
LOGICAL_AND : '&&';
INTEGER : '0'
        | ([1-9][0-9]*);

IDENTIFIER : ([a-zA-Z_][a-zA-Z_0-9]*);
COMMENT : '//' ~[\r\n]* -> skip;
WS : (' ' | '\t' | '\r'| '\n') -> skip;