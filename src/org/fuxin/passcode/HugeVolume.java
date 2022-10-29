package org.fuxin.passcode;

import java.util.Date;

public class HugeVolume {
	public String symbol;					//股票代码
	public int startindex;					//开始的索引，其实是最后一天
	public Date startdate;					//最后一天的日期
	public int endindex;					//结束的索引，其实是第一天
	public Date enddate;					//第一天的日期
	public Float averageTurnover;			//这些日子的平均换手率
	public Float totalTurnover;				//这些日子的总换手率
}
