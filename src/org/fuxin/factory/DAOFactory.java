package org.fuxin.factory;

import org.fuxin.dao.IStockDailyDAO;
import org.fuxin.dao.StockDailyDAOProxy;

public class DAOFactory {
	public static IStockDailyDAO getStockDailyDAOInstance() throws Exception
	{
		return new StockDailyDAOProxy();
	}
}
