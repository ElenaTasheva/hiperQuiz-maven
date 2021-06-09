package hiperQuiz.commands;

import java.util.Scanner;

public class ReadingCommand {

    private Scanner scanner;

    public ReadingCommand(Scanner scanner){
        this.scanner = scanner;
    }

    public ReadingCommand() {
        scanner = new Scanner(System.in);
    }

    public String readLIne(){
        return scanner.nextLine();
    }
}
