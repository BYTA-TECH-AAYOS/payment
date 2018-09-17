package com.bytatech.ayoos.payment.service.mapper;

import com.bytatech.ayoos.payment.domain.*;
import com.bytatech.ayoos.payment.service.dto.AmountDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AmountDetails and its DTO AmountDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AmountDetailsMapper extends EntityMapper<AmountDetailsDTO, AmountDetails> {



    default AmountDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        AmountDetails amountDetails = new AmountDetails();
        amountDetails.setId(id);
        return amountDetails;
    }
}
