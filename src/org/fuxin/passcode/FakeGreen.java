package org.fuxin.passcode;

import org.fuxin.vo.StockDaily;

/***
 * ������¼�����������������Ի�����ô����������Ƶ����������������
 * ����Сɢ׬���㻨Ǯ�����Ǻ���ֵľٶ�
 * �̳� StockDaily
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
