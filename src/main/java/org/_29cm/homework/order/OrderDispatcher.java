package org._29cm.homework.order;

import org._29cm.homework.order.command.Command;
import org._29cm.homework.product.Product;
import org._29cm.homework.product.ProductInitializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Scanner;

@Component
public class OrderDispatcher {
    private final List<Command> commandList;

    public OrderDispatcher(List<Command> commandList) {
        this.commandList = commandList;
    }

    @PostConstruct
    public void init() {
        while(true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("입력(o[order]: 주문, q[quit]: 종료 ) : ");
            String command = sc.next();
            System.out.println(command);

            commandList.stream()
                    .filter(R -> R.isMatch(command))
                    .findFirst().ifPresent(Command::execute);

        }
    }
}
