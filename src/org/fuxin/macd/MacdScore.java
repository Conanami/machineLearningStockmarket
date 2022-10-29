package org.fuxin.macd;

import java.util.Date;

/***
 * 记录一个股票的MACD值
 * @author Administrator
 *
 */
public class MacdScore {
	public String symbol;				//代码
	public String name;					//名称
	public Date date;					//日期
	public int index;					//内存中索引
	public Float DIFF;					//DIFF值
	public Float DEA;					//DEA值
	public Float MACD;					//MACD值，红柱绿柱
	
	@Override
	public String toString() {
		return "MacdScore [symbol=" + symbol + ", name=" + name + ", date="
				+ date + ", index=" + index + ", DIFF=" + DIFF + ", DEA=" + DEA
				+ ", MACD=" + MACD + "]";
	}
	
}
