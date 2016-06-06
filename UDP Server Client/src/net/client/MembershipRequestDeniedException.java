package net.client;

public class MembershipRequestDeniedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5517695561250999304L;
    public MembershipRequestDeniedException(String message) {
        super(message);
    }
    public MembershipRequestDeniedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
