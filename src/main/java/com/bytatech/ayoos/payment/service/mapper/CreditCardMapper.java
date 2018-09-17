package com.bytatech.ayoos.payment.service.mapper;

import com.bytatech.ayoos.payment.domain.*;
import com.bytatech.ayoos.payment.service.dto.CreditCardDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CreditCard and its DTO CreditCardDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CreditCardMapper extends EntityMapper<CreditCardDTO, CreditCard> {



    default CreditCard fromId(Long id) {
        if (id == null) {
            return null;
        }
        CreditCard creditCard = new CreditCard();
        creditCard.setId(id);
        return creditCard;
    }
}
