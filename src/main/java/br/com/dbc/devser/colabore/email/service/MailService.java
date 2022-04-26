package br.com.dbc.devser.colabore.email.service;


import br.com.dbc.devser.colabore.email.config.MailConfig;
import br.com.dbc.devser.colabore.entity.DonationEntity;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;


@Component
@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final MailConfig mailConfig;


    public void fundraiserMailService(FundraiserEntity fundraiserEntity) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom("Colabore <colaborevemser@gmail.com>");
        simpleMailMessage.setTo(fundraiserEntity.getFundraiserCreator().getEmail());
        simpleMailMessage.setSubject("Parabéns! Sua campanha atingiu a meta!");
        simpleMailMessage.setText("Olá, " + fundraiserEntity.getFundraiserCreator().getName() + "!\n\n" +
                "Sua meta de R$ " + String.format("%.2f", fundraiserEntity.getGoal()) + " da sua campanha \"" + fundraiserEntity.getTitle() + "\" foi atingida com sucesso!\n" +
                "Para resgatar o valor total da campanha, responda a este e-mail e iremos lhe auxiliar durante o processo :)\n\n" +
                "Obrigado por utilizar a nossa plataforma!\n" +
                "Colabore - VemSerDBC");

        mailConfig.mailSender().send(simpleMailMessage);
    }

    public void donatorMailService(DonationEntity donationEntity, FundraiserEntity fundraiserEntity) {
        String formattedEndingDate = fundraiserEntity.getEndingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("Colabore <colaborevemser@gmail.com>");
        simpleMailMessage.setTo(donationEntity.getDonator().getEmail());
        simpleMailMessage.setSubject("Obrigado pela sua contribuição!");
        simpleMailMessage.setText("Olá, " + donationEntity.getDonator().getName() + "!\n\n" +
                "Você acaba de contribuir R$ " + String.format("%.2f", donationEntity.getValue()) + " para a campanha \"" + fundraiserEntity.getTitle() + "\". " +
                "Caso sinta-se à vontade, você poderá doar novamente para esta causa até " + formattedEndingDate + ".\n\n" +
                "Em nome dos autores da campanha e de toda a equipe Colabore, agradecemos pelo seu apoio!\n" +
                "Colabore - VemSerDBC");

        mailConfig.mailSender().send(simpleMailMessage);
    }
}
