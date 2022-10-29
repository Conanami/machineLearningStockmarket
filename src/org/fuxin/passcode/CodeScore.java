package org.fuxin.passcode;

import java.util.ArrayList;
import java.util.Date;

import org.fuxin.vo.StockDaily;

/***
 * ��¼������������
 * @author Administrator
 *
 */
public class CodeScore {
	
	//����5������basicinfo
	public String symbol;
	public int startindex;
	public Date startdate;
	public int endindex;
	public Date enddate;
	
	public int score = 0;
	
	public HugeVolume hv;					//����һ���޴����ѣ���ʾǿׯ���ֶ���
	public ArrayList<Candle> drList;		//С�����б�
	public ArrayList<FakeGreen> fakeGreenList;  //���ֵļ��������ĸ���
	public ArrayList<Candle> legList;			//���Ȳ����б�
	public ArrayList<Candle> lysvList;  		//���������б�
	public ArrayList<Candle>  vupdownList;     	//���������б�
	public ArrayList<Candle> longHeadList;		//����Ӱ��
	public ArrayList<Candle> dietingList;		//��ͣ�б�
	public ArrayList<Candle> zhangtingList;		//��ͣ�б�
	public Float maxVolumeMinVolumeRate;		//�����С������֮��
	public Float maxVolume;						//�������
	public Float minVolume;						//��С������
	public Candle minT;							//��С����������
	public Candle LeftTop;						//���
	public Candle HalfYearMaxPrice;				//������߼�
	public Candle HalfYearMinPrice;				//������ͼ�
	public Float RecentRaise;					//��20����Ƿ�
	public ArrayList<Candle> goldList;			//�ƽ���
	public Float yangRate;						//��20�����������ߵı���
	public String isStrong;						//�Ƿ�ǿ��ֵ
	public CodeScore()
	{
		fakeGreenList = new ArrayList<FakeGreen>();
		hv = new HugeVolume();		//���û��һ���޴�����ѣ���ǿׯ���֣���������ƾ�һ��
	}

	public void getGene(String symbol, ArrayList<StockDaily> dailydata,
			int startindex, int timespan) {
		//�ȴ�������������쳣���
		if(dailydata==null) return;
		if(startindex<0) return;
		if(startindex>=dailydata.size()) return;
		if(timespan<=0 || timespan>=600) return;
		if(symbol==null) return;
		
		getBasicInfo(symbol,dailydata,startindex,timespan);
		getFakeGreen(dailydata);				//��ȡ��������
		getHugeVolume(dailydata);				//��ȡ��߳ɽ���
		getDoubleRaise(dailydata);				//��ȡС����
		getLongLeg(dailydata);					//��ȡ���Ȳ���
		getVolumeUpDown(dailydata);				//��ȡ��������
		getLongYingShortVolume(dailydata);		//��ȡ��������
		getLongHeadList(dailydata);				//����Ӱ��
		getDietingList(dailydata);				//��ȡ��ͣ�б�
		getZhangtingList(dailydata,20);			//��ȡ��ͣ�б�
		getGoldList(dailydata,20);				//��ûƽ����б�
		
		int lefttopspan = timespan * 5;
		LeftTop = getLeftTop(dailydata,lefttopspan);		//�õ���壬�չ���崴�¸�Ϊ��
		HalfYearMaxPrice = getLeftTop(dailydata,200);
		HalfYearMinPrice = getLeftBottom(dailydata,200);
		maxVolumeMinVolumeRate = getMaxVolumeMinVolumeRate(dailydata);
		RecentRaise = getRecentRaise(dailydata,20);
		yangRate = getYangRate(dailydata,20);
		score = CalculateScore();						//����÷�
	}
	
	
	private Float getYangRate(ArrayList<StockDaily> dailydata, int days) {
		int tmpendindex = startindex + days - 1;
		if(tmpendindex>=dailydata.size()) tmpendindex = dailydata.size()-1;
		float yangcnt = 0f;
		float yingcnt = 0f;
		//���뿴�����������
		for(int i=startindex ;i<tmpendindex-1;++i)
		{
			
			StockDaily today = dailydata.get(i);    //�ƽ���
			StockDaily yesterday = dailydata.get(i+1);
			//����ж��ǻƽ����������ƽ����б�
			Candle candle = new Candle(today,yesterday,i);
			if(candle.YingYang==1) yangcnt++;  //��������ߣ�������++
			else yingcnt++;					//
		}
		return yangcnt/yingcnt;
		
	}

