package org.fuxin.passcode;

import org.fuxin.vo.StockDaily;

/***
 * С���������ߣ����ҷ���2������
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
