package org.fuxin.vo;

import java.util.Date;

public class StockDaily implements Comparable<Object> {
	private String symbol;
	private Date date;
	private Float open;
	private Float low;
	private Float high;
	private Float close;
	private Long volume;
	private Long Amount;
	private String name;
	private Float turnover;
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Float getOpen() {
		return open;
	}
	public void setOpen(Float open) {
		this.open = open;
	}
	public Float getLow() {
		return low;
	}
	public void setLow(Float low) {
		this.low = low;
	}
	public Float getHigh() {
		return high;
	}
	public void setHigh(Float high) {
		this.high = high;
	}
	public Float getClose() {
		return close;
	}
	public void setClose(Float close) {
		this.close = close;
	}
	public Long getVolume() {
		return volume;
	}
	public void setVolume(Long volume) {
		this.volume = volume;
	}
	public Long getAmount() {
		return Amount;
	}
	public void setAmount(Long amount) {
		Amount = amount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Override
	public String toString() {
		return "StockDaily[symbol=" + symbol  + ", name="
				+ name + ", date=" + date + ", open="
				+ open + ", low=" + low + ", high=" + high + ", close=" + close
				+ ", volume=" + volume + ", Amount=" + Amount + ", turnover=" + turnover ;
	}
	
	public String toString2() {
		return "����:" + symbol + "\r\n"
				+ "��Ʊ����:"+name +"\r\n"
				+ "����:" + date + "\r\n"
				+ "���̼�:" + open + "\r\n"
				+ "��ͼ�:" + low + "\r\n"
				+ "��߼�:" + high + "\r\n"
				+ "���̼�:" + close +"\r\n"
				+ "�ɽ���:" + volume + "\r\n"
				+ "�ɽ����:" + Amount + "\r\n"
				+ "������:" + turnover +"\r\n";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}
	
	//ֻҪ�����������ȣ�����Ϊ���
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StockDaily other = (StockDaily) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	
	
	@Override
	public int compareTo(Object o) {
		StockDaily other = (StockDaily) o;
		if(this.date.getTime()>other.getDate().getTime())
		{
			return 1;
		}
		else
		{
			if(this.date.getTime()<other.getDate().getTime())
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
		
	}
	public Float getTurnover() {
		return turnover;
	}
	public void setTurnover(Float turnover) {
		this.turnover = turnover;
	}
	
	
	

}
