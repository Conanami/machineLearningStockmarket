package org.fuxin.analyst;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.report.SelectReport;
import org.fuxin.report.StatisticReport;
import org.fuxin.stock.ShapeGrp;
import org.fuxin.util.FuOutput;
import org.fuxin.util.Saver;
import org.fuxin.util.ScopeUtil;
import org.fuxin.util.Translator;
import org.fuxin.vo.StockDaily;

public class StockSelector {

	public static HashSet<SelectReport> doSelect(MapExpert me, String datestr,
			String batch_name) {
		ArrayList<StatisticReport> srlist = Saver.loadStatisticReport(batch_name);
		ArrayList<String> symbolList = me.getSymbolList();
		//对所有的股票进行选股
		//返回一个选股后报告
		HashSet<SelectReport> selectreportlist = new HashSet<SelectReport>();
		for(int i=0;i<symbolList.size();++i)
		{
			String symbol = symbolList.get(i);
			ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
			if(dailydata!=null)
			{
				for(StatisticReport sr:srlist)
				{
					TimePeriod tp = Translator.getTimePeriod(sr.getDs());
					ShapeGrp sgrp = Translator.getShapeGrp(symbolList.get(i), 0 , tp, dailydata);
					if(sgrp.isFit(sr))
					{
						SelectReport singleSelect = new SelectReport(symbol,dailydata,sr);
						singleSelect.setFinance(me.getFinancedata().get(symbol));
						selectreportlist.add(singleSelect);
					}
				}
			}
		}
		
		return selectreportlist;
	}

	public static HashSet<SelectReport> doSelect(MapExpert me, 
			String batch_name, Date selectday) {
		ArrayList<StatisticReport> srlist = Saver.loadStatisticReport(batch_name);
		ArrayList<String> symbolList = me.getSymbolList();
		//对所有的股票进行选股
		//返回一个选股后报告
		
		HashSet<SelectReport> selectreportlist = new HashSet<SelectReport>();
		for(int i=0;i<symbolList.size();++i)
		{
			String symbol = symbolList.get(i);
			ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
			if(dailydata!=null)
			{
				int index = ScopeUtil.getOnlyIndex(selectday, dailydata);
				if(index==-1) continue;
				for(StatisticReport sr:srlist)
				{
					TimePeriod tp = Translator.getTimePeriod(sr.getDs());
					ShapeGrp sgrp = Translator.getShapeGrp(symbolList.get(i), index+1 , tp, dailydata);
					if(sgrp.isFit(sr))
					{
						SelectReport singleSelect = new SelectReport(symbol,dailydata,sr);
						singleSelect.setFinance(me.getFinancedata().get(symbol));
						selectreportlist.add(singleSelect);
					}
				}
			}
		}
		
		return selectreportlist;
	}

}
