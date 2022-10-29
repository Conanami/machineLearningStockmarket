package org.fuxin.analyst;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeMap;

import org.fuxin.basic.CandleNew;
import org.fuxin.basic.DiscreteMan;
import org.fuxin.basic.DiscreteValue;
import org.fuxin.basic.OriginValue;
import org.fuxin.basic.PrintReport;
import org.fuxin.basic.ReportMan;
import org.fuxin.basic.StatMan;
import org.fuxin.passcode.Candle;
import org.fuxin.passcode.CodeChooseLimit;
import org.fuxin.passcode.PoolManager;
import org.fuxin.passcode.StockDailyWithReason;
import org.fuxin.report.SelectReport;
import org.fuxin.report.SrScorer;
import org.fuxin.report.StatisUtil;
import org.fuxin.report.StatisticReport;
import org.fuxin.simulator.Account;
import org.fuxin.simulator.AveragePriceSelector;
import org.fuxin.simulator.HeadSelector;
import org.fuxin.simulator.Simulator;
import org.fuxin.simulator.TradingStrategy;
import org.fuxin.statistic.Statist;
import org.fuxin.stock.NewShape;
import org.fuxin.stock.ShapeGrp;
import org.fuxin.stock.SimpleShape;
import org.fuxin.stock.VerifyResult;
import org.fuxin.util.ArrayUtil;
import org.fuxin.util.AudioPlay;
import org.fuxin.util.C;
import org.fuxin.util.DateUtil;
import org.fuxin.util.FuInput;
import org.fuxin.util.FuOutput;
import org.fuxin.util.Saver;
import org.fuxin.util.ScopeUtil;
import org.fuxin.util.TimePeriodMaker;
import org.fuxin.util.Translator;
import org.fuxin.vo.StockDaily;
import org.fuxin.volume.VolumeSelector;



public class Boss {
	
	public static void main (String[] args) throws Exception 
	{
		sop("Now Starting...");
		long start = System.currentTimeMillis();
		//testLowVolume();							//测试最简单的地量+红倍柱方法
		//testCodeMacdSelector();						//macd加上涨停密码
		//testMacd();	 								//macd的买入卖出点选择，长期测试用
		//testVerifyDaily();					//读文件验证结果
		//testVerifyReport();					//对交易报告进行检验
		//testTop10();						
		//testSPSSreport();						//为SPSS提供数据源
		//testStatistic();						//基本决策树统计
		//testCodeSelector();					
		//使用涨停密码选股  78
		//testPasscode();							//对涨停密码这本书的学习
		//testSelect();
		//testTmp();
		//testSaver();
		//testVerifyNew();
		//testFinanceSelector();
		//testNewTrader();					//新的全面学习
		//testLoadFinance();
		//testTurnover();
		//testSimulator();
		//testRandom();
		//testVolumeShape();
		//testVV();							//按照vs选股，同时进行验证
		//想知道特定时间段内，涨得最好的，或者跌的最惨的前百分之多少的股票
		//testNewSelector();				//按照均线系统+vs选股
		
		testGetBankName();
		long end = System.currentTimeMillis();
		sop((end-start)+"ms");
		AudioPlay ap=new AudioPlay("e:\\work\\alarm07.wav");
		ap.run();
	}
	

	private static void testGetBankName() {
		// TODO Auto-generated method stub
		
	}


	private static void testLowVolume() throws ParseException {
		// TODO Auto-generated method stub
		//账户资金
		Account acct1 = new Account(100000000f);
		//股票池
		HashSet<String> stockpool = new HashSet<String>();
		//起始模拟时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startday = sdf.parse("2016-01-27");
		Date endday = sdf.parse("2016-12-31");
				
		//最大个股占用资金比
		Float ratiolimit = 0.005f;
				
		//选股范围
		//主板+中小板+创业板
		int scope = C.MA_ZX_CY;
		//选股代码通配符
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList,true,endday);
		stockpool.addAll(PoolManager.doSelect(me));
		
				
		//创建一个新的模拟器
		Simulator sim = new Simulator(acct1,me,startday,endday,ratiolimit,stockpool,7,2);
				
