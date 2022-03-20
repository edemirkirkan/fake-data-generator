package com.cs4221;

import com.cs4221.commands.Command;
import com.cs4221.database.Database;
import com.cs4221.utility.Parser;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        boolean isQuit = false;
        Scanner scanner = new Scanner(System.in);
        Database db = new Database();
        Parser parser = new Parser();
        while (!isQuit) {
            prompt();
            String input = scanner.nextLine();
            try {
                Command command = parser.parse(input);
                command.execute(db);
                isQuit = command.isQuit();
            } catch (Exception ex) {
                error("Wrong Format\nAll available commands can be seen using 'help' command");
            }
        }
        db.printEntities();
        scanner.close();
    }

    private static void error(String msg) {
        System.out.println(msg);
    }

    private static void prompt() {
        System.out.println("Enter a command, all available commands can be seen using 'help' command");
    }
}
