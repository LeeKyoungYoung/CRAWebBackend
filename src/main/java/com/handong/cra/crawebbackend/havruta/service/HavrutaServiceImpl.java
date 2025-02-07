package com.handong.cra.crawebbackend.havruta.service;

import com.handong.cra.crawebbackend.exception.havruta.HavrutaNotFoundException;
import com.handong.cra.crawebbackend.havruta.domain.Havruta;
import com.handong.cra.crawebbackend.board.domain.Board;
import com.handong.cra.crawebbackend.havruta.dto.CreateHavrutaDto;
import com.handong.cra.crawebbackend.havruta.dto.DetailHavrutaDto;
import com.handong.cra.crawebbackend.havruta.dto.ListHavrutaDto;
import com.handong.cra.crawebbackend.havruta.dto.UpdateHavrutaDto;

import com.handong.cra.crawebbackend.havruta.repository.HavrutaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class HavrutaServiceImpl implements HavrutaService {
    private final HavrutaRepository havrutaRepository;

    @Override
    public List<ListHavrutaDto> getAllHavrutas() {

        List<Havruta> havrutas = havrutaRepository.findAll();

        List<ListHavrutaDto> havrutaDtos = havrutas.stream().map(ListHavrutaDto::from).filter(Objects::nonNull).toList();
        return havrutaDtos;
    }

    @Override
    public DetailHavrutaDto getHavrutaById(Long id) {
        Havruta havruta = havrutaRepository.findById(id).orElseThrow(HavrutaNotFoundException::new);
        if (havruta.getDeleted()) throw new HavrutaNotFoundException();
        return DetailHavrutaDto.from(havruta);
    }

    @Override
    @Transactional
    public CreateHavrutaDto createHavruta(CreateHavrutaDto createHavrutaDto) {
        Havruta havruta = Havruta.from(createHavrutaDto);
        havruta = havrutaRepository.save(havruta);

        return CreateHavrutaDto.from(havruta);
    }

    @Override
    @Transactional
    public UpdateHavrutaDto updateHavruta(UpdateHavrutaDto updateHavrutaDto) {
        Havruta havruta = havrutaRepository.findById(updateHavrutaDto.getId()).orElseThrow(HavrutaNotFoundException::new);
        havruta = havruta.update(updateHavrutaDto);
        return UpdateHavrutaDto.from(havruta);
    }

    @Override
    @Transactional
    public Boolean deleteHavruta(Long id) {
        // delete managing obj
        Havruta havruta = havrutaRepository.findById(id).orElseThrow(HavrutaNotFoundException::new);

        // delete boards
        List<Board> boards = havruta.getBoards();

        for (Board board : boards) board.delete();
        havruta.delete();

        return true;
    }
}
