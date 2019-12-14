package com.luugiathuy.apps;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application
{

	public static void main(String[] args)
	{
		new SpringApplicationBuilder(Application.class).headless(false).run(args);
	}
}
