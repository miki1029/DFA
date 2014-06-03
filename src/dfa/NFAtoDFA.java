package dfa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

public class NFAtoDFA {
	private NFA nfa;
	private DFA dfa;
	
	public NFAtoDFA(NFA nfa) {
		this.nfa = nfa;
		dfa = new DFA();
	}
	
	public DFA convert() {
		// ∑
		dfa.setsSet(nfa.sSet());
		
		// q0
		Set<String> q0 = new TreeSet<String>();
		q0.add(nfa.q0());
		dfa.setQ0(q0);
		
		// δ, Q
		Map<DeltaKey<Set<String>, String>, Set<String>> dMap;
		dMap = makeDelta(nfa.sSet(), nfa.dMap(), q0);
		dfa.setdMap(dMap);
		
		// F
		Set<Set<String>> fSets = findF(dfa.getqSetList(), nfa.fSet());
		dfa.setfSets(fSets);
		
		return dfa;
	}

	// Q의 부분 집합을 구하는 메소드. 구현은 했지만 inaccessible state를 제외하면서 사용하지 않음.
	private Set<Set<String>> createSubset(Set<String> set) {
		Set<Set<String>> subset = new HashSet<Set<String>>();
		Stack<String> stack = new Stack<String>();
		// set을 list로 복사하여 알고리즘 수행(인덱스 접근을 위해)
		List<String> list = new ArrayList<String>();
		list.addAll(set);
		int size=1;
		int i=0, nextI=list.size();
		String rm;
		boolean finish = false;
		
		while(!finish) {
			while(stack.size() < size) {
				if(i<list.size()) {
					stack.push(list.get(i++));
				}
				else {
					if(!stack.empty()) {
						rm = stack.pop();
						nextI = list.indexOf(rm) + 1;
						i = nextI;
					}
					else if(nextI > list.size()-1) {
						size++; i=0;
						stack.clear();
						if(size > list.size()) {
							finish=true;
							break;
						}
					}
				}
			}
			if(stack.size() == size) {
				Set<String> newSubset = new TreeSet<String>();
				newSubset.addAll(stack);
				subset.add(newSubset);
				rm = stack.pop();
			}
		}
		return subset;
	}
	
	private Set<Set<String>> findF(List<Set<String>> qSets, Set<String> fSet) {
		Set<Set<String>> fSets = new HashSet<Set<String>>();
		for(Set<String> qSet: qSets) {
			for(String f: fSet) {
				if(qSet.contains(f)) {
					fSets.add(qSet);
					break;
				}
			}
		}
		
		return fSets;
	}
	
	private Map<DeltaKey<Set<String>, String>, Set<String>> makeDelta
			( Set<String> sSet,
			Map<DeltaKey<String, String>, Set<String>> nfaDMap,
			Set<String> q0)
	{
		Map<DeltaKey<Set<String>, String>, Set<String>> dMap
				= new TreeMap<DeltaKey<Set<String>, String>, Set<String>>();
		List<Set<String>> dKey = new ArrayList<Set<String>>();
		Set<Set<String>> nextqSets = new HashSet<Set<String>>();
		Set<Set<String>> curqSets = new HashSet<Set<String>>();
		
		dKey.add(q0);
		nextqSets.add(q0);
		
		while(!nextqSets.isEmpty()) {
			curqSets.clear();
			curqSets.addAll(nextqSets);
			Iterator<Set<String>> qItr = curqSets.iterator();
			nextqSets.clear();
			while(qItr.hasNext()) {
				Set<String> qSet = qItr.next();
				Iterator<String> sItr = sSet.iterator();
				while(sItr.hasNext()) {
					String sStr = sItr.next();
					Set<String> newSet = new TreeSet<String>();
					for(String qStr: qSet) {
						newSet.addAll(nfaDMap.get(new DeltaKey<String, String>(qStr, sStr)));
						while(newSet.contains("")) newSet.remove("");
					}
					dMap.put(new DeltaKey<Set<String>, String>(qSet, sStr), newSet);
					if(!(dKey.contains(newSet) || newSet.isEmpty())) {
						dKey.add(newSet);
						nextqSets.add(newSet);
					}
				}
			}
		}
		dfa.setqSetList(dKey);
		
		return dMap;
	}
	
}