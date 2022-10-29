package org.fuxin.stock;

import java.util.ArrayList;

import org.fuxin.report.StatisticReport;

public class ShapeGrp {
	public ArrayList<NewShape> shapelist;
	public Float futurevalue=0f;

	public ShapeGrp() {
		super();
		shapelist = new ArrayList<NewShape>();
	}

	public Double getStatisValue() {
		// TODO Auto-generated method stub
		return (double)futurevalue;
	}

	/***
	 * 判断是否符合某种形态条件
	 * @param sr
	 * @return
	 */
	public boolean isFit(StatisticReport sr) {
		if(shapelist.size()<=0) return false;
		for(int i=0;i<shapelist.size();++i)
		{
			if(shapelist.get(i).raise > sr.getDs().typename.get(i).max
				|| shapelist.get(i).raise < sr.getDs().typename.get(i).min)
			{
				return false;
			}
		}
		return true;
		
	}
	

	
}