	//��ȡ�ƽ����б�
	private void getGoldList(ArrayList<StockDaily> dailydata, int days) {
		// ����ƽ����б�Ϊ�գ������ƽ����б�
		if(goldList==null) goldList = new ArrayList<Candle>();
		
		//������������Ϊֹ
		int tmpendindex = startindex + days - 1;
		if(tmpendindex>=dailydata.size()) tmpendindex = dailydata.size()-1;
		
		//���뿴�����������
		for(int i=startindex + 3 ;i<tmpendindex;++i)
		{
			
			StockDaily today = dailydata.get(i);    //�ƽ���
			StockDaily day1 = dailydata.get(i-1);	//��һ��
			StockDaily day2 = dailydata.get(i-2);   //������
			StockDaily day3 = dailydata.get(i-3);   //������
			StockDaily yesterday = dailydata.get(i+1);
			//����ж��ǻƽ����������ƽ����б�
			if(IsGoldZhu(today,day1,day2,day3))
			{
				Candle candle = new Candle(today,yesterday,i);
				goldList.add(candle);
			}
		}
	}

	

	private boolean IsGoldZhu(StockDaily today, StockDaily day1,
			StockDaily day2, StockDaily day3) {
		// TODO Auto-generated method stub
		//�жϻƽ���
		//���ͼ�
		if(IsGoldVolume(today,day1,day2,day3) 
			&& IsGoldPrice(today,day1,day2,day3))
			return true;
		return false;
	}

	//�Ƿ���ϻƽ����ļ۸�����
	private boolean IsGoldPrice(StockDaily today, StockDaily day1,
			StockDaily day2, StockDaily day3) {
		if(today.getClose()>=today.getOpen()*1.02f
				&& (day1.getClose()+day2.getClose()+day3.getClose())/3>today.getClose()
			)
			return true;
		return false;
	}	

	//�ɽ����Ƿ���ϻƽ������ص�
	private boolean IsGoldVolume(StockDaily today, StockDaily day1,
			StockDaily day2, StockDaily day3) {
		if(today.getTurnover()>=day1.getTurnover()*0.7
			&& today.getTurnover()>=day2.getTurnover()*0.7
			&& today.getTurnover()>=day3.getTurnover()*0.7
			&& today.getTurnover()>(day1.getTurnover()+day2.getTurnover()+day3.getTurnover())/3)
			return true;
		return false;
	}

	
	//ȡ����ͣ�б�
	private void getZhangtingList(ArrayList<StockDaily> dailydata, int days) {
		if(zhangtingList==null) zhangtingList = new ArrayList<Candle>();
		int tmpendindex = startindex + days - 1;
		if(tmpendindex>=dailydata.size()) tmpendindex = dailydata.size()-1;
		for(int i=startindex;i<tmpendindex;++i)
		{
			StockDaily today = dailydata.get(i);
			StockDaily yesterday = dailydata.get(i+1);
			Candle candle = new Candle(today,yesterday,i);
			if( candle.raiseRate>=0.095 ) 
				zhangtingList.add(candle);
		}
		
		
	}

	//��ȡ��ͣ�б�
	private void getDietingList(ArrayList<StockDaily> dailydata) {
		if(dietingList==null) dietingList = new ArrayList<Candle>();
		for(int i=startindex;i<endindex;++i)
		{
			StockDaily today = dailydata.get(i);
			StockDaily yesterday = dailydata.get(i+1);
			Candle candle = new Candle(today,yesterday,i);
			if( candle.raiseRate<=-0.095 ) 
				dietingList.add(candle);
		}
		
	}

	/***
	 * �õ���20����Ƿ�
	 * @param dailydata
	 * @param days
	 * @return
	 */
	private Float getRecentRaise(ArrayList<StockDaily> dailydata, int days) {
		if(dailydata==null) return null;
		if(dailydata.get(startindex)==null) return null;
		Float nowPrice = dailydata.get(startindex).getClose();
		int tmpend = startindex + days - 1;
		if(tmpend>=dailydata.size()) tmpend = dailydata.size() - 1;
		Float beforePrice = dailydata.get(tmpend).getOpen();
		return nowPrice/beforePrice;
	}

