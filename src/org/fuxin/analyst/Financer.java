package org.fuxin.analyst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.report.SelectReport;
import org.fuxin.stock.SimpleShape;
import org.fuxin.vo.FinanceData;

/***
 * 财务分析专家
 * @author Administrator
 *
 */
public class Financer {

	/***
	 * 从财务角度进行选择
	 * @param result
	 * @return
	 */
	public static HashSet<SelectReport> doSelect(HashSet<SelectReport> result) {
		HashSet<SelectReport> rtSet = new HashSet<SelectReport>();
		for(SelectReport sltrpt:result)
		{
			if( isGood(sltrpt))
			{
				rtSet.add(sltrpt);
			}
		}
		return rtSet;
	}
	
	private static boolean isGood(FinanceData tmpfd)
	{
		if(tmpfd.getUnlimit_shares()<=150000)
		{
			//每股收益大于0.05，净利润和主营业务增长都超过10%
			if((tmpfd.getEarning_ps()/tmpfd.getYearRatio())>0.05
				&& tmpfd.getNetprofit_rose()>=10
				&& tmpfd.getRevenue_rose()>=10
				)
				return true;
			
			
			
			//if(sltrpt.getPe_ratio()<=30)
			//	return true;
		}
		
		return false;
		
	}
	private static boolean isGood(SelectReport sltrpt) {
		FinanceData tmpfd = sltrpt.getFinance();
		if(tmpfd.getUnlimit_shares()<=100000)
		{
			//每股收益大于0.05，净利润和主营业务增长都超过10%
			if((tmpfd.getEarning_ps()/tmpfd.getYearRatio())>0.05
				&& tmpfd.getNetprofit_rose()>=10
				&& tmpfd.getRevenue_rose()>=0
				)
				return true;
			/*
			//市盈率低于40，净利润增长超过10% 并且 营收没有出现减少
			if(sltrpt.getPe_ratio()<=40 &&
				sltrpt.getPe_ratio()>=0 &&
				tmpfd.getNetprofit_rose()>10 )
				return true;
			*/
			//if(sltrpt.getPe_ratio()<=30)
			//	return true;
		}
		
		return false;
	}

	

	public static HashSet<SelectReport> doFairSelect(
			HashSet<SelectReport> result) {
		HashSet<SelectReport> rtSet = new HashSet<SelectReport>();
		for(SelectReport sltrpt:result)
		{
			if( isFairGood(sltrpt))
			{
				rtSet.add(sltrpt);
			}
		}
		return rtSet;
	}

	private static boolean isFairGood(SelectReport sltrpt) {
		
		FinanceData tmpfd = sltrpt.getFinance();
		if(tmpfd.getUnlimit_shares()<=200000)
		{
			return true;
		}
		
		return false;
	}

	public static HashSet<String> doPoolSelect(
			MapExpert me) {
		HashSet<String> rtlist = new HashSet<String>();
		for(String symbol:me.getFinancedata().keySet())
		{
			if(isGood(me.getFinancedata().get(symbol)))
				rtlist.add(symbol);
		}
	
		return rtlist;
	}

	public static HashSet<String> doPoolFairSelect(MapExpert me) {
		HashSet<String> rtlist = new HashSet<String>();
		for(String symbol:me.getFinancedata().keySet())
		{
			if(isFairGood(me.getFinancedata().get(symbol)))
				rtlist.add(symbol);
		}
	
		return rtlist;
	}

	private static boolean isFairGood(FinanceData tmpfd) {
		if(tmpfd.getUnlimit_shares()<=100000)
		{
			//只看小盘
			return true;
		}
		return false;
		
	}
	
	

		
	
	
}
