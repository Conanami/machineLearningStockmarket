package org.fuxin.report;

import java.util.ArrayList;

import org.fuxin.analyst.TimePeriod;


/***
 * StatisticReport的打分者
 * @author Administrator
 *
 */
public class SrScorer implements Comparable {
	
	/***
	 * 构造函数
	 * @param all
	 * @param afterFilter
	 */
	public SrScorer(ArrayList<StatisticReport> all,
			ArrayList<StatisticReport> afterFilter,TimePeriod tp) {
		this.all = all;
		this.afterFilter = afterFilter;
		this.tp = tp;
		calculate();
	}
	
	/***
	 * 计算主要的统计量
	 */
	private void calculate() {
		this.totalSampleCnt = getTotalSampleCnt(all);
		this.filterSampleCnt = getTotalSampleCnt(afterFilter);
		this.filterRatio = (float)(this.filterSampleCnt)/(float)(this.totalSampleCnt);
		this.weightedAverageProfit = getWeightedAP(afterFilter);
	}

	/***
	 * 计算加权平均利润
	 * @param afterFilter2
	 * @return
	 */
	private float getWeightedAP(ArrayList<StatisticReport> list) {
		float total = 0;
		float cnt = 0;
		for(StatisticReport sr:list)
		{
			total += (float)sr.getSamplecount()*sr.getAverage();
			cnt += (float)sr.getSamplecount();
		}
		return total/cnt;
	}

	/***
	 * 得到列表中所有的样本个数之和
	 * @param all
	 * @return
	 */
	private int getTotalSampleCnt(ArrayList<StatisticReport> all) {
		int rtcnt = 0;
		for(StatisticReport sr:all)
		{
			rtcnt+= sr.getSamplecount();
		}
		return rtcnt;
	}
	
	public TimePeriod tp;
	public ArrayList<StatisticReport> all;
	//所有的统计报表
	public ArrayList<StatisticReport> afterFilter;
	//经过删选的统计报表
	public int totalSampleCnt;    
	//所有样本数
	public int filterSampleCnt;
	//经过删选的样本数
	public float weightedAverageProfit;
	//加权平均获益率
	public float filterRatio;
	//删选后样本占所有样本的比例（0-1）之间
	@Override
	public String toString() {
		if(all.size()>0)
		{
		return tp+","+ totalSampleCnt
				+ ", filterSampleCnt=" + filterSampleCnt
				+ ", filterRatio=" + filterRatio 
				+ ", weightedAverageProfit=" + weightedAverageProfit;
		}
		else
			return "empty";
		
	}

	@Override
	public int compareTo(Object o) {
		if( o instanceof SrScorer)
		{
			if(((SrScorer) o).weightedAverageProfit < this.weightedAverageProfit)
				return -1;
			else
				return 1;
		}
		return 0;
	}
	
}
