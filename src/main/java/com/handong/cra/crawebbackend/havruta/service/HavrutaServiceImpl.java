package com.handong.cra.crawebbackend.havruta.service;

import com.handong.cra.crawebbackend.havruta.domain.Havruta;
import com.handong.cra.crawebbackend.havruta.dto.CreateHavrutaDto;
import com.handong.cra.crawebbackend.havruta.dto.DetailHavrutaDto;
import com.handong.cra.crawebbackend.havruta.dto.ListHavrutaDto;
import com.handong.cra.crawebbackend.havruta.dto.UpdateHavrutaDto;
import com.handong.cra.crawebbackend.havruta.dto.request.ReqCreateHavrutaDto;
import com.handong.cra.crawebbackend.havruta.repository.HavrutaRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HavrutaServiceImpl implements HavrutaService {

    private final HavrutaRepository havrutaRepository;

    @Override
    public Boolean deleteHavruta(Long id) {
        havrutaRepository.deleteById(id);
        return true;
    }

    @Override
    public UpdateHavrutaDto updateHavruta(UpdateHavrutaDto updateHavrutaDto) {

        return null;
    }

    @Override
    public DetailHavrutaDto getDetailHavruta(Long id) {
        Havruta havruta =  havrutaRepository.findById(id).orElseThrow();
        return new DetailHavrutaDto(havruta);

    }

    @Override
    public List<ListHavrutaDto> getListHavruta() {
        List<Havruta> havrutas = havrutaRepository.findAll();
        return havrutas.stream().map(ListHavrutaDto::of).toList();
    }

    @Override
    public CreateHavrutaDto createHavruta(ReqCreateHavrutaDto reqCreateHavrutaDto) {
        return null;
    }
}
