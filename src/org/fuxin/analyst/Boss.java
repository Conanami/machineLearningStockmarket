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
		//testLowVolume();							//������򵥵ĵ���+�챶������
		//testCodeMacdSelector();						//macd������ͣ����
		//testMacd();	 								//macd������������ѡ�񣬳��ڲ�����
		//testVerifyDaily();					//���ļ���֤���
		//testVerifyReport();					//�Խ��ױ�����м���
		//testTop10();						
		//testSPSSreport();						//ΪSPSS�ṩ����Դ
		//testStatistic();						//����������ͳ��
		//testCodeSelector();					
		//ʹ����ͣ����ѡ��  78
		//testPasscode();							//����ͣ�����Ȿ���ѧϰ
		//testSelect();
		//testTmp();
		//testSaver();
		//testVerifyNew();
		//testFinanceSelector();
		//testNewTrader();					//�µ�ȫ��ѧϰ
		//testLoadFinance();
		//testTurnover();
		//testSimulator();
		//testRandom();
		//testVolumeShape();
		//testVV();							//����vsѡ�ɣ�ͬʱ������֤
		//��֪���ض�ʱ����ڣ��ǵ���õģ����ߵ�����ҵ�ǰ�ٷ�֮���ٵĹ�Ʊ
		//testNewSelector();				//���վ���ϵͳ+vsѡ��
		
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
		//�˻��ʽ�
		Account acct1 = new Account(100000000f);
		//��Ʊ��
		HashSet<String> stockpool = new HashSet<String>();
		//��ʼģ��ʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startday = sdf.parse("2016-01-27");
		Date endday = sdf.parse("2016-12-31");
				
		//������ռ���ʽ��
		Float ratiolimit = 0.005f;
				
		//ѡ�ɷ�Χ
		//����+��С��+��ҵ��
		int scope = C.MA_ZX_CY;
		//ѡ�ɴ���ͨ���
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		MapExpert me = new MapExpert(symbolList,true,endday);
		stockpool.addAll(PoolManager.doSelect(me));
		
				
		//����һ���µ�ģ����
		Simulator sim = new Simulator(acct1,me,startday,endday,ratiolimit,stockpool,7,2);
				
		//ģ��������
		sim.run();
		//��ӡ�����˻����
		sop(sim.account);
	}


	private static void testCodeMacdSelector() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date endday = sdf.parse("2017-03-07");
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		MapExpert me = new MapExpert(symbolList,true,endday);
		HashSet<String> stockpool = new HashSet<String>();
		stockpool.addAll(PoolManager.doSelect(me));
		CodeChooseLimit chooselimit = new CodeChooseLimit();
		chooselimit.maxprice=20f;
		//��͵÷�
		chooselimit.minscore =20f;
		chooselimit.maxscore =100f;
		chooselimit.minVList=11;
		chooselimit.maxVolumeRate=100f;
		chooselimit.minVolumeRate =1f;
		chooselimit.minFList=1;
		chooselimit.timespan=60;
		chooselimit.maxLysvList = 100;
		//�������
		chooselimit.maxVolume = 20f;
		chooselimit.minLongHeadList = 0;
		chooselimit.mingoldList = 0;
		chooselimit.minYangrate= 1.04;
		
		ArrayList<StockDailyWithReason> selectlist = HeadSelector.doCurrentMacdCodeSelect(endday, stockpool, me,chooselimit);
		FuOutput.writeToFile(selectlist,"macode"+sdf.format(endday));
		
	}


	//���ǲ���MACD��PASSCODE���ۺ�
	private static void testMacd() throws ParseException {
		//�˻��ʽ�
		Account acct1 = new Account(100000000f);
		//��Ʊ��
		HashSet<String> stockpool = new HashSet<String>();
		//��ʼģ��ʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startday = sdf.parse("2016-10-01");
		Date endday = sdf.parse("2016-12-31");
		
		//������ռ���ʽ��
		Float ratiolimit = 0.04f;
		
		//ѡ�ɷ�Χ
		//����+��С��+��ҵ��      
		int scope = C.MA_ZX_CY;
		//ѡ�ɴ���ͨ���
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		MapExpert me = new MapExpert(symbolList,true,endday);
		stockpool.addAll(PoolManager.doSelect(me));
		
		//ArrayList<String> stockpoolList = new ArrayList(stockpool);
		//FuOutput.writeToFile(stockpoolList, "stockpool");
		//ѡ����������
		CodeChooseLimit chooselimit = new CodeChooseLimit();
		//ѡ����߼�
		chooselimit.maxprice=20f;
		//��͵÷�
		chooselimit.minscore =20f;
		chooselimit.maxscore =100f;
		chooselimit.minVList=14;
		chooselimit.maxVolumeRate=100f;
		chooselimit.minVolumeRate =1f;
		chooselimit.minFList=1;
		chooselimit.timespan=60;
		chooselimit.maxLysvList = 100;
		//�������
		chooselimit.maxVolume = 20f;
		chooselimit.minLongHeadList = 1;
		chooselimit.mingoldList = 0;
		chooselimit.maxgoldList = 10;
		chooselimit.minYangrate = 1.04;
	
		
		
		//����һ���µ�ģ����
		Simulator sim = new Simulator(acct1,me,startday,endday,ratiolimit,stockpool);
		sim.chooselimit = chooselimit;
		
		//ģ��������
		sim.run();
		//��ӡ�����˻����
		sop(sim.account);
	}


	private static void testSPSSreport() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date endday = sdf.parse("2014-09-30");
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		MapExpert me = new MapExpert(symbolList,true,endday);
		Date startday = sdf.parse("2014-09-01");
		TreeMap<String,OriginValue> result = StatMan.getRaise(me,startday,endday,"60dayRaise"); 
		TreeMap<String,DiscreteValue> discreteResult = DiscreteMan.getDiscrete(result,3); 
		//ArrayList<PrintReport> preport = ReportMan.makeReport(result);
		 
		
		TreeMap<String,CandleNew> candleresult = StatMan.getCandleResult(me,startday,endday);
		
		ArrayList<PrintReport> preport = ReportMan.makeDiscreteReport(discreteResult);
		FuOutput.writeToFile(preport, "spss");
		
	}


	//���������ͳ�ƽ����ʼ�о�
	private static void testStatistic() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date endday = sdf.parse("2014-09-30");
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
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
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
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
		//�¿�һ�������˻�������5000���ʽ�
		Account acct1 = new Account(50000000f);
		
		HashSet<String> stockpool = new HashSet<String>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startday = sdf.parse("2015-01-01");
		Date endday = sdf.parse("2015-02-15");
		
		Float ratiolimit = 0.002f;
		
		
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		MapExpert me = new MapExpert(symbolList,true,endday);
		stockpool.addAll(PoolManager.doSelect(me));
		
		//ArrayList<String> stockpoolList = new ArrayList(stockpool);
		//FuOutput.writeToFile(stockpoolList, "stockpool");
		//����һ���µ�ģ����
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



	//������֤ÿ���ѡ�ɽ����ĳһ�׶ε��Ƿ�����ѡ��˼·��������
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
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
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
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//����ע�ⲻҪѡ����ĩ
		String datestr = "2014-09-23";   
		Date selectday = sdf.parse(datestr);
		
		MapExpert me = new MapExpert(symbolList,true,selectday);
		//����ɽһ�ȵ�
		VolumeSelector vs = new VolumeSelector();
		
		//��������ѡ��
		
		HashSet<SelectReport> randresult = vs.doRandomSelect(me);
		randresult = Cleaner.doClean(randresult, selectday);
		randresult = Financer.doSelect(randresult);
		FuOutput.writeToFile(randresult, "rand_"+datestr);
		ArrayList<VerifyResult> vrrandlist = Verifier.check(randresult,200);
		FuOutput.writeToFile(vrrandlist, "vr_rand"+datestr);
		
		
		
		
	}

	//���Խ׶������ѡ�ɵĽ��

	private static void testTop10() throws ParseException {
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String enddateStr = "2015-03-10";   
		Date selectday = sdf.parse(enddateStr);
		MapExpert me = new MapExpert(symbolList,true,selectday);
		
		String startdateStr = "2015-02-27";
		Date startday = sdf.parse(startdateStr);
		
		boolean IsBest = true;          //ȡ��õģ�falseΪȡ����
		Float topPercent = 50f;         //ǰ10λ
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
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//����ע�ⲻҪѡ����ĩ
		String datestr = "2014-08-15";   
		Date endday = sdf.parse(datestr);
		
		MapExpert me = new MapExpert(symbolList,true,endday);
		//����ɽһ�ȵ�
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
		
		
		
		//��������ѡ��
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
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//����ע�ⲻҪѡ����ĩ
		String datestr = "2014-02-17";   
		Date selectday = sdf.parse(datestr);
		
		MapExpert me = new MapExpert(symbolList,true,selectday);
		//����ɽһ�ȵ�
		VolumeSelector vs = new VolumeSelector();
		HashSet<SelectReport> result = vs.doSelect(me,selectday);
		
		//String batch_name2 = "short02";
		//HashSet<SelectReport> shortlist = StockSelector.doSelect(me,datestr,batch_name2);
		
		//result.retainAll(shortlist);
		
		result = Cleaner.doClean(result,selectday);
		result = Financer.doFairSelect(result);
		
		
		// ��������ѡ��
		HashSet<SelectReport> randresult = vs.doRandomSelect(me);
		randresult = Cleaner.doClean(randresult, selectday);
		randresult = Financer.doSelect(randresult);
		FuOutput.writeToFile(randresult, "rand_"+datestr);
		
		
		FuOutput.writeToFile(result, "twoMnoraise_"+datestr);
	}



	private static void testSimulator() throws ParseException {
		
		//�¿�һ�������˻�������1000���ʽ�
		Account acct1 = new Account(10000000f);
		
		HashSet<String> stockpool = new HashSet<String>();
		
		
		//����һ��ѡ�ɲ����б�
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
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		MapExpert me = new MapExpert(symbolList,true,endday);
		//stockpool.addAll(Financer.doPoolFairSelect(me));
		stockpool.addAll(Financer.doPoolSelect(me));  //�޸ĳ�һ��������Ĺ�Ʊ��
		ArrayList<String> stockpoolList = new ArrayList(stockpool);
		FuOutput.writeToFile(stockpoolList, "stockpool");
		//����һ���µ�ģ����
		Simulator sim = new Simulator(acct1,me,startday,endday,ratiolimit,stockpool);
		sim.run();
		sop(sim.account);
	}



	/***
	 * �������ר�Ų��Ի�����
	 * @throws ParseException
	 */
	private static void testTurnover() throws ParseException {
		int scope = C.MA_ZX_CY;
		String queryStr = "%2048";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		MapExpert me = new MapExpert(symbolList,true,sdf.parse("2014-08-08"));
		for(int i=0;i<3;++i)
			sop(me.getAlldata().get("SZ002048").get(i));
		
	}



	private static void testLoadFinance() {
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
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
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		MapExpert me = new MapExpert(symbolList);
		
		//����Ҫ�о���ȡ��ʱ���
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

		
		//��������ʱ����һ�����׵���̬��������
		//ArrayList<ArrayList<Integer>> p = ArrayUtil.transform(q);
		
		ArrayList<ArrayList<Integer>> p = TimePeriodMaker.make3Combination();
		
				
		Float[] d = {-100f,-1.5f,-1.0f,-0.8f,-0.6f,-0.4f,-0.25f,-0.1f,0f,
				0.1f,0.25f,0.4f,0.6f,0.8f,1.0f,1.5f,100f};
		
		Discretor dis1 = new Discretor(d);
		ArrayList<SrScorer> scorelist = new ArrayList<SrScorer>();
		for(int i=0;i<p.size();++i)
		{
			TimePeriod p1 = new TimePeriod(p.get(i));
			//�����о���ʱ���ȡ����ȡ���ܶȣ��Ƿ��������������ת����һ����״̬
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
	
	
	
	//���Դ����ݿ����batch_name(������)ΪXXX��StatisticReport����
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
		
		//��������ʱ����һ�����׵���̬��������
		ArrayList<ArrayList<Integer>> p = ArrayUtil.transform(q);
		
		sop(p);
		sop(p.size());
	}
	
	//����ѡ�ɹ���
	private static void testSelect() throws ParseException
	{
		//����һ��ֱ�Ӷ����ݿ�᲻�����
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
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
		
		//ȡ���ַ����Ľ���
		HashSet<SelectReport> result = new HashSet<SelectReport>();
		result.addAll(sltrptlist1);
		//result.retainAll(sltrptlist1);
		
		//�Ѳ����������������
		result = Cleaner.doClean(result,selectday);
		//result = Financer.doSelect(result);
		FuOutput.writeToFile(result, batch_name1+"&_"+datestr);
		
	}
	
	
	//����ѧϰ����ı���
	private static void testSaver()
	{
		int scope = C.MA_ZX_CY;
		String queryStr = "%";
		ArrayList<String> symbolList = ScopeUtil.getSymbol(queryStr,scope);
		//Ϊ�˸����о����Ȱ��������ݷŽ��ڴ�
		MapExpert me = new MapExpert(symbolList);
		
		//����Ҫ�о���ȡ��ʱ���
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
		
		//��������ʱ����һ�����׵���̬��������
		ArrayList<ArrayList<Integer>> p = ArrayUtil.transform(q);
		
		Float[] d = {-100f,-0.8f,-0.6f,-0.4f,-0.25f,-0.1f,0f,
				0.1f,0.25f,0.4f,0.6f,0.8f,100f};
		
		Discretor dis1 = new Discretor(d);
		ArrayList<SrScorer> scorelist = new ArrayList<SrScorer>();
		for(int i=0;i<p.size();++i)
		{
			TimePeriod p1 = new TimePeriod(p.get(i));
			//�����о���ʱ���ȡ����ȡ���ܶȣ��Ƿ��������������ת����һ����״̬
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
	 * ��֤ĳ�ε�ѡ�ɽ��
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

	