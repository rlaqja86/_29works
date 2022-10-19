package org._29cm.homework.order.command;

public interface Command {
     void execute();
     boolean isMatch(String command);
}
