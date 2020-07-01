package CurriculumDesign.Server;

import java.io.*;

/**
 * @Author: Ddsof_Cai
 * @Email: Ddsof_Cai@163.com
 * @version V1.0
 */
public class Message implements Serializable{
	int msgType;//��Ϣ���ͣ��ο���MessageType�еĶ���
	int status;//��Ϣ��״̬����Ϊ�ɹ���ʧ��
	Object content;//��Ϣ������
	Player fromPlayer;//��Ϣ�ķ��ͷ�
	Player toPlayer;//��Ϣ�Ľ��շ�

	public Player getFromPlayer() {
		return fromPlayer;
	}
	public void setFromPlayer(Player fromPlayer) {
		this.fromPlayer = fromPlayer;
	}
	public Player getToPlayer() {
		return toPlayer;
	}
	public void setToPlayer(Player toPlayer) {
		this.toPlayer = toPlayer;
	}	
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public String toString(){
		return "["+msgType +"," + status + ", " + fromPlayer + ", " + toPlayer + ", " + content;
	}
	
	/**
	 * ����ϢMessage�Ķ���ת�����ֽ����飨���ݸ�DatagramPacket��������socketͨ��
	 * @param obj
	 * @return
	 */
	public static byte[] convertToBytes(Message obj){
		try(
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream ois = new ObjectOutputStream(baos);					
			) {
			ois.writeObject(obj);
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
    
	/**
	 * ���ֽ����飨��DatagramPacket�л�ȡ��ת����Message�Ķ���
	 * @param bytes
	 * @param offset
	 * @param length
	 * @return
	 */
	public static Message convertToObj(byte[] bytes, int offset, int length){
		try (
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);		
		){
			return (Message) ois.readObject();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;	
	}
}
