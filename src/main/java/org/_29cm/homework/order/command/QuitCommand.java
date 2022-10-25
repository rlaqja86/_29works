package org._29cm.homework.order.command;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class QuitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("this is quit command");
        System.exit(1);
    }

    @Override
    public boolean match(String command) {
        return StringUtils.equalsIgnoreCase(command, "q");
    }
}
