package com.example.samuraitravel.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.service.VerificationTokenService;

@Component //ListerクラスのインスタンスがDIコンテナに登録されるようになる
public class SignupEventListener {
	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender javaMailSender;

	public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
		this.verificationTokenService = verificationTokenService;
		this.javaMailSender = mailSender;
	}

	@EventListener //イベント発生時に実行したいメソッド
	private void onSignupEvent(SignupEvent signupEvent) { //SignupEventクラスから通知を受けたときに実行される処理
		User user = signupEvent.getUser();
		String token = UUID.randomUUID().toString(); //重複しない一意のIDを生成
		verificationTokenService.create(user, token);

		String recipientAddress = user.getEmail();
		String subject = "メール認証";
		String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
		String message = "以下のリンクをクリックして会員登録を完了してください。";

		//JavaMailSenderインターフェースを使ってメールを送信する
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);
		javaMailSender.send(mailMessage);
	}
}
