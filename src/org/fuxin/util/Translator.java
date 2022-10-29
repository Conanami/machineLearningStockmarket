package org.fuxin.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.analyst.MapExpert;
import org.fuxin.analyst.TimePeriod;
import org.fuxin.passcode.CodeScore;
import org.fuxin.passcode.StockDailyWithReason;
import org.fuxin.report.SelectReport;
import org.fuxin.stock.DiscreteScope;
import org.fuxin.stock.NewShape;
import org.fuxin.stock.ShapeGrp;
import org.fuxin.vo.StockDaily;

/***
 * 翻译者，把内存数据库中的数据转换成shapeGrp,形态组
 * @author Administrator
 *
 */
public class Translator {
	/***
	 * 输入是代码，结束下标，时间区间p2,和内存数据库中的数据
	 * 输出是一个形态组合
	 * @param symbol
	 * @param endindex
	 * @param p2
	 * @param dailydata
	 * @return
	 */
	public static ShapeGrp getShapeGrp(String symbol, int endindex, TimePeriod p2,
			ArrayList<StockDaily> dailydata) {
		ShapeGrp rtsg = new ShapeGrp();
		int tmpend = endindex;
		int tmpstart = 0; 
		for(int i=0;i<p2.period.size();++i)
		{
			tmpstart = tmpend + p2.period.get(i) - 1;
			
			//判断一下，防止越界，按理说不会，上面已经有控制
			if(tmpstart>=dailydata.size())
				tmpstart = dailydata.size()-1;
			
			rtsg.shapelist.add(getNewShape(symbol,tmpend,tmpstart,dailydata));
			tmpend = tmpstart + 1;
		}
		return rtsg;
	}
	
	private static NewShape getNewShape(String symbol, int tmpend, int tmpstart,
			ArrayList<StockDaily> dailydata) {
		return (new NewShape(symbol,tmpend,tmpstart,dailydata));
	}

	//把一个范围限制变成一个时间段
	public static TimePeriod getTimePeriod(DiscreteScope ds) {
		TimePeriod rtp = new TimePeriod();
		for(int i=0;i<ds.typename.size();++i)
		{
			rtp.period.add(ds.typename.get(i).period);
		}
		rtp.calculateSize();
		return rtp;
	}

	//从选择报告得到buylist
	public static ArrayList<StockDaily> getStockDailyFromSelectReport(
			Date currentday, HashSet<SelectReport> sltrptlist, MapExpert me) {
		ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
		for(SelectReport sr:sltrptlist)
		{
			String symbol = sr.symbol;
			ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
			
			if(dailydata==null || dailydata.size()==0) continue;
		
			int index = ScopeUtil.getOnlyIndex(currentday, dailydata);
			if(index==-1) continue;
			else
			{
				rtlist.add(dailydata.get(index));
			}
		}
		return rtlist;
	}

	
	public static ArrayList<StockDaily> getStockDailyFromWithReason(
			ArrayList<StockDailyWithReason> list) {
		ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
		if(list==null) return rtlist;
		
		for(StockDailyWithReason sdwr:list)
		{
			rtlist.add(sdwr.sd);
		}
		return rtlist;
	}

	/***
	 * 把选股报告变成StockDailyWithReason
	 * @param currentday
	 * @param result
	 * @param me
	 * @return
	 */
	public static ArrayList<StockDailyWithReason> getStockDailyWithReasonFromSelectReport(
			Date currentday, HashSet<SelectReport> result, MapExpert me) {
		ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
		for(SelectReport sr:result)
		{
			String symbol = sr.symbol;
			ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
			
			if(dailydata==null || dailydata.size()==0) continue;
		
			int index = ScopeUtil.getOnlyIndex(currentday, dailydata);
			if(index==-1) continue;
			else
			{
				CodeScore score = null;
				StockDailyWithReason sdwr = new StockDailyWithReason(dailydata.get(index),score);
				rtlist.add(sdwr);
			}
		}
		return rtlist;
	}

	public static ArrayList<StockDailyWithReason> getStockDailyWithReasonFromStockDaily(
			 ArrayList<StockDaily> averagelist) {
		ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
		for(StockDaily sd:averagelist)
		{
			CodeScore score = null;
			rtlist.add(new StockDailyWithReason(sd,score));
		}
		return rtlist;
	}

}
