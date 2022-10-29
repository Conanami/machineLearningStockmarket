package org.fuxin.macd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.analyst.MapExpert;
import org.fuxin.passcode.CodeScore;
import org.fuxin.passcode.StockDailyWithReason;
import org.fuxin.simulator.Account;
import org.fuxin.simulator.HoldingStock;
import org.fuxin.util.ScopeUtil;
import org.fuxin.vo.StockDaily;

/***
 * 这个类是一个纯的MACD选股器
 * @author Administrator
 *
 */

public class MacdSelector {
	
	public static ArrayList<StockDailyWithReason> doSelect(Date currentday,
			HashSet<String> mystockpool, MapExpert me) {
		// TODO Auto-generated method stub
		ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
		for(String symbol:mystockpool)
		{
			if(me.getAlldata().get(symbol)!=null)
			{
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				int index = ScopeUtil.getIndexForward(currentday, dailydata);
				if(index>=0) 
				{
					//计算前一天的MACD值
					index = index + 1;    //这行不能省，否则变成作弊了
					if(index>=dailydata.size()) index = dailydata.size()-1;
					
					Float lastdayClose = dailydata.get(index).getClose();
					MacdScore today = GetMacdScore(symbol,dailydata,index);
					MacdScore yesterday = GetMacdScore(symbol,dailydata,index+1);
					
					//System.out.println("---------------------");
					//System.out.println(today);
					//System.out.println(yesterday);
					//如果形成符合条件的金叉，则可以买入
					if(IsGolden(today,yesterday,lastdayClose)) 
					{
						//返回的必须是当天的，虽然符合条件是前一天
						rtlist.add(new StockDailyWithReason(dailydata.get(index-1),today));
					} 
				}
			}
		}
		return rtlist;
	}

	/***
	 * MACD金叉定义
	 * @param today
	 * @param yesterday
	 * @param lastdayClose 
	 * @return
	 */
	
	private static boolean IsGolden(MacdScore today,MacdScore yesterday, Float lastdayClose) 
	{
		if(Math.abs(today.DIFF-today.DEA)<=lastdayClose*0.015
			&& today.MACD>=yesterday.MACD-0.005
			&& today.DIFF>=yesterday.DIFF-0.005
			//&& (yesterday.DIFF-0.01)<yesterday.DEA
			&& today.DEA>= -1*(lastdayClose*0.015)
			//&& today.DEA<= lastdayClose * 0.025
				)
			return true;
		else
			return false;
	}

	/***
	 * 取得该股票当日的MACD三个值
	 * @param symbol
	 * @param dailydata
	 * @param index
	 * @return
	 */
	
	private static MacdScore GetMacdScore(String symbol,
			ArrayList<StockDaily> dailydata, int index) {
		if(dailydata==null) return null;
		
		MacdScore rtmscore = new MacdScore();
		if(index>=dailydata.size()) index = dailydata.size()-1;
		if(index<0) index = 0;
		
		if(dailydata.get(index)!=null)
		{
			rtmscore = MacdMan.getValues(dailydata,index);
		}
		return rtmscore;
	}

	/***
	 * 当前的选择
	 * @param currentday
	 * @param stockpool
	 * @param me
	 * @return
	 */
	public static ArrayList<StockDailyWithReason> doCurrentSelect(
			Date currentday, HashSet<String> stockpool, MapExpert me) {
		ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
		for(String symbol:stockpool)
		{
			if(me.getAlldata().get(symbol)!=null)
			{
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				int index = ScopeUtil.getIndexForward(currentday, dailydata);
				if(index>=0) 
				{
					//计算前一天的MACD值
					//index = index + 1;    //这行不能省，否则变成作弊了
					Float lastdayClose = dailydata.get(index+1).getClose();
					MacdScore today = GetMacdScore(symbol,dailydata,index);
					MacdScore yesterday = GetMacdScore(symbol,dailydata,index+1);
					//如果形成符合条件的金叉，则可以买入
					if(IsGolden(today,yesterday,lastdayClose)) 
					{
						//System.out.println("---------------------");
						//System.out.println(today);
						//System.out.println(yesterday);
						//当天的选择，返回也是当天
						rtlist.add(new StockDailyWithReason(dailydata.get(index),today));
					} 
				}
			}
		}
		return rtlist;
	}

