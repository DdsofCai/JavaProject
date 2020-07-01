package CurriculumDesign.Client;

import javafx.stage.FileChooser;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
/**
 * @Author: Ddsof_Cai
 * @Email: Ddsof_Cai@163.com
 * @version V1.0
 */
public class Chess5FileSend extends Thread {

    public void run() {

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("ѡ����Ҫ�鿴���ļ�");
            File f = fileChooser.showOpenDialog(null);

            Socket s = null;
            s = new Socket("localhost", 4567);
            //�����׽�����������Է������ݸ�������
            DataOutputStream out = new DataOutputStream(
                    new BufferedOutputStream(
                            s.getOutputStream()));

            //�����ļ�������
            DataInputStream in = new DataInputStream(
                    new BufferedInputStream(
                            new FileInputStream(f)));

            //�����׽��������������շ�����������Ϣ
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            s.getInputStream()));
            long fileLen = f.length();  //�����ļ�����
            //�����ļ����ơ��ļ�����
            out.writeUTF(f.getName());
            out.writeLong(fileLen);
            out.flush();
            //�����ļ�����
            int numRead = 0; //���ζ�ȡ���ֽ���
            int numFinished = 0; //������ֽ���
            byte[] buffer = new byte[8096];
            while (numFinished < fileLen && (numRead = in.read(buffer)) != -1) { //�ļ��ɶ�
                out.write(buffer, 0, numRead);  //����
                out.flush();
                numFinished += numRead; //������ֽ���
            }//end while
            in.close();

            String response = br.readLine();//��ȡ���ش�
            if (response.equalsIgnoreCase("M_DONE")) { //�������ɹ�����
                JOptionPane.showMessageDialog(null, "���ͳɹ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            } else if (response.equalsIgnoreCase("M_LOST")) { //����������ʧ��
                JOptionPane.showMessageDialog(null, "����ʧ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            }//end if
            //�ر���
            br.close();
            out.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
