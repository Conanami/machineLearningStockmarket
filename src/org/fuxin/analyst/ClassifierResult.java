package org.fuxin.analyst;

import java.util.ArrayList;

import org.fuxin.stock.DiscreteScope;
import org.fuxin.stock.ShapeGrp;

/***
 * 分类结果
 * @author Administrator
 * 
 */

public class ClassifierResult {
	public ClassifierResult(DiscreteScope ds1, ArrayList<ShapeGrp> shapegrpList) {
		this.disScope = ds1;
		this.shapegrpList = shapegrpList;
	}
	public DiscreteScope disScope;
	public ArrayList<ShapeGrp> shapegrpList;
	@Override
	public String toString() {
		return "disScope=" + disScope + ", shapegrpCnt="
				+ shapegrpList.size();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((disScope == null) ? 0 : disScope.hashCode());
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
		ClassifierResult other = (ClassifierResult) obj;
		if (disScope == null) {
			if (other.disScope != null)
				return false;
		} else if (!disScope.equals(other.disScope))
			return false;
		return true;
	}
	
}
