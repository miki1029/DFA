package dfa;

@SuppressWarnings("serial")
public class InvalidDeltaKeyException extends RuntimeException {
	public InvalidDeltaKeyException() {
		super("비교가 불가능한 키입니다.");
	}
	
	public InvalidDeltaKeyException(String s) {
		super(s);
	}
}
