package CurriculumDesign.Client03;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @Author: ����
 * @Date: 2019/8/20
 * @version V1.0
 * @Description:���ڶ���ͻ���Ҫ�õ���һЩ����
 * @Project: �����̼���
 * @Copyright: All rights reserved
 */

public class ClientConfig {
	//�������ĵ�ַ
	public static final SocketAddress SERVER_ADDR 
			= new InetSocketAddress("172.16.134.2", 6666);
	//���صĵ�ַ
	public static final SocketAddress LOCAL_ADDR 
			= new InetSocketAddress("172.16.134.2", 3333);
	
	public static final int ACTION_TYPE_ADD = 1;//������
	public static final int ACTION_TYPE_DEL = 0;//����
	public static final int COLOR_WHITE=2;
	public static final int COLOR_BLACK=1;
	public static final int COLOR_BLANK=0;
	
}
