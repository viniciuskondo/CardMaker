package com.pokedyna.fele;

import java.util.ArrayList;

public class Statement
{
	private String tableName;
	private String action;
	private ArrayList<String> arguments;
	private String procedure;
	static final String[] actions = { "=>", "<<", ">>" };

	public enum enumActions
	{
		CREATE, INSERT, SELECT
	}

	Statement(String originalQuery)
	{
		String query = originalQuery.trim();

		int actionIndex = -1;
		for(int i = 0; i < actions.length && actionIndex == -1; i++)
			actionIndex = query.indexOf(actions[i]);

		tableName = query.substring(0, actionIndex);
		action = query.substring(actionIndex, actionIndex + 2);

		int beginArguments = query.indexOf('{', actionIndex + 2);

		boolean readingLiteral = false;
		StringBuilder argument = new StringBuilder();
		int i;
		arguments = new ArrayList<>();
		for(i = beginArguments + 1; i < query.length(); i++)
		{
			char ch = query.charAt(i);
			if(ch == '"')
			{
				readingLiteral = !readingLiteral;
				argument.append(ch);
			}
			else if(readingLiteral)
			{
				argument.append(ch);
			}
			else if(!readingLiteral && ch == ',')
			{
				arguments.add(argument.toString());
				argument = new StringBuilder();
			}
			else if(!readingLiteral && ch == '}')
			{
				if(argument.length() > 1)
				{
					arguments.add(argument.toString());
				}
				break;
			}
			else if(!readingLiteral && !Character.isWhitespace(ch))
			{
				argument.append(ch);
			}
		}

		int endStatement = query.lastIndexOf('}');
		if(endStatement > 0)
		{
			procedure = query.substring(i + 1, endStatement + 1);
		}
		else
		{
			procedure = new String();
		}
	}

	String getTableName()
	{
		return tableName;
	}
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}

	ArrayList<String> getArguments()
	{
		return arguments;
	}
	public void setArguments(ArrayList<String> arguments)
	{
		this.arguments = arguments;
	}

	String getProcedure()
	{
		return procedure;
	}
	public void setProcedure(String procedure)
	{
		this.procedure = procedure;
	}
}