	private Candle getLeftBottom(ArrayList<StockDaily> dailydata, int lefttopspan) {
		int rtIndex = -1;
		int tmpendindex = startindex + lefttopspan;
		if(tmpendindex>=dailydata.size()) tmpendindex=dailydata.size()-1;
		Float tmpMinprice = null;
		for(int i=startindex;i<tmpendindex;++i)
		{
			StockDaily today = dailydata.get(i);
			if(tmpMinprice==null || tmpMinprice > today.getLow())
			{
				tmpMinprice = today.getLow();
				rtIndex = i;
			}
		}
		if(rtIndex==-1) rtIndex = 0;
		int yesterdayIndex = rtIndex + 1;
		if(yesterdayIndex>=dailydata.size()) yesterdayIndex = dailydata.size()-1;
		
		Candle rtcandle = new Candle(dailydata.get(rtIndex),dailydata.get(yesterdayIndex), rtIndex);
		return rtcandle;	
		
	}


	private Candle getLeftTop(ArrayList<StockDaily> dailydata, int lefttopspan) {
		int rtIndex = -1;
		int tmpendindex = startindex + lefttopspan;
		if(tmpendindex>=dailydata.size()) tmpendindex=dailydata.size()-1;
		Float tmpMaxprice = null;
		for(int i=startindex;i<tmpendindex;++i)
		{
			StockDaily today = dailydata.get(i);
			if(tmpMaxprice==null || tmpMaxprice<today.getHigh())
			{
				tmpMaxprice = today.getHigh();
				rtIndex = i;
			}
		}
		if(rtIndex==-1) rtIndex = 0;
		int yesterdayIndex = rtIndex + 1;
		if(yesterdayIndex>=dailydata.size()) yesterdayIndex = dailydata.size()-1;
		
		Candle rtcandle = new Candle(dailydata.get(rtIndex),dailydata.get(yesterdayIndex), rtIndex);
		return rtcandle;	
		
	}

	

	//��ȡ����Ӱ�ߵ��б�
	private void getLongHeadList(ArrayList<StockDaily> dailydata) {
		if(longHeadList==null) longHeadList = new ArrayList<Candle>();
		for(int i=startindex;i<endindex;++i)
		{
			StockDaily today = dailydata.get(i);
			StockDaily yesterday = dailydata.get(i+1);
			Candle candle = new Candle(today,yesterday,i);
			if(candle.UpShadow>0.015) longHeadList.add(candle);
		}
		
	}

	private Float getMaxVolumeMinVolumeRate(ArrayList<StockDaily> dailydata) {
		if(dailydata==null) return null;
		Float maxVolume = null;
		Float minVolume = null;
		int minIndex = -1;
		for(int i=startindex;i<endindex;++i)
		{
			if(maxVolume==null || maxVolume<dailydata.get(i).getTurnover())
				maxVolume = dailydata.get(i).getTurnover();
			if(minVolume==null || minVolume>dailydata.get(i).getTurnover())
			{
				minVolume = dailydata.get(i).getTurnover();
				minIndex = i;
			}
		}
		this.maxVolume = maxVolume;
		this.minVolume = minVolume;
		
		if(minIndex==-1) minIndex = 0;
		int yesterdayIndex = minIndex + 1;
		if(yesterdayIndex>=dailydata.size()) yesterdayIndex = dailydata.size()-1;
		if(dailydata.get(minIndex)==null||dailydata.get(yesterdayIndex)==null) 
			return 0f;
		minT = new Candle(dailydata.get(minIndex),dailydata.get(yesterdayIndex), minIndex);
		if(maxVolume==null || minVolume==null) return null;
		return maxVolume/minVolume;
	}

	private int CalculateScore() {
		if(drList==null) drList = new ArrayList<Candle>();
		if(fakeGreenList==null) fakeGreenList = new ArrayList<FakeGreen>();
		if(legList==null) legList = new ArrayList<Candle>();
		if(lysvList==null) lysvList = new ArrayList<Candle>();
		if(vupdownList==null) vupdownList = new ArrayList<Candle>();
		if(longHeadList==null) longHeadList = new ArrayList<Candle>();
		return fakeGreenList.size()
			+ legList.size()
			+ lysvList.size()
			//+ longHeadList.size()
			+ vupdownList.size();
	}

