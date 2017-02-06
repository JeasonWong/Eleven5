import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * Created by bazhang on 2017/2/6.
 */
class MailManager {

    static def sendMail(text) {
        def to = Constants.MAIL_TO;
        def from = Constants.MAIL_FROM;
        def host = Constants.MAIL_HOST;

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constants.MAIL_ACCOUNT, Constants.MAIL_PWD);
            }
        });

        session.setDebug(true)

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("11选5" + System.currentTimeMillis() + "");
            message.setText(text);

            Transport.send(message);
            println "发送消息成功"
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
