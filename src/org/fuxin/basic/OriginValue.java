package org.fuxin.basic;

/***
 * �����ʾһ��������X����Y
 * @author Administrator
 *
 */
public class OriginValue implements Comparable<OriginValue> {
	
	public OriginValue(String symbol, float value, String description) {
		this.symbol = symbol;
		this.value = value;
		this.description = description;
	}
	public String symbol;					//��Ʊ����
	public String description;				//�ñ�������
	public Float value;						//�ñ�����ֵ
	
	
	@Override
	public String toString() {
		return  symbol + "," + description
				+ "," + value;
	}


	public int compareTo(OriginValue other) {
		if(this.value==other.value)
		return 0;
		else if(this.value<other.value) return 1;
		else return -1;
	}



}
