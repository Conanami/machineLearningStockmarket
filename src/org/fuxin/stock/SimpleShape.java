package org.fuxin.stock;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.fuxin.util.DateUtil;

/***
 * 最简单的某股某日某价
 * @author Administrator
 *
 */
public class SimpleShape {
	public String symbol;
	public String name="";
	public Date date;
	public Float price;
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tmpStr = name + ",";
		return symbol + ","+ (name.equals("")?"":tmpStr) 
				+ sdf.format(date) + ","+price;
	}

	
	
	
}
