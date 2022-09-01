package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {

    public static void main(String[] args) {
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 23456);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
                System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
                Scanner userInput = new Scanner(System.in);
            switch (userInput.nextLine()) {
                case "1" -> {
                    System.out.print("Enter filename: ");
                    output.writeUTF("GET " + userInput.nextLine());
                    System.out.println("The request was sent.");
                    if ("200".equals(input.readUTF())) {
                        System.out.print("The content of the file is: ");
                        System.out.println(input.readUTF());
                    } else {
                        System.out.println("The response says that the file was not found!");
                    }
                }
                case "2" -> {
                    System.out.print("Enter filename: ");
                    String newFileName = userInput.nextLine();
                    System.out.print("Enter file content: ");
                    String newFileContent = userInput.nextLine();
                    output.writeUTF("PUT " + newFileName + " " + newFileContent);
                    System.out.println("The request was sent.");
                    if ("200".equals(input.readUTF())) {
                        System.out.println("The response says that file was created!");
                    } else {
                        System.out.println("The response says that creating the file was forbidden!");
                    }
                }
                case "3" -> {
                    System.out.print("Enter filename: ");
                    output.writeUTF("DELETE " + userInput.nextLine());
                    System.out.println("The request was sent.");
                    if ("200".equals(input.readUTF())) {
                        System.out.println("The response says that the file was successfully deleted!");
                    } else {
                        System.out.println("The response says that the file was not found!");
                    }
                }
                case "exit" -> {
                    output.writeUTF("exit");
                    System.out.println("The request was sent.");
                }
                default -> {
                    System.out.println("Command not recognized. Try again.");
                    output.writeUTF("nop");
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred while interacting with the server. Details:");
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }
        //System.out.println("Client " + Thread.currentThread().getId() + " terminated!");
    }
}
