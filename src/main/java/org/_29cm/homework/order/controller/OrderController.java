package org._29cm.homework.order.controller;

import org._29cm.homework.order.command.Command;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Scanner;

@Component
public class OrderController {
    private final List<Command> commands;

    public OrderController(List<Command> commands) {
        this.commands = commands;
    }

    @PostConstruct
    public void init() {
            runInputInterface();
    }

    private void runInputInterface() {
        while(true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("입력(o[order]: 주문, q[quit]: 종료 ) : ");
            String command = sc.next();
            System.out.println(command);

            commands.stream()
                    .filter(R -> R.match(command))
                    .findFirst().ifPresent(Command::execute);

        }
    }
}
