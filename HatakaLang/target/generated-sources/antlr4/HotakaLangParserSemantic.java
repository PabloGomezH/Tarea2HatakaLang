import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class HotakaLangParserSemantic extends HotakaLangParserBaseVisitor<Object>
{
	protected Map<String, String> todas_variables = new HashMap<String, String>();
	
	public HotakaLangParserSemantic( ) {}
	
	@Override
	public Object visitInicio(HotakaLangParserParser.InicioContext ctx)
	{ 
    	if(ctx.BEGIN().getText().equals("home"))
    	{
    		System.out.println("#include <stdio.h>");
			System.out.println("");
			System.out.println("int main(void) {");
		}
    	
    	return null;
	}
	
	@Override
	public Object visitFinale(HotakaLangParserParser.FinaleContext input)
	{
		if(input.END().getText().equals("mutunga"))
		{
			System.out.println("\treturn 0;");
			System.out.println("}");
		} else {
			System.out.println("Error: falta el final del codigo ('mutunga')");
		}
		return null;
	}
	
	@Override
	public Object visitDeclaracionvar(HotakaLangParserParser.DeclaracionvarContext input)
	{
		String tipo_variable = obtenerTipoVar(input.variable().getText());
		String nombre_variable;
		String valor_variable = "";
		String aux = "";
		//System.out.println(tipo_variable);
		
		try
		{
			nombre_variable = input.ID().getText();
		} catch (Exception e) {
			nombre_variable = input.asignvar().ID().getText();
			try
			{
				aux = tipoVar(input.asignvar().tipos().getText());
			} catch(Exception noMeSirve) {
				throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta "
						+ "asignando un valor que no es de su tipo. Si es un string, es probable que "
						+ "le falten las comillas. Por favor, verifique.");
			}
			valor_variable = input.asignvar().tipos().getText();
		}
		
		if(!todas_variables.containsKey(nombre_variable))
		{
			todas_variables.put(nombre_variable, tipo_variable);
			if(!valor_variable.isEmpty())
			{
				if(aux.equals("true"))
					valor_variable = "0";
				else if(aux.equals("false"))
					valor_variable = "1";
				
				if(tipo_variable.equals("int") && !esNumero(valor_variable))
					throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
				else if(tipo_variable.equals("char") && esFloat(valor_variable))
					throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
				else if(tipo_variable.equals("float") && !esFloat(valor_variable))
					throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
				
				System.out.println(String.format("\t%s %s = %s;", tipo_variable, nombre_variable, valor_variable));
			} else
				System.out.println(String.format("\t%s %s;", tipo_variable, nombre_variable));
		} else {
			throw new IllegalArgumentException("La variable '" + nombre_variable + "' ya existe y no hace falta definirla.");
		}
		return null;
	}
	
	public Object visitAsignvar(HotakaLangParserParser.AsignvarContext input)
	{
		String nombre_variable = input.ID().getText();
		Boolean op = false;
		if(todas_variables.containsKey(input.ID().getText()))
		{
			String valor_variable = "";
			String tipo_variable = todas_variables.get(input.ID().getText());
			try
			{
				valor_variable = input.tipos().getText();
			} catch(Exception noMeSirve) {
				op = true;
				valor_variable = input.operaciones().getText();
			}
			String operaciones = "";
			String aux = "";
			
			if(!valor_variable.isEmpty())
			{
				try
				{
					aux = tipoVar(input.tipos().getText());
				} catch(Exception noMeSirvex2) {
					aux = tipoVar(input.operaciones().getText());
				}
				if(aux.equals("true"))
					valor_variable = "0";
				else if(aux.equals("false"))
					valor_variable = "1";
				
				if(tipo_variable.equals("int") && !esNumero(valor_variable))
					throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
				else if(tipo_variable.equals("char") && esNumero(valor_variable))
					throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
				else if(tipo_variable.equals("float") && !esFloat(valor_variable))
					if(!op)
						throw new IllegalArgumentException("A la variable '" + nombre_variable + "' se le esta asignando un valor que no es de su tipo.");
				
				
				System.out.println(String.format("\t%s = %s;", nombre_variable, valor_variable));
			} else {
				throw new IllegalArgumentException("Falta el valor en la asignacion de la variable '" + nombre_variable + "'.");
			}
		} else {
			throw new IllegalArgumentException("A la variable '" + nombre_variable + "' no existe.");
		}
		return null;
	}
	
	@Override
	public Object visitLeer(HotakaLangParserParser.LeerContext input)
	{
		if(todas_variables.containsKey(input.ID().getText()))
		{
			String tipo = todas_variables.get(input.ID().getText());
			if(tipo.equals("int"))
				System.out.println("\tscanf(\"%d\",&" + input.ID().getText() + ");");
			else if(tipo.equals("char"))
				System.out.println("\tscanf(\"%s\",&" + input.ID().getText() + ");");
			else if(tipo.equals("float"))
				System.out.println("\tscanf(\"%f\",&" + input.ID().getText() + ");");
		} else
			throw new IllegalArgumentException("La variable '" + input.ID().getText() + "' no existe");
		return null;
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