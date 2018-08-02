import java.util.HashMap;
import java.util.Map;

class HotakaLangParserSemantic extends HotakaLangParserBaseVisitor<Object>
{
	protected Map<String, String> todas_variables = new HashMap<String, String>();	/*crea el mapa que contiene las variables como id => tipo*/
	
	public HotakaLangParserSemantic( ) {}
	
	@Override
	public Object visitInicio(HotakaLangParserParser.InicioContext ctx)
	{ 
    	if(ctx.BEGIN().getText().equals("home"))		/* verifica que la palabra para empezar el programa sea home */
    	{
    		System.out.println("#include <stdio.h>");	//
			System.out.println("");						// escribe el inicio del programa, incluyendo el header stdio
			System.out.println("int main(void) {");		//
		}
    	
    	return null;
	}
	
	@Override
	public Object visitFinale(HotakaLangParserParser.FinaleContext input)
	{
		if(input.END().getText().equals("mutunga"))		/* verifica que la palabra de cierre sea mutunga */
		{
			System.out.println("\treturn 0;");			/* termina el programa */
			System.out.println("}");
		} else {
			System.out.println("Error: falta el final del codigo ('mutunga')");		/* si no encuentra la palabra o no es la esperada, muestra el error */
		}
		return null;
	}
	
	@Override
	public Object visitDeclaracionvar(HotakaLangParserParser.DeclaracionvarContext input)
	{
		String tipo_variable = obtenerTipoVar(input.variable().getText());		/* crea la variable que contiene el nombre del tipo de la variable */
		String nombre_variable;				
		String valor_variable = "";
		String aux = "";
		
		try
		{
			nombre_variable = input.ID().getText();		/* comprueba si es una declaracion simple */
		} catch (Exception e) {			/* en caso de ser una declaracion con asignacion inmediata */
			nombre_variable = input.asignvar().ID().getText();		/* saca el nombre de la variable desde la seccion de asignacion */
			try
			{
				aux = tipoVar(input.asignvar().tipos().getText());			/* comprueba que se le este asignando un valor compatible con el tipo de variable */
			} catch(Exception noMeSirve) {
				throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta "
						+ "asignando un valor que no es de su tipo. Si es un string, es probable que "
						+ "le falten las comillas. Por favor, verifique.");
			}
			valor_variable = input.asignvar().tipos().getText();	/* obtiene el valor que se le asigna a la variable */
		}
		
