package org.fuxin.passcode;

import java.text.SimpleDateFormat;

import org.fuxin.macd.MacdScore;

/***
 * ��������
 * @author Administrator
 *
 */
public class BuyReason {
	
	public BuyReason(CodeScore score) {
		if(score instanceof CodeScore)
		{
			this.cscore = score;
			this.mscore = null;
		}
	}

	public BuyReason(MacdScore score) {
		// TODO Auto-generated constructor stub
		if(score instanceof MacdScore)
		{
			this.mscore = score;
			this.cscore = null;
		}
	}

	public BuyReason(String str) {
		// TODO Auto-generated constructor stub
		reason = str;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(cscore!=null)
		{
			return cscore.show();
		}
		if(mscore!=null)
		{
			return "date=" + sdf.format(mscore.date) + ","
					+ "macd=" + mscore.MACD + ","
					+ "DIFF=" + mscore.DIFF + ","
					+ "DEA=" + mscore.DEA + ","
					;
		}	
		return "BuyReason="+reason;
	}


	public CodeScore cscore;			//��ͣ�����ֵ
	public MacdScore mscore;			//macdֵ
	public String reason;
}
