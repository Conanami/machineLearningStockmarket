package org.fuxin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.fuxin.report.StatisticReport;
import org.fuxin.stock.DiscreteScope;
import org.fuxin.stock.SimpleShape;
import org.fuxin.util.Pair;
import org.fuxin.vo.FinanceData;
import org.fuxin.vo.StockDaily;



public class StockDailyDAOImpl implements IStockDailyDAO{
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	
	public StockDailyDAOImpl(Connection conn)
	{
		this.conn = conn;
	}
	
	public List<StockDaily> findAll(String symbol,Date startdate,Date enddate) throws Exception
	{
		List<StockDaily> rtlist = new ArrayList<StockDaily>();
		String sql = "SELECT symbol,date,open,low,high,close,"
				+ "volume,amount,name FROM daily_par "
				+ "WHERE symbol=? and date>=>? and date<=?";
		
		this.pstmt = this.conn.prepareStatement(sql);
		
		this.pstmt.setString(1, symbol);
		this.pstmt.setDate(2, new java.sql.Date(startdate.getTime()));
		this.pstmt.setDate(3, new java.sql.Date(startdate.getTime()));
		
		ResultSet rs = this.pstmt.executeQuery();
		StockDaily sd = null;
		while(rs.next())
		{
			sd = new StockDaily();
			sd.setSymbol(rs.getString(1));
			sd.setDate(rs.getDate(2));
			sd.setOpen(rs.getFloat(3));
			sd.setLow(rs.getFloat(4));
			sd.setHigh(rs.getFloat(5));
			sd.setClose(rs.getFloat(6));
			sd.setVolume(rs.getLong(7));
			sd.setAmount(rs.getLong(8));
			sd.setName(rs.getString(9));
			rtlist.add(sd);
		}
		
		this.pstmt.close();
		return rtlist;
	}

	@Override
	public List<String> findSymbol(String keyWord) throws Exception {
		// TODO Auto-generated method stub
		List<String> rtlist = new ArrayList<String>();
		String sql = "SELECT distinct(symbol) "
				+ " FROM daily_par "
				+ "WHERE symbol like ?";
		
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setString(1, keyWord);
		ResultSet rs = this.pstmt.executeQuery();
		String rtsymbol = null;
		while(rs.next())
		{
			rtsymbol=rs.getString(1);
			rtlist.add(rtsymbol);
		}
		this.pstmt.close();
		return rtlist;
	}

	
	public List<String> findStockSymbol(String keyWord) throws Exception {
		// TODO Auto-generated method stub
		List<String> rtlist = new ArrayList<String>();
		String sql = "SELECT distinct(symbol) "
				+ " FROM daily_par "
				+ "WHERE symbol LIKE ? AND (symbol like 'SZ00%' OR "
				+ "symbol LIKE 'SH60%' OR symbol like 'SZ30%')";
		
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setString(1, keyWord);
		ResultSet rs = this.pstmt.executeQuery();
		String rtsymbol = null;
		while(rs.next())
		{
			rtsymbol=rs.getString(1);
			rtlist.add(rtsymbol);
		}
		this.pstmt.close();
		return rtlist;
	}

	
	
	/***
	 * b是否包括中小板
	 * c是否包括创业板
	 */
	@Override
	public ArrayList<String> findStockSymbol(String keyWord, boolean mainban, boolean zxb ,boolean cyb)
			throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> rtlist = new ArrayList<String>();
		
		String sql = "SELECT distinct(symbol) "
				+ " FROM daily_par "
				+ "WHERE symbol LIKE ? AND ( 1=0 ";
		
		if(mainban)
		{
			sql = sql + " OR symbol like 'SZ000%' OR "
				+ "symbol LIKE 'SH60%' ";
		}
		if(zxb)
		{
			sql = sql + " OR symbol like 'SZ002%' ";
		}
		
		if(cyb)
		{
			sql = sql +  " OR symbol like 'SZ30%' ";
		}
		
