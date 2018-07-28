lexer grammar HotakaLangLexer;


//tipos de dato
fragment DIG		: [0-9]		;
fragment PUNTO		: '.'		;
fragment TRUE		: 'pono'	;	//no te ama
fragment NOTRUE		: 'rino'	;	//su amor

//variables
REAL	: 'tau'		;	//real
ENTERO	: 'katoa'	;	//int
BOOL	: 'engari'	;	//bool
STRING	: 'mekameka'	;	//cadena = aho  -  string


//output input
MUESTRA	: 'tauira'	;	//muestra  -  imprimir = ta
LEE		: 'panui'	;	//leer


//operadores matematicos
SUMA	: '+'	;
RESTA	: '-'	;
MULT	: '*'	;
DIV		: '/'	;
MOD		: '%'	;


//operadores logicos
AND		: 'a'	;	//and
OR		: 'ranei'	;	//or
NOT		: 'kore'	;	//!


//operadores logicos x2
MAYOR	: '>'	;
MENOR	: '<'	;
MAIG	: '>='	;
MEIG	: '<='	;
IG		: '=='	;
NIG		: '!='	;


//condicionales
IF		: 'ae'		;	//if
ELSE	: 'aee'		;	//else
SWITCH	: 'huringa'	;	//interruptor = whakawhiti  -  switch


//estructuras repetitivas
WHILE	: 'ahakoa'	;	//while
FOR		: 'mo'		;	//for


//asignacion
ASIGN	: '='	;


//parentesis
LLI		: '{'	;
LLD		: '}'	;
PI		: '('	;
PD		: ')'	;
PCI		: '['	;
PCD		: ']'	;
COM		: '"'	;
PIP		: '|'	;


//puntuacion
DP		: ':'	;


//inicio y fin
BEGIN	: 'home'		;	//introIngInf
END		: 'mutunga'		;	//proyTi


//para el switch
CASE	:  'tuhinga'	;   //caso
DEFAULT :  'mate'		;	//defecto

//funciones matematicas
SQRT	: 'pakiaka'	;	//sqrt
FIB		: 'fiboo'	;    //fibonacci  -  fiboo
SEN		: 'pokapu'	;	//sin
PRIM	: 'teina'	;   //teina
POT		: 'mana'	;	//pou
//por alguna razon, no escribe en el archivo de tokens del lexer de aqui para abajo (en el HotakaLangLexer.tokens)
//los ordene para que queden todos los numeros seguidos y que no haya null en el HotakaLangParserLexer
COS		: 'tuhinga'	;	//cos




NUM		:	DIG+			;
FLOAT	:	NUM PUNTO NUM	; 
SINO	: 	TRUE | NOTRUE	;
STR		:	'"'.*?'"'		;

ID : [a-zA-Z]+[a-zA-Z0-9-]* ;     // un id puede empezar con letras o  y puede ser seguido o no de letras, numeros,  y -
WS : [ \n\t\r]+ -> skip ;             // skip spaces, tabs, newlines

