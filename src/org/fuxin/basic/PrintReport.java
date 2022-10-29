package org.fuxin.basic;

import java.util.ArrayList;

/***
 * 基本的打印报告格式
 * @author Administrator
 *
 */
public class PrintReport {
	public ArrayList<String> valuelist;

	public PrintReport(String value1) {
		this.addValue(value1);
	}

	@Override
	public String toString() {
		if(valuelist!=null)
		{
			return showValueList();
		}
		else
			return "";
	}

	private String showValueList() {
		StringBuffer sbuf = new StringBuffer();
		if(valuelist==null) return "";
		for(int i=0;i<valuelist.size();++i)
		{
			sbuf.append(valuelist.get(i));
			sbuf.append(",");
		}
		return sbuf.toString();
		
	}

	public void addValue(String value) {
		if(valuelist==null) valuelist = new ArrayList<String>();
		valuelist.add(value);
	}
	
}
