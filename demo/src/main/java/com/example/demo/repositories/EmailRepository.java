package com.example.demo.repositories;

import com.example.demo.entity.Mail;

public interface EmailRepository {

	void sendHTMLEmail(Mail mail);

}
