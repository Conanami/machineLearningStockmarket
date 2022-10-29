package org.fuxin.analyst;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.factory.DAOFactory;
import org.fuxin.report.SelectReport;
import org.fuxin.stock.SimpleShape;
import org.fuxin.stock.VerifyResult;

public class Verifier {

	public static ArrayList<VerifyResult> check(ArrayList<SimpleShape> slist,
			int i) {
		// TODO Auto-generated method stub
		ArrayList<VerifyResult> vrlist = new ArrayList<VerifyResult>();
		for(SimpleShape ss:slist)
		{
			vrlist.add(GetSimpleShape(ss,i));
		}
		return vrlist;
	}

	private static VerifyResult GetSimpleShape(SimpleShape ss, int i) {
		// TODO Auto-generated method stub
		VerifyResult vr = new VerifyResult(ss);
		try {
			SimpleShape now = DAOFactory.getStockDailyDAOInstance().getSimpleShape(ss,i);
			System.out.println(now);
			vr.setNow(now);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vr;
	}

	/***
	 * 取得未来多少天的价格
	 * @param result
	 * @parasm futureday
	 * @return
	 */
	public static ArrayList<VerifyResult> check(HashSet<SelectReport> result,
			int futureday) {
		// TODO Auto-generated method stub
		ArrayList<SimpleShape> sslist = GetSimpleShapeList(result);
		return check(sslist,futureday);
	}

	private static ArrayList<SimpleShape> GetSimpleShapeList(
			HashSet<SelectReport> result) {
		// TODO Auto-generated method stub
		ArrayList<SimpleShape> rtlist = new ArrayList<SimpleShape>();
		for(SelectReport sr:result)
		{
			SimpleShape tmpss = new SimpleShape();
			tmpss.date = sr.endday;
			tmpss.price = sr.price;
			tmpss.symbol = sr.symbol;
			rtlist.add(tmpss);
		}
		return rtlist;
	}

	public static ArrayList<VerifyResult> checkMax(
			ArrayList<SimpleShape> slist, Date checkDate) {
		ArrayList<VerifyResult> vrlist = new ArrayList<VerifyResult>();
		for(SimpleShape ss:slist)
		{
			vrlist.add(GetMaxSimpleShape(ss,checkDate));
		}
		return vrlist;
	}

	private static VerifyResult GetMaxSimpleShape(SimpleShape ss, Date checkDate) {
		VerifyResult vr = new VerifyResult(ss);
		try {
			SimpleShape now = DAOFactory.getStockDailyDAOInstance().getMaxSimpleShape(ss,checkDate);
			System.out.println(now);
			vr.setNow(now);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vr;
	}

	public static ArrayList<VerifyResult> checkMax(
			ArrayList<SimpleShape> slist, Integer daysAfter) {
		ArrayList<VerifyResult> vrlist = new ArrayList<VerifyResult>();
		for(SimpleShape ss:slist)
		{
			//vrlist.add(GetMaxSimpleShape(ss,daysAfter));
		}
		return vrlist;
	}

	

	
	

}