	private void getLongYingShortVolume(ArrayList<StockDaily> dailydata) {
		if(lysvList==null) lysvList = new ArrayList<Candle>();
		for(int i=startindex;i<endindex;++i)
		{
			StockDaily today = dailydata.get(i);
			StockDaily yesterday = dailydata.get(i+1);
			Candle candle = new Candle(today,yesterday,i);
			int timespan = 10;
			Float averageTurnover = getAverageTurnover(dailydata,i,timespan);
			if(candle.sd.getTurnover()<averageTurnover
				&& candle.raiseRate<-0.02 )
				lysvList.add(candle);
		}
		
	}

	//����ƽ��������
	private Float getAverageTurnover(ArrayList<StockDaily> dailydata, int index,
			int timespan) {
		Float totalTurnover = 0f;
		int endindex = index + timespan;
		int cnt = 0;
		if(endindex>=dailydata.size()) endindex = dailydata.size()-1;
		for(int i=index;i<endindex;++i)
		{
			totalTurnover +=dailydata.get(i).getTurnover();
			cnt++;
		}
		return totalTurnover/cnt;
	}

	//��ȡ���������б������ܹؼ�
	private void getVolumeUpDown(ArrayList<StockDaily> dailydata) {
		if(vupdownList==null) vupdownList = new ArrayList<Candle>();
		for(int i=startindex;i<endindex;++i)
		{
			StockDaily today = dailydata.get(i);
			StockDaily yesterday = dailydata.get(i+1);
			Candle candle = new Candle(today,yesterday,i);
			if(candle.volumeRate>=1.8
				|| candle.volumeRate<=0.6 ) vupdownList.add(candle);
		}
		
	}

	//��ȡ���Ȳ���
	private void getLongLeg(ArrayList<StockDaily> dailydata) {
		if(legList==null) legList = new ArrayList<Candle>();
		for(int i=startindex;i<endindex;++i)
		{
			StockDaily today = dailydata.get(i);
			StockDaily yesterday = dailydata.get(i+1);
			Candle candle = new Candle(today,yesterday,i);
			if(candle.DownShadow>0.02 && candle.Body<candle.DownShadow) legList.add(candle);
		}
		
	}

	/***
	 * Get DrList,�ж���������������ؼ�
	 * @param dailydata
	 */
	private void getDoubleRaise(ArrayList<StockDaily> dailydata) {
		if(drList==null) drList = new ArrayList<Candle>(); 
		
		for(int i=startindex;i<endindex;++i)
		{
			StockDaily today = dailydata.get(i);
			StockDaily yesterday = dailydata.get(i+1);
			int dayBeforeYesIndex = i+2;
			if(dayBeforeYesIndex>=dailydata.size()) dayBeforeYesIndex = dailydata.size()-1;
			StockDaily dayBeforeYesterday = dailydata.get(dayBeforeYesIndex);
			Candle candle = new Candle(today,yesterday,i);
			Candle yesCandle = new Candle(yesterday,dayBeforeYesterday,i+1);
			int timespan = 10;
			Float averageTurnover = getAverageTurnover(dailydata,i,timespan);
			
			//С�����ж�����
			if(candle.volumeRate>1.2
				&& candle.volumeRate<=3.5
				&& candle.raiseRate>=0.01 
				&& candle.raiseRate<=0.08
				//&& candle.raiseRate>yesCandle.raiseRate
				//&& yesCandle.sd.getTurnover()<(averageTurnover*1.1) 
			)
			{
				drList.add(candle);
			}
			
		}
		
	}

	private void getHugeVolume(ArrayList<StockDaily> dailydata) 
	{
		if(hv==null) hv = new HugeVolume(); 
		Float maxTurnover = null;
		int maxIndex = -1;
		for(int i=startindex;i<endindex;++i)
		{
			//�����ж�����
			Float tmpTurnover = dailydata.get(i).getTurnover();
			if(maxTurnover==null || maxTurnover < tmpTurnover)
			{
				maxIndex = i;
				maxTurnover = tmpTurnover;
			}
			
		
		}
		if(maxIndex!=-1)
		{
			hv.endindex = maxIndex;
			hv.startindex = maxIndex;
			hv.startdate =dailydata.get(maxIndex).getDate();
			hv.enddate = hv.startdate;
			hv.averageTurnover = maxTurnover;
			hv.totalTurnover = maxTurnover;
		}
		
		
		
	}

