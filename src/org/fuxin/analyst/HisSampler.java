package org.fuxin.analyst;

import java.util.ArrayList;

import org.fuxin.stock.ShapeGrp;


public class HisSampler {

	public TimePeriod p;
	public MapExpert me;
	public Integer density;
	public Boolean isRandom;
	public ArrayList<ShapeGrp> samplelist;
	
	public HisSampler(TimePeriod p, MapExpert me, Integer density,
			Boolean isRandom) {
		super();
		this.p = p;
		this.me = me;
		this.density = density;
		this.isRandom = isRandom;
		getAllSample();
		//getFutureValue();
	}
	
	/***
	 * 取得day日后的收盘价
	 * @param day
	 */
	public void getFutureValue(int day)
	{
		for(ShapeGrp sg:samplelist)
		{
			int futureindex = sg.shapelist.get(0).endindex - 1 - day;
			String symbol = sg.shapelist.get(0).symbol;
			Float closeprice = sg.shapelist.get(0).close;
			
			if(futureindex<0) futureindex = 0;
			Float futureprice = me.getAlldata().get(symbol).get(futureindex).getClose();
			sg.futurevalue = futureprice/closeprice - 1.002f;
		}
	}
	
	/***
	 * 返回一个未来的最大值
	 * @param day
	 */
	public void getFutureMaxValue(int day)
	{
		for(ShapeGrp sg:samplelist)
		{
			int futureindex = sg.shapelist.get(0).endindex - 1 - day;
			int futurestart = sg.shapelist.get(0).endindex - 1;
			String symbol = sg.shapelist.get(0).symbol;
			Float closeprice = sg.shapelist.get(0).close;
			
			if(futureindex<0) 
			{
				futureindex = 0;
			}
			if(futurestart<0)
			{
				futurestart = 0;
			}
			
			
			Float futureMaxPrice = getMaxPrice(symbol,me,futureindex,futurestart);
			sg.futurevalue = futureMaxPrice/closeprice - 1.002f;
		}
	}
	
	/***
	 * 取得未来一段时间内的最高价格
	 * @param symbol
	 * @param me2
	 * @param futureindex
	 * @param futurestart
	 * @return
	 */
	private Float getMaxPrice(String symbol, MapExpert me2, int futureindex,
			int futurestart) {
		Float maxprice = 0f;
		
		if(futurestart>=me2.getAlldata().get(symbol).size())
			futurestart = me2.getAlldata().get(symbol).size()-1;
		
		for(int i=futurestart;i>=futureindex;--i)
		{
			Float tmpmax = me2.getAlldata().get(symbol).get(i).getHigh();
			if(tmpmax>maxprice)
				maxprice = tmpmax;
		}
		return maxprice;
	}

	private void getAllSample() {
		samplelist = new ArrayList<ShapeGrp>();
		Integer symbolcnt = me.getSymbolList().size();
				
		for(int i=0;i<symbolcnt;++i)
		{
			String symbol = me.getSymbolList().get(i);
			RunSampler myrun = new RunSampler(symbol,me.getAlldata().get(symbol),p,density,isRandom,this);
			myrun.run();
			
		}
		System.out.println(samplelist.size());
	
	}
	
	
			
	

	
	

}
