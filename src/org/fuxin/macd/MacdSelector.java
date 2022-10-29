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
 * �������һ������MACDѡ����
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
					//����ǰһ���MACDֵ
					index = index + 1;    //���в���ʡ��������������
					if(index>=dailydata.size()) index = dailydata.size()-1;
					
					Float lastdayClose = dailydata.get(index).getClose();
					MacdScore today = GetMacdScore(symbol,dailydata,index);
					MacdScore yesterday = GetMacdScore(symbol,dailydata,index+1);
					
					//System.out.println("---------------------");
					//System.out.println(today);
					//System.out.println(yesterday);
					//����γɷ��������Ľ�棬���������
					if(IsGolden(today,yesterday,lastdayClose)) 
					{
						//���صı����ǵ���ģ���Ȼ����������ǰһ��
						rtlist.add(new StockDailyWithReason(dailydata.get(index-1),today));
					} 
				}
			}
		}
		return rtlist;
	}

	/***
	 * MACD��涨��
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
	 * ȡ�øù�Ʊ���յ�MACD����ֵ
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
	 * ��ǰ��ѡ��
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
					//����ǰһ���MACDֵ
					//index = index + 1;    //���в���ʡ��������������
					Float lastdayClose = dailydata.get(index+1).getClose();
					MacdScore today = GetMacdScore(symbol,dailydata,index);
					MacdScore yesterday = GetMacdScore(symbol,dailydata,index+1);
					//����γɷ��������Ľ�棬���������
					if(IsGolden(today,yesterday,lastdayClose)) 
					{
						//System.out.println("---------------------");
						//System.out.println(today);
						//System.out.println(yesterday);
						//�����ѡ�񣬷���Ҳ�ǵ���
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
			//������Ĳ����Ե�����
			if(!account.holdlist.get(symbol).buydate.equals(currentday))
			{
				HoldingStock holdinfo = account.holdlist.get(symbol);
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				int index = ScopeUtil.getOnlyIndex(currentday, dailydata);
				if(index==-1) continue; 			//�������ͣ�ƾ��ǲ�����
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
	 * �ж��Ƿ�������ۣ�����0����Ҫ����
	 * @param index
	 * @param dailydata
	 * @param holdinfo
	 * @return
	 */
	private static int CanSell(int index, ArrayList<StockDaily> dailydata,
			HoldingStock holdinfo) {
		if(dailydata==null) return -1;
		
		//�����Ǹ���������������
		int selldayindex = index + 1;
		if(dailydata.get(selldayindex)==null) return -1;
		
		String symbol = dailydata.get(selldayindex).getSymbol();
		
		//��ǰ�۸��ǵ������ͼ�
		Float nowPrice = dailydata.get(index).getLow();
		//ǰһ�����̼�
		StockDaily yesterdaySd = dailydata.get(selldayindex);
		Float lastdayClose = dailydata.get(selldayindex).getClose();
		//�������index
		int buyindex = ScopeUtil.getOnlyIndex(holdinfo.buydate, dailydata);
		
		MacdScore today = GetMacdScore(symbol,dailydata,selldayindex);
		MacdScore yesterday = GetMacdScore(symbol,dailydata,selldayindex+1);
		
		//�������˺�ͣ������
		if(  
			yesterday.MACD>=nowPrice*0.01 &&
			today.MACD<(yesterday.MACD-0.01)
			) return 1;
		
		//������û��ɹ����������£����˳�
		if(
				today.MACD <= 0  &&
				today.MACD < (yesterday.MACD - 0.01) 
			)
		return 3;
		
		//������ִ���25�����˳�
		//if( yesterdaySd.getTurnover()>=25 ) 
		//	return 5;
		
		//����ֹ��㣬��ֹ��
		//2016-01-11��Ϊ�����߾���
		if(ReachLossLimit(lastdayClose,holdinfo.buyprice,0.99f) 
				) return 2;
	                               
		//���ʱ�䳬��20�죬��˵��ѡ������Ҳ�߰�
		//if(ReachTimeLimit(index,buyindex,20)) return 4;
		return -1;
	}

	//�ж��Ƿ񵽴���ʱ�����Ƶ�
	private static boolean ReachTimeLimit(int index, int buyindex, int daylimit) {
		if(buyindex-index>=daylimit) return true;
		return false;
	}

	//�ж��Ƿ񵽴���ֹ���
	private static boolean ReachLossLimit(Float lastdayClose, Float buyprice,
			float losslimit) {
		if(lastdayClose<=buyprice*losslimit) return true;
		return false;
	}

	/***
	 * �������۲���
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
			//������Ĳ����Ե�����
			if(!account.holdlist.get(symbol).buydate.equals(currentday))
			{
				HoldingStock holdinfo = account.holdlist.get(symbol);
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				int index = ScopeUtil.getOnlyIndex(currentday, dailydata);
				if(index==-1) continue; 			//�������ͣ�ƾ��ǲ�����
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
