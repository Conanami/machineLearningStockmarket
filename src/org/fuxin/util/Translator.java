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
 * �����ߣ����ڴ����ݿ��е�����ת����shapeGrp,��̬��
 * @author Administrator
 *
 */
public class Translator {
	/***
	 * �����Ǵ��룬�����±꣬ʱ������p2,���ڴ����ݿ��е�����
	 * �����һ����̬���
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
			
			//�ж�һ�£���ֹԽ�磬����˵���ᣬ�����Ѿ��п���
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

	//��һ����Χ���Ʊ��һ��ʱ���
	public static TimePeriod getTimePeriod(DiscreteScope ds) {
		TimePeriod rtp = new TimePeriod();
		for(int i=0;i<ds.typename.size();++i)
		{
			rtp.period.add(ds.typename.get(i).period);
		}
		rtp.calculateSize();
		return rtp;
	}

	//��ѡ�񱨸�õ�buylist
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
	 * ��ѡ�ɱ�����StockDailyWithReason
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
