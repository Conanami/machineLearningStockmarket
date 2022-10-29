package org.fuxin.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.vo.StockDaily;

public class TradingStrategy {
	
	
	public TradingStrategy(ArrayList<String> batchlist, boolean useFin, int holdday, Float losslimit) {
		this.selectbatchlist=batchlist;
		this.useFinancer = useFin;
		this.holdday = holdday;
		this.losslimit = losslimit;
	}
	
	public ArrayList<String> selectbatchlist;      //选股方案集合
	public boolean useFinancer;					   //是否加入财务删选
	public Integer holdday;                        //持股日期
	public Float losslimit;                        //止损点
	
	public ArrayList<StockDaily> doSelect(Date currentday,
			HashSet<String> mystockpool) {
		// TODO Auto-generated method stub
		return null;
	}

}
