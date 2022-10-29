package org.fuxin.simulator;

import java.util.Date;

import org.fuxin.passcode.BuyReason;

public class HoldingStock {
	public String symbol;					//股票代码
	public Date buydate;					//最早买入日期
	public Float buyprice;					//平均买入价格
	public Integer shares;					//买入股数
	public Float totalcost;					//总成本
	public BuyReason reason;				//买入的理由
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoldingStock other = (HoldingStock) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "HoldingStock [symbol=" + symbol + ", buydate=" + buydate
				+ ", buyprice=" + buyprice + ", shares=" + shares
				+ ", totalcost=" + totalcost + ", reason=" + reason + "]";
	}
	
	public String toReport()
	{
		return "HoldingStock [symbol=" + symbol + ", buydate=" + buydate
				+ ", buyprice=" + buyprice + ", shares=" + shares
				+ ", totalcost=" + totalcost +"]";
	}
	
}
