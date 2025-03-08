package com.example.demo.service.exception;

public class TicketError extends RuntimeException {
	private static final long serialVersionUID = 4196736240932139012L;

	public TicketError(Object id) {
		super("Ticket error. id " + id);
	}

}
