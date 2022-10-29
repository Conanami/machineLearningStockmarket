package org.fuxin.volume;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.fuxin.vo.StockDaily;

/***
 * 成交量的两座山
 * @author Administrator
 *
 */

public class TwoMount {
	public int firstMountIndex = -1;
	public Date firstMountDate;
	public float firstMountPrice = -100;
	public Float firstMountVolume;
	public Float firstFootPrice;            //后山前最低价
	public Float firstTopPrice;             //后山后最高价
	public int secondMountIndex = -1;
	public Date secondMountDate;
	public float secondMountPrice = -100;
	public float secondMountVolume;
	public Float minPriceBeforeSecond;      //在前山前最低价
	public Float maxPriceAfterSecond;       //在前山前最高价
	public StockPoint recentBottom;         //最近50天的最高价
	public StockPoint recentTop;            //最近50天的最低价
	public Float recent5raise;				//最近5天涨幅
	public boolean findFirst = false;
	public boolean findSecond = false;
	public Float smallTopBetweenTwoRatio;   //两山之间最高的量与山峰平均值的比较
	public Float recentMaxVolume;
	public StockPoint globalBottomPrice;			//全局最低价
	public StockPoint bottomAfterFirstHill;      //后山后的最低价
	public Float secondFootPrice;			//前山的山脚价（即山峰前20天内最低价）
	public Float nowPrice;					//最近收盘价
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
