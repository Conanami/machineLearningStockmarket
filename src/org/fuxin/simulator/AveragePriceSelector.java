package org.fuxin.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.fuxin.analyst.MapExpert;
import org.fuxin.util.MathUtil;
import org.fuxin.util.ScopeUtil;
import org.fuxin.vo.StockDaily;

public class AveragePriceSelector {

	//选择是否要卖出
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
				if(sellReason>0)
				{
					System.out.println(sellReason);
					rtlist.add(dailydata.get(index));
				}
			}
		}
		return rtlist;
			
	}

	private static int CanSell(int index, ArrayList<StockDaily> dailydata, HoldingStock holdinfo) {
		
		Float lastdayClose = dailydata.get(index+1).getClose();
		Float last2dayClose = null;
		if(index+2<dailydata.size())
			last2dayClose = dailydata.get(index+2).getClose();
		else
			last2dayClose = lastdayClose;
		
		
		Float lastdayHigh = dailydata.get(index+1).getHigh();		//当天冲高
		Float lastdayOpen = dailydata.get(index+1).getOpen();
		
		Float gap0 = getGap(dailydata,index,5,10);
		Float gap1 = getGap(dailydata,index+1,5,10);
		Float gap05 = getGap(dailydata,index,1,5);
		Float gap15 = getGap(dailydata,index+1,1,5);
		
		Float p30 = getAveragePrice(dailydata, index, 30);
		Float p5 = getAveragePrice(dailydata, index, 5);
		Float p10 = getAveragePrice(dailydata, index, 10);
		Float p20 = getAveragePrice(dailydata, index, 20);
		Float p60 = getAveragePrice(dailydata, index, 60);
		Float p60_ago = getAveragePrice(dailydata,index+30,60);  //30天前的60日均线
		
		int buyindex = ScopeUtil.getOnlyIndex(holdinfo.buydate, dailydata);
		
		//鳄鱼闭嘴的形态，如果当日是5日线下穿10日线了，则去判断是否要止损，或者是否是最后一坑
		//返回值=1，是鳄鱼闭嘴
		//返回值=2，是下穿30日均线，止损
		//返回值=3，是平均日涨幅很好，止盈
		//返回值=4， 新的合嘴
		//返回值=-1，是不要卖
		if( ((gap1-gap0)>lastdayClose*0.003) 
				&& (gap0>0) 
				&& (gap0<=lastdayClose*0.035) 
				&& (Math.abs(gap1)>(lastdayClose*0.015))) return 1;
		else 
		{
			//if(ReachCloseMouth(gap05,gap15,lastdayClose)) return 4;
			
			//到达止损点，则止损
			//if(ReachLossLimit(lastdayClose,p30) 
			//	//&& !LastPit(p5,p10,p20,p30,p60,p60_ago)
			//	) return 2;
			
			//到达止盈点，则止盈，理论告诉我不应该止盈
			//if(ReachWinLimit(lastdayClose,holdinfo,index,buyindex)) return 3;
			
			//碰到回调，如果有利润就逃
			//if(ReachYinLimit(lastdayClose,last2dayClose,lastdayOpen,holdinfo)) return 4;
			
			//时间控制不好
			//if(ReachTimeLimit(index,buyindex)) return 4;
			return -1;
		}
	}

	
	private static boolean ReachYinLimit(Float lastdayClose,
			Float last2dayClose, Float lastdayOpen, HoldingStock holdinfo) {
		// TODO Auto-generated method stub
		if(	lastdayClose < (last2dayClose*0.96f) 
				&& lastdayClose/holdinfo.buyprice>1.07  
				)
			return true;
		return false;
	}

	private static boolean ReachCloseMouth(Float gap05, Float gap15,
			Float lastdayClose) {
		if( ((gap15-gap05)>lastdayClose*0.01) && gap05>0 && (Math.abs(gap15)>(lastdayClose*0.02))) return true;
		else return false;
	}

	//提高资金利用率
	private static boolean ReachTimeLimit(int index, int buyindex) {
		if(buyindex-index > 2) return true;
		return false;
	}

	private static boolean LastPit(Float p5, Float p10, Float p20,
			Float p30, Float p60, Float p60_ago) {
		Float average = (p5+p10+p20+p30+p60)/5;
		Float low = average * (1-0.04f);
		Float high = average * (1 + 0.04f);
		
		if(MathUtil.isBetween(p5, low, high)
			&& MathUtil.isBetween(p10, low, high)
			&& MathUtil.isBetween(p20, low, high)
			&& MathUtil.isBetween(p30, low, high)
			&& MathUtil.isBetween(p60, low, high)
			&& p60>p60_ago
			&& p60<=p60_ago*1.03 )
			return true;
		else
			return false;
	}

	//止盈，一天涨幅超过2.5%，两天超过5%，就撤
	private static boolean ReachWinLimit(Float lastdayClose,
			HoldingStock holdinfo, int index, int buyindex) {
		if((lastdayClose-holdinfo.buyprice)/holdinfo.buyprice/(buyindex-index)>0.035 ) return true;
		return false;
	}

	//止损点的设置
	private static boolean ReachLossLimit(Float lastdayClose, Float averagePrice) {
		// TODO Auto-generated method stub
		if(lastdayClose < averagePrice * 0.92f) return true;
		else return false;
	}

	private static Float getGap(ArrayList<StockDaily> dailydata, int index,
			int shortterm, int longterm) {
		Float p_short = getAveragePrice(dailydata,index,shortterm);
		Float p_long = getAveragePrice(dailydata,index,longterm);
		return p_short-p_long;
	}

	//计算平均价
	private static Float getAveragePrice(ArrayList<StockDaily> dailydata,
			int index, int averageday) {
		Float tmpTotal = 0f;
		int cnt = 0;
		int start = index + 1;
		int end = index + averageday;
		if(end>=dailydata.size()) end = dailydata.size() - 1;
		if(start>=dailydata.size()) start = dailydata.size() -1;
		if(start<=0) start = 1;
		if(end<=0) end = 1;
		
		for(int i=start;i<=end;++i)
		{
			tmpTotal += dailydata.get(i).getClose();
			cnt++;
		}
		return tmpTotal/cnt;
	}

	public static ArrayList<StockDaily> doBuySelect(Date currentday,
			HashSet<String> mystockpool, MapExpert me) {
		ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
		for(String symbol:mystockpool)
		{
			ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
			if(dailydata==null) continue;
			int index = ScopeUtil.getOnlyIndex(currentday, dailydata);
			if(index==-1) continue;				//如果当天停牌就是不能买
			if(CanBuy(index,dailydata))
			{
				rtlist.add(dailydata.get(index));
			}
		}
		return rtlist;
	}

	public static ArrayList<StockDaily> doCurrentSelect(Date currentday,
			HashSet<String> mystockpool, MapExpert me) {
		ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
		for(String symbol:mystockpool)
		{
			if(me.getAlldata().get(symbol)!=null)
			{
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				if(dailydata==null) continue;
				int index = ScopeUtil.getOnlyIndex(currentday, dailydata);
				if(index==-1) continue;				//如果当天停牌就是不能买
				if(CanBuy(index-1,dailydata))
				{
					rtlist.add(dailydata.get(index));
				}
			}
		}
		return rtlist;
	}
	
	
	private static boolean CanBuy(int index, ArrayList<StockDaily> dailydata) {
		if(index>=dailydata.size()-1) return false;
		Float lastdayClose = dailydata.get(index+1).getClose();
		//Float last3dayClose = dailydata.get(index+3).getClose();
		
		Float p10 = getAveragePrice(dailydata, index, 10);
		Float p20 = getAveragePrice(dailydata, index, 20);
		Float p30 = getAveragePrice(dailydata, index, 30);
		Float p30_ago = getAveragePrice(dailydata,index+10,30);
		Float p20_ago = getAveragePrice(dailydata,index+10,20);
		Float p120_ago = getAveragePrice(dailydata,index+10,120);
		Float p60_ago = getAveragePrice(dailydata,index+10,60);
		Float p60 = getAveragePrice(dailydata, index, 60);
		Float p120 = getAveragePrice(dailydata, index, 120);
		Float p250 = getAveragePrice(dailydata,index,250);
		Float p250_ago = getAveragePrice(dailydata,index+20,250);
		Float p5 = getAveragePrice(dailydata, index ,5);
		
		Float gap0 = getGap(dailydata,index,5,10);
		Float gap1 = getGap(dailydata,index+1,5,10);
		Float totalTurnOver = MountAhead(dailydata,index,30,10);
		
		if(		IsBull(p20,p30,p20_ago,p30_ago)
				//&& IsBull(p30,p60,p30_ago,p60_ago)
				//&& IsBull(p120,p250,p120_ago,p250_ago)
				//&& IsBull(p60,p120,p60_ago,p120_ago)        //这是大熊市的保险，牛市可以不用
				//&& (lastdayClose>=p60)						//牛市无益
				//&& (!IsDroping(p5,p10,gap0,gap1))			//急跌不买
				//&& (!IsDroping(lastdayClose,last3dayClose)) //也是另一种急跌不买
				//&& (Droping(dailydata,index,3)>=-0.07)	//三天跌幅超过7%
				//&& (p30 > p60*0.99f)						//60日线不能还在上面压着，起码要差不多
				//&& (totalTurnOver<50)						//放巨量换手不买
				//&& MathUtil.isNear(lastdayClose,p30,2f)
				&& MathUtil.isNear(lastdayClose,p20,1.5f)
				&& MathUtil.isNear(lastdayClose,p30,1.5f)
				&& lastdayClose<17						//限制价格是不必要的
				) return true;
		else
			return false;
		
	}

	private static boolean IsDroping(Float lastdayClose, Float last3dayClose) {
		if((last3dayClose-lastdayClose)/lastdayClose>0.055) return true;
		else return false;
	}

	private static Float Droping(ArrayList<StockDaily> dailydata, int index,
			int timespan) {
		// TODO Auto-generated method stub
		int ago = index+timespan;
		int now = index+1;
		if(ago>=dailydata.size()) ago = dailydata.size()-1;
		if(ago<=0) ago = 1;
		if(now>=dailydata.size()) now = dailydata.size()-1;
		if(now<=0) now = 1;
		
		Float HighDayAgo = dailydata.get(ago).getHigh();
		Float lastdayClose = dailydata.get(now).getClose();
		
		return (lastdayClose-HighDayAgo)/lastdayClose;
		
	}

	private static Float MountAhead(ArrayList<StockDaily> dailydata, int index,
			int beforeday,int addday) {
		Float rtTurnOver = 0f;
		if(dailydata==null || dailydata.size()==0) return rtTurnOver;
		int end = index + beforeday;
		int start = index + 1;
		if(end>=dailydata.size()) end = dailydata.size() - 1;
		if(start>=dailydata.size()) start = dailydata.size() -1 ;
		Integer bigvolumeindex = null;
		Float maxTurnOver = null;
		for(int i=start;i<end;++i)
		{
			if(dailydata.get(i).getTurnover()>dailydata.get(i+1).getTurnover() * 4)
			{	
				bigvolumeindex = i;
				break;
			}
			else
			{
				if(maxTurnOver==null || maxTurnOver<dailydata.get(i).getTurnover())
				{	
					maxTurnOver = dailydata.get(i).getTurnover();
					bigvolumeindex = i;
				
				}
			}
			
		}
		if(bigvolumeindex!=null)
		{
			start = bigvolumeindex - addday;
			end = bigvolumeindex;
			if(start<=0) start = 1;
			
			for(int i=start;i<=end;++i)
			{
				rtTurnOver += dailydata.get(i).getTurnover();
			}
		}
		return rtTurnOver;
	}

	//短线是牛市就可以
	private static boolean IsBull(Float shortterm, Float longterm, Float short_ago,
			Float long_ago) {
		if( (shortterm>longterm) 
				&& shortterm>short_ago 
				&& longterm>long_ago) return true;
		else return false;
	}

	private static boolean IsDroping(Float p5, Float p10, Float gap0, Float gap1) {
		//如果5日线已经在10日线之下，而且还在张口，就算急跌
		if(p5<p10 && ((p10-p5)/p5>0.018)
				&& Math.abs(gap0)>Math.abs(gap1) 
				&& Math.abs(gap0)>p5*0.015 ) return true;
		else return false;
	}

	private static boolean IsBull(Float p30, Float p60, Float p120,
			Float p30_ago, Float p60_ago, Float p120_ago) {
		//判断是否牛市排列
		if(
				p60>p120
				&& p60_ago>p120_ago
				&& p60>p60_ago
				&& p120>p120_ago
			)
			return true;
		else 
			return false;
		
	}

	
	
}
