package CurriculumDesign.Server;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: Ddsof_Cai
 * @Email: Ddsof_Cai@163.com
 * @version V1.0
 */
public class Chess5Server {
    private static int[][] paneIntData = new int[10][10];
    ArrayList<String> paneData = new ArrayList<String>();
    ArrayList<String> pre = new ArrayList<String>();
    ArrayList<int[][]> currentData = new ArrayList<int[][]>();
    ArrayList<Player> guanZhanPlayer = new ArrayList<Player>();
    DatagramSocket socket = new DatagramSocket(ServerConfig.SERVER_ADDR);
    DatagramPacket pout = null;

    public static void main(String[] args) throws SocketException {
        // TODO Auto-generated method stub
        Chess5Server cs = new Chess5Server();
        cs.service();
    }

    public Chess5Server() throws SocketException {
        init();
    }

    public int[][] getCurrentPane() {
        int[][] c = currentData.get(currentData.size() - 1);
        return c;
    }

    public void init() {
        Chess5Update chess5Update = new Chess5Update(socket, pout);
        chess5Update.start();
    }

    public static String getPaneIntData() {
        return printArr1(paneIntData);
    }

    public void setInitPaneIntData() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                paneIntData[i][j] = 0;
            }
        }
    }

    public static String printArr1(int[][] arr) {
        String str = "";
        for (int x = 0; x < arr.length; x++) {
            for (int y = 0; y < arr[x].length; y++) {
                str = str + arr[x][y] + ",";
            }
            str += "-";
        }
        return str;
    }


    public void service() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                paneIntData[i][j] = 0;
            }
        }
