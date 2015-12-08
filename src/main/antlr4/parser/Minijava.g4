grammar Minijava;

program: mainClass (classDecl | classDeclExtends)*;
mainClass: 'class' identifier '{' 'public' 'static' 'void' 'main' '(' 'String[]' identifier ')' '{' statement '}' '}';
classDecl: 'class' identifier '{' varDecl* methodDecl* '}';
classDeclExtends: 'class' identifier 'extends' identifier '{' varDecl* methodDecl* '}';
varDecl: type identifier ';';
methodDecl: 'public' type identifier '(' formalList ')' '{' varDecl* statement* 'return' exp ';' '}';
formalList: type identifier formalRest* | ;
formalRest: ',' type identifier;
type: 'int[]' # ArrayType
    | 'boolean' # BooleanType
    | 'int' # IntegerType
    | identifier # IdentifierType;
statement: '{' statement* '}' # Block
    | 'if' '(' exp ')' statement ('else' statement)? # If
    | 'while' '(' exp ')' statement # While
    | 'System.out.println' '(' exp ')' ';' # Print
    | identifier '=' exp ';' # Assign
    | identifier '['exp']' '=' exp ';' # ArrayAssign;
exp: exp op=('*'|'/') exp # MulDiv
    | exp op=('+'|'-') exp # AddSub
    | identifier '[' exp ']' # ArrayLookup
    | exp '.length' # ArrayLength
    | exp '.' identifier '(' expList ')' # Call
    | INT # IntegerLiteral
    | 'true' # True
    | 'false' # False
    | type? identifier # IdentifierExp
    | 'this' # This
    | 'new' 'int' '[' exp ']' # NewArray
    | 'new' identifier '('')' # NewObject
    | '!' exp # Not
    | '(' exp ')' # Parentesis
    | exp '&&' exp # And
    | exp op=('<'|'==') exp # LessThanEquals;
expList: exp expRest*
    | ;
expRest: ',' exp;
identifier: ID;
ID :[a-zA-Z][a-zA-Z0-9_]*;
INT: [0-9]+;
MUL: '*';
DIV: '/';
ADD: '+';
SUB: '-';
EQ: '==';
LT: '<';
Comment: '//'.*?[\r\n]+ -> skip;
NEWLINE : [\r\n\t ]+ -> skip;