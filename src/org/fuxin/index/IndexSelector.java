package org.fuxin.index;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.fuxin.analyst.MapExpert;
import org.fuxin.passcode.StockDailyWithReason;
import org.fuxin.util.FuOutput;
import org.fuxin.util.ScopeUtil;
import org.fuxin.vo.StockDaily;

public class IndexSelector {

	/***
	 * ��stockpool����ѡ����Դ���������ǿ�ĸ���
	 * ���������ƽ���е���������ʹ��
	 * @param currentday
	 * @param stockpool
	 * @param me
	 * @return
	 */
	public static ArrayList<StockDailyWithReason> doCurrentSelect(
			Date currentday, HashSet<String> stockpool, MapExpert me) {
		//�����б��Ƚ�������
		ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
		for(String symbol:stockpool)
		{
			if(me.getAlldata().get(symbol)!=null)
			{
				//������Щ�������
				ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
				//ָ����Щ�������
				ArrayList<StockDaily> indexdata = me.getShindex();
				//�����index��Ҫ���������û��
				int index = ScopeUtil.getIndexForward(currentday, dailydata);
				int gegu = ScopeUtil.getIndexForward(currentday,dailydata);
				if(gegu<0) gegu=0;
				if(index>=0) 
				{
					//�������ѡ������������򷵻���Դ���ǿ�Ƹ����б�
					Float strongValue = IsStrong(dailydata,indexdata,index);
					if(strongValue<1)
					{
						rtlist.add(new StockDailyWithReason(dailydata.get(gegu),"IndexStrong="+strongValue));
					}
				}
			}
		}
		return rtlist;
	}

	private static Float IsStrong(ArrayList<StockDaily> dailydata,
			ArrayList<StockDaily> indexdata, int index) {
		// TODO Auto-generated method stub
		//���ҵ�20������ָ��ƫ�������ӣ��Լ�ָ������ĵ���
		ArrayList<StockDaily> indexweak=GetIndexWeakList(indexdata,20,index);
		//FuOutput.println("�����������ӣ�", indexweak);
		//������Щ����ȥƥ�������Ʊ��Щ��ı���
		//������ַ���һ���ض�����ֵ������Ϊ�ù�ƱΪǿ��
		Float strongValue = CompareIndex(indexweak,dailydata);
		
		return strongValue;
	}

	/***
	 * �Ƚϴ��̵����ƺ͸��ɵ�����
	 * @param indexweak
	 * @param dailydata
	 * @return
	 */
	private static Float CompareIndex(ArrayList<StockDaily> indexweak,
			ArrayList<StockDaily> dailydata) {
		Float rtValue = 0f;
		for(int i=0;i<indexweak.size();++i)
		{
			Integer index=ScopeUtil.getIndexByDate(indexweak.get(i).getDate(),dailydata);
			if(index>=0)
			{
				StockDaily today = dailydata.get(index);
				StockDaily yesterday = null;
				if((index+1)<dailydata.size())
					yesterday = dailydata.get(index+1);
				else
					yesterday = dailydata.get(index);
				
				Float raiseRate = (today.getClose()/yesterday.getClose()-1);
				
				//���Ҫ�о����������ǿ�������԰������ӡ����
				//System.out.println(today+"||"+raiseRate+"||"+indexweak.get(i).getTurnover());
				rtValue += raiseRate-indexweak.get(i).getTurnover();
				
			}
		}
		//System.out.println(rtValue);
		if(indexweak.size()==0) return 0.02f;
		
		return rtValue/indexweak.size();
	}

	

	/***
	 * �ҵ��������Ƶ���Щ����
	 * @param indexdata
	 * @param days
	 * @param index
	 * @return
	 */
	private static ArrayList<StockDaily> GetIndexWeakList(
			ArrayList<StockDaily> indexdata, int days, int index) {
		ArrayList<StockDaily> rtlist = new ArrayList<StockDaily>();
		for(int i=index;i<=index+days;++i)
		{
			StockDaily today = indexdata.get(i);
			StockDaily yesterday = indexdata.get(i+1);
			today.setTurnover(today.getClose()/yesterday.getClose()-1);
			if(today.getTurnover()<=-0.01)
				rtlist.add(today);
		}
		return rtlist;
	}

	
	public static ArrayList<StockDailyWithReason> doSelect(Date currentday,
			HashSet<String> stockpool, MapExpert me) {
		//�����б��Ƚ�������
				ArrayList<StockDailyWithReason> rtlist = new ArrayList<StockDailyWithReason>();
				for(String symbol:stockpool)
				{
					if(me.getAlldata().get(symbol)!=null)
					{
						//������Щ�������
						ArrayList<StockDaily> dailydata = me.getAlldata().get(symbol);
						//ָ����Щ�������
						ArrayList<StockDaily> indexdata = me.getShindex();
						//�����index��Ҫ���������û��
						//ָ�����±�
						int index = ScopeUtil.getIndexForward(currentday, indexdata);
						//���ɵ��±�
						int gegu = ScopeUtil.getIndexForward(currentday,dailydata);
						if(gegu<0) gegu=0;
						if(index>=0) 
						{
							//��ģ�����У����׵���ֻ����ǰһ������ݣ�����Ϊ����
							Integer strongindex = -1;
							
							if( index+1 < indexdata.size())  
								strongindex = index+1;
							
							Float strongValue = IsStrong(dailydata,indexdata,strongindex);
							//�������ѡ������������򷵻���Դ���ǿ�Ƹ����б�
							if(strongValue<-0.02)
							{
								rtlist.add(new StockDailyWithReason(dailydata.get(gegu),"IndexStrong="+strongValue));
							}
						}
					}
				}
				return rtlist;
	}

	
}
