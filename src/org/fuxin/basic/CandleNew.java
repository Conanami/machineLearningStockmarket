package org.fuxin.basic;

import java.util.ArrayList;
import java.util.Date;

import org.fuxin.vo.StockDaily;

public class CandleNew {
	public String symbol;					//1����Ʊ����
	public String name;						//2����Ʊ����
	public Float high;						//3����߼�
	public Float low;						//4����ͼ�
	public Float open;						//5�����̼�
	public Float close;						//6�����̼�
	public Integer startindex;				//7����ʼ����
	public Integer endindex;				//8����������
	public Date startdate;					//9����ʼ����
	public Date enddate;					//10����������
	public Float turnover;					//11��������
	public Float raiseRate;					//12���Ƿ�
	public Float raisePrice;				//13���ǽ��
	public Float volumeRate;				//14������
	public Float upShadow;					//15����Ӱ��
	public Float downShadow;				//16����Ӱ��
	public Float body;						//17��ʵ��
	public int RedOrGreen;					//18��������̣����߰�
	
	
	public CandleNew(ArrayList<StockDaily> dailydata, int startindex,
			int endindex) {
		
	}

}
