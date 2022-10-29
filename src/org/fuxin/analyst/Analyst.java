package org.fuxin.analyst;

import java.util.ArrayList;

import org.fuxin.report.SrScorer;
import org.fuxin.report.StatisUtil;
import org.fuxin.report.StatisticReport;
import org.fuxin.util.FuOutput;
import org.fuxin.util.Saver;
import org.fuxin.util.StatisticReportFilter;

public class Analyst implements Runnable {
	private TimePeriod p;
	private Integer density;
	private boolean isRandom;
	private Discretor dis;
	private MapExpert me;
	private boolean isSave;
	private Integer futureday;
	public SrScorer scorer;
	
	public Analyst(TimePeriod p, Integer density, boolean isRandom,
			Discretor dis, MapExpert me, boolean isSave, Integer futureday2) {
		super();
		this.p = p;
		this.density = density;
		this.isRandom = isRandom;
		this.dis = dis;
		this.me = me;
		this.isSave = isSave;
		this.futureday = futureday2;
	}


	@Override
	public void run() {
		HisSampler hs1 = new HisSampler(p,me,density,isRandom);
		//hs1.getFutureValue(futureday);
		hs1.getFutureMaxValue(futureday);
		Classifier cf1 = new Classifier(dis,hs1);
		
		ArrayList<StatisticReport> statlist = new ArrayList<StatisticReport>();
		for(ClassifierResult cfr:cf1.clsResultList)
		{
			StatisUtil su = new StatisUtil();
			statlist.add(su.Analyse(cfr.shapegrpList,cfr.disScope, futureday));
		}
		//ArrayList<StatisticReport> afterFilter = StatisticReportFilter.filterFromList(statlist,20,0.04f);
		
		ArrayList<StatisticReport> afterFilter = StatisticReportFilter.filterFromList(statlist,0.1f,200);
		//上面一行是取统计结果中，最好的前10%，所以一个参数是0.1f，另外一个参数是表示样本数必须大于几，太偶然的形态我们就不要了。
		scorer = new SrScorer(statlist,afterFilter,p);
		//FuOutput.writeToFile(statlist, p.toString());
		//FuOutput.writeToFile(afterFilter,"af_"+p.toString());
		if(isSave)
			Saver.SaveToDB(afterFilter);
	}
	
}
