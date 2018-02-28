package com.pokedyna.fele;

import java.util.Scanner;


public class Main
{
    public static void main(String[] args)
	{
		Scanner cin = new Scanner(System.in);
		String in;
		System.out.println("Insert database name");
		in = cin.nextLine();
		Parser parser = new Parser(in);
		boolean quit = false; // Fix
		while(!quit)
		{
			in = cin.nextLine();

			if(in.equals("/quit"))
			{
				quit = true;
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
}
