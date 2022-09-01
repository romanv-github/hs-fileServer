package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class Server {

    public static void main(String[] args) {
        String fileServerRootFolder = "C:\\Users\\RomanVatagin\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data\\";
        System.out.println("Server started!");
        try (ServerSocket serverSocket = new ServerSocket(23456)) {
            while (true) {
                try (
                     Socket acceptSocket = serverSocket.accept();
                     DataInputStream input = new DataInputStream(acceptSocket.getInputStream());
                     DataOutputStream output = new DataOutputStream(acceptSocket.getOutputStream())
                ) {

                    String[] command = input.readUTF().split(" ");
                    switch (command[0].toUpperCase()) {
                        case "GET" -> {
                            try {
                                File getFile = new File(fileServerRootFolder + command[1]);
                                if (getFile.exists()) {
                                    output.writeUTF("200");
                                    try (Scanner fileReader = new Scanner(getFile)) {
                                        while (fileReader.hasNext()) {
                                            output.writeUTF(fileReader.nextLine());
                                        }
                                    }
                                } else {
                                    output.writeUTF("404");
                                }
                            } catch (Exception e) {
                                output.writeUTF("500");
                                System.out.println("Failed to get file \"" + command[1] + "\". Details:");
                                System.out.println(e.getClass());
                                System.out.println(e.getMessage());
                            }
                        }
                        case "PUT" -> {
                            try {
                                File newFile = new File(fileServerRootFolder + command[1]);
                                if (newFile.exists()) {
                                    output.writeUTF("403");
                                } else {
                                    if (newFile.createNewFile()) {
                                        try (FileWriter theWriter = new FileWriter(newFile)) {
                                            StringBuilder sb = new StringBuilder();
                                            for (int i = 2; i < command.length; i++) {
                                                sb.append(command[i]);
                                                sb.append(" ");
                                            }
                                            theWriter.write(sb.toString());
                                            output.writeUTF("200");
                                        }
                                    } else {
                                        output.writeUTF("500");
                                    }
                                }
                            } catch (Exception e) {
                                output.writeUTF("500");
                                System.out.println("Failed to create file \"" + command[1] + "\". Details:");
                                System.out.println(e.getClass());
                                System.out.println(e.getMessage());
                            }
                        }
                        case "DELETE" -> {
                            try {
                                File delFile = new File(fileServerRootFolder + command[1]);
                                System.out.println("Attempting to delete the file:");
                                System.out.println(fileServerRootFolder + command[1]);
                                if (delFile.exists()) {
                                    if (delFile.delete()) {
                                        output.writeUTF("200");
                                        System.out.println("FILE DELETED!");
                                    } else {
                                        output.writeUTF("500");
                                        System.out.println("FAILED TO DELETE FILE!");
                                    }
                                } else {
                                    output.writeUTF("404");
                                    System.out.println("FILE NOT FOUND!");
                                }
                            } catch (Exception e) {
                                output.writeUTF("500");
                                System.out.println("Failed to delete file \"" + command[1] + "\". Details:");
                                System.out.println(e.getClass());
                                System.out.println(e.getMessage());
                            }
                        }
                        case "EXIT" -> System.exit(0);
                        default -> output.writeUTF("400");
                    }
                } catch (Exception e) {
                    System.out.println("Error occurred processing Client's commands. Details:");
                    System.out.println(e.getClass());
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to start the server. Details:");
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }
        //System.out.println("Server " + Thread.currentThread().getId() + " terminated!");
    }
}