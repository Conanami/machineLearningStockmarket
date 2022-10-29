package org.fuxin.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.fuxin.report.StatisticReport;

/***
 * �����������Ƕ���һ��StatisticReport�б�
 * ���ض���������ѡ��
 * Ŀǰʵ�ֵķ�����
 * filterFromList(list,minCnt,minAverageProfit)
 * @author Administrator
 *
 */
public class StatisticReportFilter {

	/***
	 * ��statlist����ѡȡ��С����������minCnt
	 * ƽ�������ʴ���AverageProfit
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
	 * @param statlist  ������б�
	 * @param topRatio  ȡ��ǰ��İٷ�֮����
	 * @param minCnt    ���������ڶ��ٸ���
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
	 * �õ��б������е���������֮��
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
