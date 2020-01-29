package ru.highload.lab7.components;

import org.zeromq.*;

public class Proxy {
    public static void main(String[] args) {
        ZContext context = new ZContext();
        ZMQ.Socket frontend = context.createSocket(SocketType.ROUTER);
        ZMQ.Socket backend = context.createSocket(SocketType.ROUTER);
        frontend.bind("tcp://localhost:5555");
        backend.bind("tcp://localhost:5556");

        System.out.println("Proxy:\n");

        while (!Thread.currentThread().isInterrupted()) {
            ZMQ.Poller items = context.createPoller(2);
            items.register(frontend, ZMQ.Poller.POLLIN);
            items.register(backend, ZMQ.Poller.POLLIN);

            if (items.pollin(0)){
                ZMsg m = ZMsg.recvMsg(backend);
                ZFrame frame = m.unwrap();

                String command = new String(frame.getData(), ZMQ.CHARSET);
                String[] commands = command.split(" ");

                if (commands.length == 2 && commands[0].equals("GET")) {
                    int k = Integer.parseInt(commands[1]);
                    // get from random
                }
                if (commands.length == 3 && commands[0].equals("PUT")) {
                    int k = Integer.parseInt(commands[1]);
                    // search in cache and send
                }

            } else if (items.pollin(1)){
                ZMsg m = ZMsg.recvMsg(backend);
                ZFrame frame = m.unwrap();

                String heartbeat = new String(frame.getData(), ZMQ.CHARSET);
                String[] vargs = heartbeat.split(" ");



            }



        }


        context.destroySocket(frontend);
        context.destroySocket(backend);
        context.destroy();
    }
}




// - Центральный прокси.
// Открывает два сокета ROUTER. От одного принимаются команды от клиентов.
// От другого - команды NOTIFY.
// С помощью команд NOTIFY ведется актуальный список подключенных частей кэша.

//Команда от клиентов содержит номер ячейки и тип (PUT или GET)
//При получении команды от клиента ищем в актуальном списке частей кэша —
// подходящие (интервал которых содержит заданный номер ячейки)
// и рассылаем всем им команду PUT.
// Команда GET отправляется случайному серверу содержащему ячейку.
