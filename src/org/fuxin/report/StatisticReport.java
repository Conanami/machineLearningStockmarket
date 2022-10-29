package org.fuxin.report;

import org.fuxin.factory.DAOFactory;
import org.fuxin.stock.DiscreteScope;

/***
 * 这个类是生成一个统计报表
 * @author Administrator
 *
 */
public class StatisticReport implements Comparable<StatisticReport>,Cloneable {
	private Integer samplecount;       //有多少样本
	private Double max;				   //统计量的最大值	
	private Double min;				   //统计量的最小值
	private Double profitRate;	       //获利可能性
	private Double variance;           //方差
	private Double average;			   //平均值
	private long futureday;    		   //未来多少天
	private DiscreteScope ds;		   //离散分类
	
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
	 * 持久化一个StatisticReport对象到数据库中
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
