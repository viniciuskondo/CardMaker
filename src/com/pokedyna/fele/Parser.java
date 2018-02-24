package com.pokedyna.fele;

public class Parser
{
	public Parser()
	{
		queryBuffer = new StringBuilder();
	}

	public void readLine(String line)
	{
		queryBuffer.append(line);

		if(queryBuffer.toString().endsWith("};"))
		{
			this.execute();
		}
	}

	private void execute()
	{
		Statement statement = new Statement(queryBuffer.toString());

		String action = statement.getAction();

		if(action.equals(Statement.actions[Statement.enumActions.CREATE.ordinal()]))
		{
			this.create(statement);
		}
		else if(action.equals(Statement.actions[Statement.enumActions.SELECT.ordinal()]))
		{
			this.select(statement);
		}
		else if(action.equals(Statement.actions[Statement.enumActions.INSERT.ordinal()]))
		{
			this.insert(statement);
		}

		queryBuffer.setLength(0);
	}

	private void create(Statement statement)
	{
		StringBuilder sql = new StringBuilder("CREATE TABLE " + statement.getTableName() +
				"( id INTEGER PRIMARY KEY AUTOINCREMENT"
		);

		for(String argument : statement.getArguments())
		{
			sql.append( ", " + argument.replaceFirst(":", " ")	);
		}

		sql.append(");");

		System.out.println(sql);
	}

	private void select(Statement statement)
	{
		StringBuilder sql = new StringBuilder("SELECT * FROM " + statement.getTableName());

		if(statement.getArguments().isEmpty())
		{
			sql.append(';');
		}
		else
		{
			sql.append("WHERE " + statement.getArguments().get(0) + ';');
		}

		System.out.println(sql);
	}

	private void insert(Statement statement)
	{
		StringBuilder sql = new StringBuilder("INSERT INTO " + statement.getTableName() + "(");

		String[] arguments = new String[statement.getArguments().size()];
		arguments = statement.getArguments().toArray(arguments);

		int[] equalsSignIndex = new int[arguments.length];

		for(int i = 0; i < arguments.length; i++)
		{
			equalsSignIndex[i] = arguments[i].indexOf('=');

			if(equalsSignIndex[i] > 0)
			{
				sql.append(arguments[i].substring(0, equalsSignIndex[i]));
				if(i != (arguments.length - 1))
				{
					sql.append(',');
				}
				else
				{
					sql.append(") VALUES (");
				}
			}
		}

		for(int i = 0; i < arguments.length; i++)
		{
			if(equalsSignIndex[i] > 0)
			{
				sql.append(arguments[i].substring(equalsSignIndex[i] + 1));
				if(i != (arguments.length - 1))
				{
					sql.append(',');
				}
			}
		}

		sql.append(");");

		System.out.println(sql);
	}

	private StringBuilder queryBuffer;
}
