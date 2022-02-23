package com.ljz.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class TestShell {
	
	public static String exec(String command) throws Exception {
        String returnString = "";
        Process pro = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            System.err.println("Create runtime false!");
        }
        BufferedReader input = null;
        PrintWriter output = null;
        try {
            pro = runTime.exec(command);
            input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                returnString = returnString + line + "\n";
            }
            input.close();
            output.close();
            pro.destroy();
        } catch (IOException e) {
            e.printStackTrace();
            return "1111,"+e.getMessage();
        }
        return returnString;
    }

}
