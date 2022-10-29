package org.fuxin.util;

/***
 * ��ʾһ����Χ�������������ֵ����Сֵ
 * @author Administrator
 *
 */
public class Pair {
	public Integer period;     //����
	public Float min;		   //��Сֵ
	public Float max;		   //���ֵ
	public Pair(Integer period,Float min,Float max)
	{
		this.period = period;
		this.min = min;
		this.max = max;
	}
	
	
	@Override
	public String toString() {
		return period+"["+ min +"," + max + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((max == null) ? 0 : max.hashCode());
		result = prime * result + ((min == null) ? 0 : min.hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		} else if (!min.equals(other.min))
			return false;
		if (period == null) {
			if (other.period != null)
				return false;
		} else if (!period.equals(other.period))
			return false;
		return true;
	}
	
	
	
	
}
