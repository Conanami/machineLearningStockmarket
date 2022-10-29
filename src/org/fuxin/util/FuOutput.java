package org.fuxin.util;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.passcode.StockDailyWithReason;
import org.fuxin.report.SelectReport;
import org.fuxin.vo.StockDaily;



public class FuOutput {
	
		
	public static String Date2String(java.util.Date date,String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}


	public static void writeToFile(ArrayList<?> resultListNew, String prefix) {
		// TODO Auto-generated method stub
		if(resultListNew==null || resultListNew.size()==0) return;
		SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyyMMdd-HHmmss");
		String tstamp=sdf.format(new Date());
		writeTofile6(resultListNew, "d:\\tmp\\"+prefix+"-"+tstamp+".txt");
	}



	private static void writeTofile6(ArrayList<?> insp,
			String filename) {
		// TODO Auto-generated method stub
		if(insp==null || insp.size()==0) return;
		FileOutputStream fos = null;
		try {
			// String str=System.currentTimeMillis();
			fos = new FileOutputStream(filename, true);// 第二个参数设定是否追加文件
			@SuppressWarnings("resource")
			PrintWriter pw = new PrintWriter(fos);
			for (int i=0;i<insp.size();i++) {
				if(insp.get(i)==null)
				{
					pw.write("null");
				}
				else
				{
					pw.write(insp.get(i).toString());
				}
				pw.write("\r\n");
			}
			pw.flush(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public static void writeToFile(HashSet<SelectReport> resultListNew,
			String prefix) {
		if(resultListNew==null || resultListNew.size()==0) return;
		System.out.println("选中！");
		ArrayList<SelectReport> list = new ArrayList<SelectReport>();
		for(SelectReport sr:resultListNew)
		{
			list.add(sr);
		}
		SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyyMMdd-HHmmss");
		String tstamp=sdf.format(new Date());
		writeTofile6(list, "d:\\tmp\\"+prefix+"-"+tstamp+".txt");
		
	}


	public static void println(String string,
			ArrayList<?> indexlist) {
		// TODO Auto-generated method stub
		System.out.println(string);
		for(int i=0;i<indexlist.size();++i)
			System.out.println(indexlist.get(i));
		
		
	}


	public static void println(String string, ArrayList<StockDaily> indexlist,
			int days) {
		System.out.println(string);
		for(int i=0;i<days+1;++i)
			System.out.println(indexlist.get(i));
		
	}


	

	






	
}
