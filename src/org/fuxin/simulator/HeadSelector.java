package org.fuxin.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.analyst.MapExpert;
import org.fuxin.analyst.StockSelector;
import org.fuxin.index.IndexSelector;
import org.fuxin.macd.MacdSelector;
import org.fuxin.passcode.CodeChooseLimit;
import org.fuxin.passcode.CodeSelector;
import org.fuxin.passcode.StockDailyWithReason;
import org.fuxin.report.SelectReport;
import org.fuxin.util.ArrayUtil;
import org.fuxin.util.FuOutput;
import org.fuxin.util.Translator;
import org.fuxin.vo.StockDaily;
import org.fuxin.volume.EasySelector;
import org.fuxin.volume.VolumeSelector;

public class HeadSelector {

	public static ArrayList<StockDailyWithReason> doSelect(Date currentday,
			HashSet<String> mystockpool, MapExpert me) {
		VolumeSelector vs = new VolumeSelector();
		HashSet<SelectReport> result = vs.doSelect(me,currentday);
		ArrayList<StockDaily> list1 = Translator.getStockDailyFromSelectReport(currentday,result,me);
		ArrayList<StockDaily> list2 = AveragePriceSelector.doBuySelect(currentday,mystockpool,me);
		list1.retainAll(list2);
		ArrayList<StockDailyWithReason> rtlist = Translator.getStockDailyWithReasonFromStockDaily(list1);
		return rtlist;
	}
	
	public static ArrayList<StockDaily> doCurrentSelect(Date currentday,
			HashSet<String> mystockpool, MapExpert me) {
		VolumeSelector vs = new VolumeSelector();
		HashSet<SelectReport> result = vs.doSelect(me,currentday);
		ArrayList<StockDaily> list1 = Translator.getStockDailyFromSelectReport(currentday,result,me);
		ArrayList<StockDaily> list2 = AveragePriceSelector.doCurrentSelect(currentday,mystockpool,me);
		list1.retainAll(list2);
		return list1;
	}

	public static ArrayList<StockDailyWithReason> doCodeSelect(Date currentday,
			HashSet<String> mystockpool, MapExpert me,CodeChooseLimit chooselimit) {
		ArrayList<StockDailyWithReason> list = CodeSelector.doSelect(currentday,mystockpool,me,chooselimit);
		return list;
	}

	//是否抛出选择
	public static ArrayList<StockDaily> doSellSelect(Date currentday,
			Account account, MapExpert me) {
		ArrayList<StockDaily> list = CodeSelector.doSellSelect(currentday,account,me);
		return list;
	}

	public static ArrayList<StockDailyWithReason> doCodeVsSelect(
			Date currentday, HashSet<String> mystockpool, MapExpert me,CodeChooseLimit chooselimit) {
		VolumeSelector vs = new VolumeSelector();
		HashSet<SelectReport> result = vs.doSelect(me,currentday);
		ArrayList<StockDailyWithReason> list1 = Translator.getStockDailyWithReasonFromSelectReport(currentday,result,me);
		ArrayList<StockDailyWithReason> codelist = CodeSelector.doSelect(currentday,mystockpool,me,chooselimit);
		codelist.retainAll(list1);
		return codelist;
	}

	public static ArrayList<StockDailyWithReason> doCurrentCodeSelect(Date currentday,
			HashSet<String> stockpool, MapExpert me,CodeChooseLimit chooselimit) {
		//ArrayList<StockDaily> averagelist = AveragePriceSelector.doCurrentSelect(currentday,stockpool,me);
		//ArrayList<StockDailyWithReason> list1 = Translator.getStockDailyWithReasonFromStockDaily(averagelist);
		//System.out.println("averlist size:"+list1.size());
		ArrayList<StockDailyWithReason> codelist = CodeSelector.doCurrentSelect(currentday,stockpool,me,chooselimit);
		System.out.println("codelist size:"+ codelist.size());
		//codelist.retainAll(list1);
		//System.out.println("buylist size:"+codelist.size());
		return codelist;
	}

