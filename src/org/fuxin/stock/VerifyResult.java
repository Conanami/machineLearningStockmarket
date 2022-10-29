package org.fuxin.stock;

public class VerifyResult implements Comparable<VerifyResult> {
	private SimpleShape past;
	private SimpleShape now;
	private Float roseRate;        //уг╥Ы    
	
	public VerifyResult(SimpleShape past, SimpleShape now) {
		super();
		this.past = past;
		this.now = now;
		this.setRoseRate(now.price/past.price - 1.002f);
	}
	public VerifyResult(SimpleShape ss) {
		this.past = ss ;
	}
	
	@Override
	public String toString() {
		Float winRate = 0f;
		if(now!=null && past!=null 
				&& now.price!=null && past.price!=null)
			winRate = now.price/past.price -1.002f;
		
		return "past," + past + ", now," + now + ","
				+ winRate;
	}
	public SimpleShape getPast() {
		return past;
	}
	public void setPast(SimpleShape past) {
		this.past = past;
	}
	public SimpleShape getNow() {
		return now;
	}
	public void setNow(SimpleShape now) {
		this.now = now;
	}
	public Float getRoseRate() {
		return roseRate;
	}
	private void setRoseRate(Float roseRate) {
		this.roseRate = roseRate;
	}

	@Override
	public int compareTo(VerifyResult o) {
		if(this.roseRate>((VerifyResult) o).getRoseRate()) return -1;
		else if(this.roseRate==((VerifyResult) o).getRoseRate()) return 0;
		else return 1;
	}
	
	
}
