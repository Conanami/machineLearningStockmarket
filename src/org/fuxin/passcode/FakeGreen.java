package org.fuxin.passcode;

import org.fuxin.vo.StockDaily;

/***
 * 这个类记录假阴真阳，如果不想吃货，那么有能力的人频繁制作假阴真阳，
 * 帮助小散赚点零花钱，就是很奇怪的举动
 * 继承 StockDaily
 * @author Administrator
 *
 */
public class FakeGreen {
	public StockDaily stockdaily;
	public Float raiseRate;
	public int index;
	public FakeGreen(StockDaily stockdaily,int index, Float raiseRate) {
		super();
		this.stockdaily = stockdaily;
		this.index = index;
		this.raiseRate = raiseRate;
	}
	@Override
	public String toString() {
		return "FakeGreen [stockdaily=" + stockdaily + ", raiseRate="
				+ raiseRate + "]";
	}
	
	
}
