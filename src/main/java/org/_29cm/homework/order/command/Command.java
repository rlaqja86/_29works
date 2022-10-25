package org._29cm.homework.order.command;

public interface Command {
     void execute();
     boolean match(String command);
}
