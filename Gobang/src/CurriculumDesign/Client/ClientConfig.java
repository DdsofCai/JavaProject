package CurriculumDesign.Client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @Author: Ddsof_Cai
 * @Email: Ddsof_Cai@163.com
 * @version V1.0
 */

public class ClientConfig {
	//�������ĵ�ַ
	public static final SocketAddress SERVER_ADDR 
			= new InetSocketAddress("192.168.124.18", 8888);
	//���صĵ�ַ
	public static final SocketAddress LOCAL_ADDR 
			= new InetSocketAddress("192.168.124.18", 1111);
	
	public static final int ACTION_TYPE_ADD = 1;//������
	public static final int ACTION_TYPE_DEL = 0;//����
	public static final int COLOR_WHITE=2;
	public static final int COLOR_BLACK=1;
	public static final int COLOR_BLANK=0;
	
}
