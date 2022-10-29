package org.fuxin.passcode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.analyst.MapExpert;
import org.fuxin.simulator.Account;
import org.fuxin.simulator.HoldingStock;
import org.fuxin.util.FuOutput;
import org.fuxin.util.ScopeUtil;
import org.fuxin.vo.StockDaily;

public class CodeSelector {

	/***
	 * ��ͣ�����ѡ��
	 * @param currentday
	 * @param mystockpool
	 * @param me
	 * @return
	 */
	public static ArrayList<StockDailyWithReason> doSelect(Date currentday,
			HashSet<String> mystockpool, MapExpert me,CodeChooseLimit chooselimit) {
		ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
		for(String symbol:mystockpool)
		{
			if(me.getAlldata().get(symbol)!=null)
			{
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				int index = ScopeUtil.getIndex(currentday, dailydata);
				if(index>=0) 
				{
					CodeScore score = GetGoodCode(symbol,dailydata,index,chooselimit);
					//����ϸ�о�һ����ͣ���룬�ʹ�ӡ������
					//System.out.println(score);
					if(score!=null) 
					{
						rtlist.add(new StockDailyWithReason(dailydata.get(index),score));
					}
				}
			}
		}
		//System.out.println("rtlist:"+rtlist);
		return rtlist;
	}

	private static CodeScore GetGoodCode(String symbol,
			ArrayList<StockDaily> dailydata, int index,
			CodeChooseLimit chooselimit) {
		//ֻ�ܿ�ǰһ������ݣ���������ݲ��ܿ���������������
		int lastdayindex = index + 1; 
		if(lastdayindex>=dailydata.size()) return null;
		Float NowPrice = dailydata.get(lastdayindex).getClose();
		Float NowTurnover = dailydata.get(lastdayindex).getTurnover();
		CodeScore codescore = getCodeScore(symbol,dailydata,lastdayindex,chooselimit.timespan); 
		//System.out.println(codescore.show());
		
		
		if(codescore.score>=chooselimit.minscore
			&& codescore.score<=chooselimit.maxscore
			//&& (
			//codescore.legList.size()>0
			//&& codescore.legList.get(0).index==lastdayindex
			//)
			&& ( 
			codescore.drList.size()>0
				&& codescore.drList.get(0).index==lastdayindex
			)
			&& NowPrice<=chooselimit.maxprice
			&& codescore.vupdownList.size()>=chooselimit.minVList
			&& codescore.maxVolumeMinVolumeRate<=chooselimit.maxVolumeRate
			&& codescore.maxVolumeMinVolumeRate>=chooselimit.minVolumeRate
			&& codescore.fakeGreenList.size()>=chooselimit.minFList
			&& codescore.lysvList.size()<=chooselimit.maxLysvList
			&& codescore.maxVolume<=chooselimit.maxVolume
			&& codescore.longHeadList.size() >= chooselimit.minLongHeadList
			&& codescore.RecentRaise<=1.26
			&& codescore.yangRate>=chooselimit.minYangrate
			//&& ( codescore.HalfYearMaxPrice.sd.getHigh()/codescore.HalfYearMinPrice.sd.getLow()>=1.2
			//&& codescore.HalfYearMaxPrice.sd.getHigh()/codescore.HalfYearMinPrice.sd.getLow()<=1.8 
			//&& (codescore.LeftTop.sd.getHigh()*0.97) <= NowPrice
			//&& NowTurnover<=codescore.minT.sd.getTurnover()*1.3
			
			//���֮����Ĳ�Ӧ���õ�ͣ�б�����ѡ��
			//&& codescore.dietingList.size()==0
			&& codescore.goldList.size()>= chooselimit.mingoldList
			&& codescore.goldList.size()<= chooselimit.maxgoldList
			//&& codescore.zhangtingList.size()==0
				)
		{
			return codescore;
		}	
		return null;
		//���о���ͣ����Ϊʲô��Ч���Ϳ�����
		//return codescore;
	}

