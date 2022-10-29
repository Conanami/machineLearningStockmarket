package org.fuxin.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.fuxin.factory.DAOFactory;
import org.fuxin.report.StatisticReport;


/***
 * 专门保存形态和读取形态
 * @author Administrator
 * 2014-07-28
 */
public class Saver {

	
	public static void SaveToDB(ArrayList<StatisticReport> srList) {
		SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyyMMdd-HHmm");
		String batch_name = sdf.format(new Date());
		for(StatisticReport sr:srList)
		{
			sr.save(batch_name);
		}
		
	}

	public static ArrayList<StatisticReport> loadStatisticReport(
			String batch_name) {
		try {
			return DAOFactory.getStockDailyDAOInstance().findStatisticReport(batch_name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