//        (DatagramSocket socket = new DatagramSocket(ServerConfig.SERVER_ADDR);//172.16.134.2:8000
//        )
        try {
            while (true) {
                try {
                    DataBase dataBase = new DataBase();
                    dataBase.connectDB();

                    DatagramPacket pin = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(pin);
                    Message msg = (Message) Message.convertToObj(pin.getData(), 0, pin.getLength());
                    Message rspMsg = new Message();

                    /**
                     * 1.����ǡ�JOIN_GAME������ʾ���û�Ҫ������Ϸ�������ǳ��Ƿ��Ѿ����ڣ�������ڣ���ܾ�������ǰ�������������ظ��¼�����ң�
                     * 2.����ǡ�JOIN_HOST������ʾ�����Ҫ������̨�������Ƿ��Ѿ����룬����ǣ���ܾ�������ǰ�������������ظ��¼�����̨����ң�
                     * 3.����ǡ�CHALLENGE������ʾ����ҷ�����һ����ս���������󣬽�������ת������Ӧ��������
                     * 4.����ǡ�CHALLENGE_RSP������ʾ�б���ս�������Ļظ�����ս���󣬽��ûظ�ת����������ս����ң�
                     * 5.����ǡ�PLAY������ʾ���ڱ����е�һ���������ӣ�����һ��������߶Է����ӵ���Ϣ��������ת������Ϣ����һ����
                     */

                    if (msg.getMsgType() == MessageType.JOIN_GAME) {

                        //�ж���������Ƿ����

                        if (ServerConfig.containPlayer(msg.getFromPlayer().getName())) {
                            //����,�������Ϸʧ��
                            rspMsg.setStatus(0);
                            rspMsg.setMsgType(1);
                            byte[] data = Message.convertToBytes(rspMsg);
                            pout = new DatagramPacket(data, data.length, msg.getFromPlayer().getAddress());
                            socket.send(pout);
                        } else {
                            //������,�������Ϸ�ɹ�
                            //��������Ӹ���ҵ���Ϣ
                            Player player = msg.getFromPlayer();
                            ServerConfig.addPlayer(player.getName(), player);
                            rspMsg.setStatus(1);
                            rspMsg.setMsgType(1);
                            rspMsg.setToPlayer(player);
                            byte[] data = Message.convertToBytes(rspMsg);
                            pout = new DatagramPacket(data, data.length, player.getAddress());
                            socket.send(pout);
                        }

                    } else if (msg.getMsgType() == MessageType.JOIN_HOST) {
                        Player player = msg.getFromPlayer();
                        rspMsg.setMsgType(2);
                        rspMsg.setToPlayer(player);
                        if (ServerConfig.containHost(player.getName())) {
                            rspMsg.setStatus(0);
                        } else {
                            rspMsg.setStatus(1);
                            ServerConfig.addHost(player.getName(), player);
                        }
                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, player.getAddress());
                        socket.send(pout);
                    } else if (msg.getMsgType() == MessageType.UPDATE_HOST) {
                        Player player = msg.getFromPlayer();
                        rspMsg.setMsgType(5);
                        rspMsg.setContent("�����б�:" + ServerConfig.getHostNamesList());
                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, player.getAddress());
                        socket.send(pout);
                    } else if (msg.getMsgType() == MessageType.CHALLENGE) {

                        //������ս����� challenge ����ս�������Ϣ  challenged
                        Player challenge = msg.getFromPlayer();
                        Player challenged = ServerConfig.getPlayer(msg.getToPlayer().getName());//�������ļ��л�ȡ��Ϣ
                        if (ServerConfig.containPlaying(challenged.getName())) {
                            System.out.println("�����������Ϸ��");
                            Message msgC = new Message();
                            msgC.setFromPlayer(challenge);
                            msgC.setToPlayer(challenged);
                            msgC.setStatus(-1);
                            msgC.setMsgType(4);
                            byte[] data = Message.convertToBytes(msgC);
                            pout = new DatagramPacket(data, data.length, challenge.getAddress());
                            socket.send(pout);
                        } else {
                            System.out.println("����Ҳ�����Ϸ��");
                            System.out.println(challenge.getName() + "--��ս->" + challenged.getName());
                            Message msgC = new Message();
                            msgC.setFromPlayer(challenge);
                            msgC.setToPlayer(challenged);
                            msgC.setMsgType(3);
                            byte[] data = Message.convertToBytes(msgC);
                            pout = new DatagramPacket(data, data.length, challenged.getAddress());
                            socket.send(pout);
                        }
                    } else if (msg.getMsgType() == MessageType.CHALLENGE_RSP) {
                        int result = msg.getStatus();
                        Player challenged = msg.getFromPlayer();
                        Player challenge = msg.getToPlayer();
                        rspMsg.setFromPlayer(challenged);
                        rspMsg.setToPlayer(challenge);
                        rspMsg.setMsgType(4);
                        //������ս
                        if (result == 1) {
                            System.out.println(challenged.getName() + "--������ս->" + challenge.getName());
                            rspMsg.setStatus(1);
                            ServerConfig.addPlaying(challenge.getName(), challenge);
                            ServerConfig.addPlaying(challenged.getName(), challenged);
                        } else {
                            System.out.println(challenged.getName() + "--��������ս->" + challenge.getName());

                            rspMsg.setStatus(0);
                        }

                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, challenge.getAddress());
                        socket.send(pout);

                    } else if (msg.getMsgType() == MessageType.PLAY) {

                        String panData = msg.getContent().toString().replaceAll("\\[", "").replaceAll("\\]", "");
                        String[] content = panData.split(",");
                        paneIntData[Integer.parseInt(content[1])][Integer.parseInt(content[0])] = Integer.parseInt(content[2]);
                        currentData.add(paneIntData);
                        System.out.println(printArr1(paneIntData));
                        rspMsg.setMsgType(6);
                        rspMsg.setFromPlayer(msg.getFromPlayer());
                        rspMsg.setToPlayer(msg.getToPlayer());
                        rspMsg.setContent(msg.getContent());
                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, msg.getToPlayer().getAddress());
                        socket.send(pout);
                        pre.add(printArr1(getCurrentPane()));//pre.get(pre.size()-2)��һ��

                    } else if (msg.getMsgType() == MessageType.SURRENDER) {
                        rspMsg.setMsgType(10);
                        rspMsg.setToPlayer(msg.getToPlayer());
                        rspMsg.setFromPlayer(msg.getFromPlayer());

                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, msg.getToPlayer().getAddress());
                        socket.send(pout);
                        dataBase.delExc(msg.getFromPlayer().getName());
                        dataBase.addExc(msg.getToPlayer().getName());
                        ServerConfig.delPlaying(msg.getFromPlayer().getName());
                        ServerConfig.delPlaying(msg.getToPlayer().getName());
                    } else if (msg.getMsgType() == MessageType.HUIQI) {

                        rspMsg.setMsgType(8);
                        rspMsg.setContent(pre.get(pre.size() - 2));
                        rspMsg.setToPlayer(msg.getToPlayer());
                        rspMsg.setFromPlayer(msg.getFromPlayer());
                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, msg.getToPlayer().getAddress());
                        socket.send(pout);
                    } else if (msg.getMsgType() == MessageType.HUIQI_RSP) {
                        int result = msg.getStatus();
                        rspMsg.setMsgType(9);
                        rspMsg.setToPlayer(msg.getToPlayer());
                        rspMsg.setFromPlayer(msg.getFromPlayer());
                        if (result == 1) {
                            rspMsg.setStatus(1);
                            rspMsg.setContent(paneData.get(paneData.size() - 1));
                            dataBase.addExc(msg.getFromPlayer().getName());


                        } else {
                            rspMsg.setStatus(0);
                        }
                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, msg.getToPlayer().getAddress());
                        socket.send(pout);

                    } else if (msg.getMsgType() == MessageType.DOGFALL) {
                        rspMsg.setFromPlayer(msg.getFromPlayer());
                        rspMsg.setToPlayer(msg.getToPlayer());
                        rspMsg.setMsgType(11);
                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, msg.getToPlayer().getAddress());
                        socket.send(pout);
                    } else if (msg.getMsgType() == MessageType.DOGFALL_RSP) {
                        int result = msg.getStatus();
                        rspMsg.setToPlayer(msg.getToPlayer());
                        rspMsg.setFromPlayer(msg.getFromPlayer());
                        if (result == 1) {
                            rspMsg.setStatus(1);
                            ServerConfig.delPlaying(msg.getFromPlayer().getName());
                            ServerConfig.delPlaying(msg.getToPlayer().getName());
                        } else {
                            rspMsg.setStatus(0);
                        }
                        rspMsg.setMsgType(12);
                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, msg.getToPlayer().getAddress());
                        socket.send(pout);
                    } else if (msg.getMsgType() == MessageType.JOIN_COM) {
                        String name = msg.getFromPlayer().getName();
                        ServerConfig.addComPlayer(msg.getFromPlayer().getName(), msg.getFromPlayer());
//                        �㲥
                        HashMap<String, Player> map = ServerConfig.getPlayerComMap();
                        Iterator iter = map.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            Object key = entry.getKey();
                            rspMsg.setMsgType(15);
                            rspMsg.setContent(name);
                            byte[] data = Message.convertToBytes(rspMsg);
                            pout = new DatagramPacket(data, data.length, ServerConfig.getPlayerAddr(key.toString()));
                            socket.send(pout);
                        }
                    } else if (msg.getMsgType() == MessageType.UPDATE_COM) {
                        Player player = msg.getFromPlayer();
                        rspMsg.setMsgType(20);
                        rspMsg.setContent("����Ƶ��:" + ServerConfig.getComNamesList());
                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, player.getAddress());
                        socket.send(pout);
                    } else if (msg.getMsgType() == MessageType.PUBLIC_CHAT) {
                        String content = msg.getContent().toString();
                        //                        �㲥
                        HashMap<String, Player> map = ServerConfig.getPlayerComMap();//h��ȡ�����ҵ���Ա
                        Iterator iter = map.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            Object key = entry.getKey();
                            rspMsg.setMsgType(16);
                            rspMsg.setFromPlayer(msg.getFromPlayer());
                            rspMsg.setContent(content);
                            byte[] data = Message.convertToBytes(rspMsg);
                            pout = new DatagramPacket(data, data.length, ServerConfig.getPlayerAddr(key.toString()));
                            socket.send(pout);
                        }
                        System.out.println("�յ�������Ϣ" + msg.getContent().toString());
                    } else if (msg.getMsgType() == MessageType.PRIVATE_CHAT) {
                        Player toPlayer = ServerConfig.getComPlayer(msg.getToPlayer().getName());
                        rspMsg.setFromPlayer(msg.getFromPlayer());
                        rspMsg.setMsgType(17);
                        rspMsg.setContent(msg.getContent());
                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, toPlayer.getAddress());
                        socket.send(pout);
                        System.out.println("�յ�˽����Ϣ" + msg.getContent().toString() + "������" + msg.getToPlayer().getName());
                    } else if (msg.getMsgType() == MessageType.QUIT_COM) {
                        String name = msg.getFromPlayer().getName();
                        ServerConfig.delComPlayer(msg.getFromPlayer().getName());
                        HashMap<String, Player> map = ServerConfig.getPlayerComMap();
                        Iterator iter = map.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            Object key = entry.getKey();
                            rspMsg.setMsgType(21);
                            rspMsg.setFromPlayer(msg.getFromPlayer());
                            byte[] data = Message.convertToBytes(rspMsg);
                            pout = new DatagramPacket(data, data.length, ServerConfig.getPlayerAddr(key.toString()));
                            socket.send(pout);
                        }
                    } else if (msg.getMsgType() == MessageType.SUCCESS) {
                        System.out.println("ʤ����:" + msg.getFromPlayer());
                        System.out.println("ʧ����:" + msg.getToPlayer());
                        dataBase.addExc(msg.getFromPlayer().getName());
                        dataBase.delExc(msg.getToPlayer().getName());
                        ServerConfig.delPlaying(msg.getFromPlayer().getName());
                        ServerConfig.delPlaying(msg.getToPlayer().getName());
                        setInitPaneIntData();
                        System.out.println("�������� : " + printArr1(paneIntData));
                    } else if (msg.getMsgType() == MessageType.FAIL) {
                        System.out.println("ʧ��:" + msg.getFromPlayer());
                        System.out.println("ʤ����:" + msg.getToPlayer());
                        dataBase.delExc(msg.getFromPlayer().getName());
                        dataBase.addExc(msg.getToPlayer().getName());
                        ServerConfig.delPlaying(msg.getFromPlayer().getName());
                        ServerConfig.delPlaying(msg.getToPlayer().getName());
                        setInitPaneIntData();
                        System.out.println("�������� : " + printArr1(paneIntData));

                    } else if (msg.getMsgType() == MessageType.QUIT_HOST) {
                        ServerConfig.delHostPlayer(msg.getFromPlayer().getName());
                    } else if (msg.getMsgType() == MessageType.QUIT_GAME) {
                        ServerConfig.delAllPlayer(msg.getFromPlayer().getName());
                        System.out.println("ɾ�������������Ϣ");
                    } else if (msg.getMsgType() == MessageType.SENDFILES) {
                        System.out.println(msg.getFromPlayer().getName() + "�����ļ�����" + msg.getToPlayer().getName());
                        Player toPlayer = ServerConfig.getPlayer(msg.getToPlayer().getName());
                        rspMsg.setFromPlayer(msg.getFromPlayer());
                        rspMsg.setToPlayer(toPlayer);
                        rspMsg.setMsgType(24);
                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, toPlayer.getAddress());
                        socket.send(pout);
                        System.out.println("���ͳɹ�");
                    } else if (msg.getMsgType() == MessageType.SENDFILES_RSP) {
                        System.out.println(msg.getFromPlayer().getName() + "��Ӧ�ļ�����ɹ�" + msg.getToPlayer().getName());
                        rspMsg.setMsgType(25);
                        rspMsg.setToPlayer(msg.getToPlayer());
                        rspMsg.setFromPlayer(msg.getFromPlayer());
                        rspMsg.setContent(msg.getContent());
                        byte[] data = Message.convertToBytes(rspMsg);
                        pout = new DatagramPacket(data, data.length, msg.getToPlayer().getAddress());
                        socket.send(pout);
                    } else if (msg.getMsgType() == MessageType.GUIZHAN) {
                        ServerConfig.addGuanZhanPlayer(msg.getFromPlayer().getName(), msg.getFromPlayer());//��ӹ�ս���
                        Chess5GuanZhan chess5GuanZhan = new Chess5GuanZhan(socket, printArr1(paneIntData));//������ս�߳�
                        chess5GuanZhan.start();
//
                    } else if (msg.getMsgType() == MessageType.QUI_GUANZHAN) {
                        guanZhanPlayer.remove(ServerConfig.getPlayer(msg.getFromPlayer().getName()));
                    }

                } catch (Exception e) {
                    System.out.println("��������ʧ��");
                }
            }

        } catch (Exception e) {
            System.out.println("��������ʧ��");
        }

    }


}
