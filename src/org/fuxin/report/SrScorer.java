package org.fuxin.report;

import java.util.ArrayList;

import org.fuxin.analyst.TimePeriod;


/***
 * StatisticReport�Ĵ����
 * @author Administrator
 *
 */
public class SrScorer implements Comparable {
	
	/***
	 * ���캯��
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
	 * ������Ҫ��ͳ����
	 */
	private void calculate() {
		this.totalSampleCnt = getTotalSampleCnt(all);
		this.filterSampleCnt = getTotalSampleCnt(afterFilter);
		this.filterRatio = (float)(this.filterSampleCnt)/(float)(this.totalSampleCnt);
		this.weightedAverageProfit = getWeightedAP(afterFilter);
	}

	/***
	 * �����Ȩƽ������
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
	 * �õ��б������е���������֮��
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
	//���е�ͳ�Ʊ���
	public ArrayList<StatisticReport> afterFilter;
	//����ɾѡ��ͳ�Ʊ���
	public int totalSampleCnt;    
	//����������
	public int filterSampleCnt;
	//����ɾѡ��������
	public float weightedAverageProfit;
	//��Ȩƽ��������
	public float filterRatio;
	//ɾѡ������ռ���������ı�����0-1��֮��
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
