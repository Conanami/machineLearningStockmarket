package org.fuxin.simulator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.fuxin.analyst.MapExpert;
import org.fuxin.macd.MacdSelector;
import org.fuxin.passcode.CodeChooseLimit;
import org.fuxin.passcode.StockDailyWithReason;
import org.fuxin.util.DateUtil;
import org.fuxin.util.FuOutput;
import org.fuxin.util.MathUtil;
import org.fuxin.util.ScopeUtil;
import org.fuxin.vo.StockDaily;

/***
 * 这个类是一个模拟器
 * @author Administrator
 *
 */
public class Simulator {
	
	public Simulator(Account acct1, MapExpert me, Date startday,
			Date endday, Float ratiolimit, HashSet<String> stockpool) {
		this.account = acct1;
		this.me = me;
		this.startdate = startday;
		this.enddate = endday;
		this.ratiolimit = ratiolimit;
		this.mystockpool = stockpool;
		this.tradereport = new ArrayList<SingleDeal>();
		this.buylog = new ArrayList<StockDailyWithReason>();
		this.BuyStrategy = 6;
		this.SellStrategy = 3;
	}
	
	/***
	 * 新的可以选择买入策略和卖出策略的模拟器
	 * @param acct1 			账户
	 * @param me2				所有数据的内存表
	 * @param startday			模拟开始日期
	 * @param endday			模拟结束日期
	 * @param ratiolimit2		资金使用比例限制
	 * @param stockpool			股票池
	 * @param buyStrategy		买入策略
	 * @param sellStrategy		卖出策略
	 */
	public Simulator(Account acct1, MapExpert me2, Date startday, Date endday,
			Float ratiolimit2, HashSet<String> stockpool, int buyStrategy, int sellStrategy) {
		this.account = acct1;
		this.me = me2;
		this.startdate = startday;
		this.enddate = endday;
		this.ratiolimit = ratiolimit2;
		this.mystockpool = stockpool;
		this.tradereport = new ArrayList<SingleDeal>();
		this.buylog = new ArrayList<StockDailyWithReason>();
		this.BuyStrategy = buyStrategy;
		this.SellStrategy = sellStrategy;
	}

	public Account account;						//持有的账户信息
	public Date startdate;						//起始日期
	public Date enddate;						//结束日期
	public MapExpert me;						//内存数据库
	public Float ratiolimit;                    //单一品种最大比例
	private Date currentday;					//运行到某天
	public HashSet<String> mystockpool;			//我的股票池
	public ArrayList<SingleDeal> tradereport;	//我的交易报告
	public CodeChooseLimit chooselimit;
	public ArrayList<StockDailyWithReason> buylog;  //所有的选股结果，包括重复的
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public int SellStrategy;					//卖出策略 
	public int BuyStrategy;						//买入策略
	
	public void run() {
		currentday = startdate;
		while(currentday.before(enddate))
		{
			//进行每天的交易，然后是下一天
			if(isTradingDay(currentday,me))
			{
				doDailyTrade(account,ratiolimit,currentday,me);
			}
			currentday = DateUtil.Add(currentday, 1);
			
		}
		SaveAllholding(currentday);			//把目前持股也保存到交易清单中
		FuOutput.writeToFile(tradereport, "report_"+sdf.format(startdate)+"to"+sdf.format(enddate));
		FuOutput.writeToFile(buylog, "buylog_"+sdf.format(startdate)+"to"+sdf.format(enddate));
	}

	//把目前持股信息也写入最终的报告
	private void SaveAllholding(Date endday) {
		for(HoldingStock hs:account.holdlist.values())
		{
			int endindex = -1;
			ArrayList<StockDaily> dailydata = me.getAlldata().get(hs.symbol);
			if(dailydata!=null) 
				endindex = ScopeUtil.getIndexForward(endday,dailydata);
				
			SaveSellInfo(hs,dailydata.get(endindex),-1f);
		}
		
	}

