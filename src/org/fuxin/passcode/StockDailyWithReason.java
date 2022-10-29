package org.fuxin.passcode;

import org.fuxin.macd.MacdScore;
import org.fuxin.vo.StockDaily;

public class StockDailyWithReason {
	public StockDailyWithReason(StockDaily stockDaily, CodeScore score) {
		this.sd = stockDaily;
		this.buyreason = new BuyReason(score);
	}
	
	public StockDailyWithReason(StockDaily stockDaily, MacdScore score) {
		this.sd = stockDaily;
		this.buyreason = new BuyReason(score);
	}
	
	public StockDailyWithReason(StockDaily stockDaily) {
		// TODO Auto-generated constructor stub
		this.sd = stockDaily;
		this.buyreason = new BuyReason("easy");
	}

	public StockDailyWithReason(StockDaily stockDaily, String str) {
		// TODO Auto-generated constructor stub
		this.sd = stockDaily;
		this.buyreason = new BuyReason(str);
	}

	public StockDaily sd;
	public BuyReason buyreason;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sd == null) ? 0 : sd.hashCode());
		return result;
	}
	
	/***
	 * sd一样就算相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StockDailyWithReason other = (StockDailyWithReason) obj;
		if (sd == null) {
			if (other.sd != null)
				return false;
		} else if (!sd.equals(other.sd))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StockDailyWithReason [sd=" + sd + ", buyreason=" + buyreason
				;
	}
	
}
