package org.fuxin.volume;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import org.fuxin.analyst.MapExpert;
import org.fuxin.report.SelectReport;
import org.fuxin.util.DateUtil;
import org.fuxin.util.ScopeUtil;
import org.fuxin.vo.StockDaily;


public class VolumeSelector {

	public HashSet<SelectReport> doSelect(MapExpert me, Date selectday) {
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
				int endindex = ScopeUtil.getOnlyIndex(selectday, dailydata);
				//下面这句是纯随机选
				//if(IsFitVolumeShape2(symbol,dailydata))
				if(endindex == -1) continue;    //如果当天该股票没有交易;
				TwoMount tw = GetTwoMountShape(symbol,dailydata,endindex);
				//System.out.println(tw);
				if(tw.findFirst && tw.findSecond &&
						(tw.firstMountPrice/tw.secondMountPrice<1.2 )
					&& (tw.bottomAfterFirstHill.price/tw.globalBottomPrice.price<1.2 && tw.bottomAfterFirstHill.price/tw.globalBottomPrice.price>1 )
					//&& (tw.nowPrice/tw.globalBottomPrice.price<2.5 && tw.nowPrice!=0)
					&& (tw.smallTopBetweenTwoRatio<0.6)
					//&& (tw.firstTopPrice/tw.firstFootPrice<1.45)
					//&& ((tw.recentTop.price/tw.recentBottom.price<1.29 && tw.recentBottom.index > tw.recentTop.index)
					//|| tw.recentBottom.index <tw.recentTop.index )
					//&& (tw.maxPriceAfterSecond/tw.minPriceBeforeSecond<1.8)
					//&& (tw.average120>tw.average250)//&& (tw.maxPriceAfterSecond/tw.secondFootPrice<1.3)
					)
					//&& (tw.recentMaxVolume/(tw.firstMountVolume+tw.secondMountVolume))<0.3
					//&& (tw.firstMountVolume/tw.secondMountVolume)>0.66)
				{
					SelectReport singleSelect = new SelectReport(symbol,dailydata,null);
					singleSelect.setFinance(me.getFinancedata().get(symbol));
					singleSelect.setTwomount(tw);
					selectreportlist.add(singleSelect);
				}else
				{
					/*
					if(!tw.findFirst) sop("没有后山！");
					if(!tw.findSecond) 
					{
							sop("没有前山!");
					}
					else
					{
						
						if((tw.firstMountPrice/tw.secondMountPrice)>=1.25) sop("后山太高，不平");
						if((tw.maxPriceAfterSecond / tw.minPriceBeforeSecond)>=1.8) sop("前山涨太多了");
						if (tw.recentTop.price/tw.recentBottom.price >= 1.29) sop("最近涨太高");
						if ( tw.firstTopPrice/tw.firstFootPrice >= 1.5 ) sop("后山涨太高");
						//if (tw.smallTopBetweenTwoRatio >= 0.6)  sop("两山间有小山太高");
						if (tw.recent5raise>=0.1) sop("已经启动了！");
						if ((tw.recentMaxVolume/(tw.firstMountVolume+tw.secondMountVolume))>0.3) sop("已经启动并且放量！");
						if (tw.average120<tw.average250*0.99) sop("还是熊市形态");
					}
					*/
					
				}
			}
		}
		
		return selectreportlist;
	}

	
	private void sop(Object obj) {
		System.out.println(obj);
	}


	//其实是随机选
	private boolean IsRandomSelect(String symbol,
			ArrayList<StockDaily> dailydata) {
		Random rand = new Random();
		if(rand.nextInt(100)<10) return true;
		else return false;
	}

	private TwoMount GetTwoMountShape(String symbol,
			ArrayList<StockDaily> dailydata, int endindex) {
		//判断形态是否为想要的
		//先倒推200天，找两座量的山
			
		TwoMount twomount = new TwoMount();
		
		Float after_dayAverageV = 0f;
		Float before_dayAverageV = 0f;
		
		int maxlimit = endindex + 200;
		int start = endindex + 10;
		
		if(maxlimit<=0) maxlimit = 10;
		if(start<=0) start = 10;
		if(maxlimit>=dailydata.size()) maxlimit = dailydata.size()-1;
		if(start>=dailydata.size()) start = dailydata.size() -1;

		for(int i=start;i<=maxlimit;++i)
		{
			//某日是前50日，后50日最高点，并且是5日平均的2倍，被认为是一座山
			Float currentVolume = (float)dailydata.get(i).getVolume();
			//计算5日平均量
			before_dayAverageV = getAverageVolume(dailydata,i+1,i+12);
			after_dayAverageV = getAverageVolume(dailydata,i-12,i-1);
			
			if(IsMax(currentVolume,i,40,dailydata) 
					&& IsTimes(currentVolume,before_dayAverageV,2)
					&& IsTimes(currentVolume,after_dayAverageV,1.5)
					//&& NoSmallMount(currentVolume,10,40,i,dailydata)
					)
			{
				if(twomount.findFirst)  //如果已经找到第一座山，那么找到第二座山
				{
					twomount.findSecond = true;
					twomount.secondMountIndex = i;
					twomount.secondMountDate = dailydata.get(i).getDate();
					twomount.secondMountPrice = dailydata.get(i).getHigh();
					twomount.secondMountVolume = (float)dailydata.get(i).getVolume();
					twomount.minPriceBeforeSecond = getMinPriceBS(dailydata,i,100);
					twomount.maxPriceAfterSecond = getMaxPriceAS(dailydata,i,10);
					
					//这山近日来不能乱涨，乱涨涨完风险就大了
					//所以要检查最近的高点和最近的低点，震幅不能太大
					twomount.recentBottom = getRecentBottom(dailydata,25);
					twomount.recentTop = getRecentTop(dailydata,25);
					twomount.smallTopBetweenTwoRatio = getSmallRatio(twomount.firstMountIndex,twomount.secondMountIndex,7,dailydata);
					twomount.recent5raise = getRecent5Raise(dailydata);
					twomount.recentMaxVolume = getRecentMaxVolume(dailydata,5);
					twomount.globalBottomPrice = getGlobalBottomPrice(dailydata,twomount.secondMountIndex);
					twomount.bottomAfterFirstHill = getBottomAfterFirstHill(dailydata,twomount.firstMountIndex);
					twomount.secondFootPrice = getSecondFootPrice(dailydata,i,20);
					twomount.nowPrice = getNowPrice(dailydata);
					twomount.average250 = getAveragePrice(dailydata,0,250);
					twomount.average120 = getAveragePrice(dailydata,0,120);
					return twomount;
				}
				else  //找到第一座山
				{
					twomount.findFirst = true;
					twomount.firstMountIndex = i;
					twomount.firstMountDate = dailydata.get(i).getDate();
					twomount.firstMountPrice = dailydata.get(i).getHigh();
					twomount.firstMountVolume = (float)dailydata.get(i).getVolume();
					twomount.firstFootPrice = getMinPriceBS(dailydata,i,20);
					twomount.firstTopPrice = getMaxPriceAS(dailydata,i,10);
				}
			}
				
		}
		return twomount;
	}

	private Float getAveragePrice(ArrayList<StockDaily> dailydata, int start, int end) {
		if(start<0) start = 0;
		if(end<0)   end = 0;
		if(start>end) 
		{
			int tmp = end;
			end = start;
			start = end;
		}
		if(end>=dailydata.size()) 
			end = dailydata.size()-1;
		int cnt = 0;
		Float totalPrice = 0f;
		for(int i=start;i<=end;++i)
		{
			cnt++;
			totalPrice= totalPrice+dailydata.get(i).getClose();
		}
		return (float)totalPrice/cnt;
	}


	private Float getNowPrice(ArrayList<StockDaily> dailydata) {
		// TODO Auto-generated method stub
		if(dailydata.size()>0)
			return dailydata.get(0).getClose();
		else
			return 0f;
	}


	private Float getSecondFootPrice(ArrayList<StockDaily> dailydata, int index,
			int span) {
		return getMinPriceBS(dailydata,index,span);
		
	}


	//首先要有最高峰，然后周围要有保驾护航的山峰，形成一座高山
	private boolean IsHill(Float currentVolume, int index, int tonexthill, int hillspan,
			ArrayList<StockDaily> dailydata) {
		if(IsMax(currentVolume,index,tonexthill,dailydata)
				&& 
				BecomeHill(index,hillspan,dailydata))
			return true;
		else
			return false;
	}

	
	//放量一段时间，形成一座山峰
	private boolean BecomeHill(int index, int hillspan,
			ArrayList<StockDaily> dailydata) {
		int start = index - hillspan;
		int end = index + hillspan;
		for(int i=start;i<=end;++i)
		{
			if(dailydata.get(i).getTurnover() <= getAverageTurnOver(dailydata,index-15,index+15) * 1.5)
				return false;
		}
		return true;
	}

	//获得某个时期的平均换手率
	private double getAverageTurnOver(ArrayList<StockDaily> dailydata, int start,
			int end) {
		if(start<0) start = 0;
		if(end<0)   end = 0;
		if(start>end) 
		{
			int tmp = end;
			end = start;
			start = end;
		}
		if(end>=dailydata.size()) 
			end = dailydata.size()-1;
		int cnt = 0;
		Float totalTurnover = 0f;
		for(int i=start;i<=end;++i)
		{
			cnt++;
			totalTurnover= totalTurnover+dailydata.get(i).getTurnover();
		}
		return (float)totalTurnover/cnt;
		
	}


	private StockPoint getBottomAfterFirstHill(ArrayList<StockDaily> dailydata,
			int firstMountIndex) {
		return getRecentBottom(dailydata,firstMountIndex);
	}


	private StockPoint getGlobalBottomPrice(ArrayList<StockDaily> dailydata, int secondMountIndex) {
		return getRecentBottom(dailydata,secondMountIndex);
	}


	private Float getRecentMaxVolume(ArrayList<StockDaily> dailydata, int recentday) {
		int end;
		if(recentday>=dailydata.size()) end = dailydata.size() -1;
		else
			end = recentday;
		Float maxVolume = null;
		for(int i=0;i<=end;++i)
		{
			if(maxVolume==null||maxVolume<dailydata.get(i).getVolume())
				maxVolume = (float) dailydata.get(i).getVolume();
		}
		return maxVolume;
		
	}


	private Float getRecent5Raise(ArrayList<StockDaily> dailydata) {
		int end;
		if(dailydata.size()>5) end = 5;
		else end = dailydata.size()-1;
		return dailydata.get(0).getClose()/dailydata.get(end).getOpen()-1;
	}


	private Float getSmallRatio(int firstMountIndex, int secondMountIndex,
			int hillspan, ArrayList<StockDaily> dailydata) {
		Float volumeAverage = (float) ((dailydata.get(firstMountIndex).getVolume() +
				dailydata.get(secondMountIndex).getVolume())/2);
		Float maxVolume = null;
		int trycnt = 0;
		for(int i=firstMountIndex+hillspan;i<=secondMountIndex-hillspan;++i)
		{
			//如果当天不是试盘放量，当中试盘放量只能有4次机会
			if( trycnt>3 || !isTryRaise(dailydata,i) )
			{
				if(maxVolume==null || maxVolume<dailydata.get(i).getVolume())
					maxVolume = (float)dailydata.get(i).getVolume();
			}
			else
			{
				trycnt++;
			}
		}
		if(maxVolume==null) return 0f;
		return maxVolume/volumeAverage;
	}


	private boolean isTryRaise(ArrayList<StockDaily> dailydata, int index) {
		if(index>1)
		{
			//换手率翻倍而且试盘，有一根大于百分之三的上影线
			if(dailydata.get(index).getTurnover()> dailydata.get(index+1).getTurnover()*1.8
				&& 	(dailydata.get(index).getHigh()-dailydata.get(index).getLow())/dailydata.get(index).getClose()>=0.05)
				return true;
			else 
				return false;
			
		}
		return false;
	}


	//找到最近的最高价
	private StockPoint getRecentTop(ArrayList<StockDaily> dailydata, int span) {
		StockPoint sp = new StockPoint();
		for(int i=0;i<span;++i)
		{
			if(sp.price == null || sp.price < dailydata.get(i).getHigh() )
			{
				sp.index = i;
				sp.date = dailydata.get(i).getDate();
				sp.price  = dailydata.get(i).getHigh();
			}
		}
		return sp;
		
	}

	//找到最近的最低价
	private StockPoint getRecentBottom(ArrayList<StockDaily> dailydata, int span) {
		StockPoint sp = new StockPoint();
		for(int i=0;i<span;++i)
		{
			if(sp.price == null || sp.price > dailydata.get(i).getLow() )
			{
				sp.index = i;
				sp.date = dailydata.get(i).getDate();
				sp.price  = dailydata.get(i).getLow();
			}
		}
		return sp;
	}

	//找到前山后的最高价
	private Float getMaxPriceAS(ArrayList<StockDaily> dailydata, int index, int lefthillside) {
		
		int start = index;     //开始点
		int end = index - lefthillside;					  //结束点
		if(end<0) end = 0;
		Float rtMaxprice = null;
		for(int i=start;i>=end;--i)
		{
			if( rtMaxprice==null || dailydata.get(i).getHigh() > rtMaxprice)
				rtMaxprice = dailydata.get(i).getHigh();
		}
		return rtMaxprice;
	}

	//找到前山前的最低价
	private float getMinPriceBS(ArrayList<StockDaily> dailydata, int index, int lefthillside) {
		int start = index + lefthillside;     //开始点
		if(start>=dailydata.size()) start = dailydata.size()-1;
		
		int end = index;					  //结束点
		Float rtMinprice = null;
		for(int i=start;i>=end;--i)
		{
			if( rtMinprice==null || dailydata.get(i).getLow() < rtMinprice)
				rtMinprice = dailydata.get(i).getLow();
		}
		return rtMinprice;
		
	}

	//中间不能老是有小山，必须是平原
	private boolean NoSmallMount(Float currentVolume, int mountspan, int plainspan, int index,
			ArrayList<StockDaily> dailydata) {
		
		
		int leftstart = index + mountspan;
		int leftend = index + (plainspan - mountspan);
		int rightstart = index - (plainspan - mountspan);
		int rightend = index - mountspan;
		
		if(rightstart<0) rightstart = 0;
		if(rightend<0) rightend = 0;
		
		if(leftend>=dailydata.size()) leftend = dailydata.size()-1;
		if(leftstart>=dailydata.size()) leftstart = dailydata.size()-1;
		
		for(int i=leftstart;i<=leftend;++i)
		{
			if( dailydata.get(i).getVolume() * 2 > currentVolume) return false;
		}
		for(int i=rightstart;i<=rightend;++i)
		{
			if(dailydata.get(i).getVolume() * 1.5 > currentVolume) return false;
		}
		return true;
	}

	//是否超过多少倍，山要足够高
	private boolean IsTimes(Float currentVolume, Float five_dayV, double d) {
		if( currentVolume >= five_dayV * d) return true;
		else return false;
	}

	//判断是否山峰,比一个时间范围内最高值的0.9大的，也算一个山峰
	private boolean IsMax(Float currentVolume, int index, int timespan, ArrayList<StockDaily> dailydata) {
		int start = getStartIndex(dailydata, index,timespan);
		int end = index + timespan;
		
		if(start<10) start=10;
		if(end>=dailydata.size()) end = dailydata.size()-1;
		
		for(int i = start ; i<=end; ++i)
		{
			Float tmpVolume = (float)dailydata.get(i).getVolume();
			if( tmpVolume > currentVolume ) return false;
		}
		
		return true;
	}

	//如果中间停牌很久，则StartIndex要减少
	private int getStartIndex(ArrayList<StockDaily> dailydata, int index, int timespan) {
		int afterDays = (int) (timespan * 1.5);
		Date startdate = DateUtil.Add(dailydata.get(index).getDate(), afterDays);
		return getIndexFromDate(dailydata,startdate);
	}

	//从日期得到开始的index
	private int getIndexFromDate(ArrayList<StockDaily> dailydata, Date startdate) {
		for(int i=0;i<dailydata.size();++i)
		{
			if(dailydata.get(i).getDate().compareTo(startdate)<=0)
				return i;
		}
		return -1;
	}


	//计算平均成交量
	private Float getAverageVolume(ArrayList<StockDaily> dailydata, int start, int end) {
		if(start<0) start = 0;
		if(end<0)   end = 0;
		if(start>end) 
		{
			int tmp = end;
			end = start;
			start = end;
		}
		if(end>=dailydata.size()) 
			end = dailydata.size()-1;
		int cnt = 0;
		Float totalVolume = 0f;
		for(int i=start;i<=end;++i)
		{
			cnt++;
			totalVolume= totalVolume+dailydata.get(i).getVolume();
		}
		return (float)totalVolume/cnt;
	}

	/***
	 * 随机选择
	 * @param me
	 * @return
	 */
	public HashSet<SelectReport> doRandomSelect(MapExpert me) {
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
				//下面这句是纯随机选
				if(IsRandomSelect(symbol,dailydata))
				{
					SelectReport singleSelect = new SelectReport(symbol,dailydata,null);
					singleSelect.setFinance(me.getFinancedata().get(symbol));
					selectreportlist.add(singleSelect);
				}
			}
		}
		
		return selectreportlist;
	}


}
