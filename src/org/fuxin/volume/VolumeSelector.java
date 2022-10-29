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
		//�����еĹ�Ʊ����ѡ��
		//����һ��ѡ�ɺ󱨸�
		HashSet<SelectReport> selectreportlist = new HashSet<SelectReport>();
		for(int i=0;i<symbolList.size();++i)
		{
			String symbol = symbolList.get(i);
			ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
			if(dailydata!=null)
			{
				int endindex = ScopeUtil.getOnlyIndex(selectday, dailydata);
				//��������Ǵ����ѡ
				//if(IsFitVolumeShape2(symbol,dailydata))
				if(endindex == -1) continue;    //�������ù�Ʊû�н���;
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
					if(!tw.findFirst) sop("û�к�ɽ��");
					if(!tw.findSecond) 
					{
							sop("û��ǰɽ!");
					}
					else
					{
						
						if((tw.firstMountPrice/tw.secondMountPrice)>=1.25) sop("��ɽ̫�ߣ���ƽ");
						if((tw.maxPriceAfterSecond / tw.minPriceBeforeSecond)>=1.8) sop("ǰɽ��̫����");
						if (tw.recentTop.price/tw.recentBottom.price >= 1.29) sop("�����̫��");
						if ( tw.firstTopPrice/tw.firstFootPrice >= 1.5 ) sop("��ɽ��̫��");
						//if (tw.smallTopBetweenTwoRatio >= 0.6)  sop("��ɽ����Сɽ̫��");
						if (tw.recent5raise>=0.1) sop("�Ѿ������ˣ�");
						if ((tw.recentMaxVolume/(tw.firstMountVolume+tw.secondMountVolume))>0.3) sop("�Ѿ��������ҷ�����");
						if (tw.average120<tw.average250*0.99) sop("����������̬");
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


	//��ʵ�����ѡ
	private boolean IsRandomSelect(String symbol,
			ArrayList<StockDaily> dailydata) {
		Random rand = new Random();
		if(rand.nextInt(100)<10) return true;
		else return false;
	}

	private TwoMount GetTwoMountShape(String symbol,
			ArrayList<StockDaily> dailydata, int endindex) {
		//�ж���̬�Ƿ�Ϊ��Ҫ��
		//�ȵ���200�죬����������ɽ
			
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
			//ĳ����ǰ50�գ���50����ߵ㣬������5��ƽ����2��������Ϊ��һ��ɽ
			Float currentVolume = (float)dailydata.get(i).getVolume();
			//����5��ƽ����
			before_dayAverageV = getAverageVolume(dailydata,i+1,i+12);
			after_dayAverageV = getAverageVolume(dailydata,i-12,i-1);
			
			if(IsMax(currentVolume,i,40,dailydata) 
					&& IsTimes(currentVolume,before_dayAverageV,2)
					&& IsTimes(currentVolume,after_dayAverageV,1.5)
					//&& NoSmallMount(currentVolume,10,40,i,dailydata)
					)
			{
				if(twomount.findFirst)  //����Ѿ��ҵ���һ��ɽ����ô�ҵ��ڶ���ɽ
				{
					twomount.findSecond = true;
					twomount.secondMountIndex = i;
					twomount.secondMountDate = dailydata.get(i).getDate();
					twomount.secondMountPrice = dailydata.get(i).getHigh();
					twomount.secondMountVolume = (float)dailydata.get(i).getVolume();
					twomount.minPriceBeforeSecond = getMinPriceBS(dailydata,i,100);
					twomount.maxPriceAfterSecond = getMaxPriceAS(dailydata,i,10);
					
					//��ɽ�������������ǣ�����������վʹ���
					//����Ҫ�������ĸߵ������ĵ͵㣬�������̫��
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
				else  //�ҵ���һ��ɽ
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


	//����Ҫ����߷壬Ȼ����ΧҪ�б��ݻ�����ɽ�壬�γ�һ����ɽ
	private boolean IsHill(Float currentVolume, int index, int tonexthill, int hillspan,
			ArrayList<StockDaily> dailydata) {
		if(IsMax(currentVolume,index,tonexthill,dailydata)
				&& 
				BecomeHill(index,hillspan,dailydata))
			return true;
		else
			return false;
	}

	
	//����һ��ʱ�䣬�γ�һ��ɽ��
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

	//���ĳ��ʱ�ڵ�ƽ��������
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
			//������첻�����̷������������̷���ֻ����4�λ���
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
			//�����ʷ����������̣���һ�����ڰٷ�֮������Ӱ��
			if(dailydata.get(index).getTurnover()> dailydata.get(index+1).getTurnover()*1.8
				&& 	(dailydata.get(index).getHigh()-dailydata.get(index).getLow())/dailydata.get(index).getClose()>=0.05)
				return true;
			else 
				return false;
			
		}
		return false;
	}


	//�ҵ��������߼�
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

	//�ҵ��������ͼ�
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

	//�ҵ�ǰɽ�����߼�
	private Float getMaxPriceAS(ArrayList<StockDaily> dailydata, int index, int lefthillside) {
		
		int start = index;     //��ʼ��
		int end = index - lefthillside;					  //������
		if(end<0) end = 0;
		Float rtMaxprice = null;
		for(int i=start;i>=end;--i)
		{
			if( rtMaxprice==null || dailydata.get(i).getHigh() > rtMaxprice)
				rtMaxprice = dailydata.get(i).getHigh();
		}
		return rtMaxprice;
	}

	//�ҵ�ǰɽǰ����ͼ�
	private float getMinPriceBS(ArrayList<StockDaily> dailydata, int index, int lefthillside) {
		int start = index + lefthillside;     //��ʼ��
		if(start>=dailydata.size()) start = dailydata.size()-1;
		
		int end = index;					  //������
		Float rtMinprice = null;
		for(int i=start;i>=end;--i)
		{
			if( rtMinprice==null || dailydata.get(i).getLow() < rtMinprice)
				rtMinprice = dailydata.get(i).getLow();
		}
		return rtMinprice;
		
	}

	//�м䲻��������Сɽ��������ƽԭ
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

	//�Ƿ񳬹����ٱ���ɽҪ�㹻��
	private boolean IsTimes(Float currentVolume, Float five_dayV, double d) {
		if( currentVolume >= five_dayV * d) return true;
		else return false;
	}

	//�ж��Ƿ�ɽ��,��һ��ʱ�䷶Χ�����ֵ��0.9��ģ�Ҳ��һ��ɽ��
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

	//����м�ͣ�ƺܾã���StartIndexҪ����
	private int getStartIndex(ArrayList<StockDaily> dailydata, int index, int timespan) {
		int afterDays = (int) (timespan * 1.5);
		Date startdate = DateUtil.Add(dailydata.get(index).getDate(), afterDays);
		return getIndexFromDate(dailydata,startdate);
	}

	//�����ڵõ���ʼ��index
	private int getIndexFromDate(ArrayList<StockDaily> dailydata, Date startdate) {
		for(int i=0;i<dailydata.size();++i)
		{
			if(dailydata.get(i).getDate().compareTo(startdate)<=0)
				return i;
		}
		return -1;
	}


	//����ƽ���ɽ���
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
	 * ���ѡ��
	 * @param me
	 * @return
	 */
	public HashSet<SelectReport> doRandomSelect(MapExpert me) {
		ArrayList<String> symbolList = me.getSymbolList();
		//�����еĹ�Ʊ����ѡ��
		//����һ��ѡ�ɺ󱨸�
		HashSet<SelectReport> selectreportlist = new HashSet<SelectReport>();
		for(int i=0;i<symbolList.size();++i)
		{
			String symbol = symbolList.get(i);
			ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
			if(dailydata!=null)
			{
				//��������Ǵ����ѡ
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
