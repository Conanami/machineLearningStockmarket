package org.fuxin.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.fuxin.report.StatisticReport;

/***
 * 这个类的作用是对于一个StatisticReport列表
 * 按特定条件进行选择
 * 目前实现的方法有
 * filterFromList(list,minCnt,minAverageProfit)
 * @author Administrator
 *
 */
public class StatisticReportFilter {

	/***
	 * 从statlist里面选取最小样本数大于minCnt
	 * 平均利润率大于AverageProfit
	 * @param statlist
	 * @param minCnt
	 * @param minAverageProfit
	 * @return
	 */
	public static ArrayList<StatisticReport> filterFromList(
			ArrayList<StatisticReport> statlist, int minCnt, float minAverageProfit) {
		ArrayList<StatisticReport> rtlist = new ArrayList<StatisticReport>();
		for(StatisticReport sr:statlist)
		{
			if(sr.getAverage()>=minAverageProfit &&
					sr.getSamplecount()>=minCnt)
				rtlist.add(sr);
		}
		Collections.sort(rtlist);
		return rtlist;
	}

	
	/***
	 * 
	 * @param statlist  输入的列表
	 * @param topRatio  取最前面的百分之多少
	 * @param minCnt    样本数大于多少个的
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<StatisticReport> filterFromList(
			final ArrayList<StatisticReport> statlist, final float topRatio ,final int minCnt) {
		
		ArrayList<StatisticReport> tmplist = new ArrayList<StatisticReport>();
		
		if(statlist!=null)
		{
			tmplist = (ArrayList<StatisticReport>)statlist.clone();
			Collections.sort(tmplist);
		}
		else
		{
			return null;
		}
		
		ArrayList<StatisticReport> rtlist = new ArrayList<StatisticReport>();
		
		
		int totalCnt = 0;
		totalCnt = getTotalSampleCnt(tmplist);
		float cntLimit = (float)(totalCnt * topRatio);
		int tmpCnt = 0;
		for(StatisticReport sr:tmplist)
		{
			if(sr.getSamplecount()>minCnt)
			{
				if(tmpCnt < cntLimit)
				{
					rtlist.add(sr);
				}
				else
				{
					break;
				}
				tmpCnt += sr.getSamplecount();
			}
		}
		return rtlist;
	}
	
	/***
	 * 得到列表中所有的样本个数之和
	 * @param all
	 * @return
	 */
	private static int getTotalSampleCnt(ArrayList<StatisticReport> all) {
		int rtcnt = 0;
		for(StatisticReport sr:all)
		{
			rtcnt+= sr.getSamplecount();
		}
		return rtcnt;
	}
	

}
