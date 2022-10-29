package org.fuxin.basic;

/***
 * 该类表示一个变量，X或者Y
 * @author Administrator
 *
 */
public class OriginValue implements Comparable<OriginValue> {
	
	public OriginValue(String symbol, float value, String description) {
		this.symbol = symbol;
		this.value = value;
		this.description = description;
	}
	public String symbol;					//股票代码
	public String description;				//该变量描述
	public Float value;						//该变量的值
	
	
	@Override
	public String toString() {
		return  symbol + "," + description
				+ "," + value;
	}


	public int compareTo(OriginValue other) {
		if(this.value==other.value)
		return 0;
		else if(this.value<other.value) return 1;
		else return -1;
	}



}
