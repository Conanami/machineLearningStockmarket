package org.fuxin.basic;

import java.util.ArrayList;
import java.util.Date;

import org.fuxin.vo.StockDaily;

public class CandleNew {
	public String symbol;					//1，股票代码
	public String name;						//2，股票名称
	public Float high;						//3，最高价
	public Float low;						//4，最低价
	public Float open;						//5，开盘价
	public Float close;						//6，收盘价
	public Integer startindex;				//7，开始索引
	public Integer endindex;				//8，结束索引
	public Date startdate;					//9，开始日期
	public Date enddate;					//10，结束日期
	public Float turnover;					//11，换手率
	public Float raiseRate;					//12，涨幅
	public Float raisePrice;				//13，涨金额
	public Float volumeRate;				//14，量比
	public Float upShadow;					//15，上影线
	public Float downShadow;				//16，下影线
	public Float body;						//17，实体
	public int RedOrGreen;					//18，红或者绿，或者白
	
	
	public CandleNew(ArrayList<StockDaily> dailydata, int startindex,
			int endindex) {
		
	}

}
