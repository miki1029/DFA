package dfa;

@SuppressWarnings("serial")
public class StateNotFoundException extends RuntimeException {
	public StateNotFoundException(String delta) {
		super(delta + "를 찾지 못했습니다.");
	}
}