		sql = sql +")";
		
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setString(1, keyWord);
		ResultSet rs = this.pstmt.executeQuery();
		String rtsymbol = null;
		while(rs.next())
		{
			rtsymbol=rs.getString(1);
			rtlist.add(rtsymbol);
		}
		this.pstmt.close();
		return rtlist;
	}

	public ArrayList<Date> findSymbolOldday(String symbol, Integer interval)
			throws Exception {
		// TODO Auto-generated method stub
		ArrayList<Date> rtdaylist = new ArrayList<Date>();
		String sql = "SELECT date "
				+ " FROM daily_par "
				+ "WHERE symbol=? order by date desc";
		
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setString(1, symbol);
		ResultSet rs = this.pstmt.executeQuery();
		int cnt = interval/2 + new Random().nextInt(interval);
		int tmp = 0;
		while(rs.next())
		{
			tmp++;
			if(tmp==cnt)
			{
				rtdaylist.add(rs.getDate(1));
				tmp=0;
				cnt = (int) (interval/1.3 + new Random().nextInt(interval));
			}
		}
		this.pstmt.close();
		return rtdaylist;
	}

	@Override
	public ArrayList<StockDaily> findAll(String symbol) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
		String sql = "SELECT symbol,date,open,low,high,close,"
				+ "volume,amount,name FROM daily_par "
				+ "WHERE symbol=? order by date desc";
		
		this.pstmt = this.conn.prepareStatement(sql);
		
		this.pstmt.setString(1, symbol);
				
		ResultSet rs = this.pstmt.executeQuery();
		StockDaily sd = null;
		while(rs.next())
		{
			sd = new StockDaily();
			sd.setSymbol(rs.getString(1));
			sd.setDate(rs.getDate(2));
			sd.setOpen(rs.getFloat(3));
			sd.setLow(rs.getFloat(4));
			sd.setHigh(rs.getFloat(5));
			sd.setClose(rs.getFloat(6));
			sd.setVolume(rs.getLong(7));
			sd.setAmount(rs.getLong(8));
			sd.setName(rs.getString(9));
			rtlist.add(sd);
		}
		
		this.pstmt.close();
		return rtlist;
	}

	@Override
	public HashMap<String, ArrayList<StockDaily>> findAllinMap(
			ArrayList<String> symbolList, HashMap<String, FinanceData> financedata) throws Exception {
		HashMap<String,ArrayList<StockDaily>> rtmap = new HashMap<String,ArrayList<StockDaily>>(2600);
		int cnt = 0;
		for(String symbol:symbolList)
		{
			Float unlimit_shares = financedata.get(symbol).getUnlimit_shares();
			cnt++;
			System.out.println("MapExpert:"+symbol);
			ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
			String sql = "SELECT symbol,date,open,low,high,close,"
				+ "volume,amount,name FROM daily_par "
				+ "WHERE symbol=? order by date desc";
		
			this.pstmt = this.conn.prepareStatement(sql);
		
			this.pstmt.setString(1, symbol);
				
			ResultSet rs = this.pstmt.executeQuery();
			StockDaily sd = null;
			while(rs.next())
			{
				sd = new StockDaily();
				sd.setSymbol(rs.getString(1));
				sd.setDate(rs.getDate(2));
				sd.setOpen(rs.getFloat(3));
				sd.setLow(rs.getFloat(4));
				sd.setHigh(rs.getFloat(5));
				sd.setClose(rs.getFloat(6));
				sd.setVolume(rs.getLong(7));
				sd.setAmount(rs.getLong(8));
				sd.setName(rs.getString(9));
				sd.setTurnover((float)rs.getLong(7)/unlimit_shares);
				rtlist.add(sd);
			}
		
			this.pstmt.close();
			rtmap.put(symbol,rtlist);
			if(cnt%200==0) System.gc();
		}
		return rtmap;
	}


	

	@Override
	public HashMap<String, ArrayList<StockDaily>> findRecentinMap(
			ArrayList<String> symbolList,HashMap<String, FinanceData> financedata) throws Exception {
		HashMap<String,ArrayList<StockDaily>> rtmap = new HashMap<String,ArrayList<StockDaily>>(2600);
		
		int cnt = 0;
		for(String symbol:symbolList)
		{
			Float unlimit_shares = financedata.get(symbol).getUnlimit_shares();
			cnt ++;
			System.out.println("MapExpert:"+symbol);
			ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
			String sql = "SELECT symbol,date,open,low,high,close,"
				+ "volume,amount,name FROM daily_par "
				+ "WHERE symbol=? order by date desc limit 600";
		
			this.pstmt = this.conn.prepareStatement(sql);
		
			this.pstmt.setString(1, symbol);
				
			ResultSet rs = this.pstmt.executeQuery();
			StockDaily sd = null;
			while(rs.next())
			{
				sd = new StockDaily();
				sd.setSymbol(rs.getString(1));
				sd.setDate(rs.getDate(2));
				sd.setOpen(rs.getFloat(3));
				sd.setLow(rs.getFloat(4));
				sd.setHigh(rs.getFloat(5));
				sd.setClose(rs.getFloat(6));
				sd.setVolume(rs.getLong(7));
				sd.setAmount(rs.getLong(8));
				sd.setName(rs.getString(9));
				sd.setTurnover((float)rs.getLong(7)/unlimit_shares);
				rtlist.add(sd);
			}
		
			this.pstmt.close();
			rtmap.put(symbol,rtlist);
			if(cnt%200==0)
				System.gc();
		}
		return rtmap;
	}

	@Override
	public SimpleShape getSimpleShape(SimpleShape ss, int i) throws Exception {
		SimpleShape rtss = new SimpleShape();
		String sql = "SELECT symbol,date,open,low,high,close,"
				+ "volume,amount,name FROM daily_par "
				+ "WHERE symbol=? and date>=? order by date limit ?";
		
		this.pstmt = this.conn.prepareStatement(sql);
		
		this.pstmt.setString(1, ss.symbol);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.pstmt.setString(2, sdf.format(ss.date));
		this.pstmt.setInt(3, i);
		
		rtss.symbol = ss.symbol;
		
		ResultSet rs = this.pstmt.executeQuery();
		while(rs.next())
		{
			rtss.name = rs.getString(9);
			rtss.date = rs.getDate(2);
			rtss.price = rs.getFloat(6);
		}
		
		this.pstmt.close();
		return rtss;
	}

	
	/***
	 * 将StatisticReport对象保存到数据库中
	 */
	@Override
	public void saveStatisticReport(StatisticReport sr,
			String batch_name) throws Exception {
		
		int srId;
		String sql = "INSERT INTO t_statisticreport"
				+ "(batch_name,"
				+ "sampleCnt,"
				+ "average,"
				+ "variance,"
				+ "futureday)"
				+ " VALUES (?,?,?,?,?); ";
	
		this.pstmt = this.conn.prepareStatement(sql);
		
		//1,设置第一个参数，batch_name，批量号，以后选股按照批量选
		this.pstmt.setString(1, batch_name);
		
		//2，设置第二个参数，样本数，计算归纳出该形态时的样本总数
		if(sr.getSamplecount()!=null)
			this.pstmt.setFloat(2, sr.getSamplecount());
		else
			this.pstmt.setNull(2, Types.FLOAT);
				
		//3,设置第三个参数，平均获利率，这个参数是主要的形态比较参数
		if(sr.getAverage()!=null)
			this.pstmt.setDouble(3, sr.getAverage());
		else
			this.pstmt.setNull(3, Types.FLOAT);
		
		//4，设置第四个参数，获利率的方差，方差越小，说明在该形态中的越稳定
		if(sr.getVariance()!=null)
			this.pstmt.setDouble(4, sr.getVariance());
		else
			this.pstmt.setNull(4, Types.FLOAT);
		
		//5,设置第五个参数，这个形态统计的是未来多少个交易日的结果
		if(sr.getFutureday()!=0)
			this.pstmt.setFloat(5, sr.getFutureday());
		else
			this.pstmt.setNull(5, Types.FLOAT);
		
		this.pstmt.executeUpdate();
		ResultSet rs = this.pstmt.getGeneratedKeys();
		if(rs.next())
		{
			srId = rs.getInt(1);
			for(int i=0;i<sr.getDs().typename.size();++i)
			{
				saveDiscreteScope(sr.getDs().typename.get(i),srId,i);
			}
			System.out.println(rs.getInt(1));
		}
		rs.close();
		
		this.pstmt.close();
	
		
	}

	private void saveDiscreteScope(Pair pair, int srId, int orderid) throws SQLException {
		
		
		String sql = "INSERT INTO t_pair"
				+ "(srid,"
				+ "orderid,"
				+ "period,"
				+ "min,"
				+ "max)"
				+ " VALUES (?,?,?,?,?); ";
		
	
		this.pstmt = this.conn.prepareStatement(sql);
		
		//1,设置第一个参数，srId,该pair属于哪个statisticReport,SR
		this.pstmt.setInt(1, srId);
		
		//2，设置第二个参数，orderid,顺序id,第一个是0，依次向下
		this.pstmt.setInt(2, orderid);
		
				
		//3,设置第三个参数，pair中的时间长度
		//4,设置第四，第五个参数，最大最小值
		if(pair!=null)
		{
			this.pstmt.setInt(3, pair.period);
			this.pstmt.setFloat(4,pair.min);
			this.pstmt.setFloat(5,pair.max);
		}
		else
		{
			this.pstmt.setInt(3, 0);
			this.pstmt.setFloat(4, 0f);
			this.pstmt.setFloat(5, 0f);
		}	
		
		
		this.pstmt.executeUpdate();
		
		
		this.pstmt.close();
	
		
	}

	@Override
	public ArrayList<StatisticReport> findStatisticReport(String batch_name)
			throws Exception {
		ArrayList<StatisticReport> rtSrlist = new ArrayList<StatisticReport>();
		String sql = "SELECT id,sampleCnt,average,variance,futureday "
				+ "FROM t_statisticreport "
				+ "WHERE batch_name=?";
		
		this.pstmt = this.conn.prepareStatement(sql);
		
		this.pstmt.setString(1, batch_name);
				
		ResultSet rs = this.pstmt.executeQuery();
		while(rs.next())
		{
			StatisticReport onesr = new StatisticReport();
	
			onesr.setMax(0.0);
			onesr.setMin(0.0);
			onesr.setProfitRate(0.0);
			//上面先把3个数据库不记录的参数置0，既可以起到识别作用，又可以保证没有出NULL的错
			onesr.setAverage((double) rs.getFloat(3));
			onesr.setFutureday(rs.getInt(5));
			onesr.setSamplecount(rs.getInt(2));
			onesr.setVariance((double) rs.getFloat(4));
			int srid = rs.getInt(1);
			onesr.setDs(findDiscreteScope(srid));
			rtSrlist.add(onesr);
		}
		
		this.pstmt.close();
		return rtSrlist;
	}

	
	private DiscreteScope findDiscreteScope(int srid) throws SQLException {
		DiscreteScope rtds = new DiscreteScope();
		String sql = "SELECT orderid,period,min,max "
				+" FROM t_pair "
				+ "WHERE srid=? order by orderid";
		
		this.pstmt = this.conn.prepareStatement(sql);
		
		this.pstmt.setInt(1,srid);
				
		ResultSet rs = this.pstmt.executeQuery();
		while(rs.next())
		{
			Pair onepair = new Pair(rs.getInt(2),rs.getFloat(3),rs.getFloat(4));
			rtds.typename.add(onepair);
		}
		
		this.pstmt.close();
		return rtds;
		
	}
	
	@Override
	public HashMap<String, ArrayList<StockDaily>> findRecentinMap(
			ArrayList<String> symbolList, Date selectday,HashMap<String, FinanceData> financedata) throws Exception {
		HashMap<String,ArrayList<StockDaily>> rtmap = new HashMap<String,ArrayList<StockDaily>>();
		int cnt = 0;
		for(String symbol:symbolList)
		{
			
			Float unlimit_shares = null;
			
			if(financedata.get(symbol)==null) unlimit_shares = -10000f;
			else 
				unlimit_shares = financedata.get(symbol).getUnlimit_shares();
			cnt ++;
			System.out.println("MapExpert:"+symbol);
			ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
			String sql = "SELECT symbol,date,open,low,high,close,"
				+ "volume,amount,name FROM daily_par "
				+ "WHERE symbol=? and date<=? order by date desc limit 600";
		
			this.pstmt = this.conn.prepareStatement(sql);
		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.pstmt.setString(1, symbol);
			this.pstmt.setString(2, sdf.format(selectday));
				
			ResultSet rs = this.pstmt.executeQuery();
			StockDaily sd = null;
			while(rs.next())
			{
				sd = new StockDaily();
				sd.setSymbol(rs.getString(1));
				sd.setDate(rs.getDate(2));
				sd.setOpen(rs.getFloat(3));
				sd.setLow(rs.getFloat(4));
				sd.setHigh(rs.getFloat(5));
				sd.setClose(rs.getFloat(6));
				sd.setVolume(rs.getLong(7));
				sd.setAmount(rs.getLong(8));
				sd.setName(rs.getString(9));
				sd.setTurnover((float)rs.getLong(7)/unlimit_shares);
				rtlist.add(sd);
			}
		
			this.pstmt.close();
			if(rtlist.size()>200)
				rtmap.put(symbol,rtlist);
			if(cnt%200==0)
				System.gc();
		}
		return rtmap;
	}

	//把财务数据装进内存
	@Override
	public HashMap<String, FinanceData> findFinanceinMap(
			ArrayList<String> symbolList) throws Exception {
		HashMap<String,FinanceData> rtmap = new HashMap<String,FinanceData>();
		for(String symbol:symbolList)
		{
			System.out.println("Finance:"+symbol);
			FinanceData fd = new FinanceData();
			String sql = "SELECT symbol,"
					+ "reportdate,"
					+ "earning_ps,"
					+ "net_assets_ps,"
					+ "netprofit_rose,"
					+ "revenue_rose, "
					+ "unlimit_shares "
					+ " FROM t_finance "
					+ " WHERE symbol=? order by reportdate desc ";
		
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, symbol);
			
				
			ResultSet rs = this.pstmt.executeQuery();
			
			if(rs.next())
			{
				fd.setSymbol(rs.getString(1));
				fd.setReportdate(rs.getDate(2));
				fd.setEarning_ps(rs.getFloat(3));
				fd.setNetassets_ps(rs.getFloat(4));
				fd.setNetprofit_rose(rs.getFloat(5));
				fd.setRevenue_rose(rs.getFloat(6));
				fd.setUnlimit_shares(rs.getFloat(7));
			}
		
			this.pstmt.close();
			
			if(fd.symbol!=null)
			{
				rtmap.put(symbol,fd);
				System.out.println(fd);
			}
		}
		return rtmap;
	}

	@Override
	public SimpleShape getMaxSimpleShape(SimpleShape ss, Date checkDate)
			throws Exception {
		SimpleShape rtss = new SimpleShape();
		String sql = "SELECT symbol,date,open,low,high,close,"
				+ "volume,amount,name FROM daily_par "
				+ "WHERE symbol=? and date>=? and date<=? order by close";
		
		this.pstmt = this.conn.prepareStatement(sql);
		
		this.pstmt.setString(1, ss.symbol);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.pstmt.setString(2, sdf.format(ss.date));
		this.pstmt.setString(3, sdf.format(checkDate));
		
		rtss.symbol = ss.symbol;
		
		ResultSet rs = this.pstmt.executeQuery();
		while(rs.next())
		{
			rtss.date = rs.getDate(2);
			rtss.price = rs.getFloat(6);
		}
		
		this.pstmt.close();
		return rtss;
	}

	public ArrayList<StockDaily> findIndexData(String symbol) throws Exception
	{
		ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
		String sql = "SELECT symbol,date,open,low,high,close,"
				+ "volume,amount,name FROM daily_par "
				+ "WHERE symbol=? order by date desc limit 600";
		
		this.pstmt = this.conn.prepareStatement(sql);
		
		this.pstmt.setString(1, symbol);
				
		ResultSet rs = this.pstmt.executeQuery();
		StockDaily sd = null;
		while(rs.next())
		{
			sd = new StockDaily();
			sd.setSymbol(rs.getString(1));
			sd.setDate(rs.getDate(2));
			sd.setOpen(rs.getFloat(3));
			sd.setLow(rs.getFloat(4));
			sd.setHigh(rs.getFloat(5));
			sd.setClose(rs.getFloat(6));
			sd.setVolume(rs.getLong(7));
			sd.setAmount(rs.getLong(8));
			sd.setName(rs.getString(9));
			rtlist.add(sd);
		}
		
		this.pstmt.close();
		return rtlist;
	}

	
			
	
}

	
