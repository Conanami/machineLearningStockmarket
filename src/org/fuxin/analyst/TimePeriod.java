package org.fuxin.analyst;

import java.util.ArrayList;

public class TimePeriod {
	public ArrayList<Integer> period;
	public Integer size;
	public TimePeriod()
	{
		period = new ArrayList<Integer>();
	}
	
	public TimePeriod(int[] p) {
		super();
		period = new ArrayList<Integer>();
		size = 0;
		for(int i=0;i<p.length;++i)
		{
			period.add(p[i]);
			size = size + p[i];
		}
		
	}

	public TimePeriod(ArrayList<Integer> inList) {
		period = new ArrayList<Integer>();
		size = 0;
		period = (ArrayList<Integer>)inList.clone();
		
		for(int i=0;i<period.size();++i)
			size = size + period.get(i);
		
		
	}
	
	public void calculateSize()
	{
		size = 0;
		for(int i=0;i<period.size();++i)
			size = size + period.get(i);
	}
	
	@Override
	public String toString() {
		return "TimePeriod_" + period;
	}
	
	
}