	public static ArrayList<StockDaily> doSellSelect(Date currentday,
			Account account, MapExpert me) {
		ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
		for(String symbol:account.holdlist.keySet())
		{
			//当天买的不可以当天抛
			if(!account.holdlist.get(symbol).buydate.equals(currentday))
			{
				HoldingStock holdinfo = account.holdlist.get(symbol);
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				int index = ScopeUtil.getOnlyIndex(currentday, dailydata);
				if(index==-1) continue; 			//如果当天停牌就是不能卖
				int sellReason = CanSell(index,dailydata,holdinfo);
				if( sellReason > 0 )
				{
					rtlist.add(dailydata.get(index));
				}
			}
		}
		return rtlist;
			
	}

	/***
	 * 判断是否可以抛售，大于0就是要抛了
	 * @param index
	 * @param dailydata
	 * @param holdinfo
	 * @return
	 */
	private static int CanSell(int index, ArrayList<StockDaily> dailydata,
			HoldingStock holdinfo) {
		if(dailydata==null) return -1;
		
		//必须是根据昨天的情况决定
		int selldayindex = index + 1;
		if(dailydata.get(selldayindex)==null) return -1;
		
		String symbol = dailydata.get(selldayindex).getSymbol();
		
		//当前价格，是当天的最低价
		Float nowPrice = dailydata.get(index).getLow();
		//前一天收盘价
		StockDaily yesterdaySd = dailydata.get(selldayindex);
		Float lastdayClose = dailydata.get(selldayindex).getClose();
		//买那天的index
		int buyindex = ScopeUtil.getOnlyIndex(holdinfo.buydate, dailydata);
		
		MacdScore today = GetMacdScore(symbol,dailydata,selldayindex);
		MacdScore yesterday = GetMacdScore(symbol,dailydata,selldayindex+1);
		
		//有主升浪后，停涨则走
		if(  
			yesterday.MACD>=nowPrice*0.01 &&
			today.MACD<(yesterday.MACD-0.01)
			) return 1;
		
		//如果金叉没叉成功，继续向下，则退出
		if(
				today.MACD <= 0  &&
				today.MACD < (yesterday.MACD - 0.01) 
			)
		return 3;
		
		//如果换手大于25，则退出
		//if( yesterdaySd.getTurnover()>=25 ) 
		//	return 5;
		
		//到达止损点，则止损
		//2016-01-11改为见阴线就走
		if(ReachLossLimit(lastdayClose,holdinfo.buyprice,0.99f) 
				) return 2;
	                               
		//如果时间超过20天，则说明选择有误，也走吧
		//if(ReachTimeLimit(index,buyindex,20)) return 4;
		return -1;
	}

	//判断是否到达了时间限制点
	private static boolean ReachTimeLimit(int index, int buyindex, int daylimit) {
		if(buyindex-index>=daylimit) return true;
		return false;
	}

	//判断是否到达了止损点
	private static boolean ReachLossLimit(Float lastdayClose, Float buyprice,
			float losslimit) {
		if(lastdayClose<=buyprice*losslimit) return true;
		return false;
	}

	/***
	 * 简易抛售策略
	 * @param currentday
	 * @param account
	 * @param me
	 * @return
	 */
	public static ArrayList<StockDaily> doEasySellSelect(Date currentday,
			Account account, MapExpert me) {
		ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
		for(String symbol:account.holdlist.keySet())
		{
			//当天买的不可以当天抛
			if(!account.holdlist.get(symbol).buydate.equals(currentday))
			{
				HoldingStock holdinfo = account.holdlist.get(symbol);
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				int index = ScopeUtil.getOnlyIndex(currentday, dailydata);
				if(index==-1) continue; 			//如果当天停牌就是不能卖
				int sellReason = CanSell(index,dailydata,holdinfo);
				if( sellReason > 0 )
				{
					rtlist.add(dailydata.get(index));
				}
			}
		}
		return rtlist;
	}
}
