package org.fuxin.volume;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.analyst.MapExpert;
import org.fuxin.passcode.StockDailyWithReason;
import org.fuxin.util.ScopeUtil;
import org.fuxin.vo.StockDaily;

public class EasySelector {

	public static ArrayList<StockDailyWithReason> doSelect(Date currentday,
			HashSet<String> mystockpool, MapExpert me) {
		ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
		for(String symbol:mystockpool)
		{
			if(me.getAlldata().get(symbol)!=null)
			{
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				if(dailydata==null || dailydata.size()==0) continue;
				
				int index = ScopeUtil.getIndexForward(currentday, dailydata);
				if(index>=dailydata.size()) index = dailydata.size()-1;
				if(index<0) index = 0;
				
				//如果符合简单选择的条件
				if(MeetCondition(dailydata,index,40))
				{
					rtlist.add(new StockDailyWithReason(dailydata.get(index)));
				}	
			}
		}
		return rtlist;
	}

	/***
	 * 符合简单选择的条件
	 * @param dailydata
	 * @param index
	 * @param period
	 * @return
	 */
	private static boolean MeetCondition(ArrayList<StockDaily> dailydata,
			int index, int period) {
		// TODO Auto-generated method stub
		if(index+2>=dailydata.size()) return false;
		StockDaily today = dailydata.get(index+1);
		StockDaily yesterday = dailydata.get(index+2);
		if(IsDoubleRed(today,yesterday) && IsLowVolume(yesterday,dailydata,index+2,40))
			return true;
		return false;
	}

	private static boolean IsLowVolume(StockDaily yesterday,
			ArrayList<StockDaily> dailydata, int start, int period) {
		// TODO Auto-generated method stub
		if(yesterday.getVolume() < MinVolume(dailydata,start,period)*1.1 )
			return true;
		return false;
	}
	
	/***
	 * 得到最小成交量
	 * @param dailydata
	 * @param start
	 * @param period
	 * @return
	 */
	private static Long MinVolume(ArrayList<StockDaily> dailydata, int start,
			int period) {
		Long minvolume = null;
		for(int i = start; i<=start+period; ++i)
		{
			if(i>=dailydata.size()||i<0) return minvolume;
			
			Long volume = dailydata.get(i).getVolume();
			if(minvolume==null) 
				minvolume = volume;
			else if(minvolume>volume)
				minvolume = volume;
		}
		return minvolume;
	}

	private static boolean IsDoubleRed(StockDaily today, StockDaily yesterday) {
		// TODO Auto-generated method stub
		//如果量成为昨天的1.5倍，价格涨幅超过3%，则为红倍
		if(today.getVolume() > yesterday.getVolume()*1.5
		  && today.getVolume() < yesterday.getVolume()*3
		  && today.getClose() > yesterday.getClose()*1.03
		  && today.getClose() < yesterday.getClose()*1.07 )
			return true;
		return false;
	}
	
	

}
