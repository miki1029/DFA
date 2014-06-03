package dfa;

@SuppressWarnings("serial")
public class InputException extends RuntimeException {
	public InputException() {
		super("입력 값이 Q에 존재하지 않습니다.");
	}
	
	public InputException(String s) {
		super(s);
	}
}
