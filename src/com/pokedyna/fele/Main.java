package com.pokedyna.fele;

import java.util.Scanner;

public class Main
{
	public static void start()
	{
		Scanner cin = new Scanner(System.in);
		String in;
		System.out.println("Insert database name");
		in = cin.nextLine();
		Parser parser = new Parser(in);
		boolean quit = false; // Fix
		System.out.println("Welcome to CardMaker - 0.1b\n");
		while(!quit)
		{
			in = cin.nextLine();

			if(in.equals("/quit"))
			{
				quit = true;
			}
			else if(in.matches("/file(.+)"))
			{
				String filename = in.substring(in.indexOf('(') + 1, in.indexOf(')'));

				parser.readFile(filename);
			}

			try
			{
				parser.readLine(in);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}

		parser.close();
	}

    public static void main(String[] args)
	{
        start();
	}
}