		//模拟器运行
		sim.run();
		//打印最终账户结果
		sop(sim.account);
	}


	private static void testCodeMacdSelector() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date endday = sdf.parse("2017-03-07");
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList,true,endday);
		HashSet<String> stockpool = new HashSet<String>();
		stockpool.addAll(PoolManager.doSelect(me));
		CodeChooseLimit chooselimit = new CodeChooseLimit();
		chooselimit.maxprice=20f;
		//最低得分
		chooselimit.minscore =20f;
		chooselimit.maxscore =100f;
		chooselimit.minVList=11;
		chooselimit.maxVolumeRate=100f;
		chooselimit.minVolumeRate =1f;
		chooselimit.minFList=1;
		chooselimit.timespan=60;
		chooselimit.maxLysvList = 100;
		//最大换手率
		chooselimit.maxVolume = 20f;
		chooselimit.minLongHeadList = 0;
		chooselimit.mingoldList = 0;
		chooselimit.minYangrate= 1.04;
		
		ArrayList<StockDailyWithReason> selectlist = HeadSelector.doCurrentMacdCodeSelect(endday, stockpool, me,chooselimit);
		FuOutput.writeToFile(selectlist,"macode"+sdf.format(endday));
		
	}


	//这是测试MACD和PASSCODE的综合
	private static void testMacd() throws ParseException {
		//账户资金
		Account acct1 = new Account(100000000f);
		//股票池
		HashSet<String> stockpool = new HashSet<String>();
		//起始模拟时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startday = sdf.parse("2016-10-01");
		Date endday = sdf.parse("2016-12-31");
		
		//最大个股占用资金比
		Float ratiolimit = 0.04f;
		
		//选股范围
		//主板+中小板+创业板      
		int scope = C.MA_ZX_CY;
		//选股代码通配符
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList,true,endday);
		stockpool.addAll(PoolManager.doSelect(me));
		
		//ArrayList<String> stockpoolList = new ArrayList(stockpool);
		//FuOutput.writeToFile(stockpoolList, "stockpool");
		//选股条件限制
		CodeChooseLimit chooselimit = new CodeChooseLimit();
		//选股最高价
		chooselimit.maxprice=20f;
		//最低得分
		chooselimit.minscore =20f;
		chooselimit.maxscore =100f;
		chooselimit.minVList=14;
		chooselimit.maxVolumeRate=100f;
		chooselimit.minVolumeRate =1f;
		chooselimit.minFList=1;
		chooselimit.timespan=60;
		chooselimit.maxLysvList = 100;
		//最大换手率
		chooselimit.maxVolume = 20f;
		chooselimit.minLongHeadList = 1;
		chooselimit.mingoldList = 0;
		chooselimit.maxgoldList = 10;
		chooselimit.minYangrate = 1.04;
	
		
		
		//创建一个新的模拟器
		Simulator sim = new Simulator(acct1,me,startday,endday,ratiolimit,stockpool);
		sim.chooselimit = chooselimit;
		
		//模拟器运行
		sim.run();
		//打印最终账户结果
		sop(sim.account);
	}


	private static void testSPSSreport() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date endday = sdf.parse("2014-09-30");
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList,true,endday);
		Date startday = sdf.parse("2014-09-01");
		TreeMap<String,OriginValue> result = StatMan.getRaise(me,startday,endday,"60dayRaise"); 
		TreeMap<String,DiscreteValue> discreteResult = DiscreteMan.getDiscrete(result,3); 
		//ArrayList<PrintReport> preport = ReportMan.makeReport(result);
		 
		
		TreeMap<String,CandleNew> candleresult = StatMan.getCandleResult(me,startday,endday);
		
		ArrayList<PrintReport> preport = ReportMan.makeDiscreteReport(discreteResult);
		FuOutput.writeToFile(preport, "spss");
		
	}


	//从最基础的统计结果开始研究
	private static void testStatistic() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date endday = sdf.parse("2014-09-30");
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList,true,endday);
		Date startday = sdf.parse("2014-09-01");
		TreeMap<String,OriginValue> result = StatMan.getRaise(me,startday,endday,"60dayRaise"); 
		TreeMap<String,DiscreteValue> discreteResult = DiscreteMan.getDiscrete(result,3); 
		//ArrayList<PrintReport> preport = ReportMan.makeReport(result);
		ArrayList<PrintReport> preport = ReportMan.makeDiscreteReport(discreteResult);
		FuOutput.writeToFile(preport, "test");
	}



	private static void testCodeSelector() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date endday = sdf.parse("2014-11-11");
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList,true,endday);
		HashSet<String> stockpool = new HashSet<String>();
		stockpool.addAll(PoolManager.doSelect(me));
		CodeChooseLimit chooselimit = new CodeChooseLimit();
		chooselimit.maxprice=20f;
		chooselimit.minscore =20f;
		chooselimit.maxscore =100f;
		chooselimit.minVList=5;
		chooselimit.maxVolumeRate=100f;
		chooselimit.minVolumeRate =2f;
		chooselimit.minFList=3;
		chooselimit.timespan=40;
		chooselimit.maxLysvList = 100;
		chooselimit.maxVolume = 0.5f;
		chooselimit.minLongHeadList = 0;
		
		ArrayList<StockDailyWithReason> selectlist = HeadSelector.doCurrentCodeSelect(endday, stockpool, me,chooselimit);
		FuOutput.writeToFile(selectlist,"code"+sdf.format(endday));
		
	}



	private static void testVerifyReport() throws ParseException {
		String filename="d:\\tmp\\report_2015-05-04to2015-05-23-20150523-214853.txt";
		ArrayList<SimpleShape> slist = FuInput.readReportToGetShape(filename);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date checkDate = sdf.parse("2015-04-17");
		Integer daysAfter = 40;
		ArrayList<VerifyResult> vrlist = Verifier.check(slist,daysAfter);
		FuOutput.writeToFile(vrlist, "vr_"+filename.substring(10));
	}



	private static void testPasscode() throws ParseException {
		//新开一个银行账户，存入5000万资金
		Account acct1 = new Account(50000000f);
		
		HashSet<String> stockpool = new HashSet<String>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startday = sdf.parse("2015-01-01");
		Date endday = sdf.parse("2015-02-15");
		
		Float ratiolimit = 0.002f;
		
		
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList,true,endday);
		stockpool.addAll(PoolManager.doSelect(me));
		
		//ArrayList<String> stockpoolList = new ArrayList(stockpool);
		//FuOutput.writeToFile(stockpoolList, "stockpool");
		//创建一个新的模拟器
		CodeChooseLimit chooselimit = new CodeChooseLimit();
		chooselimit.maxprice=15f;
		chooselimit.minscore =20f;
		chooselimit.maxscore =100f;
		chooselimit.minVList=5;
		chooselimit.maxVolumeRate=100f;
		chooselimit.minVolumeRate =2f;
		chooselimit.minFList=1;
		chooselimit.timespan=40;
		chooselimit.maxLysvList = 100;
		chooselimit.maxVolume = 0.5f;
		chooselimit.minLongHeadList = 0;
		
		Simulator sim = new Simulator(acct1,me,startday,endday,ratiolimit,stockpool);
		sim.chooselimit = chooselimit;
		
		sim.run();
		sop(sim.account);
		
	}



	//可以验证每天的选股结果，某一阶段的涨幅，对选股思路进行修正
	private static void testVerifyDaily() {
		String filename="d:\\tmp\\macode2015-05-04-20150505-084654.txt";
		ArrayList<SimpleShape> slist = FuInput.readToShape(filename);
		ArrayList<VerifyResult> vrlist = Verifier.check(slist,100);
		FuOutput.writeToFile(vrlist, "vr_"+filename.substring(7));
		
	}



	private static void testNewSelector() throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date endday = sdf.parse("2014-10-10");
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList,true,endday);
		HashSet<String> stockpool = new HashSet<String>();
		stockpool.addAll(Financer.doPoolSelect(me));
		
		ArrayList<StockDaily> selectlist = HeadSelector.doCurrentSelect(endday, stockpool, me);
		FuOutput.writeToFile(selectlist,"chaos"+sdf.format(endday));
	}



	private static void testRandom() throws ParseException {
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//这里注意不要选到周末
		String datestr = "2014-09-23";   
		Date selectday = sdf.parse(datestr);
		
		MapExpert me = new MapExpert(symbolList,true,selectday);
		//找两山一谷的
		VolumeSelector vs = new VolumeSelector();
		
		//这段是随机选股
		
		HashSet<SelectReport> randresult = vs.doRandomSelect(me);
		randresult = Cleaner.doClean(randresult, selectday);
		randresult = Financer.doSelect(randresult);
		FuOutput.writeToFile(randresult, "rand_"+datestr);
		ArrayList<VerifyResult> vrrandlist = Verifier.check(randresult,200);
		FuOutput.writeToFile(vrrandlist, "vr_rand"+datestr);
		
		
		
		
	}

	//测试阶段性随机选股的结果

	private static void testTop10() throws ParseException {
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String enddateStr = "2015-03-10";   
		Date selectday = sdf.parse(enddateStr);
		MapExpert me = new MapExpert(symbolList,true,selectday);
		
		String startdateStr = "2015-02-27";
		Date startday = sdf.parse(startdateStr);
		
		boolean IsBest = true;          //取最好的，false为取最差的
		Float topPercent = 50f;         //前10位
		ArrayList<VerifyResult> bestlist = Statist.doSelect(me,startday,selectday,IsBest,topPercent);
		
		String fileprefix = null;
		if(IsBest)
			fileprefix = "best";
		else
			fileprefix = "worst";
		
		fileprefix = fileprefix + topPercent +"_" 
				+ startdateStr +"To"+ enddateStr;
		
		FuOutput.writeToFile(bestlist, fileprefix);
	}



	private static void testVV() throws ParseException  {
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//这里注意不要选到周末
		String datestr = "2014-08-15";   
		Date endday = sdf.parse(datestr);
		
		MapExpert me = new MapExpert(symbolList,true,endday);
		//找两山一谷的
		datestr = "2014-05-15";
		Date selectday = sdf.parse(datestr);
		
		VolumeSelector vs = new VolumeSelector();
		HashSet<SelectReport> result = vs.doSelect(me,selectday);
		//HashSet<SelectReport>
		//String batch_name2 = "short02";
		//HashSet<SelectReport> shortlist = StockSelector.doSelect(me,datestr,batch_name2);
		
		//result.retainAll(shortlist);
		
		result = Cleaner.doClean(result,selectday);
		//result = Financer.doSelect(result);
		FuOutput.writeToFile(result, "tm_"+datestr);
		//ArrayList<VerifyResult> vrlist = Verifier.check(result,100);
		//FuOutput.writeToFile(vrlist, "vr_tm"+datestr);
		
		
		
		//这段是随机选股
		/*
		HashSet<SelectReport> randresult = vs.doRandomSelect(me);
		randresult = Cleaner.doClean(randresult, selectday);
		randresult = Financer.doSelect(randresult);
		FuOutput.writeToFile(randresult, "rand_"+datestr);
		ArrayList<VerifyResult> vrrandlist = Verifier.check(randresult,100);
		FuOutput.writeToFile(vrrandlist, "vr_rand"+datestr);
		*/
		
		
	}



	private static void testVolumeShape() throws ParseException {
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//这里注意不要选到周末
		String datestr = "2014-02-17";   
		Date selectday = sdf.parse(datestr);
		
		MapExpert me = new MapExpert(symbolList,true,selectday);
		//找两山一谷的
		VolumeSelector vs = new VolumeSelector();
		HashSet<SelectReport> result = vs.doSelect(me,selectday);
		
		//String batch_name2 = "short02";
		//HashSet<SelectReport> shortlist = StockSelector.doSelect(me,datestr,batch_name2);
		
		//result.retainAll(shortlist);
		
		result = Cleaner.doClean(result,selectday);
		result = Financer.doFairSelect(result);
		
		
		// 这段是随机选股
		HashSet<SelectReport> randresult = vs.doRandomSelect(me);
		randresult = Cleaner.doClean(randresult, selectday);
		randresult = Financer.doSelect(randresult);
		FuOutput.writeToFile(randresult, "rand_"+datestr);
		
		
		FuOutput.writeToFile(result, "twoMnoraise_"+datestr);
	}



	private static void testSimulator() throws ParseException {
		
		//新开一个银行账户，存入1000万资金
		Account acct1 = new Account(10000000f);
		
		HashSet<String> stockpool = new HashSet<String>();
		
		
		//定义一个选股策略列表
		ArrayList<String> batchlist = new ArrayList<String>();
		batchlist.add("test03");
		batchlist.add("short02");
		
		TradingStrategy ts1 = new TradingStrategy(batchlist,true,20,null);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startday = sdf.parse("2014-01-01");
		Date endday = sdf.parse("2014-09-30");
		
		Float ratiolimit = 0.05f;
		
		
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList,true,endday);
		//stockpool.addAll(Financer.doPoolFairSelect(me));
		stockpool.addAll(Financer.doPoolSelect(me));  //修改成一个更随机的股票池
		ArrayList<String> stockpoolList = new ArrayList(stockpool);
		FuOutput.writeToFile(stockpoolList, "stockpool");
		//创建一个新的模拟器
		Simulator sim = new Simulator(acct1,me,startday,endday,ratiolimit,stockpool);
		sim.run();
		sop(sim.account);
	}



	/***
	 * 这个函数专门测试换手率
	 * @throws ParseException
	 */
	private static void testTurnover() throws ParseException {
		int scope = C.MA_ZX_CY;
		String queryStr = "%2048";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		MapExpert me = new MapExpert(symbolList,true,sdf.parse("2014-08-08"));
		for(int i=0;i<3;++i)
			sop(me.getAlldata().get("SZ002048").get(i));
		
	}



	private static void testLoadFinance() {
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList);
		
		
	}



	private static void testFinanceSelector() {
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		//ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		String colname = "netprofit_rose";
		float topratio = 0.05f;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
		//ArrayList<SimpleShape> fslist = (new Financer()).select(scope,queryStr,colname,topratio,sdf.parse("2014-08-27"));
		//FuOutput.writeToFile(fslist, "fs");
	}



	@SuppressWarnings({ "unused", "unchecked" })
	private static void testNewTrader() {
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList);
		
		//输入要研究的取样时间段
		int[][] q={
				{3, 30, 60, 100},
				{3, 20, 100, 80},
				{3, 30, 100, 60},
				{3, 20, 60, 120},
				{3, 20, 80, 100},
				{2, 20, 100, 80},
				{2, 20, 60, 120},
				{3, 30, 100, 80},
				{3, 30, 80, 80},
				{2, 20, 80, 100},
				{2, 30, 60, 100},
				{3, 30, 80, 100},
				{2, 30, 100, 60},
				{3, 30, 60, 120},
				{3, 30, 60, 80},
				{3, 20, 60, 100},
				{2, 30, 80, 80},
				{3, 20, 100, 60},
				{3, 10, 60, 120},
				{3, 10, 100, 80},
		};

		
		//下面是临时制造一个简易的形态，测试用
		//ArrayList<ArrayList<Integer>> p = ArrayUtil.transform(q);
		
		ArrayList<ArrayList<Integer>> p = TimePeriodMaker.make3Combination();
		
				
		Float[] d = {-100f,-1.5f,-1.0f,-0.8f,-0.6f,-0.4f,-0.25f,-0.1f,0f,
				0.1f,0.25f,0.4f,0.6f,0.8f,1.0f,1.5f,100f};
		
		Discretor dis1 = new Discretor(d);
		ArrayList<SrScorer> scorelist = new ArrayList<SrScorer>();
		for(int i=0;i<p.size();++i)
		{
			TimePeriod p1 = new TimePeriod(p.get(i));
			//按照研究的时间段取样，取样密度，是否随机，并把样本转换成一个简单状态
			Integer density = 5;
			boolean isRandom = true;
			boolean isSave = true;	
			Integer futureday = 20;
			Analyst ana = new Analyst(p1,density,isRandom,dis1,me,isSave,futureday);
			ana.run();
			scorelist.add(ana.scorer);
			System.gc();
		}
		Collections.sort(scorelist);
		FuOutput.writeToFile(scorelist, "score");
		
	}
	
	
	
	//测试从数据库读入batch_name(批次名)为XXX的StatisticReport数据
	private static void testLoadStatisticReport()
	{
		String batch_name = "20140903-1105";
		ArrayList<StatisticReport> srlist = Saver.loadStatisticReport(batch_name);
		sop(srlist);
	}
	
	
	private static void testVerifyNew()
	{
		String filename="d:\\tmp\\report_2014-09-01to2014-11-01-20141021-120923.txt";
		ArrayList<SimpleShape> slist = FuInput.readfileToGetShape(filename);
		ArrayList<VerifyResult> vrlist = Verifier.check(slist,100);
		FuOutput.writeToFile(vrlist, "vr_"+filename.substring(7));
	}
	
	private static void testTmp() throws ParseException
	{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sop(DateUtil.getDay(sdf.parse("2014-09-13")));
	}
	
	private static void testTransform()
	{
		int[][] q={
				{10,80,100},
				{10,100,80},
				{30,100,60},
				{30,100,70},
				{10,60,120},
				{10,90,90},
				{30,90,70},
				{20,100,70},
				{10,70,110}
		};
		
		//下面是临时制造一个简易的形态，测试用
		ArrayList<ArrayList<Integer>> p = ArrayUtil.transform(q);
		
		sop(p);
		sop(p.size());
	}
	
	//测试选股功能
	private static void testSelect() throws ParseException
	{
		//测试一下直接读数据库会不会更快
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datestr = "2014-09-16";
		Date endday = sdf.parse(datestr);
		String selectdate = "2014-09-09";
		Date selectday = sdf.parse(selectdate);
		
		MapExpert me = new MapExpert(symbolList,true,endday);
		
		String batch_name1 = "short02";
		HashSet<SelectReport> sltrptlist1 = StockSelector.doSelect(me,batch_name1,selectday);
		
		//String batch_name2 = "short02";
		//HashSet<SelectReport> sltrptlist2 = StockSelector.doSelect(me,datestr,batch_name2);
		
		//取两种方案的交集
		HashSet<SelectReport> result = new HashSet<SelectReport>();
		result.addAll(sltrptlist1);
		//result.retainAll(sltrptlist1);
		
		//把不符合条件的清理掉
		result = Cleaner.doClean(result,selectday);
		//result = Financer.doSelect(result);
		FuOutput.writeToFile(result, batch_name1+"&_"+datestr);
		
	}
	
	
	//测试学习结果的保存
	private static void testSaver()
	{
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//为了更快研究，先把所有数据放进内存
		MapExpert me = new MapExpert(symbolList);
		
		//输入要研究的取样时间段
		//ArrayList<ArrayList<Integer>> p = TimePeriodMaker.make3Combination();
		
		int[][] q={
				{10,80,100},
				{10,100,80},
				{30,100,60},
				{30,100,70},
				{10,60,120},
				{10,90,90},
				{30,90,70},
				{20,100,70},
				{10,70,110}
		};
		
		//下面是临时制造一个简易的形态，测试用
		ArrayList<ArrayList<Integer>> p = ArrayUtil.transform(q);
		
		Float[] d = {-100f,-0.8f,-0.6f,-0.4f,-0.25f,-0.1f,0f,
				0.1f,0.25f,0.4f,0.6f,0.8f,100f};
		
		Discretor dis1 = new Discretor(d);
		ArrayList<SrScorer> scorelist = new ArrayList<SrScorer>();
		for(int i=0;i<p.size();++i)
		{
			TimePeriod p1 = new TimePeriod(p.get(i));
			//按照研究的时间段取样，取样密度，是否随机，并把样本转换成一个简单状态
			Integer density = 5;
			boolean isRandom = true;
			boolean isSave = true;
			Integer futureday = 20;
			Analyst ana = new Analyst(p1,density,isRandom,dis1,me,isSave,futureday);
			ana.run();
			scorelist.add(ana.scorer);
		}
		Collections.sort(scorelist);
		FuOutput.writeToFile(scorelist, "score");
	}
	
	
	/***
	 * 验证某次的选股结果
	 */
	private static void testVerify()
	{
		String filename="d:\\tmp\\test07_2014-02-17-20140905-115843.txt";
		ArrayList<SimpleShape> slist = FuInput.readfile(filename);
		ArrayList<VerifyResult> vrlist = Verifier.check(slist,20);
		FuOutput.writeToFile(vrlist, "vr_"+filename.substring(7));
	}
	
	
	
	static void sop(Object obj)
	{
		System.out.println(obj);
	}
	
}

	