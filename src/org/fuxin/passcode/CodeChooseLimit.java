package org.fuxin.passcode;

public class CodeChooseLimit {
	
	public Float maxprice;				//最高价格限制
	public float maxscore;				//最高基因个数，太高也是不行的
	public Float minscore;				//最低基因个数
	public int minVList;				//最小倍量伸缩发生次数
	public Float maxVolumeRate;			//最大量比比例
	public float minVolumeRate;			//最小量比比例
	public int minFList;				//最小假阴真阳发生次数
	public int timespan;				//考察基因的时间范围
	public int maxLysvList;				//最大的长阴小量个数，
										//最好30天内不多了，否则说明还在拼命洗
	public float maxVolume = 0;				//最大换手率
	public int minLongHeadList = 0;			//长上影出现次数
	public int mingoldList;					//最少出现的黄金柱
	public double minYangrate;				//最小阳线比例
	public int maxgoldList=99;					//最多出现的黄金柱
}
