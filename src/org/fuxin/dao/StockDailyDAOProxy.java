package org.fuxin.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;






import java.util.Map;

import org.fuxin.dbc.DatabaseConnection;
import org.fuxin.report.StatisticReport;
import org.fuxin.stock.SimpleShape;
import org.fuxin.vo.FinanceData;
import org.fuxin.vo.StockDaily;


public class StockDailyDAOProxy implements IStockDailyDAO{
	
	private DatabaseConnection dbc = null;
	private IStockDailyDAO dao = null;
	
	public StockDailyDAOProxy() throws Exception
	{
		this.dbc = new DatabaseConnection();
		this.dao = new StockDailyDAOImpl(this.dbc.getConnection());
	}
	@Override
	public List<StockDaily> findAll(String symbol, Date startdate, Date enddate)
			throws Exception {
		// TODO Auto-generated method stub
		List<StockDaily> rtlist = null;
		try{
			rtlist=this.dao.findAll(symbol, startdate, enddate);
		}catch(Exception e)
		{
			
		}finally
		{
			this.dbc.close();
		}
		return rtlist;
		
	}

	@Override
	public List<String> findSymbol(String keyWord) throws Exception {
		// TODO Auto-generated method stub
		List<String> rtlist = null;
		try{
			rtlist=this.dao.findSymbol(keyWord);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtlist;
	}
	
	public List<String> findStockSymbol(String keyWord) throws Exception {
		// TODO Auto-generated method stub
		List<String> rtlist = null;
		try{
			rtlist=this.dao.findStockSymbol(keyWord);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtlist;
	}
	
	
	
	@Override
	public ArrayList<String> findStockSymbol(String keyWord,boolean mainban, boolean zxb, boolean cyb)
			throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> rtlist = null;
		try{
			rtlist=this.dao.findStockSymbol(keyWord,mainban,zxb,cyb);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtlist;
	}
	@Override
	public ArrayList<Date> findSymbolOldday(String symbol, Integer inteval)
			throws Exception {
		ArrayList<Date> rtlist = null;
		try{
			rtlist=this.dao.findSymbolOldday(symbol, inteval);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtlist;
	}
	
	@Override
	public ArrayList<StockDaily> findAll(String symbol) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<StockDaily> rtlist = null;
		try{
			rtlist=this.dao.findAll(symbol);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtlist;
	}
	public HashMap<String, ArrayList<StockDaily>> findAllinMap(
			ArrayList<String> symbolList, HashMap<String, FinanceData> financedata) throws Exception {
		
		HashMap<String, ArrayList<StockDaily>> rtmap = null;
		try{
			rtmap=this.dao.findAllinMap(symbolList,financedata);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtmap;
	}
	
	
	@Override
	public HashMap<String, ArrayList<StockDaily>> findRecentinMap(
			ArrayList<String> symbolList,HashMap<String, FinanceData> financedata) throws Exception {
		HashMap<String, ArrayList<StockDaily>> rtmap = null;
		try{
			rtmap=this.dao.findRecentinMap(symbolList,financedata);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtmap;
	}
	
	@Override
	public SimpleShape getSimpleShape(SimpleShape ss, int i) throws Exception {
		SimpleShape rtss = null;
		try{
			rtss =this.dao.getSimpleShape(ss, i);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtss;
	}
	
	
	@Override
	public void saveStatisticReport(StatisticReport statisticReport,
			String batch_name) throws Exception {
		try{
			this.dao.saveStatisticReport(statisticReport, batch_name);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		
	}
	@Override
	public ArrayList<StatisticReport> findStatisticReport(String batch_name)
			throws Exception {
		try{
			return this.dao.findStatisticReport(batch_name);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return null;
	}
	
	
	@Override
	public HashMap<String, ArrayList<StockDaily>> findRecentinMap(
			ArrayList<String> symbolList, Date selectday, HashMap<String, FinanceData> financedata) throws Exception {
		HashMap<String, ArrayList<StockDaily>> rtmap = null;
		try{
			if(symbolList==null || symbolList.size()==0 || financedata==null) 
			{
				this.dbc.close();
				return null;
			}
			
			rtmap=this.dao.findRecentinMap(symbolList,selectday,financedata);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtmap;
	}
	
	@Override
	public HashMap<String, FinanceData> findFinanceinMap(
			ArrayList<String> symbolList) throws Exception {
		HashMap<String, FinanceData> rtmap = null;
		try{
			rtmap=this.dao.findFinanceinMap(symbolList);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtmap;
	}
	@Override
	public SimpleShape getMaxSimpleShape(SimpleShape ss, Date checkDate)
			throws Exception {
		SimpleShape rtss = null;
		try{
			rtss =this.dao.getMaxSimpleShape(ss, checkDate);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtss;
	}
	@Override
	public ArrayList<StockDaily> findIndexData(String symbol) throws Exception {
		ArrayList<StockDaily> rtlist = null;
		try{
			rtlist=this.dao.findIndexData(symbol);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			this.dbc.close();
		}
		return rtlist;
	}

}
