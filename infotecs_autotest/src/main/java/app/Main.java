package app;

import client.FtpClient;
import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;
import sun.net.ftp.FtpLoginException;

public class Main {
    private static final int DEFAULT_FTP_PORT = 21;

    public static void main(String[] args){
        try {
            FtpClient client = new FtpClient(args[0], args[1], args[2], DEFAULT_FTP_PORT);
            Menu menu = new Menu(client);
            menu.start();
        } catch (FtpLoginException e) {
            System.out.println("Invalid username/password");
        } catch (ConnectException e) {
            System.out.println("Connection refused");
        } catch (IOException e) {
            System.out.println("I/O error occurred");
        } catch (ParseException e) {
            System.out.println("Server has no files with students info");
        }
    }
}
