package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main
{

    private static final String Exit = "[ ]*[.](E|e)(X|x)(I|i)(T|t)*[ ]*";
    private static final Pattern patternExit = Pattern.compile(Exit);

    public static void main(String[] args)
    {

        List<Integer> listOfInteger = new ArrayList<>();
        listOfInteger.add(5);
        listOfInteger.add(6);
        listOfInteger.add(7);
        listOfInteger.add(8);

//        SqlParser.commandHandler("create people (id, name indexed, surname)");
//
//        SqlParser.commandHandler("insert people (\"0\", \"denis\", \"vasylyev\")");
//        SqlParser.commandHandler("insert into people (\"1\", \"dima\", \"yurchenko\")");
//        SqlParser.commandHandler("insert into people (\"2\", \"sasha\", \"maksimenko\")");
//        SqlParser.commandHandler("insert into people (\"3\", \"dima\", \"yurchenko\")");
////        SqlParser.commandHandler("insert into people (\"4\", \"vlad\", \"elisevich\")");
////        SqlParser.commandHandler("insert into people (\"5\", \"maksim\", \"chaliy\")");
////        SqlParser.commandHandler("insert into people (\"6\", \"zhenya\", \"griaznova\")");
////
//        SqlParser.commandHandler("select * from people");
////        SqlParser.commandHandler("select name, surname from people where (id >= 2)");
////        SqlParser.commandHandler("select * from people where (id < 4)");
//        SqlParser.commandHandler("select * from people where (id < 2)");
////        SqlParser.commandHandler("select surname from people where (name = \"dima\")");
////        SqlParser.commandHandler("select * from people where (name != \"denis\")");
////
//        SqlParser.commandHandler("delete from people where (name = \"dima\")");
//        SqlParser.commandHandler("select * from people");
////        SqlParser.commandHandler("delete from people");
////        SqlParser.commandHandler("select * from people");

        System.out.println("Hello! Enter your command.If you want to finish enter .EXIT");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(" > ");
            StringBuilder command = new StringBuilder();
            while (!command.toString().contains(";"))
            {
                String name = scanner.nextLine();
                command.append(name);
                String fullCommand = command.substring(0, command.toString().indexOf(";"));
                if (patternExit.matcher(fullCommand).matches())
                {
                    System.out.println("Current command is : " + command);
                    return;
                } else
                    Parser.commandHandler(fullCommand);
            }
        }
    }
}
