package org.fuxin.util;

import java.util.ArrayList;

import org.fuxin.passcode.StockDailyWithReason;

public class ArrayUtil {

	public static ArrayList<ArrayList<Integer>> transform(final int[][] q) {
		ArrayList<ArrayList<Integer>> rtarray = new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<q.length;++i)
		{
			ArrayList<Integer> single = new ArrayList<Integer>();
			for(int j=0;j<q[0].length;++j)
			{
				single.add(q[i][j]);
			}
			rtarray.add(single);
		}
		return rtarray;
	}

	/***
	 * 合并两个列表，并且把reason加入到codelist里面
	 * @param codelist
	 * @param indexlist
	 * @return
	 */
	public static ArrayList<StockDailyWithReason> Merge(ArrayList<StockDailyWithReason> codelist,
			ArrayList<StockDailyWithReason> indexlist) {
		ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
		for(int i=0;i<codelist.size();++i)
		{
			for(int j=0;j<indexlist.size();++j)
				if(codelist.get(i).equals(indexlist.get(j)))
				{
					codelist.get(i).buyreason.cscore.isStrong=indexlist.get(j).buyreason.reason;
					rtlist.add(codelist.get(i));
				}
		}
		return rtlist;
	}

}