	/***
	 * timespan�����ؼ�������
	 * @param symbol
	 * @param dailydata
	 * @param startindex
	 * @param timespan
	 * @return
	 */
	//����÷��Ǹ��ֻ�����ִ���֮��
	private static CodeScore getCodeScore(String symbol,
			ArrayList<StockDaily> dailydata, int startindex, int timespan) {
		// TODO Auto-generated method stub
		// ������ͣ��������⣬����ǰ������
		CodeScore cscore = new CodeScore();
		//���ؼ�������
		cscore.getGene(symbol,dailydata,startindex,timespan);
			
		return cscore;
		
		
	}

	/***
	 * �˴�������ʤ������ʤ����С���������룬��ԭ��
	 * @param currentday
	 * @param account
	 * @param me
	 * @return
	 */
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
				int sellReason = CanSell(index,dailydata);
				if(sellReason>0)
				{
					//System.out.println(sellReason);
					rtlist.add(dailydata.get(index));
				}
			}
		}
		return rtlist;
	}

	private static int CanSell(int index, ArrayList<StockDaily> dailydata) {
		
		if((index+10)>=dailydata.size()) return -1;
		int lastindex= index+1;					//�������죬ֻ�ܴ�ǰһ�쿪ʼ��
		//�������ҵ����������
		Candle yang = GetLeftYang(lastindex,dailydata);   
		if(yang==null) return 4;				//4����ʮ��û�������£�Ӧ�ú���
		Candle lastday = new Candle(dailydata.get(lastindex),dailydata.get(lastindex+1),lastindex);
		
		if(lastday.YingYang == -1
			&& lastday.raiseRate < 0    
			&& lastday.sd.getClose()<yang.sd.getOpen()
			)
		{
			Float lianV=getLianV(dailydata,lastindex);
			if(lianV > yang.sd.getTurnover()) return 1;			//1��ʾ��ʤ������
		}
		return -1;							//���򷵻�-1,���ǲ�����
		
		
	}

	//���ص�����������������������
	private static Float getLianV(ArrayList<StockDaily> dailydata,
			int lastindex) {
		Float totalTo = 0f;
		int endindex = lastindex + 2;
		if(endindex>=dailydata.size()) endindex = dailydata.size()-1;
		StockDaily today = dailydata.get(lastindex);
		StockDaily yesterday = dailydata.get(lastindex+1);
		StockDaily dayBeforeYesterday = dailydata.get(endindex);
		Candle yesterC = new Candle(yesterday,dayBeforeYesterday,lastindex+1);
		if(yesterC.YingYang<=0)
		{
			totalTo = today.getTurnover()+yesterday.getTurnover();
		}
		else
			totalTo = today.getTurnover();
		return totalTo;
	}

	private static Candle GetLeftYang(int index, ArrayList<StockDaily> dailydata) {
		Candle rtcandle = null;
		for(int i=index;i<=index+9;++i)
		{
			rtcandle=new Candle(dailydata.get(i),dailydata.get(i+1),i);
			if(rtcandle.raiseRate>0.01 ) return rtcandle;
		}
		return null;
	}

	//��ǰѡ�񣬼���֪������������
	public static ArrayList<StockDailyWithReason> doCurrentSelect(
			Date currentday, HashSet<String> stockpool, MapExpert me,CodeChooseLimit chooselimit) {
		ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
		for(String symbol:stockpool)
		{
			if(me.getAlldata().get(symbol)!=null)
			{
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				int index = ScopeUtil.getIndex(currentday, dailydata);
				if(index>=0) 
				{
					CodeScore score = GetGoodCode(symbol,dailydata,index-1,chooselimit);
					if(score!=null) 
					{
						rtlist.add(new StockDailyWithReason(dailydata.get(index),score));
					}
				}
			}
		}
		return rtlist;
	}
	
	
}
