package com.cs4221.commands;

import com.cs4221.database.Database;
import com.cs4221.utility.UI;

import java.io.IOException;

public class ParameterCommand implements Command {
    @Override
    public void execute(Database db) throws IOException, NoSuchMethodException {
        UI ui = new UI();
        System.out.println(ui.getParameterText());
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
