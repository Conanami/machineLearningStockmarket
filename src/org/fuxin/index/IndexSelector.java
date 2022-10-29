package org.fuxin.index;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.analyst.MapExpert;
import org.fuxin.passcode.StockDailyWithReason;
import org.fuxin.util.FuOutput;
import org.fuxin.util.ScopeUtil;
import org.fuxin.vo.StockDaily;

public class IndexSelector {

	/***
	 * 从stockpool里面选择相对大盘能量更强的个股
	 * 这个方法在平衡市道和弱势中使用
	 * @param currentday
	 * @param stockpool
	 * @param me
	 * @return
	 */
	public static ArrayList<StockDailyWithReason> doCurrentSelect(
			Date currentday, HashSet<String> stockpool, MapExpert me) {
		//返回列表先建立出来
		ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
		for(String symbol:stockpool)
		{
			if(me.getAlldata().get(symbol)!=null)
			{
				//个股这些天的数据
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				//指数这些天的数据
				ArrayList<StockDaily> indexdata = me.getShindex();
				//当天的index总要先算出来有没有
				int index = ScopeUtil.getIndexForward(currentday, dailydata);
				int gegu = ScopeUtil.getIndexForward(currentday,dailydata);
				if(gegu<0) gegu=0;
				if(index>=0) 
				{
					//如果经过选择符合条件，则返回相对大盘强势个股列表
					Float strongValue = IsStrong(dailydata,indexdata,index);
					if(strongValue<1)
					{
						rtlist.add(new StockDailyWithReason(dailydata.get(gegu),"IndexStrong="+strongValue));
					}
				}
			}
		}
		return rtlist;
	}

	private static Float IsStrong(ArrayList<StockDaily> dailydata,
			ArrayList<StockDaily> indexdata, int index) {
		// TODO Auto-generated method stub
		//先找到20天来，指数偏弱的日子，以及指数当天的跌幅
		ArrayList<StockDaily> indexweak=GetIndexWeakList(indexdata,20,index);
		//FuOutput.println("大盘弱的日子：", indexweak);
		//再用这些日子去匹配这个股票那些天的表现
		//如果表现符合一个特定参数值，则认为该股票为强势
		Float strongValue = CompareIndex(indexweak,dailydata);
		
		return strongValue;
	}

	/***
	 * 比较大盘的弱势和个股的弱势
	 * @param indexweak
	 * @param dailydata
	 * @return
	 */
	private static Float CompareIndex(ArrayList<StockDaily> indexweak,
			ArrayList<StockDaily> dailydata) {
		Float rtValue = 0f;
		for(int i=0;i<indexweak.size();++i)
		{
			Integer index=ScopeUtil.getIndexByDate(indexweak.get(i).getDate(),dailydata);
			if(index>=0)
			{
				StockDaily today = dailydata.get(index);
				StockDaily yesterday = null;
				if((index+1)<dailydata.size())
					yesterday = dailydata.get(index+1);
				else
					yesterday = dailydata.get(index);
				
				Float raiseRate = (today.getClose()/yesterday.getClose()-1);
				
				//如果要研究个股最近的强弱，可以把这个打印出来
				//System.out.println(today+"||"+raiseRate+"||"+indexweak.get(i).getTurnover());
				rtValue += raiseRate-indexweak.get(i).getTurnover();
				
			}
		}
		//System.out.println(rtValue);
		if(indexweak.size()==0) return 0.02f;
		
		return rtValue/indexweak.size();
	}

	

	/***
	 * 找到大盘弱势的那些日子
	 * @param indexdata
	 * @param days
	 * @param index
	 * @return
	 */
	private static ArrayList<StockDaily> GetIndexWeakList(
			ArrayList<StockDaily> indexdata, int days, int index) {
		ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
		for(int i=index;i<=index+days;++i)
		{
			StockDaily today = indexdata.get(i);
			StockDaily yesterday = indexdata.get(i+1);
			today.setTurnover(today.getClose()/yesterday.getClose()-1);
			if(today.getTurnover()<=-0.01)
				rtlist.add(today);
		}
		return rtlist;
	}

	
	public static ArrayList<StockDailyWithReason> doSelect(Date currentday,
			HashSet<String> stockpool, MapExpert me) {
		//返回列表先建立出来
				ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
				for(String symbol:stockpool)
				{
					if(me.getAlldata().get(symbol)!=null)
					{
						//个股这些天的数据
						ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
						//指数这些天的数据
						ArrayList<StockDaily> indexdata = me.getShindex();
						//当天的index总要先算出来有没有
						//指数的下标
						int index = ScopeUtil.getIndexForward(currentday, indexdata);
						//个股的下标
						int gegu = ScopeUtil.getIndexForward(currentday,dailydata);
						if(gegu<0) gegu=0;
						if(index>=0) 
						{
							//在模拟器中，交易当天只能有前一天的数据，否则为作弊
							Integer strongindex = -1;
							
							if( index+1 < indexdata.size())  
								strongindex = index+1;
							
							Float strongValue = IsStrong(dailydata,indexdata,strongindex);
							//如果经过选择符合条件，则返回相对大盘强势个股列表
							if(strongValue<-0.02)
							{
								rtlist.add(new StockDailyWithReason(dailydata.get(gegu),"IndexStrong="+strongValue));
							}
						}
					}
				}
				return rtlist;
	}

	
}
