package org.fuxin.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.vo.StockDaily;

public class TradingStrategy {
	
	
	public TradingStrategy(ArrayList<String> batchlist, boolean useFin, int holdday, Float losslimit) {
		this.selectbatchlist=batchlist;
		this.useFinancer = useFin;
		this.holdday = holdday;
		this.losslimit = losslimit;
	}
	
	public ArrayList<String> selectbatchlist;      //ѡ�ɷ�������
	public boolean useFinancer;					   //�Ƿ�������ɾѡ
	public Integer holdday;                        //�ֹ�����
	public Float losslimit;                        //ֹ���
	
	public ArrayList<StockDaily> doSelect(Date currentday,
			HashSet<String> mystockpool) {
		// TODO Auto-generated method stub
		return null;
	}

}
