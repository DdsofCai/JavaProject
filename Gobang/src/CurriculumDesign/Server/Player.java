package CurriculumDesign.Server;

import java.io.Serializable;
import java.net.SocketAddress;

/**
 * @Author: Ddsof_Cai
 * @Email: Ddsof_Cai@163.com
 * @version V1.0
 */

public class Player implements Serializable{
	String name;//�������
	SocketAddress address;//��ҵ�socket��ַ
	int color;//��ҵ�������ɫ

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Player(String name, SocketAddress address){
		this.name = name;
		this.address = address;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public SocketAddress getAddress()  {

		return address;
	}
	public void setAddress(SocketAddress address) {
		this.address = address;
	}
	public String toString(){
		return "[" + name+"," + address + "]";
	}
}