	public static ArrayList<StockDailyWithReason> doCodeAverageSelect(
			Date currentday, HashSet<String> mystockpool, MapExpert me,
			CodeChooseLimit chooselimit) {
		ArrayList<StockDaily> averagelist = AveragePriceSelector.doCurrentSelect(currentday,mystockpool,me);
		ArrayList<StockDailyWithReason> list1 = Translator.getStockDailyWithReasonFromStockDaily(averagelist);
		System.out.println("averlist size:"+list1.size());
		ArrayList<StockDailyWithReason> codelist = CodeSelector.doCurrentSelect(currentday,mystockpool,me,chooselimit);
		System.out.println("codelist size:"+ codelist.size());
		codelist.retainAll(list1);
		System.out.println("buylist size:"+codelist.size());
		return codelist;
	}

	
	//根据MACD和涨停密码选股的系统
	public static ArrayList<StockDailyWithReason> doMacdCodeSelect(
			Date currentday, HashSet<String> mystockpool, MapExpert me,
			CodeChooseLimit chooselimit) {
		ArrayList<StockDailyWithReason> list1 = CodeSelector.doSelect(currentday,mystockpool,me,chooselimit);
		ArrayList<StockDailyWithReason> list2 = MacdSelector.doSelect(currentday,mystockpool,me);
		ArrayList<StockDailyWithReason> list3 = IndexSelector.doSelect(currentday,mystockpool,me);
		//调试个股时候用下面三行
		/*
		System.out.println(currentday);
		System.out.println("codelist:"+list1);
		System.out.println("macdlist:"+list2);
		System.out.println("indexlist:"+list3);
		//System.out.println("list3:"+list3.size());
		*/
		list1.retainAll(list2);
		
		//System.out.println("list1:"+list1.size());
		return list1;
		//return ArrayUtil.Merge(list1,list3);
	}

	public static ArrayList<StockDailyWithReason> doCurrentMacdCodeSelect(
			Date currentday, HashSet<String> stockpool, MapExpert me,
			CodeChooseLimit chooselimit) {
		ArrayList<StockDailyWithReason> codelist = CodeSelector.doCurrentSelect(currentday,stockpool,me,chooselimit);
		System.out.println(codelist);
		System.out.println("codelist size:" + codelist.size());
		FuOutput.writeToFile(codelist, "today_code");
		ArrayList<StockDailyWithReason> macdlist = MacdSelector.doCurrentSelect(currentday,stockpool,me);
		System.out.println("macdlist size:" + macdlist.size());
		FuOutput.writeToFile(macdlist, "today_macd");
		ArrayList<StockDailyWithReason> indexlist = IndexSelector.doCurrentSelect(currentday,stockpool,me);
		FuOutput.writeToFile(indexlist, "today_index");
		codelist.retainAll(macdlist);
		
		ArrayList<StockDailyWithReason> finallist=
				ArrayUtil.Merge(codelist,indexlist);
		//codelist.retainAll(indexlist);
		return finallist;
	}

	public static ArrayList<StockDailyWithReason> doMacdLowSelect(
			Date currentday, HashSet<String> mystockpool, MapExpert me) {
		// TODO Auto-generated method stub
		ArrayList<StockDailyWithReason> list1 = EasySelector.doSelect(currentday,mystockpool,me);
		ArrayList<StockDailyWithReason> list2 = MacdSelector.doSelect(currentday,mystockpool,me);
		//调试个股时候用下面三行
		System.out.println(currentday);
		System.out.println("easylist:"+list1);
		System.out.println("macdlist:"+list2);
		
		list1.retainAll(list2);
		return list1;
	}

	public static ArrayList<StockDailyWithReason> doLowSelect(Date currentday,
			HashSet<String> mystockpool, MapExpert me) {
		ArrayList<StockDailyWithReason> list1 = EasySelector.doSelect(currentday,mystockpool,me);
		//调试个股时候用下面三行
		System.out.println(currentday);
		System.out.println("easylist:"+list1);

		return list1;
	}

}
