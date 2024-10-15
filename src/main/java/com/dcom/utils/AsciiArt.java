package com.dcom.utils;

public class AsciiArt {

    public static void printDHEL() {
        String[] lines = new String[8];

        lines[0] = "===========================================";
        lines[1] = "  ____       _    _      _____      _      ";
        lines[2] = " |  _ \\     | |  | |    | ____|    | |     ";
        lines[3] = " | | | |    | |__| |    |  _|      | |     ";
        lines[4] = " | |_| |    |  __  |    | |___     | |___  ";
        lines[5] = " |____/     |_|  |_|    |_____|    |_____| ";
        lines[6] = "                                           ";
        lines[7] = "===========================================";
 
        for (String line : lines) {
            System.out.println(line);
        }
    }


    public static void printDivider(){
        System.out.println("...........................................");
        System.out.println("...........................................");
        System.out.println("...........................................");
        System.out.println("...........................................");
        System.out.println("...........................................");
        System.out.println("...........................................");
        

    }
}

