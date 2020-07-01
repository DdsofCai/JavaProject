package CurriculumDesign.Client;


import CurriculumDesign.Server.Message;
import CurriculumDesign.Server.Point;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @Author: Ddsof_Cai
 * @Email: Ddsof_Cai@163.com
 * @version V1.0
 */
public class Chess5Pane extends Pane {

    private int x_0 = 30, y_0 = 30; //�������Ͻǵ�λ��
    private int N = 9; //N*N������
    private int k = 60; //������ߣ���
    private int r = 20; //���ӣ�Բ���뾶
    private int color = ClientConfig.COLOR_BLACK;//����Ϊ����: 1��ʾ���ӣ�2��ʾ���ӣ�0��ʾû������
    private int[][] data = new int[N + 1][N + 1];
    public Competition competition;

    public Chess5Pane() {

        setMaxSize(N * k + x_0 * 2, N * k + y_0 * 2);
        setMinSize(N * k + x_0 * 2, N * k + y_0 * 2);

        //-fx-border-color�����ñ߿���ɫ��-fx-border-width���ñ߿��ȣ�-fx-background-color�����ñ�����ɫ
        setStyle("-fx-border-color:#EED8AE;-fx-background-color:#EED8AE;-fx-border-width:5");

        setOnMouseClicked(this::mouseClickHandler);

        //������
        drawBoard();
    }

    public Competition getCompetition() {
        return this.competition;
    }

    /**
     * ��������
     */
    public void drawBoard() {
        //��������
        for (int i = 0; i <= N; i++) {
            getChildren().add(new Line(x_0, y_0 + i * k, x_0 + N * k, y_0 + i * k));
        }
        //��������
        for (int i = 0; i <= N; i++) {
            getChildren().add(new Line(x_0 + i * k, y_0, x_0 + i * k, y_0 + N * k));
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setCompetition(Competition com) { this.competition = com; }

    public void setData(int[][] data){this.data=data;}
    public int[][] getData() {
        return data;
    }

//    public void setData(int[][] data) {
//        this.data = data;
//    }

    /**
     * ��ָ��������������ָ����ɫ�����ӣ�Բ��
     *
     * @param row    ��������
     * @param column ��������
     * @param color  ������ɫ��1Ϊ���ӣ�2Ϊ����
     */
    public void drawChess(int row, int column, int color) {
        //���ݸ���������ֵ�����ʵ�ʵ������
        int x_c = x_0 + column * k;
        int y_c = y_0 + row * k;
        Circle circle = new Circle(x_c, y_c, r, color == 1 ? Color.BLACK : Color.WHITE);
        getChildren().add(circle);
        data[row][column] = color;

    }

    public void removeChess(int row, int column, int color) {
        //���ݸ���������ֵ�����ʵ�ʵ������
        int x_c = x_0 + column * k;
        int y_c = y_0 + row * k;
//		Circle circle = new Circle(x_c,y_c,r,color==1?Color.BLACK:Color.WHITE);
        Circle circle = new Circle(x_c, y_c, r, color == 1 ? Color.BLACK : Color.WHITE);
//		getChildren().removeIf(a ->(Color.WHITE));

        getChildren().remove(circle);
        data[row][column] = 0;


    }
    public static String printArr1(int[][] arr) {
        String str = "";
        for (int x = 0; x < arr.length; x++) {
            for (int y = 0; y < arr[x].length; y++) {
                str = str + arr[x][y];
            }
        }
        return str;
    }
    /**
     * ��Ӧ����¼�������Ӧλ�õ�������������ڶ�Ӧλ���������
     *
     * @param e
     */
    public void mouseClickHandler(MouseEvent e) {

        if (e.getClickCount() == 2) {//���˫��

            int column = (int) Math.round((e.getX() - x_0) / (k));
            int row = (int) Math.round((e.getY() - y_0) / (k));

            if (data[row][column] == 0) {

                drawChess(row, column, color);    //������



                try (DatagramSocket socket = new DatagramSocket(0)) {
//                    socket.setSoTimeout(5000);
                    Message msg = new Message();
                    msg.setFromPlayer(competition.getLocalPlayer());
                    msg.setToPlayer(competition.getRemotePlayer());
                    msg.setMsgType(6);
                    msg.setContent(new Point(column, row, color, ClientConfig.ACTION_TYPE_ADD));
                    byte[] msgBytes = Message.convertToBytes(msg);
                    DatagramPacket pout = new DatagramPacket(msgBytes, msgBytes.length,
                            ClientConfig.SERVER_ADDR);
                    socket.send(pout);
                    setMouseTransparent(true);

                } catch (Exception ex) {
                    // TODO: handle exception
                    ex.printStackTrace();
                }
                //color= color==1?2:1;//�任������ɫ

                if (Judgement.judge(data) == color) {
                        competition.setResult(color);
                        setMouseTransparent(true);
                }
            } else {
                Chess5Utils.showAlert("��λ��������");
            }
        } else {
            //System.out.println("click");
        }
    }

    /**
     * 1. ����������������ӣ�
     * 2. ͬʱ������data������Ԫ��ֵ����Ϊ0��
     */
    public void clear() {
        //ֻ�Ƴ����ӣ������ﲻ����getChildren().clear()������Ὣ������Ҳ���
        getChildren().removeIf(shape -> (shape instanceof Circle));
        //������data������Ԫ��ֵ����Ϊ0
        data = new int[N + 1][N + 1];
    }
}

