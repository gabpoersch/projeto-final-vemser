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

    public void fundraiserMailService(FundraiserEntity fundraiserEntity, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom("Colabore <colaborevemser@gmail.com>");
        simpleMailMessage.setTo(fundraiserEntity.getFundraiserCreator().getEmail());
        simpleMailMessage.setSubject("Parabéns! Sua campanha atingiu a meta!");
        simpleMailMessage.setText(message);

        mailConfig.mailSender().send(simpleMailMessage);
    }

    public void donatorMailService(DonationEntity donationEntity, FundraiserEntity fundraiserEntity) {
        String formattedEndingDate = fundraiserEntity.getEndingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("Colabore <colaborevemser@gmail.com>");
        simpleMailMessage.setTo(donationEntity.getDonator().getEmail());
        simpleMailMessage.setSubject("Obrigado pela sua contribuição!");
        simpleMailMessage.setText(String.format("Olá, %s!%n%nVocê acaba de contribuir R$ %.2f para a campanha \"%s\"." +
                        "Caso sinta-se à vontade, você poderá doar novamente para esta causa até %s.%n" +
                        "Em nome dos autores da campanha e de toda a equipe Colabore, agradecemos pelo seu apoio!%nColabore - VemSerDBC"
                , donationEntity.getDonator().getName(), donationEntity.getValue()
                , fundraiserEntity.getTitle(), formattedEndingDate));

        mailConfig.mailSender().send(simpleMailMessage);
    }
}
