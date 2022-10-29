package org.fuxin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.fuxin.stock.SimpleShape;
import org.fuxin.vo.StockDaily;


public class FuInput {

	public static ArrayList<SimpleShape> readfileToGetShape(String filename) {
		ArrayList<SimpleShape> rtlist = new ArrayList<SimpleShape>();
		try {
            String encoding="GBK";
            File file=new File(filename);
            if(file.isFile() && file.exists())
            { 
            	//判断文件是否存在
            	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            	InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	StringTokenizer st = new StringTokenizer(lineTxt, ",");
                	int cnt = 0;
                	SimpleShape ss = new SimpleShape();
                	boolean nextIsPrice = false ;
                	while (st.hasMoreTokens()) { 
                		// 每一行的多个字段用TAB隔开表示 
                		String commaSplit = st.nextToken();
                		// 如果看到prev字样，就结束
                		if( commaSplit.contains("statreport") ) break;
                		
                		int index = commaSplit.indexOf("symbol");
                		if(index>=0)
                		{
                			ss.symbol = commaSplit.substring(index+7);
                		}
                		index = commaSplit.indexOf("endday");
                		if(index>=0)
                		{
                			String daystr = commaSplit.substring(index+7);
                			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                			ss.date = sdf.parse(daystr);
                		}
                		index = commaSplit.indexOf("price");
                		if(index>=0)
                		{
                			ss.price = Float.parseFloat(commaSplit.substring(index+6));
                		}
                		
                		
                		cnt ++ ;
                	} 
                	System.out.println(ss);
                	rtlist.add(ss);
                }
                read.close();
                return rtlist;
            }
            else
            {
            	System.out.println("找不到指定的文件");
            }
	    } 
		catch (Exception e) 
	    {
	        System.out.println("读取文件内容出错");
	        e.printStackTrace();
	    }
		return rtlist;
	}
	
	public static ArrayList<SimpleShape> readfile(String filename) {
		ArrayList<SimpleShape> rtlist = new ArrayList<SimpleShape>();
		try {
            String encoding="GBK";
            File file=new File(filename);
            if(file.isFile() && file.exists())
            { 
            	//判断文件是否存在
            	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            	InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	StringTokenizer st = new StringTokenizer(lineTxt, ",");
                	int cnt = 0;
                	SimpleShape ss = new SimpleShape();
                	boolean nextIsPrice = false ;
                	while (st.hasMoreTokens()) { 
                		// 每一行的多个字段用TAB隔开表示 
                		String commaSplit = st.nextToken();
                		// 如果看到prev字样，就结束
                		if( commaSplit.contains("prev") ) break;
                		
                		StringTokenizer st2 = new StringTokenizer(commaSplit," ");
                		// 如果包含有股票代码，则保存股票代码
                		// 如果包含有一个大的日期，则保存日期
                		// 如果包含有closeprice字样，则读取后面的
                		while (st2.hasMoreTokens())
                		{
                			String tmpstr = st2.nextToken();
                			if(tmpstr.contains("[SH")||
                					tmpstr.contains("[SZ"))
                			{
                				ss.symbol = tmpstr.substring(1);
                			}
                			else
                			{
                				if( (tmpstr.contains("SH") || tmpstr.contains("SZ")) && !tmpstr.contains("["))
                				{
                					ss.symbol = tmpstr;
                				}
                			}
              
                			if(DateUtil.isValidDate(tmpstr))
                			{
                				ss.date = formatDate.parse(tmpstr);
                			}
                			
                			if(nextIsPrice)
                			{
                				ss.price = Float.parseFloat(tmpstr);
                				nextIsPrice = false;
                				break;
                			}
                			
                			int index=tmpstr.indexOf("closeprice");
                			if(index!=-1)
                			{
                				if(index+11>=tmpstr.length())
                					nextIsPrice = true;
                				else
                				{
                					ss.price = Float.parseFloat(tmpstr.substring(tmpstr.indexOf("closeprice")+11));
                					break;
                				}
                			}
                			
                		}
                		cnt ++ ;
                	} 
                	System.out.println(ss);
                	rtlist.add(ss);
                }
                read.close();
                return rtlist;
            }
            else
            {
            	System.out.println("找不到指定的文件");
            }
	    } 
		catch (Exception e) 
	    {
	        System.out.println("读取文件内容出错");
	        e.printStackTrace();
	    }
		return rtlist;
	 
	}

	public static ArrayList<SimpleShape> readToShape(String filename) {
		ArrayList<SimpleShape> rtlist = new ArrayList<SimpleShape>();
		try {
            String encoding="GBK";
            File file=new File(filename);
            if(file.isFile() && file.exists())
            { 
            	//判断文件是否存在
            	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            	InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	StringTokenizer st = new StringTokenizer(lineTxt, ",");
                	int cnt = 0;
                	SimpleShape ss = new SimpleShape();
                	boolean nextIsPrice = false ;
                	while (st.hasMoreTokens()) { 
                		// 每一行的多个字段用TAB隔开表示 
                		String commaSplit = st.nextToken();
                		// 如果看到prev字样，就结束
                		if( commaSplit.contains("statreport") ) break;
                		//如果看到
                		String stopstr = "buyreason";
                		if( commaSplit.contains(stopstr) ) break;
                		String symbolpre = "symbol";
                		int index = commaSplit.indexOf(symbolpre);
                		if(index>=0)
                		{
                			ss.symbol = commaSplit.substring(index+symbolpre.length()+1);
                		}
                		String datepre = "date";
                		index = commaSplit.indexOf(datepre);
                		if(index>=0)
                		{
                			String daystr = commaSplit.substring(index+datepre.length()+1);
                			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                			ss.date = sdf.parse(daystr);
                		}
                		String pricepre = "close";
                		index = commaSplit.indexOf(pricepre);
                		if(index>=0)
                		{
                			ss.price = Float.parseFloat(commaSplit.substring(index+pricepre.length()+1));
                		}
                		
                		
                		cnt ++ ;
                	} 
                	System.out.println(ss);
                	rtlist.add(ss);
                }
                read.close();
                return rtlist;
            }
            else
            {
            	System.out.println("找不到指定的文件");
            }
	    } 
		catch (Exception e) 
	    {
	        System.out.println("读取文件内容出错");
	        e.printStackTrace();
	    }
		return rtlist;
	}

	
	//读交易记录文件
	public static ArrayList<SimpleShape> readReportToGetShape(String filename) {
		ArrayList<SimpleShape> rtlist = new ArrayList<SimpleShape>();
		try {
            String encoding="GBK";
            File file=new File(filename);
            if(file.isFile() && file.exists())
            { 
            	//判断文件是否存在
            	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            	InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	StringTokenizer st = new StringTokenizer(lineTxt, ",");
                	int cnt = 0;
                	SimpleShape ss = new SimpleShape();
                	boolean nextIsPrice = false ;
                	while (st.hasMoreTokens()) { 
                		// 每一行的多个字段用TAB隔开表示 
                		String commaSplit = st.nextToken();
                		// 如果看到prev字样，就结束
                		if( commaSplit.contains("shares") ) break;
                		
                		String symbolpre = "symbol";
                		int index = commaSplit.indexOf(symbolpre);
                		if(index>=0)
                		{
                			ss.symbol = commaSplit.substring(index+symbolpre.length()+1);
                		}
                		String datepre = "buyday";
                		index = commaSplit.indexOf(datepre);
                		if(index>=0)
                		{
                			String daystr = commaSplit.substring(index+datepre.length()+1);
                			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                			ss.date = sdf.parse(daystr);
                		}
                		String pricepre = "buyprice";
                		index = commaSplit.indexOf(pricepre);
                		if(index>=0)
                		{
                			ss.price = Float.parseFloat(commaSplit.substring(index+pricepre.length()+1));
                		}
                		
                		
                		cnt ++ ;
                	} 
                	System.out.println(ss);
                	rtlist.add(ss);
                }
                read.close();
                return rtlist;
            }
            else
            {
            	System.out.println("找不到指定的文件");
            }
	    } 
		catch (Exception e) 
	    {
	        System.out.println("读取文件内容出错");
	        e.printStackTrace();
	    }
		return rtlist;
	}

	

}
