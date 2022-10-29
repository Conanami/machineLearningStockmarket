package org.fuxin.statistic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.fuxin.analyst.MapExpert;
import org.fuxin.stock.SimpleShape;
import org.fuxin.stock.VerifyResult;
import org.fuxin.vo.StockDaily;

/***
 * 这是个做统计的类，可以统计某个时间段内涨得最好的10%的股票
 * @author Administrator
 *
 */
public class Statist {

	public static ArrayList<VerifyResult> doSelect(MapExpert me, Date startday,
			Date endday, boolean isBest, Float topPercent) {
		ArrayList<VerifyResult> rtlist = new ArrayList<VerifyResult>();
		for(String symbol:me.getSymbolList())
		{
			SimpleShape startSS = getSS(me.getAlldata().get(symbol),startday);
			SimpleShape endSS = getSS(me.getAlldata().get(symbol),endday);
			if(startSS!=null && endSS!=null &&
					startSS.date.compareTo(endSS.date)==-1 &&
					startSS.date.compareTo(startday)<=0)
			{
				VerifyResult vr = new VerifyResult(startSS,endSS);
				rtlist.add(vr);
			}
		}
		
		Collections.sort(rtlist);
		rtlist = getPart(isBest,topPercent,rtlist);    //取得部分
		return rtlist;
		
	}

	//取得最前面的部分或者最后面的部分
	private static ArrayList<VerifyResult> getPart(boolean isBest,
			Float topPercent, ArrayList<VerifyResult> inlist) {
		int start,end;
		int ratio = (int) (100/topPercent);
		if(isBest)
		{
			start = 0;
			end = inlist.size()/ratio;
		}
		else
		{
			start = (ratio-1) * inlist.size()/ratio;
			end = inlist.size()-1;
		}
		
		if(start>=inlist.size()) start = inlist.size()-1;
		if(start>end) end = start;
		
		if(end>=inlist.size()) end = inlist.size()-1;
		
		
		ArrayList<VerifyResult> rtlist = new ArrayList<VerifyResult>();
		for(int i=start;i<=end;++i)
		{
			rtlist.add(inlist.get(i));
		}
		if(!isBest) Collections.reverse(rtlist);
		return rtlist;
	}

	private static SimpleShape getSS(ArrayList<StockDaily> dailydata,
			Date findday) {
		if(dailydata==null || dailydata.size()==0) return null;
		
		SimpleShape rtss = new SimpleShape();
		
		for(int i=0;i<=dailydata.size();++i)
		{
			if(dailydata.get(i).getDate().equals(findday))
			{
				rtss.date = dailydata.get(i).getDate();
				rtss.price = dailydata.get(i).getClose();
				rtss.symbol = dailydata.get(i).getSymbol();
				return rtss;
			}
			else if(dailydata.get(i).getDate().compareTo(findday)==-1)
			{
				rtss.date = dailydata.get(i).getDate();
				rtss.price = dailydata.get(i).getClose();
				rtss.symbol = dailydata.get(i).getSymbol();
				return rtss;
			}
		}
		return null;
	}

}
