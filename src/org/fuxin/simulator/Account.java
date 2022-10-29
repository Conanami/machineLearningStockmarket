package org.fuxin.simulator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.fuxin.analyst.MapExpert;
import org.fuxin.util.ScopeUtil;
import org.fuxin.vo.StockDaily;

public class Account {
	public float balance;						//账户总金额
	public HashMap<String,HoldingStock> holdlist;      //持股集合
	public float totalassets;					//总资产
	
	public Account(float startmoney) {
		this.balance = startmoney;
		this.totalassets = this.balance;
		holdlist = new HashMap<String,HoldingStock>();
	}
	

	
	@Override
	public String toString() {
		return "Account [balance=" + balance + ", holdlist=" + holdlist
				+ ", totalassets=" + totalassets + "]";
	}



	public void getTotalassets(Date currentday, MapExpert me) {
		// TODO Auto-generated method stub
		Float tmptotal = balance;
		for(String symbol:holdlist.keySet())
		{
			Float nowPrice = getNowPrice(symbol,currentday,me);
			tmptotal += nowPrice * holdlist.get(symbol).shares;
		}
		totalassets = tmptotal;
	}



	private Float getNowPrice(String symbol, Date currentday, MapExpert me) {
		ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
		int index = ScopeUtil.getIndexForward(currentday, dailydata);
		return dailydata.get(index).getClose();
	}



	public void showBrief(Date currentday, MapExpert me) {
		getTotalassets(currentday,me);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(currentday)+
				"|账户余额:" + balance +
				"|总资产:" + totalassets);
		
	}
	
}
