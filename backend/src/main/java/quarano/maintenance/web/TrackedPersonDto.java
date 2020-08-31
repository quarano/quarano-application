package quarano.maintenance.web;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class TrackedPersonDto extends QuaranoDto {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String mobilePhoneNumber;
    private AddressDto address;
    private LocalDate dateOfBirth;
    private AccountDto account;
    private List<EncounterDto> encounters;
    private List<DiaryEntryDto> diaries;
    private List<ActivationCodeDto> codes;
    private List<ActionItemDto> actionItems;
    private List<ContactPersonDto> contacts;
    private List<TrackedCaseDto> trackedCases;
}