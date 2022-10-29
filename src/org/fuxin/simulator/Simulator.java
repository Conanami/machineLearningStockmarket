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
 * �������һ��ģ����
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
	 * �µĿ���ѡ��������Ժ��������Ե�ģ����
	 * @param acct1 			�˻�
	 * @param me2				�������ݵ��ڴ��
	 * @param startday			ģ�⿪ʼ����
	 * @param endday			ģ���������
	 * @param ratiolimit2		�ʽ�ʹ�ñ�������
	 * @param stockpool			��Ʊ��
	 * @param buyStrategy		�������
	 * @param sellStrategy		��������
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

	public Account account;						//���е��˻���Ϣ
	public Date startdate;						//��ʼ����
	public Date enddate;						//��������
	public MapExpert me;						//�ڴ����ݿ�
	public Float ratiolimit;                    //��һƷ��������
	private Date currentday;					//���е�ĳ��
	public HashSet<String> mystockpool;			//�ҵĹ�Ʊ��
	public ArrayList<SingleDeal> tradereport;	//�ҵĽ��ױ���
	public CodeChooseLimit chooselimit;
	public ArrayList<StockDailyWithReason> buylog;  //���е�ѡ�ɽ���������ظ���
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public int SellStrategy;					//�������� 
	public int BuyStrategy;						//�������
	
	public void run() {
		currentday = startdate;
		while(currentday.before(enddate))
		{
			//����ÿ��Ľ��ף�Ȼ������һ��
			if(isTradingDay(currentday,me))
			{
				doDailyTrade(account,ratiolimit,currentday,me);
			}
			currentday = DateUtil.Add(currentday, 1);
			
		}
		SaveAllholding(currentday);			//��Ŀǰ�ֹ�Ҳ���浽�����嵥��
		FuOutput.writeToFile(tradereport, "report_"+sdf.format(startdate)+"to"+sdf.format(enddate));
		FuOutput.writeToFile(buylog, "buylog_"+sdf.format(startdate)+"to"+sdf.format(enddate));
	}

	//��Ŀǰ�ֹ���ϢҲд�����յı���
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

	//�жϵ����Ƿ��ǽ�����
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
		if( cnt > me.getSymbolList().size()*0.5 ) return true;   //��������г���һ���Ʊ�ڽ��ף����ж�Ϊ������
		return false;
		
	}

	//��ÿ��Ľ���
	private void doDailyTrade(Account account,
			Float ratiolimit,
			Date currentday, MapExpert me) {
		//��������������գ��ǿ϶�������
		if(DateUtil.getDay(currentday)==6 ||
				DateUtil.getDay(currentday)==0 ) return;
		boolean happenTrade=false; //�����Ƿ�������
		//����˻�����ֽ�������ʲ���ĳ�������������ѡ�ɣ��������Ʊ
		//ִ��������׵Ĳ��ԣ�ȷ�����ʽ�����
		//account.getTotalassets(currentday, me);
		if(account.balance >= 0.33 * account.totalassets * ratiolimit)
		{
			ArrayList<StockDailyWithReason> buylist = null;
			//����������Ծ��������б�
			switch(BuyStrategy)
			{
			//6������macd����ͣ�������ѡ�ɵ�ϵͳ
			case 6:
				buylist = HeadSelector.doMacdCodeSelect(currentday,mystockpool,me,chooselimit);
				break;
			case 7:							//����MACD�͵ͳɽ�����ʽ����
				buylist = HeadSelector.doMacdLowSelect(currentday,mystockpool,me);
				break;
			case 8:							//ֻ���ݵͳɽ������������
				buylist = HeadSelector.doLowSelect(currentday,mystockpool,me);
				break;
			default:
				buylist = null;
			}
			//ArrayList<StockDaily> buylist = AveragePriceSelector.doBuySelect(currentday,mystockpool,me);
			//2,�����Ǹ��ݾ��ߺ�������ɽ����ѡ�ɵ�ϵͳ
			//ArrayList<StockDailyWithReason> buylist = HeadSelector.doSelect(currentday,mystockpool,me);
			//3,������ͣ�������ѡ�ɵ�ϵͳ
			//ArrayList<StockDailyWithReason> buylist = HeadSelector.doCodeSelect(currentday,mystockpool,me,chooselimit);
			//4, ������ͣ���������ɽѡ�ɵ�ϵͳ
			//ArrayList<StockDailyWithReason> buylist = HeadSelector.doCodeVsSelect(currentday,mystockpool,me,chooselimit);
			//5, ���ݾ��ߺ���ͣ����ѡ�ɵ�ϵͳ
			//ArrayList<StockDailyWithReason> buylist2 = HeadSelector.doCodeAverageSelect(currentday,mystockpool,me,chooselimit);
			//buylist.addAll(buylist2);
			
	
			if(buylist!=null && buylist.size()>0 )
			{
				DoBuy(buylist);
				happenTrade = true;
			}
		}
		
		//������еĹ�Ʊ����
		//ArrayList<StockDaily> selllist = HeadSelector.doSellSelect(currentday,account,me);
		//2�����ݾ��ߺ���������ϵͳ
		//ArrayList<StockDaily> selllist = AveragePriceSelector.doSellSelect(currentday,account,me);
		//3,����MACDϵͳ������ϵͳ
		ArrayList<StockDaily> selllist = null;
		//�����������Ծ��������б�
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
	 * ����ѡ�е�buylist�������붯��
	 * @param buylist
	 */
	private void DoBuy(ArrayList<StockDailyWithReason> buylist) {
		//��¼����ѡ�еĹ�Ʊ
		buylog.addAll(buylist);
		for(int i = 0;i<buylist.size();++i)
		{
			//ʵ�в����֣����ظ������
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
			//System.out.println(sdf.format(newhs.buydate)+"����:"+newhs);
		}
		
	}

		
		
	/***
	 * �����׳�list�����׳�
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
		//System.out.println("������ʾ:"+account);
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
			//�����ʾ�Ծɳֹ�
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
		sb.append("�Լ۸�").append(sd.getClose());
		sb.append("�׳�").append(sd.getSymbol()).append(sd.getName());
		sb.append(hs.shares).append("��,");
		sb.append("�ɱ�Ϊ").append(hs.buyprice);
		sb.append("����ӯ����").append((selltotal-hs.totalcost));
		
		System.out.println(sb);
	}

	

	
}
