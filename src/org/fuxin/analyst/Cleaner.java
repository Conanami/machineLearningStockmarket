package org.fuxin.analyst;

import java.util.Date;
import java.util.HashSet;

import org.fuxin.report.SelectReport;

/***
 * ר�����������
 * @author Administrator
 *
 */
public class Cleaner {

	public static HashSet<SelectReport> doClean(HashSet<SelectReport> result,
			Date selectday) {
		HashSet<SelectReport> rtset = new HashSet<SelectReport>();
		for(SelectReport sltrpt:result)
		{
			if(sltrpt.endday.equals(selectday))
			{
				rtset.add(sltrpt);
			}
		}
		return rtset;
	}

}
