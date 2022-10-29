package org.fuxin.analyst;

import java.util.ArrayList;

import org.fuxin.stock.DiscreteScope;
import org.fuxin.stock.NewShape;
import org.fuxin.stock.ShapeGrp;
import org.fuxin.util.Pair;

public class Classifier {
	public ArrayList<ClassifierResult> clsResultList;
	
	public Classifier(Discretor dis1, HisSampler hs1) {
		clsResultList = new ArrayList<ClassifierResult>();
		int gcCnt = 0;
		for(ShapeGrp sgp:hs1.samplelist)
		{
			++gcCnt;
			putIntoResultList(sgp,dis1);
			if(gcCnt%50000==0)
				System.gc();
		}
	}

	private void putIntoResultList(ShapeGrp sgp, Discretor dis1) {
		DiscreteScope ds1 = getDiscreteScope(sgp,dis1);
		
		int iFound = clsResultList.indexOf(new ClassifierResult(ds1,null));
		if(iFound!=-1)
		{
			clsResultList.get(iFound).shapegrpList.add(sgp);
		}
		else
		{
			ArrayList<ShapeGrp> newsgp = new ArrayList<ShapeGrp>();
			newsgp.add(sgp);
			clsResultList.add(new ClassifierResult(ds1,newsgp));
		}
		
	}

	//获得离散的分类
	private DiscreteScope getDiscreteScope(ShapeGrp sgp, Discretor dis1) {
		DiscreteScope rtds = new DiscreteScope();
		for(NewShape ns:sgp.shapelist)
		{
			Pair pair = getPair(ns,dis1);
			rtds.typename.add(pair);
		}
		return rtds;
	}

	//获得一个类型的分类
	private Pair getPair(NewShape ns, Discretor dis1) {
		Integer period = (ns.startindex-ns.endindex + 1);
		Pair rtpair = new Pair(period,0f,0f);
		for(int i=0;i<dis1.border.size()-1;++i)
		{
			if(period<5)       //5天内的自动把dis1缩小成1/10
			{
				if(ns.raise>=(dis1.border.get(i)/10) && ns.raise<(dis1.border.get(i+1)/10))
				{
					rtpair.min = dis1.border.get(i)/10;
					rtpair.max = dis1.border.get(i+1)/10;
					return rtpair;
				}
			}
			else
			{
				if(ns.raise>=dis1.border.get(i) && ns.raise<dis1.border.get(i+1))
				{
					rtpair.min = dis1.border.get(i);
					rtpair.max = dis1.border.get(i+1);
					return rtpair;
				}
			}
		}
		return rtpair;
		
	}

	public void showList() {
		for(ClassifierResult cr:this.clsResultList)
		{
			System.out.println(cr);
		}
		
	}

}
