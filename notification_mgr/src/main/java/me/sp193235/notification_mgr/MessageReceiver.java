package me.sp193235.notification_mgr;

import me.sp193235.interfaces.UserProxy;
import me.sp193235.interfaces.Vinr;
import me.sp193235.interfaces.RabbitMesg;
import me.sp193235.notification_mgr.repo.NotificationRepository;
import me.sp193235.notification_mgr.repo.UserRepository;
import me.sp193235.notification_mgr.repo.VinrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class MessageReceiver {

	@Autowired
	private NotificationRepository repository;

	@Autowired
	private VinrRepository vinrRepo;

	@Autowired
	private UserRepository userRepo;


	public void receiveMessage(RabbitMesg<Vinr> message) {

		System.out.println(message.getObj());

		if (vinrRepo.findById("everyone").isEmpty()) {
			vinrRepo.save(new Vinr("everyone"));
			userRepo.save(new UserProxy("everyone"));
		}

		System.out.println("Received <" + message + ">");
		if (Objects.equals(message.getType(), RabbitMesg.TYPE_CREATE)) {
			vinrRepo.save(message.getObj());
			userRepo.save(new UserProxy(message.getObj().getUsername()));
			System.out.println("User " + message.getObj() + " created!");
		}
		if (Objects.equals(message.getType(), RabbitMesg.TYPE_DELETE)) {
			Optional<Vinr> maybeUser = vinrRepo.findById(message.getObj().getUsername());
			maybeUser.ifPresent(user -> {
				vinrRepo.delete(message.getObj());
				userRepo.delete(new UserProxy(message.getObj().getUsername()));
				});

			System.out.println("User " + message.getObj() + " deleted!");

		}
	}

}
