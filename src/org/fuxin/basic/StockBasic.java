package org.fuxin.basic;

import java.util.ArrayList;

import org.fuxin.vo.StockDaily;

/***
 * 股票统计计算的基本类，计算涨幅，震幅，量比等等
 * @author Administrator
 *
 */
public class StockBasic {

	/***
	 * 
	 * @param dailydata
	 * @param startindex
	 * @param endindex
	 * @return -100表示参数有错
	 */
	public static float getRaise(ArrayList<StockDaily> dailydata,
			int startindex, int endindex) {
		
		if(dailydata==null || dailydata.size()==0) return -100f;
		if(startindex<0 || startindex>=dailydata.size()) return -100f;
		if(endindex<0 || endindex>=dailydata.size()) return -100f;
		if(dailydata.get(startindex).getClose()==null
			|| dailydata.get(endindex).getClose()==null) return -100f;
		
		return  (dailydata.get(endindex).getClose()/dailydata.get(startindex).getClose()-1);
	}

}