	/**
	 * �ҵ���������
	 * @param dailydata
	 * @param  
	 */

	private void getFakeGreen(ArrayList<StockDaily> dailydata) {
		
		if(fakeGreenList==null) fakeGreenList = new ArrayList<FakeGreen>(); 
		
		for(int i=startindex;i<endindex;++i)
		{
			//���������ж�����
			if(dailydata.get(i).getClose()>dailydata.get(i+1).getClose()-0.01
				&& dailydata.get(i).getOpen()>dailydata.get(i).getClose()
				//&& dailydata.get(i).getClose()/dailydata.get(i+1).getClose()<1.09
				)  
			{
				Float raiseRate = dailydata.get(i+1).getClose()/dailydata.get(i).getClose() - 1;
				FakeGreen fg = new FakeGreen(dailydata.get(i),i,raiseRate);
				fakeGreenList.add(fg);
			}
			
		}
		
		
	}

	/***
	 * ��ȡ������Ϣ
	 * ��������
	 * ���������о���ʼ���ڣ���������
	 * @param symbol2
	 * @param dailydata
	 * @param startindex2
	 * @param timespan
	 */
	private void getBasicInfo(String symbol2, ArrayList<StockDaily> dailydata,
			int startindex2, int timespan) {
		this.symbol = symbol2;
		this.startindex = startindex2;
		if(dailydata.get(startindex2)!=null 
			&& dailydata.get(startindex2).getDate()!=null )
		{
			this.startdate = dailydata.get(startindex2).getDate();
		}
		
		this.endindex = startindex2 + timespan;
		if(endindex>=dailydata.size()) 
			this.endindex = dailydata.size()-1;
		
		if(dailydata.get(endindex)!=null 
				&& dailydata.get(endindex).getDate()!=null )
		{
			this.enddate = dailydata.get(endindex).getDate();
		}
		
	}

	@Override
	public String toString() {
		/*return "CodeScore [symbol=" + symbol + ", startindex=" + startindex
				+ ", startdate=" + startdate + ", endindex=" + endindex
				+ ", enddate=" + enddate + ", hv=" + hv + ", drList=" + drList
				+ ", fakeGreenList=" + fakeGreenList + ", legList=" + legList
				+ ", lysvList=" + lysvList + ", vupdownList=" + vupdownList
				+ "MaxRaise =" + HalfYearMaxPrice.sd.getHigh()/HalfYearMinPrice.sd.getLow()
				+"]";
		*/
		return "fakeGreenList:"+"\r\n"+showList(fakeGreenList);
	}

	private String showList(ArrayList<?> list) {
		StringBuffer sb = new StringBuffer();
		for(int i =0;i<list.size();++i)
		{
			sb.append(list.get(i)).append("\r\n");
		}
		return sb.toString();
	}

	public String show() {
		return "codescore="+this.score 
				+ ","+"FakeGreen="+this.fakeGreenList.size()+","
				+ "LongLeg="+this.legList.size()+","
				+ "LongYingShortVolume="+this.lysvList.size()+","
				+ "VolumeDownUp=" + this.vupdownList.size()
				+ ",MaxMinVolumeRate=" +this.maxVolumeMinVolumeRate
				+ ",MaxVolume="+ this.maxVolume
				+",LongHeadList=" + this.longHeadList.size()
				+ ",DrIndex=" + (this.drList.size()==0?-1:this.drList.get(0).sd.getDate())
				+",DrSize="+ this.drList.size()
				+ ",MaxRaise=" + HalfYearMaxPrice.sd.getHigh()/HalfYearMinPrice.sd.getLow()
				+ ",RecentRaise=" + RecentRaise
				+ ",YangRate=" + yangRate
				+ ",GoldList=" + (this.goldList.size()==0?-1:this.goldList.get(0).sd.getDate())
				+ ",GoldListSize=" + this.goldList.size()
				+  ","+ this.isStrong
				;
	}

	
	
	
	
}
