package dfa;

public class Main {
	public static void main(String[] args) {
		NFA nfa = new NFA();
		nfa.printNFA();
		
		NFAtoDFA converter = new NFAtoDFA(nfa);
		DFA dfa = converter.convert();
		dfa.printDFA();
		
		RenameDFA rdfa = new RenameDFA(dfa);
		rdfa.printDFA();
	}
}
