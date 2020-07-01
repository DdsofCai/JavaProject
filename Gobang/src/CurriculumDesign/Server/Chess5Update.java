package CurriculumDesign.Server;

import CurriculumDesign.Server.Message;
import CurriculumDesign.Server.Player;
import CurriculumDesign.Server.ServerConfig;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * @Author: Ddsof_Cai
 * @Email: Ddsof_Cai@163.com
 * @version V1.0
 */
public class Chess5Update extends Thread {
    DatagramSocket socket;
    DatagramPacket pout = null;

    public Chess5Update(DatagramSocket socket, DatagramPacket pout) {
        this.socket = socket;
        this.pout = pout;
    }

    public void run() {
        while (true) {
            if (ServerConfig.getHostMap().isEmpty() == false) {
                HashMap<String, Player> map = ServerConfig.getHostMap();
                Iterator iter = map.entrySet().iterator();
                Message msg = new Message();
                while (iter.hasNext()) {

                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    msg.setMsgType(5);
                    msg.setContent("�����б�:" + ServerConfig.getHostNamesList());
                    byte[] data = Message.convertToBytes(msg);
                    pout = new DatagramPacket(data, data.length, ServerConfig.getPlayerAddr(key.toString()));
                    try {
                        socket.send(pout);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    System.out.println("���͸��µ�������: " + key.toString());
                }
            } else {
//                System.out.println("��������");

            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
