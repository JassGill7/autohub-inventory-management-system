
package com.autohub.console;

public class ConsoleMenuTest {

    public static void main(String[] args) {

        try (InputReader input = new InputReader()) {

            ConsoleMenu menu = new ConsoleMenu(input);

            int choice = menu.showMenu();

            System.out.println(
                    "\nSelected Menu Option: " + choice);

            if (choice == 5) {
                menu.displayExitMessage();
            }
        }
    }
}
