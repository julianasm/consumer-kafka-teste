package consumer.emailController;

import consumer.emailService.EmailService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    public void listener() throws Exception {
        emailService.sendEmail("Vinicius Silva Farias", "julianasouzamelo@live.com", "Salve Salve");
    }
}
