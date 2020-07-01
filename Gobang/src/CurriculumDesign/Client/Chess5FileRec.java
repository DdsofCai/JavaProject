package CurriculumDesign.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
/**
 * @Author: Ddsof_Cai
 * @Email: Ddsof_Cai@163.com
 * @version V1.0
 */
public class Chess5FileRec extends Thread {
    ServerSocket socket=new ServerSocket(0);

    public Chess5FileRec() throws IOException {
    }


    public void run(){
        try {
            while (true){
                Socket s = socket.accept();
                System.out.println(s.toString());

                //�����׽��������������տͻ�������
                DataInputStream in=new DataInputStream(
                        new BufferedInputStream(
                                s.getInputStream()));
                //�����׽�����������Է������ݸ��ͻ���
                BufferedWriter out=new BufferedWriter(
                        new OutputStreamWriter(
                                s.getOutputStream()));

                //�����ļ������ļ�����
                String filename=in.readUTF(); //�ļ���
                int fileLen=(int)in.readLong(); //�ļ�����
                //�����ļ������
                File f =new File(new Date().getTime() + "." + filename);
                System.out.println(new Date().getTime() + "." + filename);
                DataOutputStream dos =new DataOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(f)));

                byte buffer[] = new byte[8096];
                int numRead = 0;
                int numFinished = 0;
                while (numFinished<fileLen && (numRead = in.read(buffer))!=-1){
                    dos.write(buffer,0, numRead);
                    dos.flush();
                    numFinished += numRead;
                }
                dos.close();

                if (numFinished>=fileLen) {//�ļ�������ɣ�
                    out.write("M_DONE"); //���ͳɹ���Ϣ
                    System.out.println(filename +"  ���ճɹ���");
                }else {
                    out.write("M_LOST"); //����ʧ����Ϣ
                    System.out.println(filename +"  ����ʧ�ܣ�") ;
                }//end if
                out.newLine();
                out.flush();
                out.close();
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String getServerSocket(){
        String addr=socket.getInetAddress().toString();
        int port=socket.getLocalPort();

        return addr+","+port;
    }





}
