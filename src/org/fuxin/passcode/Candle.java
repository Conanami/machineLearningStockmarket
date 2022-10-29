package org.fuxin.passcode;

import java.util.ArrayList;

import org.fuxin.vo.StockDaily;

/***
 * ��¼����
 * @author Administrator
 *
 */

public class Candle {
	public StockDaily sd;			//��߼ۣ���ͼۣ����̼ۣ����̼ۣ��ɽ������ɽ���������
	public int index;				//���ڴ��е�����λ��
	public Float raiseRate;			//�Ƿ��������̼���ǰһ�����̼۵ı�
	public Float volumeRate;		//���ȣ������컻������ǰһ�ջ����ʵı�
	public Float UpShadow;			//��Ӱ�������̼۵ı�
	public Float DownShadow;		//��Ӱ�������̼۵ı�
	public Float Body; 				//ʵ�峤�������̼۵ı�
	public int YingYang;			//1Ϊ���ߺ죬-1Ϊ�����̣�0Ϊ����
	
	
	private void calculate(StockDaily today, StockDaily yesterday) {
		if(today==null || yesterday==null) return;
	
		raiseRate = today.getClose()/yesterday.getClose() - 1;
		volumeRate = today.getTurnover()/yesterday.getTurnover();
		
		if(today.getClose()>today.getOpen()) YingYang = 1;
		else if(today.getClose()==today.getOpen()) YingYang = 0;
		else YingYang = -1;
		
		if(YingYang==1)
		{
			UpShadow = (today.getHigh()-today.getClose())/today.getClose();
			DownShadow = (today.getOpen()-today.getLow())/today.getClose();
		}	
		else
		{
			UpShadow = (today.getHigh()-today.getOpen())/today.getClose();
			DownShadow = (today.getClose()-today.getLow())/today.getClose();
		}
		Body = Math.abs(today.getClose()-today.getOpen())/today.getClose();
	}
	
	public Candle(StockDaily today,StockDaily yesterday,int index)
	{
		this.index = index;
		this.sd = today;
		calculate(today,yesterday);
	}

	public Candle(ArrayList<StockDaily> dailydata, int startindex, int endindex) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Candle [" + "index=" + index + ", raiseRate="
				+ raiseRate + ", volumeRate=" + volumeRate + ", UpShadow="
				+ UpShadow + ", DownShadow=" + DownShadow + ", Body=" + Body
				+ ", YingYang=" + YingYang + "]\r\n";
	}
	
	
}
