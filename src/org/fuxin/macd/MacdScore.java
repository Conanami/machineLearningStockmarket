package org.fuxin.macd;

import java.util.Date;

/***
 * ��¼һ����Ʊ��MACDֵ
 * @author Administrator
 *
 */
public class MacdScore {
	public String symbol;				//����
	public String name;					//����
	public Date date;					//����
	public int index;					//�ڴ�������
	public Float DIFF;					//DIFFֵ
	public Float DEA;					//DEAֵ
	public Float MACD;					//MACDֵ����������
	
	@Override
	public String toString() {
		return "MacdScore [symbol=" + symbol + ", name=" + name + ", date="
				+ date + ", index=" + index + ", DIFF=" + DIFF + ", DEA=" + DEA
				+ ", MACD=" + MACD + "]";
	}
	
}
