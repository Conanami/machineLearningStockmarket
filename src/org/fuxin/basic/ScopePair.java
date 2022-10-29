package org.fuxin.basic;

//一个离散值的范围
public class ScopePair {
	
	public ScopePair(int type, float maxValue, boolean maxIsEqual, Float minValue, boolean minIsEqual) {
		this.discretetype = type;
		this.max = maxValue;
		this.maxIsEqual = maxIsEqual;
		this.min = minValue;
		this.minIsEqual = minIsEqual;
	}
	public int discretetype;
	public Float min;
	public boolean minIsEqual;
	public Float max;
	public boolean maxIsEqual;
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(discretetype).append("=");
		if(minIsEqual)
			str.append("[");
		else
			str.append("(");
		
		str.append(min).append(",").append(max);
		
		if(maxIsEqual)
			str.append("]");
		else
			str.append(")");
		
		return str.toString();
		
		
		
	}
	
	
}
