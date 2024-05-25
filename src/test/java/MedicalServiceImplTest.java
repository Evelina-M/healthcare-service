import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {

    @Test
    void checkBloodPressureTest() {

        PatientInfoFileRepository patientInfoFileRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepositoryMock.getById((Mockito.anyString())))
                .thenReturn(new PatientInfo("123", "Иван", "Иванович",
                        LocalDate.of(2010, 7, 8),
                        new HealthInfo(new BigDecimal(55), new BloodPressure(135, 75))));

        SendAlertService alertServiceMock = Mockito.mock(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepositoryMock, alertServiceMock);
        medicalService.checkBloodPressure("123", new BloodPressure(130, 110));


        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertServiceMock).send(argumentCaptor.capture());

        Assertions.assertEquals("Warning, patient with id: 123, need help", argumentCaptor.getValue());

    }

    @Test
    void checkTemperatureTest() {

        PatientInfoFileRepository patientInfoFileRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepositoryMock.getById((Mockito.anyString())))
                .thenReturn(new PatientInfo("123", "Иван", "Иванович",
                        LocalDate.of(2010, 7, 8),
                        new HealthInfo(new BigDecimal(55), new BloodPressure(130, 110))));

        SendAlertService alertServiceMock = Mockito.mock(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepositoryMock, alertServiceMock);
        medicalService.checkTemperature("123", new BigDecimal("34.00"));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertServiceMock).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 123, need help", argumentCaptor.getValue());

    }

    @Test
    void indicatorsAreNormal() {

        PatientInfoFileRepository patientInfoFileRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepositoryMock.getById((Mockito.anyString())))
                .thenReturn(new PatientInfo("123", "Иван", "Иванович",
                        LocalDate.of(2010, 7, 8),
                        new HealthInfo(new BigDecimal(36), new BloodPressure(130, 110))));

        SendAlertService alertServiceMock = Mockito.mock(SendAlertServiceImpl.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepositoryMock, alertServiceMock);

        medicalService.checkTemperature("123", new BigDecimal("36.00"));
        medicalService.checkBloodPressure("123", new BloodPressure(130, 110));

        Mockito.verify(alertServiceMock, Mockito.times(0))
                .send(Mockito.anyString());


    }
}
