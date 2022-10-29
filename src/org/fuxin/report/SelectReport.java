package org.fuxin.report;

import java.util.ArrayList;
import java.util.Date;

import org.fuxin.vo.FinanceData;
import org.fuxin.vo.StockDaily;
import org.fuxin.volume.TwoMount;

public class SelectReport {
	
	public SelectReport(String symbol, ArrayList<StockDaily> dailydata, StatisticReport sr) {
		this.symbol = symbol;
		if(dailydata.size()>0)
		{
			this.endday = dailydata.get(0).getDate();
			this.price = dailydata.get(0).getClose();
		}
		this.statreport = sr;
	}
	
	public String symbol;
	public Date endday;
	public float price;
	public StatisticReport statreport;
	private TwoMount twomount;
	private FinanceData finance;     	//财务数据
	private Float pe_ratio;    			//市盈率
	private Float pb_ratio;	 	 	//市净率
	
	

	@Override
	public String toString() {
		if(twomount!=null)
		{
			return "SelectReport [symbol=" + symbol + ", endday=" + endday
				+ ", price=" + price + ", statreport=" + statreport
				+ ", finance=" + finance.showInSelect() + ", 市盈率=" + pe_ratio
				+ ", 市净率=" + pb_ratio + "," + twomount; //.toReport();
		}
		else
			return "SelectReport [symbol=" + symbol + ", endday=" + endday
				+ ", price=" + price + ", statreport=" + statreport
				+ ", finance=" + finance.showInSelect() + ", 市盈率=" + pe_ratio
				+ ", 市净率=" + pb_ratio + "," + twomount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endday == null) ? 0 : endday.hashCode());
		result = prime * result + Float.floatToIntBits(price);
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
		SelectReport other = (SelectReport) obj;
		if (endday == null) {
			if (other.endday != null)
				return false;
		} else if (!endday.equals(other.endday))
			return false;
		if (Float.floatToIntBits(price) != Float.floatToIntBits(other.price))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	public FinanceData getFinance() {
		return finance;
	}

	public void setFinance(FinanceData finance) {
		this.finance = finance;
		this.pb_ratio = this.price/this.finance.getNetassets_ps();
		this.pe_ratio = this.price/(this.finance.getEarning_ps()/this.finance.getYearRatio());
	}

	public Float getPe_ratio() {
		return pe_ratio;
	}

	public Float getPb_ratio() {
		return pb_ratio;
	}

	public TwoMount getTwomount() {
		return twomount;
	}

	public void setTwomount(TwoMount twomount) {
		this.twomount = twomount;
	}

	

	
	
	
}
