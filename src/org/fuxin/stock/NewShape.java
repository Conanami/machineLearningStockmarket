package org.fuxin.stock;

import java.util.ArrayList;
import java.util.Date;

import org.fuxin.vo.StockDaily;

/***
 * 新的形态记录，更清晰和简单
 * 2014-8-21 fuxin
 * @author Administrator
 *
 */
public class NewShape {
	public String symbol;
	public Date startday;
	public Integer startindex;
	public Date endday;
	public Integer endindex;
	public Float open;
	public Float close;
	public Float max;
	public Float min;
	public Float raise=0f;
	public Float range=0f;
	
	public NewShape(String symbol, Date startday, Date endday, Float open,
			Float close, Float max, Float min, Float raise, Float range) {
		super();
		this.symbol = symbol;
		this.startday = startday;
		this.endday = endday;
		this.open = open;
		this.close = close;
		this.max = max;
		this.min = min;
		this.raise = raise;
		this.range = range;
	}

	public NewShape() {
		super();
	}

	public NewShape(String symbol2, int tmpend, int tmpstart,
			ArrayList<StockDaily> dailydata) {
		this.symbol = symbol2;
		//先检查一下
		if(tmpend<= 0) tmpend = 1;
		if(tmpend>=dailydata.size()) tmpend = dailydata.size()-1;
		if(tmpstart>=dailydata.size()) 
			tmpstart = dailydata.size()-1;
		if(tmpstart<=0) tmpstart = 1;
		if(tmpend>tmpstart) tmpstart = tmpend; 
		if(dailydata==null||dailydata.size()==0) 
		{
			return;
		}
		
		this.endday = dailydata.get(tmpend).getDate();
		this.endindex = tmpend;
		this.startday = dailydata.get(tmpstart).getDate();
		this.startindex = tmpstart;
		this.open = dailydata.get(tmpstart).getOpen();
		this.close = dailydata.get(tmpend).getClose();
		this.max = dailydata.get(tmpend).getHigh();
		this.min = dailydata.get(tmpend).getLow();
		for(int i=tmpend;i<=tmpstart;++i)
		{
			if( dailydata.get(i).getHigh() > this.max )
				this.max = dailydata.get(i).getHigh();
			if( dailydata.get(i).getLow() < this.min )
				this.min = dailydata.get(i).getLow();
		}
		this.range = this.max/this.min - 1;
		this.raise = this.close/this.open - 1;
		
	}

	@Override
	public String toString() {
		return "NewShape [symbol=" + symbol + ", startday=" + startday
				+ ", endday=" + endday + ", open=" + open + ", close=" + close
				+ ", max=" + max + ", min=" + min + ", raise=" + raise
				+ ", range=" + range + "]";
	}
	
	
	
	
	
	
}
