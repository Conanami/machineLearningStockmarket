package org.fuxin.analyst;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;




import org.fuxin.factory.DAOFactory;
import org.fuxin.vo.FinanceData;
import org.fuxin.vo.StockDaily;

/***
 * 内存映射专家，把数据库数据全部读入内存，方便以后的分析使用
 * @author Administrator
 *
 */
public class MapExpert {
	//下面是所有的数据
	private HashMap<String,ArrayList<StockDaily>> alldata = new HashMap<String,ArrayList<StockDaily>>();
	//下面是股票代码列表
	private ArrayList<String> symbolList = null;
	//下面是所有的财务数据
	private HashMap<String,FinanceData> financedata = new HashMap<String,FinanceData>();
	//下面是所有的大盘数据
	private ArrayList<StockDaily> shindex = null;
	public MapExpert(ArrayList<String> symbolList) {
		try {
			this.symbolList = symbolList;
			setFinancedata(DAOFactory.getStockDailyDAOInstance().findFinanceinMap(this.symbolList));
			setAlldata(DAOFactory.getStockDailyDAOInstance().findAllinMap(this.symbolList,this.financedata));
			setIndexdata(DAOFactory.getStockDailyDAOInstance().findIndexData("SH000001"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setIndexdata(ArrayList<StockDaily> findIndexData) {
		// TODO Auto-generated method stub
		System.out.println("载入上证指数数据...."+findIndexData.size());
		this.setShindex(findIndexData);
	}

	public MapExpert(ArrayList<String> symbolList, boolean b) {
		try {
			this.symbolList = symbolList;
			if(b==true)
			{
				setFinancedata(DAOFactory.getStockDailyDAOInstance().findFinanceinMap(this.symbolList));
				setAlldata(DAOFactory.getStockDailyDAOInstance().findRecentinMap(symbolList,this.financedata));
				setIndexdata(DAOFactory.getStockDailyDAOInstance().findIndexData("SH000001"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MapExpert(ArrayList<String> symbolList, boolean b, Date selectday) {
		try {
			this.symbolList = symbolList;
			if(b==true)
			{
				setFinancedata(DAOFactory.getStockDailyDAOInstance().findFinanceinMap(this.symbolList));
				setAlldata(DAOFactory.getStockDailyDAOInstance().findRecentinMap(symbolList,selectday,this.financedata));
				setIndexdata(DAOFactory.getStockDailyDAOInstance().findIndexData("SH000001"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getSymbolList() {
		return symbolList;
	}
	public void setSymbolList(ArrayList<String> symbolList) {
		this.symbolList = symbolList;
	}

	public HashMap<String,ArrayList<StockDaily>> getAlldata() {
		return alldata;
	}

	public void setAlldata(HashMap<String,ArrayList<StockDaily>> alldata) {
		this.alldata = alldata;
	}

	public HashMap<String,FinanceData> getFinancedata() {
		return financedata;
	}

	public void setFinancedata(HashMap<String,FinanceData> financedata) {
		this.financedata = financedata;
	}

	public ArrayList<StockDaily> getShindex() {
		return shindex;
	}

	public void setShindex(ArrayList<StockDaily> shindex) {
		this.shindex = shindex;
	}
		
}
