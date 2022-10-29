package org.fuxin.basic;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import org.fuxin.analyst.MapExpert;
import org.fuxin.passcode.Candle;
import org.fuxin.util.ScopeUtil;
import org.fuxin.vo.StockDaily;

//最基础的统计人员
public class StatMan {

	//得到从startday，到endday的涨幅
	public static TreeMap<String, OriginValue> getRaise(MapExpert me,
			Date startday, Date endday,String description) {
		TreeMap<String,OriginValue> rtmap = new TreeMap<String,OriginValue>();
		for(String symbol:me.getSymbolList())
		{
			ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
			int startindex = ScopeUtil.getIndexForward(startday, dailydata);
			int endindex = ScopeUtil.getIndexForward(endday, dailydata);
			if(startindex>=0 && endindex>=0)
			{
				float raiseValue = StockBasic.getRaise(dailydata,startindex,endindex);
				rtmap.put(symbol, new OriginValue(symbol,raiseValue,description) );
			}
			else
			{
				rtmap.put(symbol, new OriginValue(symbol,0f,description) );
			}
		}
		return rtmap;
	}

	//得到一个K线
	public static TreeMap<String, CandleNew> getCandleResult(MapExpert me,
			Date startday, Date endday) {
		TreeMap<String,CandleNew> rtmap = new TreeMap<String,CandleNew>();
		for(String symbol:me.getSymbolList())
		{
			ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
			int startindex = ScopeUtil.getIndexForward(startday, dailydata);
			int endindex = ScopeUtil.getIndexForward(endday, dailydata);
			//CandleNew candle = new CandleNew(dailydata,startindex,endindex);
			//rtmap.put(symbol, candle);
		}
		return rtmap;
	}

}
