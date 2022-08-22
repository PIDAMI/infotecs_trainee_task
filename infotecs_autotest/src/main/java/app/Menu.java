package app;

import client.FtpClient;
import domain.Student;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final FtpClient client;

    public Menu(FtpClient client) {
        this.client = client;
    }

    public void start() throws IOException, ParseException {
        COMMAND command = COMMAND.DEFAULT;
        Scanner scan= new Scanner(System.in);
        String argument = "";
        client.open();
        while (!command.equals(COMMAND.EXIT)) {
            System.out.println("Enter number of command (without arguments): ");
            System.out.println("1 - Print info about all students");
            System.out.println("2 - Print info about a student with given id");
            System.out.println("3 - Add a student with given name");
            System.out.println("4 - Remove a student with given id");
            System.out.println("5 - Exit application\n");

            command = COMMAND.getValue(Integer.parseInt(scan.nextLine()));
            if (command.equals(COMMAND.EXIT))
                break;
            if (!command.equals(COMMAND.GET_ALL)) {
                System.out.println("Enter argument: ");
                argument = scan.nextLine();
            }
            executeCommand(command, argument);
        }
        client.close();
    }

    public void executeCommand(COMMAND command, String argument) {
        switch (command) {
            case GET_BY_ID:
                String res = client.getById(Long.parseLong(argument));
                if (res == null)
                    System.out.println("No student with such id");
                else
                    System.out.println(res);
                break;
            case ADD_BY_NAME:
                client.addStudent(argument);
                break;
            case GET_ALL:
                List<Student> students = client.getAll();
                if (students.isEmpty())
                    System.out.println("No students");
                else
                    students.forEach(System.out::println);
                break;
            case REMOVE_BY_ID:
                if (client.removeStudentById(Long.parseLong(argument)) == null)
                    System.out.println("No student with such id");
                break;
            case EXIT:
                break;
            case DEFAULT:
                throw new RuntimeException("invalid command");
        }
    }

    enum COMMAND {
        GET_ALL(1),
        GET_BY_ID(2),
        ADD_BY_NAME(3),
        REMOVE_BY_ID(4),
        EXIT(5),
        DEFAULT(-1);

        final int number;
        COMMAND(int number) {this.number = number;}
        public static COMMAND getValue(int number)
        {
            for (COMMAND command : COMMAND.values()) {
                if (command.number == number)
                    return command;
            }
            return DEFAULT;
        }
    }
}
