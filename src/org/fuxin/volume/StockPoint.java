package org.fuxin.volume;

import java.util.Date;

public class StockPoint {
	public int index;
	public Date date;
	public Float price;
	@Override
	public String toString() {
		return "StockPoint [index=" + index + ", date=" + date + ", price="
				+ price + "]";
	}
	
}
