package org.fuxin.passcode;

import java.util.Collection;
import java.util.HashSet;

import org.fuxin.analyst.MapExpert;
import org.fuxin.report.SelectReport;

/***
 * 这个包中所有的类都是为了检验涨停密码，涨停基因这本书
 * @author Administrator
 * PoolManager负责类的选择
 */
public class PoolManager {
	
	/***doSelect是选择特定股票进入股票池
	 * 为了公平的检验，先做到不选择，全部选中
	 * @param me
	 * @return
	 */
	public static HashSet<String> doSelect(MapExpert me) {
		if(me==null) return null;
		HashSet<String> rtSet = new HashSet<String>();
		for(String symbol:me.getSymbolList())
		{
			if(me.getFinancedata().get(symbol)!=null)
			{
				//流通股小于30亿，净利润增长
				//if(me.getFinancedata().get(symbol).getUnlimit_shares()<=300000
					//&& (
					//me.getFinancedata().get(symbol).getNetprofit_rose()>=0 
					//|| me.getFinancedata().get(symbol).getRevenue_rose()>=0 ) 
				//	)
					rtSet.add(symbol);
			}
		}
		return rtSet;
	}

}
