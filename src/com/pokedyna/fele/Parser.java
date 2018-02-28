package com.pokedyna.fele;

import org.python.util.PythonInterpreter;

import java.io.*;
import java.sql.*;

public class Parser
{
	private StringBuilder queryBuffer;
	private Connection con;


	public Parser(String databaseName)
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:test.db");
		}
		catch(Exception e)
		{
			con = null;
			System.err.println(e.getMessage());
		}

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

		String sql;

		if(action.equals(Statement.actions[Statement.enumActions.CREATE.ordinal()]))
		{
			sql = this.create(statement);
		}
		else if(action.equals(Statement.actions[Statement.enumActions.SELECT.ordinal()]))
		{
			sql = this.select(statement);
			if(statement.getProcedure().isEmpty())
			{
				try
				{
					FileReader file = new FileReader("defaultprint.py");
					BufferedReader bufferedReader = new BufferedReader(file);
					StringBuilder proc = new StringBuilder();
					for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine())
					{
						proc.append(line + "\n");
					}

					System.out.println(proc);
					statement.setProcedure(proc.toString());
				}
				catch (Exception e)
				{
					System.err.println(e.getMessage());
				}
			}
		}
		else if(action.equals(Statement.actions[Statement.enumActions.INSERT.ordinal()]))
		{
			sql = this.insert(statement);
		}
		else
		{
			throw new IllegalArgumentException("Action not suppoerted: " + statement.getAction());
		}

		if(statement.getProcedure().isEmpty())
		{
			ExecuteSql(con, sql);
		}
		else
		{
			ExecuteSql(con, sql, statement.getProcedure());
		}

		queryBuffer.setLength(0);
	}

	private static String create(Statement statement)
	{
		StringBuilder sql = new StringBuilder("CREATE TABLE " + statement.getTableName() +
				"( id INTEGER PRIMARY KEY AUTOINCREMENT"
		);

		for(String argument : statement.getArguments())
		{
			sql.append( ", " + argument.replaceFirst(":", " ")	);
		}

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
			sql.append("WHERE " + statement.getArguments().get(0) + ';');
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

	private static String ExecuteSql(Connection con, String sql)
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
			return e.getClass().getName() + ": " + e.getMessage();
		}

		return null;
	}

	private static String ExecuteSql(Connection con, String sql, String procedure)
	{
		java.sql.Statement stmt;

		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			PythonInterpreter interp = new PythonInterpreter();

			interp.set("entry", rs);
			interp.exec(procedure);
			interp.cleanup();

			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return e.getClass().getName() + ": " + e.getMessage();
		}

		return null;
	}
}
