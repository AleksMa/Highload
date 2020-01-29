package ru.highload.lab7.components;

import org.zeromq.*;

import java.util.HashMap;
import java.util.Map;


public class Storage {
    public static void main(String[] args) {

        int min = Integer.parseInt(args[0]);
        int max = Integer.parseInt(args[1]);

        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
        socket.connect("tcp://localhost:5556");

        ZFrame frame = new ZFrame(String.format("%s %d %d", "R", min, max));
        frame.send(socket, 0);

        System.out.println("Storage:\n");

        ZMQ.Poller poller = context.createPoller(1);
        poller.register(socket, ZMQ.Poller.POLLIN);

        Map<Integer, Integer> storage = new HashMap<>();

        long heartbeatTime = System.currentTimeMillis() + 1000;

        while(!Thread.currentThread().isInterrupted()) {
            if (poller.poll(1000) == -1){
                break;
            }

            if (poller.pollin(0)){
                ZMsg msg = ZMsg.recvMsg(socket);
                if (msg == null) {
                    break;
                }

                if (msg.size() == 1) {
                    frame = msg.getFirst();
                    String frameData = new String(frame.getData(), ZMQ.CHARSET);

                    if (!"H".equals(frameData)) {
                        System.out.println("Invalid message from proxy");
                        msg.dump(System.out);
                    }

                    msg.destroy();
                } else if (msg.size() == 3) {
                    frame = msg.getFirst();
                    String command = new String(frame.getData(), ZMQ.CHARSET);

                    String[] commands = command.split(" ");

                    if (commands.length == 2 && commands[0].equals("GET")) {
                        int k = Integer.parseInt(commands[1]);
                        // get from random
                    }
                    if (commands.length == 3 && commands[0].equals("PUT")) {
                        int k = Integer.parseInt(commands[1]);
                        int v = Integer.parseInt(commands[2]);

                        String response = "WRONG KEY";
                        if (storage.containsKey(k)) {
                            int value = storage.get(k);
                            response = Integer.toString(value);
                        }

                        msg.getLast().reset(response);
                        msg.send(socket);
                    }
                }
            }

            if (System.currentTimeMillis() >= heartbeatTime) {
                heartbeatTime = System.currentTimeMillis() + 1000;
                frame = new ZFrame("H");
                frame.send(socket, 0);
            }
        }

        context.destroySocket(socket);
        context.destroy();

    }
}


// - Хранилище части распределенного кэша. Открывает сокет DEALER, подключается к центральному прокси.
// После подключения с определнным интервалом времени высылает сообщение NOTIFY
// в котором сообщает интервал хранимых значений. Также принимает из сокета два вида команд —
// на изменение ячейки кэша и на извлечение ячейки.

//Через параметры командной строки в момент запуска получает интервал ключей и их значения.




// NAIVE CLIENT:

/*

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
                System.out.println("SET: " + k + " " + v);
                socket.send("SET: " + k + " " + v, 0);
                String resp = socket.recvStr();
                System.out.println("resp: " + resp);
            } else {
                System.out.println("UNKNOWN COMMAND");
            }
        }

        context.destroySocket(socket);
        context.destroy();

 */