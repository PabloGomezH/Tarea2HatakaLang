lexer grammar HotakaLangLexer;


ID : [a-zA-Z]+[a-zA-Z0-9-]* ;     // un id puede empezar con letras o  y puede ser seguido o no de letras, numeros,  y -

WS : [ \t\r]+ -> skip ;             // skip spaces, tabs, newlines

//tipos de dato
fragment DIG		: [0-9]		;
fragment PUNTO		: '.'		;
fragment TRUE		: 'pono'	;	//no te ama
fragment NOTRUE		: 'rino'	;	//su amor


SALTO	:	'\n'			;
NUM		:	DIG+			;
FLOAT	:	NUM PUNTO NUM	; 
SINO	: 	TRUE | NOTRUE	;
STR		:	'"'.*?'"'		;

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

//funciones matematicas
SQRT	: 'pakiaka'	;	//sqrt
FIB		: 'fiboo'	;    //fibonacci  -  fiboo
COS		: 'tuhinga'	;	//cos
SEN		: 'pokapu'	;	//sin
PRIM	: 'teina'	;   //teina
POT		: 'mana'	;	//pou


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

BEGIN	: 'home'		;	//introIngInf
END		: 'mutunga'		;	//proyTi

CASE	:  'tuhinga'	;   //caso
DEFAULT :  'mate'		;	//defecto
