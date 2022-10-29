package org.fuxin.stock;

import java.util.ArrayList;

import org.fuxin.util.Pair;

public class DiscreteScope {
	public ArrayList<Pair> typename;

	public DiscreteScope() {
		super();
		typename = new ArrayList<Pair>();
	}

	@Override
	public String toString() {
		return typename.toString();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiscreteScope other = (DiscreteScope) obj;
		if (typename == null) {
			if (other.typename != null)
				return false;
		} else if (!typename.equals(other.typename))
			return false;
		return true;
	}



	
}
