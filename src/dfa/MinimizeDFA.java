package dfa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MinimizeDFA {
	private RenameDFA rdfa;  // 축소 전 Rename DFA
	private RenameDFA mrdfa; // 축소 후 Rename DFA
	
	// StateTable: state와 다음 상태의 파티션 번호를 함께 나타내는 클래스
	// List<StateTable>: 같은 파티션의 StateTable들의 리스트
	// Map<String, List<StateTable>> partMap: String은 파티션의 키, 파티션의 리스트를 저장
	private Map<String, List<StateTable>> partMap;
	private List<String> partKey; // partMap의 키를 저장

	public MinimizeDFA(RenameDFA rdfa) {
		this.rdfa = rdfa;
		partMap = new TreeMap<String, List<StateTable>>();
		partKey = new ArrayList<String>();
	}
	
	public RenameDFA minimize() {
		initPartTable();
		setSigmaMap();
		while(compareAndSplit()) {
			setSigmaMap();
		}
		
		return mrdfa;
	}
	
	// 종결상태와 종결이 아닌 상태로 파티션을 나누고 StateTable에 state값 설정
	private void initPartTable() {
		partMap.clear();
		partKey.clear();
		Set<String> fSet = rdfa.fSet(); // 종결 상태
		Set<String> nfSet = new HashSet<String>(); // 종결이 아닌 상태
		nfSet.addAll(rdfa.qSet());
		nfSet.removeAll(fSet);

		Character c = 'P';
		// 종결 상태의 StateTable 초기화(state만)
		initStateTable(fSet, c);
		c++;
		
		// 종결이 아닌 상태의 StateTable 초기화(state만)
		initStateTable(nfSet, c);
		c++;
	}
	
	private void initStateTable(Set<String> qSet, Character c) {
		// 종결 상태의 StateTable 초기화(state만)
		List<StateTable> newStateTableList = new ArrayList<StateTable>();
		for(String qStr: qSet) {
			newStateTableList.add(new StateTable(qStr));
		}
		partMap.put(c.toString(), newStateTableList);
		partKey.add(c.toString());
	}
	
	// StateTable의 map값 설정(∑에 대한 파티션 키 값을 구함)
	private void setSigmaMap() {
		// StateTable의 다음 상태 파티션 번호 설정(map)
		for(String key: partKey) {
			List<StateTable> stateTableList = partMap.get(key); 
			for(StateTable st: stateTableList) {
				// partTable의 모든 StateTable을 순회하며 아래 for문 수행
				for(String sigma: rdfa.sSet()) {
					// 각 StateTable에 대한 모든 ∑에 대해 아래 코드를 수행하여 map(키:∑, 값:partKey) 설정
					String delta = rdfa.dMap().get(new DeltaKey<String, String>(st.key, sigma));
					String part = findState(delta);
					try {
						if(part == null) throw new StateNotFoundException(delta);
					}
					catch(StateNotFoundException e) {
						e.printStackTrace();
						System.exit(1);
					}
					st.map.put(sigma, part);
				}
			}
		}
	}
	
	// String 형의 상태 값을 파티션된 인덱스 값으로 반환
	private String findState(String delta) {
		for(String key: partKey) {
			List<StateTable> stateTableList = partMap.get(key); 
			for(StateTable st: stateTableList) {
				if(st.key == delta) return key;
			}
		}
		return null;
	}
	
	// partTable에서 같은 파티션 내의 다른 map 값을 가진 것을 선별
	private boolean compareAndSplit() {
		for(String key: partKey) {
			List<StateTable> stateTableList = partMap.get(key); 
			for(String sKey: rdfa.sSet()) {
				for(StateTable st: stateTableList) {
					
				}
			}
		}
	}
	
	// state(Q)가 ∑에 따라 어느 state로 가는지를 표현하는 클래스
	private class StateTable {
		public String key; // partKey
		public Map<String, String> map; // <∑, partKey>
		
		public StateTable(String key) {
			this.key = key;
			map = new TreeMap<String, String>();
		}
	}
	
	private class StateNotFoundException extends RuntimeException {
		public StateNotFoundException(String delta) {
			super(delta + "를 찾지 못했습니다.");
		}
	}
}
