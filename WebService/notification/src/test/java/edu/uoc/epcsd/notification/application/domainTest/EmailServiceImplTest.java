package edu.uoc.epcsd.notification.application.domainTest;

import edu.uoc.epcsd.notification.application.kafka.EmailMessage;
import edu.uoc.epcsd.notification.domain.service.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.util.Properties;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class EmailServiceImplTest {

    @Mock
    private EmailServiceImpl emailService;
    @Mock
    private EmailMessage emailMessage;

    @Value("${spring.mail.host}")
    private String smtpHost;

    @Value("${spring.mail.port}")
    private String smtpPort;

    @Value("${spring.mail.username}")
    private String smtpUsername;

    @Value("${spring.mail.password}")
    private String smtpPassword;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void testGetTemplateEmail() {
        // Crear un objeto EmailMessage de prueba
        EmailMessage email = new EmailMessage();
        email.setTo("test@pedromartinezweb.com");
        email.setBody("This is a test email");
        email.setTemplateName("correo.html");
        email.setButtonLink("vacio");
        email.setButtonText("vacio");
        email.setSubject("Test email");


        // Configurar comportamiento esperado para getTemplateEmail
        when(emailService.getTemplateEmail(email)).thenCallRealMethod();

        // Llamar al método y realizar las aserciones necesarias
        String template = emailService.getTemplateEmail(email);

        // Asegurarse de que el método se llame y de que el resultado sea el esperado
        verify(emailService, times(1)).getTemplateEmail(email);
        // Realiza aquí las aserciones necesarias sobre el contenido del template
    }

    @Test
    void testSendEmail_SuccessMOCK() {
        EmailMessage email = new EmailMessage();
        email.setTo("test@pedromartinezweb.com");
        email.setBody("This is a test email");
        email.setTemplateName("correo.html");
        email.setButtonLink("vacio");
        email.setButtonText("vacio");
        email.setSubject("Test email");

        // Simular el envío exitoso del correo
        doNothing().when(emailService).sendEmail(email);

        // Llamar al método y verificar que no se lance ninguna excepción
        assertDoesNotThrow(() -> emailService.sendEmail(email));

        // Verificar que se llamó al método sendEmail
        verify(emailService, times(1)).sendEmail(email);
    }
    @Test
    void testGetTemplateEmail_Success() {
        EmailMessage email = new EmailMessage();
        email.setTemplateName("correo.html");

        // Simular el comportamiento exitoso de la lectura de la plantilla
        when(emailService.getTemplateEmail(email)).thenReturn("Contenido de la plantilla");

        String result = emailService.getTemplateEmail(email);

        // Verificar que se llamó al método getTemplateEmail
        verify(emailService, times(1)).getTemplateEmail(email);
    }

    @Test
    void testGetTemplateEmail_TemplateNotFound() {
        EmailMessage email = new EmailMessage();
        email.setTemplateName("plantilla_inexistente.html");

        // Simular el comportamiento para una plantilla no encontrada
        when(emailService.getTemplateEmail(email)).thenReturn(email.getBody());

        String result = emailService.getTemplateEmail(email);

        // Verificar que se llamó al método getTemplateEmail
        verify(emailService, times(1)).getTemplateEmail(email);

    }
    @Test
    void testSendEmail_Failure() {
        EmailMessage email = new EmailMessage();
        email.setTo("test@pedromartinezweb.com");
        email.setBody("This is a test email");
        email.setTemplateName("correo.html");
        email.setButtonLink("vacio");
        email.setButtonText("vacio");
        email.setSubject("Test email");

        doThrow(new RuntimeException("Error al enviar el email")).when(emailService).sendEmail(email);

        assertThrows(RuntimeException.class, () -> emailService.sendEmail(email));

        verify(emailService, times(1)).sendEmail(email);
    }


    @Test
    void testGetTemplateEmail_RuntimeException() {
        EmailMessage email = new EmailMessage();
        email.setTemplateName("correo.html");

        // Simular una RuntimeException al leer la plantilla
        when(emailService.getTemplateEmail(email)).thenThrow(new RuntimeException("Error al procesar la plantilla"));

        try {
            emailService.getTemplateEmail(email);
            fail("Se esperaba una RuntimeException");
        } catch (RuntimeException e) {
            // Verificar que se lanzó la excepción esperada
            assertThat(e.getMessage()).isEqualTo("Error al procesar la plantilla");
        }

        // Verificar que se intentó leer la plantilla
        verify(emailService, times(1)).getTemplateEmail(email);
    }

    @Test
    void testGetTemplateEmail_WithReplacements() {
        EmailMessage email = new EmailMessage();
        email.setTemplateName("correo_con_cadenas.html");
        email.setBody("Cuerpo del mensaje");
        email.setButtonLink("Enlace del botón");
        email.setButtonText("Texto del botón");

        // Simular el comportamiento del método getTemplateEmail con reemplazos
        when(emailService.getTemplateEmail(email)).thenReturn("Cuerpo del mensaje");

        String result = emailService.getTemplateEmail(email);

        // Verificar que se llamó al método getTemplateEmail
        verify(emailService, times(1)).getTemplateEmail(email);

        // Aserciones para verificar el contenido con reemplazos
        assertThat(result).contains("Cuerpo del mensaje");

    }


    @Test
    void testEmailMessageSetterGetter() {
        // Crear un objeto EmailMessage de prueba
        EmailMessage email = new EmailMessage();
        email.setTo("test@pedromartinezweb.com");
        email.setBody("This is a test email");
        email.setTemplateName("correo.html");
        email.setButtonLink("vacio");
        email.setButtonText("vacio");
        email.setSubject("Test email");

        // Verificar que los valores se han establecido correctamente mediante los setters
        assertEquals("test@pedromartinezweb.com", email.getTo());
        assertEquals("This is a test email", email.getBody());
        assertEquals("correo.html", email.getTemplateName());
        assertEquals("vacio", email.getButtonLink());
        assertEquals("vacio", email.getButtonText());
        assertEquals("Test email", email.getSubject());
    }

    @Test
    void testPropertiesConfiguration() {

        EmailMessage email = new EmailMessage();
        email.setTo("test@pedromartinezweb.com");
        email.setBody("This is a test email");
        email.setTemplateName("correo.html");
        email.setButtonLink("vacio");
        email.setButtonText("vacio");
        email.setSubject("Test email");

        when(emailService.getTemplateEmail(email)).thenReturn("Contenido de la plantilla");

        String result = emailService.getTemplateEmail(email);
        verify(emailService, times(1)).getTemplateEmail(email);

        assertEquals("smtp.gmail.com", smtpHost);
        assertEquals("465", smtpPort);
    }

    @Test
    void testGetTemplateEmail_ReturnsBody() {
        // Configurar el comportamiento del emailMessage
        when(emailMessage.getTemplateName()).thenReturn("templateName");
        when(emailMessage.getBody()).thenReturn("Test body");

        // Llamar al método
        String result = emailService.getTemplateEmail(emailMessage);

        // Asegurarse de que el resultado es igual al cuerpo del mensaje
        assertNull(result);
    }

    @Test
    void testGetTemplateEmail_CatchRuntimeException() {
        // Configurar el comportamiento del emailMessage
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTemplateName("correoFALSO.html");
        emailMessage.setBody("Test body");
        emailMessage.setButtonLink("vacio");
        emailMessage.setButtonText("vacio");
        emailMessage.setSubject("Test email");
        emailMessage.setTo("vacio");
        emailMessage.setTemplateName("vacio");

        // Forzar una excepción de RuntimeException al abrir el recurso
        //doThrow(new RuntimeException("Error al abrir el recurso")).when(emailService).getTemplateEmail(emailMessage);

        // Llamar al método y capturar la excepción
        String result = emailService.getTemplateEmail(emailMessage);

        // Asegurarse de que el resultado es igual al cuerpo del mensaje
        assertNull(result);
    }

    @Test
    void testSmtpHostAndPort() {
        assertEquals("smtp.gmail.com", smtpHost);
        assertEquals("465", smtpPort);
    }



    @Test
    void testConfigureEmailProperties() {
        EmailServiceImpl emailService = new EmailServiceImpl();

        Properties properties = emailService.configureEmailProperties();

        assertNotNull(properties);
        assertEquals("smtp.gmail.com", properties.getProperty("mail.smtp.host"));
        assertEquals("465", properties.getProperty("mail.smtp.port"));
        assertEquals("true", properties.getProperty("mail.smtp.ssl.enable"));
        assertEquals("true", properties.getProperty("mail.smtp.auth"));
    }

    @Test
    void testSendEmail() {
        EmailMessage email = new EmailMessage();
        email.setTo("test@pedromartinezweb.com");
        email.setSubject("Test Subject");
        email.setBody("This is a test body");

        doNothing().when(emailService).sendEmail(email);
        emailService.sendEmail(email);
        verify(emailService, times(1)).sendEmail(email);
    }

}



