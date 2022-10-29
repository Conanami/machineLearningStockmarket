package org.fuxin.volume;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.fuxin.vo.StockDaily;

/***
 * �ɽ���������ɽ
 * @author Administrator
 *
 */

public class TwoMount {
	public int firstMountIndex = -1;
	public Date firstMountDate;
	public float firstMountPrice = -100;
	public Float firstMountVolume;
	public Float firstFootPrice;            //��ɽǰ��ͼ�
	public Float firstTopPrice;             //��ɽ����߼�
	public int secondMountIndex = -1;
	public Date secondMountDate;
	public float secondMountPrice = -100;
	public float secondMountVolume;
	public Float minPriceBeforeSecond;      //��ǰɽǰ��ͼ�
	public Float maxPriceAfterSecond;       //��ǰɽǰ��߼�
	public StockPoint recentBottom;         //���50�����߼�
	public StockPoint recentTop;            //���50�����ͼ�
	public Float recent5raise;				//���5���Ƿ�
	public boolean findFirst = false;
	public boolean findSecond = false;
	public Float smallTopBetweenTwoRatio;   //��ɽ֮����ߵ�����ɽ��ƽ��ֵ�ıȽ�
	public Float recentMaxVolume;
	public StockPoint globalBottomPrice;			//ȫ����ͼ�
	public StockPoint bottomAfterFirstHill;      //��ɽ�����ͼ�
	public Float secondFootPrice;			//ǰɽ��ɽ�żۣ���ɽ��ǰ20������ͼۣ�
	public Float nowPrice;					//������̼�
	public Float average250;
	public Float average120;
	
	public String toReport() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(firstMountDate==null || secondMountDate==null)
			return "No,Two,Mountains";
		else
			return "TwoMount [firstMountDate=" + sdf.format(firstMountDate)
				+ ", secondMountDate=" + sdf.format(secondMountDate) +"]";
	}

	@Override
	public String toString() {
		return "TwoMount [firstMountIndex=" + firstMountIndex
				+ ", firstMountDate=" + firstMountDate + ", firstMountPrice="
				+ firstMountPrice + ", firstMountVolume=" + firstMountVolume
				+ ", firstFootPrice=" + firstFootPrice + ", firstTopPrice="
				+ firstTopPrice + ", secondMountIndex=" + secondMountIndex
				+ ", secondMountDate=" + secondMountDate
				+ ", secondMountPrice=" + secondMountPrice
				+ ", secondMountVolume=" + secondMountVolume
				+ ", minPriceBeforeSecond=" + minPriceBeforeSecond
				+ ", maxPriceAfterSecond=" + maxPriceAfterSecond
				+ ", recentBottom=" + recentBottom + ", recentTop=" + recentTop
				+ ", recent5raise=" + recent5raise + ", findFirst=" + findFirst
				+ ", findSecond=" + findSecond + ", smallTopBetweenTwoRatio="
				+ smallTopBetweenTwoRatio + ", recentMaxVolume="
				+ recentMaxVolume + ", globalBottomPrice=" + globalBottomPrice
				+ ", bottomAfterFirstHill=" + bottomAfterFirstHill
				+ ", secondFootPrice=" + secondFootPrice + ", nowPrice="
				+ nowPrice + ", average250=" + average250 + ", average120="
				+ average120 + "]";
	}



	
	

	
	
	
}
