package CurriculumDesign.Server;

/**
 * @Author: Ddsof_Cai
 * @Email: Ddsof_Cai@163.com
 * @version V1.0
 */
public class MessageType {
    public static final int SUCCESS = 0;
    public static final int FAIL = -1;
    public static final int JOIN_GAME = 1;//���������Ϸ��������Ϣ
    public static final int JOIN_HOST = 2;//�������������������Ϣ
    public static final int CHALLENGE = 3;//��ս������������Ϣ
    public static final int CHALLENGE_RSP = 4;//��ս������������Ϣ
    public static final int UPDATE_HOST = 5;//���������б����Ϣ
    public static final int PLAY = 6;//�������һ�������Ϣ
    public static final int HUIQI = 8;//����
    public static final int HUIQI_RSP = 9;
    public static final int SURRENDER = 10;//Ͷ��
    public static final int DOGFALL = 11;//ƽ��
    public static final int DOGFALL_RSP = 12;//ƽ�ֻ�Ӧ
    public static final int JOIN_COM = 15;//����������
    public static final int PUBLIC_CHAT = 16;//�㲥
    public static final int PRIVATE_CHAT = 17;//˽��
    public static final int UPDATE_COM = 20;//����������ҵ���Ϣ
    public static final int QUIT_COM=21;//�˳�������
    public static final int QUIT_HOST=22;//�˳���̨
    public static final int QUIT_GAME=23;//�˳���Ϸ
    public static final int SENDFILES=24;//����ͼƬ����
    public static final int SENDFILES_RSP=25;//ͼƬ��Ӧ
    public static final int GUIZHAN=26;//��ս
    public static final int QUI_GUANZHAN=27;//�˳���ս
}
