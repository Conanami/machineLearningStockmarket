package org.fuxin.util;

import java.util.ArrayList;
import java.util.Date;

import org.fuxin.factory.DAOFactory;
import org.fuxin.vo.StockDaily;

public class ScopeUtil {
	/***
	 * 通过范围，决定输出的代码
	 * @param scope
	 * @return
	 */
	public static boolean[] getFlag(int scope) {
		boolean[] rtflag = new boolean[3];
		switch(scope)
		{
			/*
			ZXB = 1;
			CYB = 2;
			MA_ZX = 3;
			MA_CY = 4;
			ZX_CY = 5;
			MA_ZX_CY = 6;
			*/
		case 0:
			rtflag[0]=true;
			rtflag[1]=false;
			rtflag[2]=false;
			break;
		case 1:
			rtflag[0]=false;
			rtflag[1]=true;
			rtflag[2]=false;
			break;
		case 2:
			rtflag[0]=false;
			rtflag[1]=false;
			rtflag[2]=true;
			break;
		case 3:
			rtflag[0]=true;
			rtflag[1]=true;
			rtflag[2]=false;
			break;
		case 4:
			rtflag[0]=true;
			rtflag[1]=false;
			rtflag[2]=true;
			break;
		case 5:
			rtflag[0]=false;
			rtflag[1]=true;
			rtflag[2]=true;
			break;
		case 6:
			rtflag[0]=true;
			rtflag[1]=true;
			rtflag[2]=true;
			break;
			
		default:
			rtflag[0]=false;
			rtflag[1]=false;
			rtflag[2]=false;
			
		}
		return rtflag;
	}
	
	public static ArrayList<String> getSymbol(String queryStr, int scope) {
		try {
			boolean flag[]=ScopeUtil.getFlag(scope);
			return (ArrayList<String>) DAOFactory.
				getStockDailyDAOInstance().findStockSymbol(queryStr,flag[0],flag[1],flag[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * 取得后一天的index
	 * @param startday
	 * @param pricelist
	 * @return
	 */
	public static int getIndex(Date startday, ArrayList<StockDaily> pricelist)
	{
		if(pricelist==null || pricelist.size()==0)
		{
			System.out.println("数据库中无数据");
			return -1;
		}
		
		StockDaily tmpsd = new StockDaily();
		tmpsd.setSymbol(pricelist.get(0).getSymbol());
		tmpsd.setDate(startday);
				
		if(startday.getTime()>pricelist.get(0)
				.getDate().getTime()) 
			return -1;
		
		if(startday.getTime()<
				pricelist.get(pricelist.size()-1).
				getDate().getTime()) 
		{
			return -1;
		}
		
		while(pricelist.indexOf(tmpsd)==-1)
		{
			startday = DateUtil.Add(startday, 1);
			tmpsd.setDate(startday);
		}
		return pricelist.indexOf(tmpsd);
	}

	
	/***
	 * 取得前一天的index
	 * @param endday
	 * @param dailydata
	 * @return
	 */
	public static int getIndexForward(Date endday,
			ArrayList<StockDaily> dailydata) {
		
		if(dailydata==null || dailydata.size()==0)
		{
			System.out.println("数据库中无数据");
			return -1;
		}
		for(int i=0;i<dailydata.size();++i)
		{
			if(dailydata.get(i).getDate().equals(endday))
			{
				return i;
			}
			else
			{
				if(dailydata.get(i).getDate().before(endday))
				{
					return i;
				}
			}
		}
		return -1;
	
	}
	
	//只返回精确的当天index
	public static int getOnlyIndex(Date currentday,
			ArrayList<StockDaily> dailydata) {
		if(dailydata==null || dailydata.size()==0)
		{
			System.out.println("数据库中无数据");
			return -1;
		}
		for(int i=0;i<dailydata.size();++i)
		{
			if(dailydata.get(i).getDate()==null) continue;
			if(dailydata.get(i).getDate().equals(currentday))
			{
				return i;
			}
		}
		return -1;
	}

	/***
	 * 根据日期查到的下标索引
	 * @param date
	 * @param dailydata
	 * @return
	 */
	public static Integer getIndexByDate(Date date,
			ArrayList<StockDaily> dailydata) {
		for(int i=0;i<dailydata.size();++i)
		{
			if(dailydata.get(i).getDate().equals(date))
			{
				return i;
			}
		}
		return -1;
	}
	
}
