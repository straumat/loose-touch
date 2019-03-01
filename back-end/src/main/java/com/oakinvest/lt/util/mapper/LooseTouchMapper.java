package com.oakinvest.lt.util.mapper;

import com.oakinvest.lt.domain.Contact;
import com.oakinvest.lt.domain.User;
import com.oakinvest.lt.dto.v1.ContactDTO;
import com.oakinvest.lt.dto.v1.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

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
     * Maps a user to a userDTO.
     *
     * @param user user data
     * @return userDTO
     */
    @Mappings({
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "pictureUrl", target = "pictureUrl")
    })
    UserDTO userToUserDTO(User user);

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

}
