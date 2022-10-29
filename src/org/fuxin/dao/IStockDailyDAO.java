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
	//�������ݿ���������ݣ��ŵ��ڴ���
	public HashMap<String, ArrayList<StockDaily>> findAllinMap(
			ArrayList<String> symbolList, HashMap<String, FinanceData> financedata) throws Exception;
	//�������ݿ������е����ݣ��ŵ��ڴ���
	
	//����i����Simple Shape������̬����Shape��Ϣ������
	public SimpleShape getSimpleShape(SimpleShape ss, int i) 
			throws Exception;
	
	
	//����һ��ͳ�Ʊ���Statistic Report �����ݿ�
	public void saveStatisticReport(StatisticReport statisticReport,
			String batch_name) throws Exception;
	
	//��������������ѯ���ݿ⣬����һ��StatisticReport
	public ArrayList<StatisticReport> findStatisticReport(String batch_name)
			throws Exception;
	
	//���մ����б�Ҫѡ�ɵĻ�׼�գ������ݷ��ص��ڴ���
	public HashMap<String, ArrayList<StockDaily>> findRecentinMap(
			ArrayList<String> symbolList, Date selectday , HashMap<String, FinanceData> financedata) 
					throws Exception;
	
	//���մ����б��Ѳ������ݷ����ڴ���
	public HashMap<String, FinanceData> findFinanceinMap(
			ArrayList<String> symbolList) throws Exception;
	//ȡ�ô�ss��checkDate֮����߼۵ĵ�
	public SimpleShape getMaxSimpleShape(SimpleShape ss, Date checkDate) throws Exception;
	//ȡ��ss����������ߵ�
	//public SimpleShape getMaxSimpleShapeAfterDays(SimpleShape ss,
	//		Integer daysAfter) throws Exception;
	
	//ȡ�û��д�������
	public ArrayList<StockDaily> findIndexData(String symbol) throws Exception;
	
	
}

