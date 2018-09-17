package com.bytatech.ayoos.payment.service.mapper;

import com.bytatech.ayoos.payment.domain.*;
import com.bytatech.ayoos.payment.service.dto.FundingInstrumentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FundingInstrument and its DTO FundingInstrumentDTO.
 */
@Mapper(componentModel = "spring", uses = {PayerMapper.class, CreditCardMapper.class})
public interface FundingInstrumentMapper extends EntityMapper<FundingInstrumentDTO, FundingInstrument> {

    @Mapping(source = "payer.id", target = "payerId")
    @Mapping(source = "creditCard.id", target = "creditCardId")
    FundingInstrumentDTO toDto(FundingInstrument fundingInstrument);

    @Mapping(source = "payerId", target = "payer")
    @Mapping(source = "creditCardId", target = "creditCard")
    FundingInstrument toEntity(FundingInstrumentDTO fundingInstrumentDTO);

    default FundingInstrument fromId(Long id) {
        if (id == null) {
            return null;
        }
        FundingInstrument fundingInstrument = new FundingInstrument();
        fundingInstrument.setId(id);
        return fundingInstrument;
    }
}
