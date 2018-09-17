package com.bytatech.ayoos.payment.service.mapper;

import com.bytatech.ayoos.payment.domain.*;
import com.bytatech.ayoos.payment.service.dto.PayeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Payee and its DTO PayeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PayeeMapper extends EntityMapper<PayeeDTO, Payee> {



    default Payee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payee payee = new Payee();
        payee.setId(id);
        return payee;
    }
}
