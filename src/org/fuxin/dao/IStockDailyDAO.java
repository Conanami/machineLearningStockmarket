package org.fuxin.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.fuxin.report.StatisticReport;
import org.fuxin.stock.DiscreteScope;
import org.fuxin.stock.SimpleShape;
import org.fuxin.vo.*;

public interface IStockDailyDAO {
	public List<StockDaily> findAll(String symbol,Date startdate,Date enddate) throws Exception;
	public List<String> findSymbol(String keyWord) throws Exception;
	public List<String> findStockSymbol(String keyWord) throws Exception;
	
	public ArrayList<String> findStockSymbol(String str, boolean mainban, boolean zxban, boolean cyban) throws Exception;
	
	public ArrayList<Date> findSymbolOldday(String symbol,Integer inteval) throws Exception;
	
	public ArrayList<StockDaily> findAll(String symbol) throws Exception;
	
	
	public HashMap<String, ArrayList<StockDaily>> findRecentinMap(
			ArrayList<String> symbolList, HashMap<String, FinanceData> financedata) throws Exception;
	//返回数据库最近的数据，放到内存中
	public HashMap<String, ArrayList<StockDaily>> findAllinMap(
			ArrayList<String> symbolList, HashMap<String, FinanceData> financedata) throws Exception;
	//返回数据库中所有的数据，放到内存中
	
	//返回i天后的Simple Shape，简单形态，比Shape信息量更少
	public SimpleShape getSimpleShape(SimpleShape ss, int i) 
			throws Exception;
	
	
	//保存一份统计报告Statistic Report 到数据库
	public void saveStatisticReport(StatisticReport statisticReport,
			String batch_name) throws Exception;
	
	//按照批量名，查询数据库，返回一组StatisticReport
	public ArrayList<StatisticReport> findStatisticReport(String batch_name)
			throws Exception;
	
	//按照代码列表，要选股的基准日，把数据返回到内存中
	public HashMap<String, ArrayList<StockDaily>> findRecentinMap(
			ArrayList<String> symbolList, Date selectday , HashMap<String, FinanceData> financedata) 
					throws Exception;
	
	//按照代码列表，把财务数据返回内存中
	public HashMap<String, FinanceData> findFinanceinMap(
			ArrayList<String> symbolList) throws Exception;
	//取得从ss到checkDate之间最高价的点
	public SimpleShape getMaxSimpleShape(SimpleShape ss, Date checkDate) throws Exception;
	//取得ss后多少天的最高点
	//public SimpleShape getMaxSimpleShapeAfterDays(SimpleShape ss,
	//		Integer daysAfter) throws Exception;
	
	//取得沪市大盘数据
	public ArrayList<StockDaily> findIndexData(String symbol) throws Exception;
	
	
}

