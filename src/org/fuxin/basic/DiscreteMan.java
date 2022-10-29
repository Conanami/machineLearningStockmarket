package org.fuxin.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import org.fuxin.util.FuOutput;

//用来做离散化的人
public class DiscreteMan {

	//对result做离散化
	public static TreeMap<String, DiscreteValue> getDiscrete(
			TreeMap<String, OriginValue> result, int classes) {
		// TODO Auto-generated method stub
		
		ArrayList<OriginValue> orderedresult = GetOrderedList(result);
		ArrayList<ScopePair> scopelist = GetScopeList(orderedresult,classes);
		TreeMap<String,DiscreteValue> rtmap = GetDiscreteMap(result,scopelist);
		return rtmap;
	}

	
	private static TreeMap<String, DiscreteValue> GetDiscreteMap(
			TreeMap<String, OriginValue> result, ArrayList<ScopePair> scopelist) {
		TreeMap<String, DiscreteValue> rtmap = new TreeMap<String, DiscreteValue>();
		for(String symbol:result.keySet())
		{
			DiscreteValue dv = GetDiscreteValue(result.get(symbol),scopelist);
			rtmap.put(symbol, dv);
		}
		return rtmap;
		
	}


	//获得dv符合的离散区间
	private static DiscreteValue GetDiscreteValue(OriginValue originvalue,
			ArrayList<ScopePair> scopelist) {
		DiscreteValue dv = new DiscreteValue();
		ScopePair scopepair = GetScopePair(originvalue.value,scopelist);
		dv.scopepair = scopepair;
		dv.originvalue = originvalue;
		dv.discretevalue = scopepair.discretetype;
		return dv;
	}


	//返回离散区间
	private static ScopePair GetScopePair(Float value,
			ArrayList<ScopePair> scopelist) {
		ScopePair tmppair = null;
		for(int i=0;i<scopelist.size();++i)
		{
			tmppair = scopelist.get(i);
			if(tmppair.maxIsEqual && value<=tmppair.max
				&& tmppair.minIsEqual && value>=tmppair.min)
			{
				return tmppair;
			}
			if((!tmppair.maxIsEqual) && value<tmppair.max
				&& tmppair.minIsEqual && value>=tmppair.min)
			{
				return tmppair;
			}
			if(tmppair.maxIsEqual && value<=tmppair.max
					&& (!tmppair.minIsEqual) && value>tmppair.min)
			{
				return tmppair;
			}
			if((!tmppair.maxIsEqual) && value<tmppair.max
					&& (!tmppair.minIsEqual) && value>tmppair.min)
			{
				return tmppair;
			}
		}
		return tmppair;
	}


	//获得做离散化的区间列表
	private static ArrayList<ScopePair> GetScopeList(
			ArrayList<OriginValue> orderedresult, int classes) {
		ArrayList<ScopePair> rtlist = new ArrayList<ScopePair>();
		int startindex = 0;
		int range = (orderedresult.size()-1)/classes;
		int endindex = startindex + range;
		
			
		Float lastendvalue = null;
		for(int i=1;i<=classes;++i)
		{
			if(i==1)
			{
				Float endvalue = orderedresult.get(endindex).value;
				ScopePair scopepair = 
						new ScopePair(i,Float.POSITIVE_INFINITY,false,endvalue,true);
				rtlist.add(scopepair);
				lastendvalue = endvalue;
			}
			else if(i==classes)
			{
				Float startvalue = lastendvalue;
				ScopePair scopepair = 
						new ScopePair(i,startvalue,false,Float.NEGATIVE_INFINITY,true);
				rtlist.add(scopepair);
			}
			else
			{
				Float startValue = lastendvalue;
				Float endValue = orderedresult.get(endindex).value;
				ScopePair scopepair = 
						new ScopePair(i,startValue,false,endValue,true);
				rtlist.add(scopepair);
				lastendvalue = endValue;
			}
			
			startindex = endindex + 1;
			endindex = startindex + range;
			
			if(startindex>=orderedresult.size() && i<classes)
			{
				Float startvalue = lastendvalue;
				ScopePair scopepair = 
						new ScopePair(i+1,startvalue,false,Float.NEGATIVE_INFINITY,true);
				rtlist.add(scopepair);
				break;
			}
			
		}
		return rtlist;
	}

	//获得根据OriginValue排序的列表
	private static ArrayList<OriginValue> GetOrderedList(
			TreeMap<String, OriginValue> result) {
		ArrayList<OriginValue> orderedresult = new ArrayList<OriginValue>();
		for(OriginValue ov:result.values())
		{
			orderedresult.add(ov);
		}
		Collections.sort(orderedresult);
		return orderedresult;
	}

}
