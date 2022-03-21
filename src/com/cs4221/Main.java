package com.cs4221;

import com.cs4221.commands.Command;
import com.cs4221.database.Database;
import com.cs4221.utility.Parser;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Database db = new Database();
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
        boolean isQuit = false;
        while (!isQuit) {
            isQuit = run(db, parser, scanner);
        }
    }

    private static boolean run(Database db, Parser parser, Scanner scanner) {
            prompt();
            String input = scanner.nextLine();
            try {
                Command command = parser.parse(input);
                command.execute(db);
                return command.isQuit();
            } catch (Exception ex) {
                error();
                return false;
            }
    }

    private static void error() {
        System.out.println("Wrong Format\nAll available commands can be seen using 'help' command");
    }

    private static void prompt() {
        System.out.println("\nEnter a command, all available commands can be seen using 'help' command");
    }
}
