package com.oakinvest.lt.util.mapper;

import com.oakinvest.lt.domain.Contact;
import com.oakinvest.lt.domain.Account;
import com.oakinvest.lt.dto.v1.AccountDTO;
import com.oakinvest.lt.dto.v1.ContactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Loose touch mapper.
 */
@SuppressWarnings("unused")
@Mapper
public interface LooseTouchMapper {

    /**
     * Instance.
     */
    LooseTouchMapper INSTANCE = Mappers.getMapper(LooseTouchMapper.class);

    /**
     * Maps a account to a accountDTO.
     *
     * @param account account data
     * @return accountDTO
     */
    @Mappings({
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "pictureUrl", target = "pictureUrl")
    })
    AccountDTO accountToAccountDTO(Account account);

    /**
     * Maps a contact to a contactDTO.
     *
     * @param contact contact data
     * @return contactDTO
     */
    @Mappings({
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "notes", target = "notes"),
            @Mapping(source = "contactRecurrenceType", target = "contactRecurrenceType"),
            @Mapping(source = "contactRecurrenceValue", target = "contactRecurrenceValue"),
            @Mapping(source = "contactDueDate", target = "contactDueDate")
    })
    ContactDTO contactToContactDTO(Contact contact);

    /**
     * Maps a list to another one.
     *
     * @param contacts contacts
     * @return contacts
     */
    List<ContactDTO> contactsToContactDTOs(List<Contact> contacts);

}
