package com.bytatech.ayoos.payment.service.mapper;

import com.bytatech.ayoos.payment.domain.*;
import com.bytatech.ayoos.payment.service.dto.PayerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Payer and its DTO PayerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PayerMapper extends EntityMapper<PayerDTO, Payer> {


    @Mapping(target = "fundingInstruments", ignore = true)
    Payer toEntity(PayerDTO payerDTO);

    default Payer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payer payer = new Payer();
        payer.setId(id);
        return payer;
    }
}
