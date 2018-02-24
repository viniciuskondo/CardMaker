package com.pokedyna.fele;

import org.python.compiler.Module;
import org.python.util.PythonInterpreter;
import org.python.core.*;
import java.sql.*;
import java.util.Scanner;


public class Main
{
    public static void main(String[] args)
	{
		Scanner cin = new Scanner(System.in);
		String in;
		Parser parser = new Parser();
		boolean quit = false;
		while(!quit)
		{
			in = cin.nextLine();

			try
			{
				parser.readLine(in);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}

		/*Connection con = null;
		java.sql.Statement stmt = null;

		try
		{
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:test.db");
			System.out.println("Database opened successfully");

			stmt = con.createStatement();

			String sql = "CREATE TABLE test (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name VARCHAR(128) NOT NULL," +
					"value INTEGER NOT NULL" +
					");";
			stmt.executeUpdate(sql);
			stmt.close();
			con.close();
		}
		catch (Exception e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		PythonInterpreter interp = new PythonInterpreter();

		interp.exec("import com.pokedyna.fele.Statement;\n");


		interp.set("statement", stmt);

		interp.exec("print(statement.getTableName())");



		Statement stmt = new Statement("poke_dyna << { name:\"Vinicius\", ign:\"fele\", fc:\"0576-8181-8396\", character:\"lillie\" };");

		System.out.printf(
			"Table name: %s\n" +
			"Action: %s\n" +
			"Arguments: %s\n" +
			"Procedure: %s\n",
			stmt.getTableName(), stmt.getAction(), stmt.getArguments(), stmt.getProcedure()
		);
		*/
	}
}
