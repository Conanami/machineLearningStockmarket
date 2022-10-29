package org.fuxin.passcode;

import org.fuxin.vo.StockDaily;

/***
 * 小倍阳，阳线，而且放量2倍以上
 * @author Administrator
 *
 */
public class DoubleRaise {
	public DoubleRaise(StockDaily stockDaily, int i, Float raiseRate) {
		this.sd = stockDaily;
		this.index = i;
		this.raiseRate = raiseRate;
	}
	public StockDaily sd;
	public int index;
	public Float raiseRate;
	@Override
	public String toString() {
		return "DoubleRaise [sd=" + sd + ", index=" + index + ", raiseRate="
				+ raiseRate + "]";
	}
	
}
