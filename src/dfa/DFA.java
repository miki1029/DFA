package dfa;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DFA {
	private List<Set<String>> qSetList;	// Q
	private Set<String> sSet;			// ∑
	private Map<DeltaKey<Set<String>, String>, Set<String>> dMap; // δ
	private Set<String> q0;				// q0
	private Set<Set<String>> fSets;		// F

	public List<Set<String>> getqSetList() { return qSetList; }
	public void setqSetList(List<Set<String>> qSets) { this.qSetList = qSets; }
	
	public Set<String> getsSet() { return sSet; }
	public void setsSet(Set<String> sSet) { this.sSet = sSet; }
	
	public Map<DeltaKey<Set<String>, String>, Set<String>> getdMap() { return dMap; }
	public void setdMap(Map<DeltaKey<Set<String>, String>, Set<String>> dMap) { this.dMap = dMap; }
	
	public Set<String> getQ0() { return q0; }
	public void setQ0(Set<String> q0) { this.q0 = q0; }
	
	public Set<Set<String>> getfSets() { return fSets; }
	public void setfSets(Set<Set<String>> fSets) { this.fSets = fSets; }
	
	public void printDFA() {
		System.out.println("DFA M'=(Q', " + sSet + ", δ', " + q0 + ", " + fSets + ")");
		System.out.println("Q'");
		System.out.println(qSetList);
		System.out.println("δ'");
		Iterator<Set<String>> qItr = qSetList.iterator();
		while(qItr.hasNext()) {
			Set<String> qSet = qItr.next();
			Iterator<String> sItr = sSet.iterator();
			while(sItr.hasNext()) {
				String sStr = sItr.next();
				System.out.println("δ'(" + qSet + ", " + sStr + ") = " + dMap.get(new DeltaKey<Set<String>, String>(qSet, sStr)));
			}
		}
		System.out.println();
	}
}