	//判断当天是否是交易日
	private boolean isTradingDay(Date currentday, MapExpert me) {
		int cnt = 0;
		for(String symbol:me.getSymbolList())
		{
			StockDaily sd = new StockDaily();
			sd.setSymbol(symbol);
			sd.setDate(currentday);
			if(me.getAlldata().get(symbol)==null) continue;
			if(me.getAlldata().get(symbol).contains(sd))
				cnt++;
		}
		if( cnt > me.getSymbolList().size()*0.5 ) return true;   //如果当天有超过一半股票在交易，则判断为交易日
		return false;
		
	}

	//做每天的交易
	private void doDailyTrade(Account account,
			Float ratiolimit,
			Date currentday, MapExpert me) {
		//如果是周六，周日，那肯定不交易
		if(DateUtil.getDay(currentday)==6 ||
				DateUtil.getDay(currentday)==0 ) return;
		boolean happenTrade=false; //这天是否发生交易
		//如果账户里的现金大于总资产的某个比例，则进行选股，并且买股票
		//执行先买后抛的策略，确保有资金再买
		//account.getTotalassets(currentday, me);
		if(account.balance >= 0.33 * account.totalassets * ratiolimit)
		{
			ArrayList<StockDailyWithReason> buylist = null;
			//根据买入策略决定买入列表
			switch(BuyStrategy)
			{
			//6，根据macd和涨停密码进行选股的系统
			case 6:
				buylist = HeadSelector.doMacdCodeSelect(currentday,mystockpool,me,chooselimit);
				break;
			case 7:							//根据MACD和低成交量方式决定
				buylist = HeadSelector.doMacdLowSelect(currentday,mystockpool,me);
				break;
			case 8:							//只根据低成交量来决定买点
				buylist = HeadSelector.doLowSelect(currentday,mystockpool,me);
				break;
			default:
				buylist = null;
			}
			//ArrayList<StockDaily> buylist = AveragePriceSelector.doBuySelect(currentday,mystockpool,me);
			//2,下面是根据均线和两座量山进行选股的系统
			//ArrayList<StockDailyWithReason> buylist = HeadSelector.doSelect(currentday,mystockpool,me);
			//3,根据涨停密码进行选股的系统
			//ArrayList<StockDailyWithReason> buylist = HeadSelector.doCodeSelect(currentday,mystockpool,me,chooselimit);
			//4, 根据涨停密码和两座山选股的系统
			//ArrayList<StockDailyWithReason> buylist = HeadSelector.doCodeVsSelect(currentday,mystockpool,me,chooselimit);
			//5, 根据均线和涨停密码选股的系统
			//ArrayList<StockDailyWithReason> buylist2 = HeadSelector.doCodeAverageSelect(currentday,mystockpool,me,chooselimit);
			//buylist.addAll(buylist2);
			
	
			if(buylist!=null && buylist.size()>0 )
			{
				DoBuy(buylist);
				happenTrade = true;
			}
		}
		
		//当天持有的股票数量
		//ArrayList<StockDaily> selllist = HeadSelector.doSellSelect(currentday,account,me);
		//2，根据均线合嘴卖出的系统
		//ArrayList<StockDaily> selllist = AveragePriceSelector.doSellSelect(currentday,account,me);
		//3,根据MACD系统卖出的系统
		ArrayList<StockDaily> selllist = null;
		//根据卖出策略决定卖出列表
		switch(SellStrategy)
		{
		case 2:
			selllist = MacdSelector.doEasySellSelect(currentday,account,me);
			break;
		case 3:
			selllist = MacdSelector.doSellSelect(currentday,account,me);
			break;
		default:
			break;
		}
		
		if(selllist!=null && selllist.size()>0)
		{
			DoSell(selllist);
			happenTrade = true;
		}
		
		if(happenTrade)
			account.showBrief(currentday,me);
	}

