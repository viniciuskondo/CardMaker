package com.pokedyna.fele;

import org.python.util.PythonInterpreter;

import java.io.*;
import java.sql.*;

public class Parser
{
	private StringBuilder queryBuffer;
	private Connection con;
	private PythonInterpreter interp;

	public Parser(String databaseName)
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
		}
		catch(Exception e)
		{
			con = null;
			System.err.println(e.getMessage());
		}

		interp = new PythonInterpreter();
		queryBuffer = new StringBuilder();
	}

	public void close()
	{
		try
		{
			con.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getLocalizedMessage());
		}
	}

	public void readLine(String line)
	{
		queryBuffer.append(line).append('\n');

		// TODO: Check for other combinations of \n in "};"
		if(queryBuffer.toString().endsWith("};\n"))
		{
			this.execute();
		}
	}

	private void execute()
	{
		Statement statement = new Statement(queryBuffer.toString());

		String action = statement.getAction();

		String sql;

		if(action.equals(Statement.actions[Statement.enumActions.CREATE.ordinal()]))
		{
			sql = create(statement);
			ExecuteSql(sql);
		}
		else if(action.equals(Statement.actions[Statement.enumActions.SELECT.ordinal()]))
		{
			sql = select(statement);
			ExecuteSql(sql, statement.getProcedure());
		}
		else if(action.equals(Statement.actions[Statement.enumActions.INSERT.ordinal()]))
		{
			sql = insert(statement);
			ExecuteSql(sql);
		}
		else
		{
			throw new IllegalArgumentException("Action not suppoerted: " + statement.getAction());
		}

		queryBuffer.setLength(0);
	}

	private static String create(Statement statement)
	{
		StringBuilder sql = new StringBuilder("CREATE TABLE " + statement.getTableName() +
				"( id INTEGER PRIMARY KEY AUTOINCREMENT"
		);

		for(String argument : statement.getArguments())
			sql.append(", ").append(argument.replaceFirst(":", " "));

		sql.append(");");

		return sql.toString();
	}

	private static String select(Statement statement)
	{
		StringBuilder sql = new StringBuilder("SELECT * FROM " + statement.getTableName());

		if(statement.getArguments().isEmpty())
		{
			sql.append(';');
		}
		else
		{
			sql.append("WHERE ").append(statement.getArguments().get(0)).append(';');
		}

		return sql.toString();
	}

	private static String insert(Statement statement)
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

		return sql.toString();
	}

	private void ExecuteSql(String sql)
	{
		java.sql.Statement stmt;
		try
		{
			stmt = con.createStatement();

			stmt.executeUpdate(sql);
			stmt.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + " " + e.getMessage());
		}
	}

	private void ExecuteSql(String sql, String procedure)
	{
		java.sql.Statement stmt;

		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next())
			{
				interp.set("entry", rs);

				if (procedure.isEmpty())
				{
					interp.execfile("defaultprint.py");
				}
				else
				{
					interp.exec(procedure);
				}

				interp.cleanup();
			}

			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage() + " " + e.getCause());
		}

	}
}
