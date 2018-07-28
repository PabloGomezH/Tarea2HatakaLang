grammar HotakaLangParser;

import HotakaLangLexer;


s 		: sentencia EOF					
		;


programa    :    inicio sentencia+ finale;

inicio        :    BEGIN;
finale        :    END;


sentencia	:    declaracionvar
            |    asignvar
            |    muestra
            |    leer
            |    condicional
            |    operaciones
            |    fors
            |	 switchz
            |    whiles
            ;


declaracionvar	:	variable ID
				|	variable asignvar
				;


variable	:	REAL
			|	BOOL
			|	ENTERO
			|	STRING
			;


asignvar	:	ID ASIGN tipos
			|   ID ASIGN operaciones
			;


tipos       :	NUM
			|	SINO
			|	STR
			|	FLOAT
			;


operaciones	:	operaciones operador operaciones
			|	ID
			|	NUM
			|	PI operaciones PD
			;


operador	:	MAYOR
			|	MENOR
			|	MAIG
			|	MEIG
			|	IG
			|	NIG
			| 	SUMA
			|	RESTA
			|	DIV
			|	MULT
			|	MOD
			|	AND
			|	OR
			;


whiles	:	WHILE PI operaciones PD LLI bloque LLD
		;


bloque	:	sentencia*
		;


sumas	:	ID SUMA ID
		|	ID SUMA NUM
		|	NUM SUMA ID
		|	NUM SUMA NUM
		;
		
muestra	:	MUESTRA (STR | ID)+
		;
		
leer	: ID ASIGN LEE
		;

fors	:	FOR asignvar PIP operaciones LLI bloque LLD
		;


condicional	 :	IF bloque_condicional
				(ELSE bloque_condicional)?
				;	

bloque_condicional 	: 	PI operaciones PD LLI bloque LLD
					| PI operaciones PD sentencia
					;	
					
switchz	:	SWITCH PI ID PD LLI casos_switch+  LLD
		;

casos_switch:	CASE NUM DP sentencia
			|	DEFAULT DP sentencia
			;