	/***
	 * 根据选中的buylist进行买入动作
	 * @param buylist
	 */
	private void DoBuy(ArrayList<StockDailyWithReason> buylist) {
		//记录所有选中的股票
		buylog.addAll(buylist);
		for(int i = 0;i<buylist.size();++i)
		{
			//实行不补仓，不重复买策略
			if(!account.holdlist.containsKey(buylist.get(i).sd.getSymbol()))
			{
				Float UseMoney = 0f;
				if(account.balance > account.totalassets*ratiolimit)
				{
					UseMoney = account.totalassets*ratiolimit;
				}else
				{
					UseMoney = account.balance;
				}
				
				Float buyprice = buylist.get(i).sd.getOpen();
				int buyshares = (int) (Math.floor(UseMoney/(buyprice*1.002*100)*100));
				
				if(buyshares<1000) return;
				
				Float realSpendMoney = (float) (buyshares * buyprice * 1.002f);
				account.balance = account.balance - realSpendMoney;
				
				HoldingStock newhs = new HoldingStock();
				newhs.symbol= buylist.get(i).sd.getSymbol();
				newhs.buyprice = buyprice;
				newhs.buydate = buylist.get(i).sd.getDate();
				newhs.shares = buyshares;
				newhs.totalcost = realSpendMoney;
				newhs.reason = buylist.get(i).buyreason;
				
				if(account.holdlist.containsKey(newhs.symbol))
				{
					HoldingStock oldhs = account.holdlist.get(newhs.symbol);
					newhs.shares += oldhs.shares;
					newhs.totalcost += oldhs.totalcost;
					newhs.buyprice = newhs.totalcost/newhs.shares;
					account.holdlist.put(newhs.symbol, newhs);
				}else
					account.holdlist.put(newhs.symbol, newhs);
				}
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//System.out.println(sdf.format(newhs.buydate)+"买入:"+newhs);
		}
		
	}

		
		
	/***
	 * 根据抛出list进行抛出
	 * @param selllist
	 */
	private void DoSell(ArrayList<StockDaily> selllist) {
		if(selllist==null || selllist.size()==0) return ;
		HashMap<String,HoldingStock> newlist = (HashMap<String, HoldingStock>) account.holdlist.clone();
		for(StockDaily sd:selllist)
		{
			for(HoldingStock hs:account.holdlist.values())
			{
				if(hs.symbol.equals(sd.getSymbol()))
				{
					Float selltotal = hs.shares * sd.getClose()* 0.998f;
					account.balance += selltotal;
					newlist.remove(hs.symbol);
					SaveSellInfo(hs,sd,selltotal);
					//PrintSellInfo(hs,sd,selltotal);
				}
			}
		}
		account.holdlist = newlist;
		account.getTotalassets(currentday,me);
		//System.out.println("卖出提示:"+account);
	}

	private void SaveSellInfo(HoldingStock hs, StockDaily sd, Float selltotal) {
		SingleDeal singledeal = new SingleDeal();
		
		
		singledeal.buyday = hs.buydate;
		singledeal.buyprice = hs.buyprice;
		singledeal.reason = hs.reason;
		singledeal.shares = hs.shares;
		singledeal.totalcost = hs.totalcost;
		singledeal.symbol = hs.symbol;
		
		if(sd!=null)
			singledeal.name = sd.getName();
		if(selltotal==-1f)
		{
			//这里表示仍旧持股
			singledeal.sellday = sd.getDate();
			singledeal.sellprice = sd.getClose();
			singledeal.totalincome = selltotal;
			singledeal.calculate();
		}
		else if(selltotal>0)
		{
			singledeal.sellday = sd.getDate();
			singledeal.sellprice = sd.getClose();
			singledeal.totalincome = selltotal;
			singledeal.calculate();
		}
		
		tradereport.add(singledeal);
		
	}

	private void PrintSellInfo(HoldingStock hs, StockDaily sd, Float selltotal) {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sb.append(sdf.format(sd.getDate()));
		sb.append("以价格").append(sd.getClose());
		sb.append("抛出").append(sd.getSymbol()).append(sd.getName());
		sb.append(hs.shares).append("股,");
		sb.append("成本为").append(hs.buyprice);
		sb.append("共计盈利：").append((selltotal-hs.totalcost));
		
		System.out.println(sb);
	}

	

	
}
