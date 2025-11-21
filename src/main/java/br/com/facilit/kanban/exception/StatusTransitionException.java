package br.com.facilit.kanban.exception;

public class StatusTransitionException extends Exception {


	private static final long serialVersionUID = -8934706062140977778L;

	public StatusTransitionException(final String message) {
		super(message);
	}

}
