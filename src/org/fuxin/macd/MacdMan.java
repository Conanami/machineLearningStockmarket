package org.fuxin.macd;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.fuxin.vo.StockDaily;

public class MacdMan {

	public static MacdScore getValues(ArrayList<StockDaily> dailydata, int index) {
		
		MacdScore  mscore = new MacdScore();
		StockDaily sd = dailydata.get(index);
		mscore.symbol  = sd.getSymbol();
		mscore.date = sd.getDate();
		mscore.index = index;
		mscore.name = sd.getName();
		
		mscore.DIFF = getDIFF(dailydata,index,12f,26f);
		mscore.DEA = getDEA(dailydata,index,9f);
		mscore.MACD = getMACD(mscore.DIFF,mscore.DEA);
		return mscore;
	}

	private static Float getMACD(Float diff, Float dea) {
		return 2*(diff-dea);
	}

	private static Float getDIFF(ArrayList<StockDaily> dailydata, int index,
			float shortday, float longday) {
		Float EMA12 = getEMA(dailydata,index,shortday);
		Float EMA26 = getEMA(dailydata,index,longday);
		return EMA12-EMA26;
	}

	private static Float getDEA(ArrayList<StockDaily> dailydata, int start,
			float days) {
		int end = (int) (start + days - 1);
		if(end>=dailydata.size()) end = dailydata.size()-1;
		int cnt = 0;
		Float tmp = 0f;
		for(int i=start;i<=end;++i)
		{
			tmp += getDIFF(dailydata,i,12f,26f);
			cnt++;
		}
		Float result = tmp/cnt;
		return result;
	}

	private static Float getEMA(ArrayList<StockDaily> dailydata, int index,
			float days) {
		
		Float Close = dailydata.get(index).getClose();
		int end = (int) (index + days - 1);
		Float lastAverage = 0f;
		if(end>=dailydata.size())
		{
			end = dailydata.size()-1;
			lastAverage = getAverage(dailydata,index+1,days);
		}
		else
		{
			lastAverage = getEMA(dailydata,index+1,days);
		}
		Float result = 2/(days+1f)*Close + (days-1f)/(days+1f)*lastAverage;
		return result;
	}

	private static Float getAverage(ArrayList<StockDaily> dailydata, int start,
			float days) {
		int end = (int) (start+days-1);
		if(end>=dailydata.size()) end = dailydata.size()-1;
		int cnt = 0;
		Float tmp = 0f;
		for(int i=start;i<=end;++i)
		{
			tmp+=dailydata.get(i).getClose();
			cnt++;
		}
		Float result = tmp/cnt;
		return result;
	}

	

}
