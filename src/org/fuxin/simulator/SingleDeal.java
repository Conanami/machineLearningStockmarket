package org.fuxin.simulator;

import java.util.Date;

import org.fuxin.passcode.BuyReason;
import org.fuxin.util.DateUtil;

public class SingleDeal {
	public String symbol;					//股票代码
	public String name;						//股票名称
	public Float buyprice;					//买入价
	public Date buyday;						//买入日期（最后一个）
	public int shares;						//持有股数
	public Float totalcost;					//总计成本
	public Date sellday;					//抛出日期
	public Float sellprice;					//抛出价格
	public Float totalincome;				//总计收入
	public Float rate;						//盈利率
	public Float profit;					//利润总数
	public long holdday;					//持有天数
	public BuyReason reason;				//买入理由
	public void calculate() {
		profit = sellprice * shares - totalcost;
		rate = profit / totalcost;
		holdday = DateUtil.Sub(sellday, buyday);
		
	}
	@Override
	public String toString() {
		return "SingleDeal [symbol=" + symbol + ", name=" + name
				+ ", buyprice=" + buyprice + ", buyday=" + buyday + ", shares="
				+ shares + ", totalcost=" + totalcost + ", sellday=" + sellday
				+ ", sellprice=" + sellprice + ", totalincome=" + totalincome
				+ ", rate=" + rate + ", profit=" + profit + ", holdday="
				+ holdday + ", reason=" + reason ;
	}
	
	
}
