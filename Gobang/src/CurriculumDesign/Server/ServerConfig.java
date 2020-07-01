package CurriculumDesign.Server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @Author: Ddsof_Cai
 * @Email: Ddsof_Cai@163.com
 * @version V1.0
 */

public class ServerConfig {
	//������������UDP�˿�
	public final static SocketAddress SERVER_ADDR = new InetSocketAddress("192.168.124.27", 8888);
	//public final static int CLIENT_LISTEN_PORT_DEFAULT = 5000;
	private static HashMap<String, Player> hostMap = new HashMap<String, Player>();//���ڴ��������Ϣ
	private static HashMap<String, Player> playerMap = new HashMap<String, Player>();//���ڴ��������ҵ���Ϣ
	private static HashMap<String,Player> playersComList=new HashMap<String, Player>();//���ڴ�������������ҵ���Ϣ
	private static HashMap<String,Player> playingMap = new HashMap<String,Player>();//���ڴ�����ڱ��������
	private static HashMap<String,Player> guanZhanMap = new HashMap<String,Player>();//��Ź�ս�����
	public static void addHost(String name, Player player){
		hostMap.put(name, player);
	}//�������
	public static void addComPlayer(String name ,Player player ){playersComList.put(name,player);}//����������������
	public static void addPlayingPlayer(String name ,Player player ){playingMap.put(name,player);}//�����Ϸ���ڶ�ս���

	public static boolean containHost(String name){
		return hostMap.containsKey(name);
	}//�ж������Ƿ����
	public static boolean containPlaying(String name){
		return playingMap.containsKey(name);
	}//�ж������Ƿ����
	public static boolean containPlayer(String name){
		return playerMap.containsKey(name);
	}//�ж�����Ƿ����

	public static HashMap<String,Player> getHostMap(){return hostMap;}//��ȡ����Map
	public static HashMap<String, Player> getPlayerMap(){
		return playerMap;
	}//��ȡ���Map
	public static HashMap<String,Player> getPlayerComMap(){return playersComList;}//��ȡ���������Map
	public static HashMap<String,Player> getGuanZhanMap(){return guanZhanMap;}//��ȡ��սMap

	public static Player getPlayer(String name){
		return playerMap.get(name);
	}//��ȡ���
	public static Player getComPlayer(String name){return playersComList.get(name);}//��ȡ���������
	public static Player getGuanZhan(String name){return guanZhanMap.get(name);}//��ȡ��ս���

	public static void addPlaying(String name, Player player){
		playingMap.put(name, player);
	}//����������
	public static void addPlayer(String name, Player player){
		playerMap.put(name, player);
	}//������
	public static void addGuanZhanPlayer(String name,Player player){guanZhanMap.put(name,player);}//��ӹ�ս���

	public static void delComPlayer(String name){
		playersComList.remove(name);
	}//ɾ�����������
	public static void delHostPlayer(String name){ hostMap.remove(name); }//ɾ������
	public static void delPlaying(String name){ playingMap.remove(name); }//ɾ���������
	public static void delMapPlayer(String name){playerMap.remove(name);}//ɾ�����
	public static void delGuanZhan(String name){guanZhanMap.remove(name);}//ɾ����ս���
	public static void delAllPlayer(String name){
		delGuanZhan(name);
		delMapPlayer(name);
		delHostPlayer(name);
		delComPlayer(name);
		delPlaying(name);
	}
	//��ȡ��ҵ�ַ
	public static SocketAddress getPlayerAddr(String name){ return getPlayer(name).getAddress(); }
	//��ȡ����������б�
	public static  String getComNamesList(){
		String str = "";
		Iterator<String> it = playersComList.keySet().iterator();
		while(it.hasNext()){
			str = str  + it.next() + ":";
		}
		System.out.println("get:"+str);
		return str;
	}
	//��ȡ�����б�
	public static String getHostNamesList(){
		String str = "";	
		Iterator<String> it = hostMap.keySet().iterator();	
		while(it.hasNext()){
			str = str  + it.next() + ":";		
		}
		System.out.println("get:"+str);
		return str;
	}
}
