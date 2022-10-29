package org.fuxin.passcode;

import java.util.ArrayList;

import org.fuxin.vo.StockDaily;

/***
 * 记录阳线
 * @author Administrator
 *
 */

public class Candle {
	public StockDaily sd;			//最高价，最低价，收盘价，开盘价，成交量，成交金额，换手率
	public int index;				//在内存中的索引位置
	public Float raiseRate;			//涨幅，即收盘价与前一日收盘价的比
	public Float volumeRate;		//量比，即当天换手率与前一日换手率的比
	public Float UpShadow;			//上影线与收盘价的比
	public Float DownShadow;		//下影线与收盘价的比
	public Float Body; 				//实体长度与收盘价的比
	public int YingYang;			//1为阳线红，-1为阴线绿，0为白线
	
	
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
