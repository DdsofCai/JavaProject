package CurriculumDesign.Client03;

import CurriculumDesign.Server.Player;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class Chess5FileChoose extends Application {


    DatagramSocket client;
    DatagramPacket pout;
    Button btn_send, btn_share, btn_file, btn_fresh, btn_quit;
    TextField tf_msg;
    TextArea recv_msg;
    Player localPlayer;
    ComboBox<String> cBox;
    ListView<String> list;
    Chess5PaneThread paneThread;

    public void start(Stage primaryStage) throws SocketException {


//        client.send();
        //���
        StackPane stackPane = new StackPane();
        GridPane gridPane = new GridPane();

        //���

        btn_file = new Button("ѡ���ļ�");


        //����
        gridPane.add(btn_file, 0, 0);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        stackPane.getChildren().add(gridPane);

        stackPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(stackPane, 100, 100);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess5 File Choose");


        primaryStage.show();
        btn_file.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                int file = Chess5ListenThread.getFile();
                if (file == 1) {
                    String[] content = Chess5ListenThread.getAddr().split(",");
                    String[] add = content[0].split("/");
                    try {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("ѡ����Ҫ�鿴���ļ�");
                        File f = fileChooser.showOpenDialog(null);

                        Socket s = null;
                        s = new Socket(add[0], Integer.parseInt(content[1]));
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
                            JOptionPane.showMessageDialog(null, "  ���ͳɹ���", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                        } else if (response.equalsIgnoreCase("M_LOST")) { //����������ʧ��
                            JOptionPane.showMessageDialog(null, "  ����ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);

                        }//end if
                        //�ر���
                        br.close();
                        out.close();
                        s.close();
                        primaryStage.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}





