package org.fuxin.analyst;

import java.util.ArrayList;

public class Discretor {
	public Discretor(Float[] d) {
		border = new ArrayList<Float>();
		for(int i=0;i<d.length;++i)
		{
			border.add(d[i]);
		}
	}
	public ArrayList<Float> border;
	
}
