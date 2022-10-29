package org.fuxin.simulator;

import java.util.Date;

import org.fuxin.passcode.BuyReason;
import org.fuxin.util.DateUtil;

public class SingleDeal {
	public String symbol;					//��Ʊ����
	public String name;						//��Ʊ����
	public Float buyprice;					//�����
	public Date buyday;						//�������ڣ����һ����
	public int shares;						//���й���
	public Float totalcost;					//�ܼƳɱ�
	public Date sellday;					//�׳�����
	public Float sellprice;					//�׳��۸�
	public Float totalincome;				//�ܼ�����
	public Float rate;						//ӯ����
	public Float profit;					//��������
	public long holdday;					//��������
	public BuyReason reason;				//��������
	public void calculate() {
		profit = sellprice * shares - totalcost;
		rate = profit / totalcost;
		holdday = DateUtil.Sub(sellday, buyday);
		
	}
	@Override
	public String toString() {
		return "SingleDeal [symbol=" + symbol + ", name=" + name
				+ ", buyprice=" + buyprice + ", buyday=" + buyday + ", shares="
				+ shares + ", totalcost=" + totalcost + ", sellday=" + sellday
				+ ", sellprice=" + sellprice + ", totalincome=" + totalincome
				+ ", rate=" + rate + ", profit=" + profit + ", holdday="
				+ holdday + ", reason=" + reason ;
	}
	
	
}
