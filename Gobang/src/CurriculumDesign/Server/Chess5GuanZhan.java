package CurriculumDesign.Server;

import CurriculumDesign.Client.ClientConfig;

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
public class Chess5GuanZhan extends Thread {
    DatagramSocket socket;
    Player player;
    String content;
    DatagramPacket pout;

    public Chess5GuanZhan(DatagramSocket socket, String content) {
        this.socket = socket;
        this.content = content;
    }

    @Override
    public void run() {
        while (true) {
            String content = Chess5Server.getPaneIntData();
//            System.out.println("��ս"+content);
            String[] row = content.split("-");//ÿ�е�����

            for (int i = 0; i < row.length; i++) {
                String[] dataP = row[i].split(",");//i��ÿ�е�����
                for (int j = 0; j < dataP.length; j++) {
                    if (Integer.parseInt(dataP[j]) != 0) {
                        HashMap<String, Player> map = ServerConfig.getGuanZhanMap();//��ȡ��ս��map
                        Iterator iter = map.entrySet().iterator();
                        while (iter.hasNext()) {//����
                            Map.Entry entry = (Map.Entry) iter.next();
                            Object key = entry.getKey();
                            try {
                                Message msg = new Message();
                                msg.setMsgType(26);
                                msg.setContent(new Point(j, i, Integer.parseInt(dataP[j]), ClientConfig.ACTION_TYPE_ADD));
                                byte[] data = Message.convertToBytes(msg);
                                pout = new DatagramPacket(data, data.length, ServerConfig.getPlayerAddr(key.toString()));
                                socket.send(pout);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
//                        System.out.println(i+"��"+j+"�е�������"+dataP[j]);
                    }

                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
