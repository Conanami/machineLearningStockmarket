package org.fuxin.report;

import org.fuxin.factory.DAOFactory;
import org.fuxin.stock.DiscreteScope;

/***
 * �����������һ��ͳ�Ʊ���
 * @author Administrator
 *
 */
public class StatisticReport implements Comparable<StatisticReport>,Cloneable {
	private Integer samplecount;       //�ж�������
	private Double max;				   //ͳ���������ֵ	
	private Double min;				   //ͳ��������Сֵ
	private Double profitRate;	       //����������
	private Double variance;           //����
	private Double average;			   //ƽ��ֵ
	private long futureday;    		   //δ��������
	private DiscreteScope ds;		   //��ɢ����
	
	public Integer getSamplecount() {
		return samplecount;
	}
	public void setSamplecount(Integer samplecount) {
		this.samplecount = samplecount;
	}
	public Double getMax() {
		return max;
	}
	public void setMax(Double max) {
		this.max = max;
	}
	public Double getMin() {
		return min;
	}
	public void setMin(Double min) {
		this.min = min;
	}
	public Double getProfitRate() {
		return profitRate;
	}
	public void setProfitRate(Double profitRate) {
		this.profitRate = profitRate;
	}
	public Double getVariance() {
		return variance;
	}
	public void setVariance(Double variance) {
		this.variance = variance;
	}
	public Double getAverage() {
		return average;
	}
	public void setAverage(Double average) {
		this.average = average;
	}
	public long getFutureday() {
		return futureday;
	}
	public void setFutureday(long futureday) {
		this.futureday = futureday;
	}
	

	public DiscreteScope getDs() {
		return ds;
	}
	public void setDs(DiscreteScope ds) {
		this.ds = ds;
	}
	@Override
	public String toString() {
		return "SR:cnt=" + samplecount + ", max=" + max
				+ ", min=" + min + ", profitRate=" + profitRate + ", variance="
				+ variance + ", average=" + average + ", futureday="
				+ futureday + ", ds=" + ds ;
	}
	
	@Override
	public int compareTo(StatisticReport o) {
		if( o instanceof StatisticReport)
		{
			if(((StatisticReport) o).getAverage()>this.getAverage())
				return 1;
			else
				return -1;
		}	
		return 0;
	}
	
	/***
	 * �־û�һ��StatisticReport�������ݿ���
	 * @param batch_name
	 */
	public void save(String batch_name) {
		try {
			DAOFactory.getStockDailyDAOInstance().saveStatisticReport(this,batch_name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}
