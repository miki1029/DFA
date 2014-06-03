package dfa;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class NFA {
	private Set<String> qSet = new TreeSet<String>();		 // Q
	private Set<String> sSet = new TreeSet<String>();		 // ∑
	private Map<DeltaKey<String, String>, Set<String>> dMap	 // δ
			= new TreeMap<DeltaKey<String, String>, Set<String>>(); 
	private String q0;										 // q0
	private Set<String> fSet = new TreeSet<String>();		 // F
	
	private Scanner keyboard = new Scanner(System.in);
	private final String replacePattern = "^[{]|[\\s]|[}]$";
	
	public NFA() { setNFA(); }
	public Set<String> qSet() { return qSet; }
	public Set<String> sSet() { return sSet; }
	public Map<DeltaKey<String, String>, Set<String>> dMap() { return dMap; }
	public String q0() { return q0; }
	public Set<String> fSet() { return fSet; }

	public void setNFA() {
		System.out.println("NFA M을 입력해 주세요.");
		System.out.println("NFA M=(Q, ∑, δ, q0, F)");
		
		System.out.print("Q: ");
		readAndSet(qSet);
		
		System.out.print("∑: ");
		readAndSet(sSet);
		
		System.out.println("δ(공집합은 빈 상태로 엔터키 입력)");
		setDelta();
		
		System.out.print("q0: ");
		setQ0();
		
		System.out.print("F: ");
		setfSet();
		
		System.out.println();
	}

	private void readAndSet(Set<String> set) {
		String input = keyboard.nextLine();
		input = input.replaceAll(replacePattern, "");
		if(input.equals("null")) return;
		String[] inputArr = input.split(",");
		for(int i=0; i<inputArr.length; i++) {
			set.add(inputArr[i]);
		}
	}

	private void setDelta() {
		Iterator<String> qItr = qSet.iterator();
		while(qItr.hasNext()) {
			String qStr = qItr.next();
			Iterator<String> sItr = sSet.iterator();
			while(sItr.hasNext()) {
				String sStr = sItr.next();
				System.out.print("δ(" + qStr + ", " + sStr + ") = ");
				Set<String> newSet = new TreeSet<String>();
				readAndSet(newSet);
				try {
					for(String str: newSet) {
						if(!(qSet.contains(str) || str.equals(""))) {
							throw new InputException();
						}
					}
				}
				catch(InputException e) {
					e.printStackTrace();
					System.exit(1);
				}
				dMap.put(new DeltaKey<String, String>(qStr, sStr), newSet);
			}
		}
	}
	
	private void setQ0() {
		String str;
		str = keyboard.nextLine();
		str = str.replaceAll(replacePattern, "");
		try {
			if(!qSet.contains(str)) {
				throw new InputException();
			}
		}
		catch(InputException e) {
			e.printStackTrace();
			System.exit(1);
		}
		q0 = str;
	}
	
	private void setfSet() {
		Set<String> newSet = new TreeSet<String>();
		readAndSet(newSet);
		try {
			for(String str: newSet) {
				if(!qSet.contains(str)) {
					throw new InputException();
				}
			}
		}
		catch(InputException e) {
			e.printStackTrace();
			System.exit(1);
		}
		fSet = newSet;
	}
	
	public void printNFA() {
		System.out.println("NFA M=(" + qSet + ", " + sSet + ", δ, " + q0 + ", " + fSet + ")");
		Iterator<String> qItr = qSet.iterator();
		while(qItr.hasNext()) {
			String qStr = qItr.next();
			Iterator<String> sItr = sSet.iterator();
			while(sItr.hasNext()) {
				String sStr = sItr.next();
				System.out.println("δ(" + qStr + ", " + sStr + ") = " + dMap.get(new DeltaKey<String, String>(qStr, sStr)));
			}
		}
		System.out.println();
	}
}
