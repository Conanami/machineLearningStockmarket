package org.fuxin.analyst;

import java.util.ArrayList;
import java.util.Random;

import org.fuxin.stock.NewShape;
import org.fuxin.stock.ShapeGrp;
import org.fuxin.util.Translator;
import org.fuxin.vo.StockDaily;


/***
 * 制作历史样本数据
 * @author Administrator
 *
 */
public class RunSampler implements Runnable {
	public TimePeriod p;
	public ArrayList<StockDaily> dailydata;
	public Integer density;
	public Boolean isRandom;
	public HisSampler hs;
	public String symbol;
	
	
	
	public RunSampler(String symbol, ArrayList<StockDaily> arrayList,
			TimePeriod p2, Integer density2, Boolean isRandom2, HisSampler hs2) {
		this.p = p2;
		this.dailydata = arrayList;
		this.density = density2;
		this.isRandom = isRandom2;
		this.hs = hs2;
		this.symbol = symbol;
	}

	@Override
	
	public void run() {
		hs.samplelist.addAll(getOneSymbolByDensity(symbol,dailydata, p, density, isRandom));
	}
	
	private ArrayList<ShapeGrp> getOneSymbolByDensity(String symbol,
			ArrayList<StockDaily> dailydata, TimePeriod p2, Integer density2, Boolean isRandom2) {
		ArrayList<ShapeGrp> rtlist = new ArrayList<ShapeGrp>();
		int endindex = 0;           //历史取样的开始点
		Random rand = new Random();
		if(isRandom2)
		{
			endindex = 40 + rand.nextInt(40);
		}
		else
		{
			endindex = 50;
		}
		
		while( endindex + p2.size < dailydata.size())
		{
			ShapeGrp sg = Translator.getShapeGrp(symbol,endindex,p2,dailydata);
			
			if(sg!=null)
			{
				System.out.println(p+sg.shapelist.get(0).symbol);
				rtlist.add(sg);
			}
			if(isRandom2)
			{
				endindex = endindex + density2/2 + rand.nextInt(density2);
			}
			else
			{
				endindex = endindex + density2;
			}
			
		}
		return rtlist;
	}
	
	
}
