package org.fuxin.basic;

import java.util.ArrayList;
import java.util.TreeMap;

/***
 * 专门把结果变成报告的人
 * @author Administrator
 *
 */
public class ReportMan {

	public static ArrayList<PrintReport> makeReport(
			TreeMap<String, OriginValue> result) {
		ArrayList<PrintReport> rtlist = new ArrayList<PrintReport>();
		for(String symbol:result.keySet())
		{
			PrintReport preport = new PrintReport(symbol);
			OriginValue originValue = result.get(symbol);
			preport.addValue(originValue.description);
			preport.addValue(originValue.value.toString());
			rtlist.add(preport);
		}
		return rtlist;
	}

	public static ArrayList<PrintReport> makeDiscreteReport(
			TreeMap<String, DiscreteValue> result) {
		ArrayList<PrintReport> rtlist = new ArrayList<PrintReport>();
		for(String symbol:result.keySet())
		{
			PrintReport preport = new PrintReport(symbol);
			DiscreteValue discreteValue = result.get(symbol);
			preport.addValue(discreteValue.originvalue.description);
			preport.addValue(discreteValue.originvalue.value.toString());
			preport.addValue(discreteValue.discretevalue.toString());
			preport.addValue(discreteValue.scopepair.toString());
			rtlist.add(preport);
		}
		return rtlist;
	}

}
