package org.fuxin.passcode;

import java.util.ArrayList;
import java.util.Date;

import org.fuxin.vo.StockDaily;

/***
 * 记录基因和密码的类
 * @author Administrator
 *
 */
public class CodeScore {
	
	//下面5个算是basicinfo
	public String symbol;
	public int startindex;
	public Date startdate;
	public int endindex;
	public Date enddate;
	
	public int score = 0;
	
	public HugeVolume hv;					//这是一个巨大量堆，表示强庄建仓动作
	public ArrayList<Candle> drList;		//小倍阳列表
	public ArrayList<FakeGreen> fakeGreenList;  //出现的假阴真阳的个数
	public ArrayList<Candle> legList;			//长腿踩线列表
	public ArrayList<Candle> lysvList;  		//长阴短柱列表
	public ArrayList<Candle>  vupdownList;     	//倍量伸缩列表
	public ArrayList<Candle> longHeadList;		//长上影线
	public ArrayList<Candle> dietingList;		//跌停列表
	public ArrayList<Candle> zhangtingList;		//涨停列表
	public Float maxVolumeMinVolumeRate;		//最大最小换手率之比
	public Float maxVolume;						//最大换手率
	public Float minVolume;						//最小换手率
	public Candle minT;							//最小换手率那天
	public Candle LeftTop;						//左峰
	public Candle HalfYearMaxPrice;				//半年最高价
	public Candle HalfYearMinPrice;				//半年最低价
	public Float RecentRaise;					//近20天的涨幅
	public ArrayList<Candle> goldList;			//黄金柱
	public Float yangRate;						//近20天阳线与阴线的比率
	public String isStrong;						//是否强势值
	public CodeScore()
	{
		fakeGreenList = new ArrayList<FakeGreen>();
		hv = new HugeVolume();		//如果没有一个巨大的量堆，供强庄建仓，则后续走势就一般
	}

	public void getGene(String symbol, ArrayList<StockDaily> dailydata,
			int startindex, int timespan) {
		//先处理输入参数的异常情况
		if(dailydata==null) return;
		if(startindex<0) return;
		if(startindex>=dailydata.size()) return;
		if(timespan<=0 || timespan>=600) return;
		if(symbol==null) return;
		
		getBasicInfo(symbol,dailydata,startindex,timespan);
		getFakeGreen(dailydata);				//获取假阴真阳
		getHugeVolume(dailydata);				//获取最高成交量
		getDoubleRaise(dailydata);				//获取小倍阳
		getLongLeg(dailydata);					//获取长腿踩线
		getVolumeUpDown(dailydata);				//获取倍量伸缩
		getLongYingShortVolume(dailydata);		//获取长阴短柱
		getLongHeadList(dailydata);				//长上影柱
		getDietingList(dailydata);				//获取跌停列表
		getZhangtingList(dailydata,20);			//获取涨停列表
		getGoldList(dailydata,20);				//获得黄金柱列表
		
		int lefttopspan = timespan * 5;
		LeftTop = getLeftTop(dailydata,lefttopspan);		//得到左峰，刚过左峰创新高为佳
		HalfYearMaxPrice = getLeftTop(dailydata,200);
		HalfYearMinPrice = getLeftBottom(dailydata,200);
		maxVolumeMinVolumeRate = getMaxVolumeMinVolumeRate(dailydata);
		RecentRaise = getRecentRaise(dailydata,20);
		yangRate = getYangRate(dailydata,20);
		score = CalculateScore();						//计算得分
	}
	
	
	private Float getYangRate(ArrayList<StockDaily> dailydata, int days) {
		int tmpendindex = startindex + days - 1;
		if(tmpendindex>=dailydata.size()) tmpendindex = dailydata.size()-1;
		float yangcnt = 0f;
		float yingcnt = 0f;
		//必须看后三天的内容
		for(int i=startindex ;i<tmpendindex-1;++i)
		{
			
			StockDaily today = dailydata.get(i);    //黄金柱
			StockDaily yesterday = dailydata.get(i+1);
			//如果判断是黄金柱，则加入黄金柱列表
			Candle candle = new Candle(today,yesterday,i);
			if(candle.YingYang==1) yangcnt++;  //如果是阳线，则阳线++
			else yingcnt++;					//
		}
		return yangcnt/yingcnt;
		
	}

	//获取黄金柱列表
	private void getGoldList(ArrayList<StockDaily> dailydata, int days) {
		// 如果黄金柱列表为空，则建立黄金柱列表
		if(goldList==null) goldList = new ArrayList<Candle>();
		
		//决定看到哪天为止
		int tmpendindex = startindex + days - 1;
		if(tmpendindex>=dailydata.size()) tmpendindex = dailydata.size()-1;
		
		//必须看后三天的内容
		for(int i=startindex + 3 ;i<tmpendindex;++i)
		{
			
			StockDaily today = dailydata.get(i);    //黄金柱
			StockDaily day1 = dailydata.get(i-1);	//后一天
			StockDaily day2 = dailydata.get(i-2);   //后两天
			StockDaily day3 = dailydata.get(i-3);   //后三天
			StockDaily yesterday = dailydata.get(i+1);
			//如果判断是黄金柱，则加入黄金柱列表
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
		//判断黄金柱
		//量和价
		if(IsGoldVolume(today,day1,day2,day3) 
			&& IsGoldPrice(today,day1,day2,day3))
			return true;
		return false;
	}

	//是否符合黄金柱的价格条件
	private boolean IsGoldPrice(StockDaily today, StockDaily day1,
			StockDaily day2, StockDaily day3) {
		if(today.getClose()>=today.getOpen()*1.02f
				&& (day1.getClose()+day2.getClose()+day3.getClose())/3>today.getClose()
			)
			return true;
		return false;
	}	

	//成交量是否符合黄金柱的特点
	private boolean IsGoldVolume(StockDaily today, StockDaily day1,
			StockDaily day2, StockDaily day3) {
		if(today.getTurnover()>=day1.getTurnover()*0.7
			&& today.getTurnover()>=day2.getTurnover()*0.7
			&& today.getTurnover()>=day3.getTurnover()*0.7
			&& today.getTurnover()>(day1.getTurnover()+day2.getTurnover()+day3.getTurnover())/3)
			return true;
		return false;
	}

	
	//取得涨停列表
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

	//获取跌停列表
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
	 * 得到近20天的涨幅
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

	

	//获取长上影线的列表
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

	//计算平均换手率
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

	//获取倍量收缩列表，参数很关键
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

	//获取长腿踩线
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
	 * Get DrList,判断条件，看起来最关键
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
			
			//小倍阳判断条件
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
			//巨量判断条件
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
	 * 找到假阴真阳
	 * @param dailydata
	 * @param  
	 */

	private void getFakeGreen(ArrayList<StockDaily> dailydata) {
		
		if(fakeGreenList==null) fakeGreenList = new ArrayList<FakeGreen>(); 
		
		for(int i=startindex;i<endindex;++i)
		{
			//假阳真阴判断条件
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
	 * 获取基本信息
	 * 包括代码
	 * 基因密码研究开始日期，结束日期
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