		if(!todas_variables.containsKey(nombre_variable))		/* comprueba si no existe la variable, por lo tanto se puede declarar */
		{
			todas_variables.put(nombre_variable, tipo_variable);	/* como no existe aun, se agrega al set de variables */
			if(!valor_variable.isEmpty())		/* comprueba que no se le este asignando un vacio */
			{
				if(aux.equals("true"))
					valor_variable = "1";		/* si se le esta asignando true, lo cambia por 1 */
				else if(aux.equals("false"))
					valor_variable = "0";		/* si se le esta asignando false, lo cambia por  0*/
				
				if(tipo_variable.equals("int") && !esNumero(valor_variable))	/* comprueba que se le asigne el tipo de valor correcto */
					throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
				else if(tipo_variable.equals("char") && esFloat(valor_variable))	/* comprueba que se le asigne el tipo de valor correcto */
					throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
				else if(tipo_variable.equals("float") && !esFloat(valor_variable))	/* comprueba que se le asigne el tipo de valor correcto */
					throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
				
				System.out.println(String.format("\t%s %s = %s;", tipo_variable, nombre_variable, valor_variable));	/* escribe la definicion con su asignacion */
			} else
				System.out.println(String.format("\t%s %s;", tipo_variable, nombre_variable));	/* escribe solo la definicion */
		} else {
			throw new IllegalArgumentException("La variable '" + nombre_variable + "' ya existe y no hace falta definirla.");	/* en caso de que ya exista la variable */
		}
		return null;
	}
	
	public Object visitAsignvar(HotakaLangParserParser.AsignvarContext input)
	{
		String nombre_variable = input.ID().getText();		/* obtiene el nombre de la variable */
		Boolean op = false;
		if(todas_variables.containsKey(input.ID().getText()))	/* comprueba que la variable exista */
		{
			String valor_variable = "";
			String tipo_variable = todas_variables.get(input.ID().getText());	/* obtiene el tipo de variable de la variable guardada */
			try
			{
				valor_variable = input.tipos().getText();	/* obtiene lo que se le esta tratando de asignar */
			} catch(Exception noMeSirve) {
				op = true;			/* aqui se indica que se le esta asignando una operacion */
				valor_variable = input.operaciones().getText();		/* se obtiene la operacion */
			}
			
			String aux = "";
			
			if(!valor_variable.isEmpty())	/* se comprueba que el valor no sea vacio */
			{
				try
				{
					aux = tipoVar(input.tipos().getText());		/* comprueba si puede obtener directamente lo que se asigna */
				} catch(Exception noMeSirvex2) {
					aux = tipoVar(input.operaciones().getText());	/* lo de arriba, pero si es una operacion */
				}
				if(aux.equals("true"))
					valor_variable = "1";		/* se le asigna 1 si se le asigna true */
				else if(aux.equals("false"))
					valor_variable = "0";		/* se le asigna 0 si se le asigna false */
				
				if(tipo_variable.equals("int") && !esNumero(valor_variable))		/* comprueba que se le este asignando el mismo tipo que el tipo de variable */
					throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
				else if(tipo_variable.equals("char") && esNumero(valor_variable))
					throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
				else if(tipo_variable.equals("float") && !esFloat(valor_variable))
					if(!op)
						throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
						/* lanza una excepcion si se le esta asignando algo indebido */
				
				System.out.println(String.format("\t%s = %s;", nombre_variable, valor_variable));	/* escribe la asignacion */
			} else {
				throw new IllegalArgumentException("Falta el valor en la asignacion de la variable '" + nombre_variable + "'.");	/* por si no tuviera valor en la asignacion */
			}
		} else {
			throw new IllegalArgumentException("A la variable '" + nombre_variable + "' no existe.");	/* por si la variable no existiera */
		}
		return null;
	}
	
	@Override
	public Object visitLeer(HotakaLangParserParser.LeerContext input)
	{
		if(todas_variables.containsKey(input.ID().getText()))	/* verifica que la variable exista */
		{
			String tipo = todas_variables.get(input.ID().getText());	/* busca el tipo de la variable en el mapa de variables */
			if(tipo.equals("int"))
				System.out.println("\tscanf(\"%d\",&" + input.ID().getText() + ");");	/* escribe el scanf adecuado */
			else if(tipo.equals("char"))
				System.out.println("\tscanf(\"%s\",&" + input.ID().getText() + ");");
			else if(tipo.equals("float"))
				System.out.println("\tscanf(\"%f\",&" + input.ID().getText() + ");");
		} else
			throw new IllegalArgumentException("La variable '" + input.ID().getText() + "' no existe");		/* en caso de que la variable no existiera */
		return null;
	}
	
	@Override
	public Object visitMuestra(HotakaLangParserParser.MuestraContext ctx) 
	{
		if(ctx.ID().size() > 0) {
			String id, format="", args="";
			for (int i=0; i<ctx.ID().size(); i++) {
				id = ctx.ID(i).getText();
				if(todas_variables.containsKey(id)) {
					format += getTipoVarModo(todas_variables.get(id)) + " ";
					args += id + ", ";	
				}else {
					throw new IllegalArgumentException("Variable '" + id + "' no definida");
				}
			}
			System.out.println(String.format("\tprintf(\"%s\", %s);", format.substring(0, format.length() - 1), args.substring(0, args.length() - 2)));
		}else {
			String text = ctx.STR().getText();
			if(text!=null) {
				System.out.println(String.format("\tprintf(%s);", text));
			}
		}
		return null;
	}
	
	@Override
	public Object visitCondicional(HotakaLangParserParser.CondicionalContext ctx) {
		String begin_if		=	"";
		
		if(ctx.IF().getText().equals("ae")) {
			begin_if		=	"\n\tif(";
		}
		
		if(ctx.bloque_condicional()!=null) {
			if(ctx.bloque_condicional().operaciones()!=null) {
				String condicion = visitOperaciones(ctx.bloque_condicional().operaciones());
				System.out.println(begin_if+""+condicion+"){");
			}	
		}
		
		for(int i=0; i<ctx.bloque_condicional().bloque().sentencia().size(); i++) {

            if(ctx.bloque_condicional().bloque().sentencia(i).asignvar() != null) {           	
            	visitAsignvar(ctx.bloque_condicional().bloque().sentencia(i).asignvar());
            	
            }else if(ctx.bloque_condicional().bloque().sentencia(i).declaracionvar() != null) {
                visitDeclaracionvar(ctx.bloque_condicional().bloque().sentencia(i).declaracionvar());
                
            }else if(ctx.bloque_condicional().bloque().sentencia(i).condicional() != null) {
            	visitCondicional(ctx.bloque_condicional().bloque().sentencia(i).condicional());
            
            }else if(ctx.bloque_condicional().bloque().sentencia(i).leer() != null) {
                visitLeer(ctx.bloque_condicional().bloque().sentencia(i).leer());

            }else if(ctx.bloque_condicional().bloque().sentencia(i).muestra() != null) {
            	visitMuestra(ctx.bloque_condicional().bloque().sentencia(i).muestra());

            }
        }
		System.out.println("\t}");

       if(ctx.ELSE() != null) {
             if(ctx.ELSE().getText().equals("aee")) {

                String inicio_else = "\telse {";
                System.out.println(inicio_else);
                
                if(ctx.bloque_condicional_else()!=null) {
                	visitBloque_condicional_else(ctx.bloque_condicional_else());
                }
            }
        }
		return null; 
	}
	
	@Override
	public Object visitBloque_condicional_else(HotakaLangParserParser.Bloque_condicional_elseContext ctx) { 
		for(int i=0; i<ctx.bloque().sentencia().size(); i++) {

            if(ctx.bloque().sentencia(i).asignvar() != null) {           	
            	visitAsignvar(ctx.bloque().sentencia(i).asignvar());
            	
            }else if(ctx.bloque().sentencia(i).declaracionvar() != null) {
                visitDeclaracionvar(ctx.bloque().sentencia(i).declaracionvar());
                
            }else if(ctx.bloque().sentencia(i).condicional() != null) {
            	visitCondicional(ctx.bloque().sentencia(i).condicional());
            
            }else if(ctx.bloque().sentencia(i).leer() != null) {
                visitLeer(ctx.bloque().sentencia(i).leer());

            }else if(ctx.bloque().sentencia(i).muestra() != null) {
            	visitMuestra(ctx.bloque().sentencia(i).muestra());
            }
        }
		
		System.out.println("\t}");
		return null;
	}
	
	@Override 
	public String visitOperaciones(HotakaLangParserParser.OperacionesContext ctx) { 
		String[] condicion = new String[3];
		for(int i=0; i<ctx.operaciones().size();i++) {
			String var	=	ctx.operaciones().get(i).getText();
			if(todas_variables.containsKey(var)) {
				if(todas_variables.get(var).equals("int")||todas_variables.get(var).equals("float")) {
					condicion[i]=var;
				}
			}else if(esNumero(var)) {
					condicion[i]=var;
			}else {
				if(!todas_variables.containsKey(var)) {
					throw new IllegalArgumentException("Variable '"+var+"'no definida");
				}
				else {
					throw new IllegalArgumentException("Variable '"+var+"'no es numerica");
				}
			}
		}
		condicion[2] = obtenerOperadorL(ctx.operador().getText());
		
		return String.format("%s %s %s", condicion[0],condicion[2],condicion[1]); 
	}
	
	private String obtenerOperadorL(String var) {
		if(var.equals("a")) return "&&";
		if(var.equals("ranei")) return "||";
		if(var.equals("kore"))return "!";
		else return var;
		
	}
	
	private String obtenerTipoVar(String var)
	{
		if(var.equals("tau"))
			return "float";
		else if(var.equals("katoa"))
			return "int";
		else if(var.equals("engari"))
			return "int";
		else
			return "char";
	}
	
	private String getTipoVarModo(String var)
	{
		if(var.equals("tau")) return "%f";
		if(var.equals("katoa")) return "%d";
		if(var.equals("engari")) return "%d";
		else return "%s";
	}
	
	private String tipoVar(String var)
	{
		try {
			int i = Integer.parseInt(var);
			return "int";
		} catch(Exception noMeSirve) {
			try
			{
				float f = Float.parseFloat(var);
				return "float";
			} catch(Exception noMeSirvex2)
			{
				if(var.equals("pono"))
					return "true";
				else if(var.equals("rino"))
					return "false";
				else
					return "string";
			}
		}
	}
	
	private Boolean esNumero(String n)
	{
		try
		{
			int i = Integer.parseInt(n);
			return true;
		} catch(Exception noMeSirve) {
			return false;
		}
	}
	
	private Boolean esFloat(String n)
	{
		try
		{
			float i = Float.parseFloat(n);
			return true;
		} catch(Exception noMeSirve) {
			return false;
		}
	}
}