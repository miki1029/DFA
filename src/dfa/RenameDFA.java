package dfa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class RenameDFA {
	private Set<String> qSet;	// Q
	private Set<String> sSet;	// ∑
	private Map<DeltaKey<String, String>, String> dMap; // δ
	private Set<String> q0;		// q0
	private Set<String> fSet;	// F
	
	private DFA dfa;
	private Map<Set<String>, String> qMap; // DFA Q -> RenameDFA Q

	public RenameDFA(DFA dfa) {
		this.dfa = dfa;
		qMap = new HashMap<Set<String>, String>();
		makeqMap();
		
		// Q
		qSet = convertSetsToSet(dfa.getqSetList());
		
		// ∑
		sSet = new TreeSet<String>();
		sSet.addAll(dfa.getsSet());
		
		// δ
		dMap = new TreeMap<DeltaKey<String, String>, String>();
		convertdMap();
		
		// q0
		q0 = new TreeSet<String>();
		q0.add(qMap.get(dfa.getQ0()));
		
		// F
		fSet = convertSetsToSet(dfa.getfSets());
	}

	public Set<String> qSet() { return qSet; }
	public Set<String> sSet() { return sSet; }
	public Map<DeltaKey<String, String>, String> dMap() { return dMap; }
	public Set<String> Q0() { return q0; }
	public Set<String> fSet() { return fSet; }

	private void makeqMap() {
		Character c = 'A';
		for(Set<String> set: dfa.getqSetList()) {
			qMap.put(set, c.toString());
			c++;
		}
	}
	
	private Set<String> convertSetsToSet(Collection<Set<String>> sets) {
		Set<String> newSet = new TreeSet<String>();
		for(Set<String> set: sets) {
			newSet.add(qMap.get(set));
		}
		
		return newSet;
	}
	
	private void convertdMap() {
		List<Set<String>> dqKeySetList = new ArrayList<Set<String>>();	// 기존 Q key 저장
		List<String> dsKeyList = new ArrayList<String>();				// 기존 s key 저장
		List<Set<String>> dValSetList = new ArrayList<Set<String>>();	// 기존 δ value 저장
		
		Iterator<Set<String>> qItr = dfa.getqSetList().iterator();
		while(qItr.hasNext()) {
			Set<String> qSet = qItr.next();
			Iterator<String> sItr = dfa.getsSet().iterator();
			while(sItr.hasNext()) {
				String sStr = sItr.next();
				dqKeySetList.add(qSet);
				dsKeyList.add(sStr);
				dValSetList.add(dfa.getdMap().get(new DeltaKey<Set<String>, String>(qSet, sStr)));
			}
		}
		
		Iterator<Set<String>> dqItr = dqKeySetList.iterator();
		Iterator<String> dsItr = dsKeyList.iterator();
		Iterator<Set<String>> dvItr = dValSetList.iterator();
		
		while(dqItr.hasNext() && dsItr.hasNext() && dvItr.hasNext()) {
			String dqStr = qMap.get(dqItr.next());
			String dsStr = dsItr.next();
			String dvStr = qMap.get(dvItr.next());
			
			dMap.put(new DeltaKey<String, String>(dqStr, dsStr), dvStr);
		}
	}
	
	public void printDFA() {
		System.out.println("DFA M'=(Q', " + sSet + ", δ', " + q0 + ", " + fSet + ")");
		System.out.println("Q'");
		System.out.println(qSet);
		System.out.println("δ'");
		Iterator<String> qItr = qSet.iterator();
		while(qItr.hasNext()) {
			String qStr = qItr.next();
			Iterator<String> sItr = sSet.iterator();
			while(sItr.hasNext()) {
				String sStr = sItr.next();
				System.out.println("δ'(" + qStr + ", " + sStr + ") = " + dMap.get(new DeltaKey<String, String>(qStr, sStr)));
			}
		}
		System.out.println();
	}
}