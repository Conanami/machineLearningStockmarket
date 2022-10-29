package org.fuxin.report;

import java.util.ArrayList;

import org.fuxin.stock.DiscreteScope;
import org.fuxin.stock.ShapeGrp;



/***
 * ͳ��ѧ�ң�����ͳ�ƽ��
 * @author Administrator
 *
 */
public class StatisUtil {
	
	private StatisticReport rep = null;
	
	public StatisticReport Analyse(ArrayList<ShapeGrp> fr, DiscreteScope ds, long l)
	{
		//ͳ�����һ�е�ƽ��ֵ�����ֵ����Сֵ��Ϊ���ı���������
		//���ͳ��ѧ���յ�һ�ſձ�����ֱ�ӷ��ؿ�ֵ��
		rep = new StatisticReport();
		if(fr.size()==0 || fr==null) 
		{
			rep.setSamplecount(0);
			rep.setFutureday(l);
			rep.setDs(ds);
			return rep;
		}
		
		rep.setSamplecount(fr.size());
		rep.setAverage(getAverage(fr));
		rep.setMax(getMax(fr));
		rep.setMin(getMin(fr));
		rep.setProfitRate(getProfitRate(fr,0.05));
		rep.setVariance(getVariance(fr));
		rep.setFutureday(l);
		rep.setDs(ds);
		return rep;
	}

	
	private Double getVariance(ArrayList<ShapeGrp> fr) {
		Double average = getAverage(fr);
		Double dist = 0.0;
		Double total = 0.0;
		for(int i=0;i<fr.size();i++)
		{
			Double tmpfloat = fr.get(i).getStatisValue();
			dist = tmpfloat - average;
			total = total + Math.pow(dist,2);
		}
		return Math.pow(total/fr.size(),0.5);
	}


	private Double getProfitRate(ArrayList<ShapeGrp> fr, double d) {
		Double winCnt = 0.0;
		for(int i=0;i<fr.size();i++)
		{
			Double tmpfloat = fr.get(i).getStatisValue();
			if(tmpfloat>d ) winCnt++;
		}
		return winCnt / fr.size();
	}


	private Double getMin(ArrayList<ShapeGrp> fr) {
		Double min = 1000.0;
		for(int i=0;i<fr.size();i++)
		{
			Double tmpfloat = fr.get(i).getStatisValue();
			if(tmpfloat<min) min = tmpfloat;
		}
		return min;
	}
	
	private Double getMax(ArrayList<ShapeGrp> fr) {
		Double max = -1000.0;
		for(int i=0;i<fr.size();i++)
		{
			Double tmpfloat = fr.get(i).getStatisValue();
			if(tmpfloat>max) max = tmpfloat;
		}
		return max;
	}
	
	private Double getAverage(ArrayList<ShapeGrp> fr) 
	{
		Double total = 0.0;
		for(int i=0;i<fr.size();i++)
		{
			Double tmpfloat = fr.get(i).getStatisValue();
			if(tmpfloat<0) tmpfloat = LossConvert(tmpfloat);
			total =  total + tmpfloat;
		}
		return total/fr.size();
	}


	/***
	 * ����ǿ�����ô���㷽ʽ��ӯ����ͬ
	 * ������� -0.33 �� ��ôʵ�ʼ����� -0.33/��1+-0.33�� = -0.5
	 * @param inloss
	 * @return
	 */
	private Double LossConvert(Double inloss) {
		Double rtvalue = 0.0;
		Double lossrate = inloss;
		if(inloss<0)
		{
			rtvalue = lossrate/(1+lossrate);
			return rtvalue;
		}
		else
			return lossrate; 
	}

}
