package org.fuxin.passcode;

import java.util.Collection;
import java.util.HashSet;

import org.fuxin.analyst.MapExpert;
import org.fuxin.report.SelectReport;

/***
 * ����������е��඼��Ϊ�˼�����ͣ���룬��ͣ�����Ȿ��
 * @author Administrator
 * PoolManager�������ѡ��
 */
public class PoolManager {
	
	/***doSelect��ѡ���ض���Ʊ�����Ʊ��
	 * Ϊ�˹�ƽ�ļ��飬��������ѡ��ȫ��ѡ��
	 * @param me
	 * @return
	 */
	public static HashSet<String> doSelect(MapExpert me) {
		if(me==null) return null;
		HashSet<String> rtSet = new HashSet<String>();
		for(String symbol:me.getSymbolList())
		{
			if(me.getFinancedata().get(symbol)!=null)
			{
				//��ͨ��С��30�ڣ�����������
				//if(me.getFinancedata().get(symbol).getUnlimit_shares()<=300000
					//&& (
					//me.getFinancedata().get(symbol).getNetprofit_rose()>=0 
					//|| me.getFinancedata().get(symbol).getRevenue_rose()>=0 ) 
				//	)
					rtSet.add(symbol);
			}
		}
		return rtSet;
	}

}
