package dfa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MinimizeDFA {
	private RenameDFA rdfa;
	private RenameDFA mrdfa;
	
	// StateTable: state와 다음 상태의 파티션 번호를 함께 나타내는 클래스
	// List<StateTable>: 같은 파티션의 StateTable들의 리스트
	// List<List<StateTable>> partTable: 인덱스는 파티션 번호를 의미, 파티션의 리스트를 저장
	private List<List<StateTable>> partTable;

	public MinimizeDFA(RenameDFA rdfa) {
		this.rdfa = rdfa;
		partTable = new ArrayList<List<StateTable>>();
	}
	
	public RenameDFA minimize() {
		initPartTable();
		comparePartTable();
		
		return mrdfa;
	}
	
	private void initPartTable() {
		partTable.clear();
		Set<String> fSet = rdfa.fSet(); // 종결 상태
		Set<String> nfSet = new HashSet<String>(); // 종결이 아닌 상태
		nfSet.addAll(rdfa.qSet());
		nfSet.removeAll(fSet);

		// 종결 상태의 StateTable 초기화
		List<StateTable> fList = new ArrayList<StateTable>();
		for(String fStr: fSet) {
			fList.add(new StateTable(fStr));
		}
		partTable.add(fList);
		
		// 종결이 아닌 상태의 StateTable 초기화
		List<StateTable> nfList = new ArrayList<StateTable>();
		for(String nfStr: nfSet) {
			nfList.add(new StateTable(nfStr));
		}
		partTable.add(nfList);
		
		// StateTable의 다음 상태 파티션 번호 설정(map)
		for(List<StateTable> stateTableList: partTable) {
			for(StateTable st: stateTableList) {
				for(String key: rdfa.sSet()) {
					String delta = rdfa.dMap().get(new DeltaKey<String, String>(st.state, key));
					int index = findState(delta);
					try {
						if(index == -1) throw new StateNotFoundException(delta);
					}
					catch(StateNotFoundException e) {
						e.printStackTrace();
						System.exit(1);
					}
					st.map.put(key, index);
				}
			}
		}
	}
	
	private int findState(String delta) {
		for(List<StateTable> stateTableList: partTable) {
			for(StateTable st: stateTableList) {
				if(st.state == delta) return partTable.indexOf(st);
			}
		}
		return -1;
	}
	
	private void comparePartTable() {
		for(List<StateTable> stateTableList: partTable) {
			for(String key: rdfa.sSet()) {
				for(StateTable st: stateTableList) {
					
				}
			}
		}
	}
	
	// state(Q)가 ∑에 따라 어느 state로 가는지를 표현하는 클래스
	private class StateTable {
		public String state;
		public Map<String, Integer> map; // <state, index>
		
		public StateTable(String state) {
			this.state = state;
			map = new TreeMap<String, Integer>();
		}
	}
}
