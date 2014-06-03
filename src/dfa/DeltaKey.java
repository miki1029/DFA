package dfa;

import java.util.Iterator;
import java.util.Set;

public class DeltaKey<Q, S> implements Comparable<DeltaKey<Q, S>> {
	public final Q q;
	public final S s;
	
	public DeltaKey(Q q, S s) {
		this.q = q;
		this.s = s;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int compareTo(DeltaKey<Q, S> dk) {
		try {
			if(q.equals(dk.q)) {
				// S 비교(String)
				if(s.equals(dk.s)) {
					return 0;
				}
				else {
					if(s instanceof Comparable && dk.s instanceof Comparable) {
						Comparable s1 = (Comparable)s;
						Comparable s2 = (Comparable)dk.s;
						return s1.compareTo(s2);
					}
					else throw new InvalidDeltaKeyException();
				}
			}
			else {
				// Q 비교(String 또는 Set)
				if(q instanceof Comparable && dk.q instanceof Comparable) {
					Comparable q1 = (Comparable)q;
					Comparable q2 = (Comparable)dk.q;
					return q1.compareTo(q2);
				}
				else if(q instanceof Set && dk.q instanceof Set) {
					Set s1 = (Set)q;
					Set s2 = (Set)dk.q;
					Iterator i1 = s1.iterator();
					Iterator i2 = s2.iterator();
					while(i1.hasNext() && i2.hasNext()) {
						Object o1 = i1.next();
						Object o2 = i2.next();
						if(o1 instanceof Comparable && o2 instanceof Comparable) {
							Comparable c1 = (Comparable)o1;
							Comparable c2 = (Comparable)o2;
							int result = c1.compareTo(c2);
							if(result!=0) return result;
						}
						else throw new InvalidDeltaKeyException();
					}
					if(i1.hasNext()) return 1;
					else if(i2.hasNext()) return -1;
				}
				else throw new InvalidDeltaKeyException();
			}
		}
		catch(InvalidDeltaKeyException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return -1;
	}
}
