package ru.highload.lab7.components;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.REQ);
        socket.connect("tcp://localhost:5555");

        System.out.println("Client:\n");

        Scanner in = new Scanner(System.in);

        while (true) {
            String command = in.nextLine();

            if (command.equals("STOP")) {
                break;
            }

            String[] commands = command.split(" ");

            if (commands.length == 2 && commands[0].equals("GET")) {
                int k = Integer.parseInt(commands[1]);
                System.out.println("GET: " + k);
                socket.send("GET: " + k, 0);
                String resp = socket.recvStr();
                System.out.println("resp: " + resp);
            } else if (commands.length == 3 && commands[0].equals("SET")) {
                int k = Integer.parseInt(commands[1]);
                int v = Integer.parseInt(commands[2]);
                System.out.println("PUT: " + k + " " + v);
                socket.send("PUT: " + k + " " + v, 0);
                String resp = socket.recvStr();
                System.out.println("resp: " + resp);
            } else {
                System.out.println("UNKNOWN COMMAND");
            }
        }

        context.destroySocket(socket);
        context.destroy();
    }
}

// Клиент. Подключается к центральному прокси. Читает команды из консоли и отправляет их в прокси.