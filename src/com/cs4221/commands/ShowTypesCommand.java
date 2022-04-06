package com.cs4221.commands;

import com.cs4221.database.Database;
import com.cs4221.utility.UI;

public class ShowTypesCommand implements Command {
    @Override
    public void execute(Database db) {
        UI ui = new UI();
        System.out.println(ui.getTypeText());
    }

    @Override
    public boolean isQuit() {
        return false;
    }
}
