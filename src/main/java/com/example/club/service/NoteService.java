package com.example.club.service;

import com.example.club.dto.NoteDTO;
import com.example.club.entity.ClubMember;
import com.example.club.entity.Note;

import java.util.List;

public interface NoteService {

    Long register(NoteDTO noteDTO);

    NoteDTO get(Long num);  // 하나 조회

    void modify(NoteDTO noteDTO);

    void remove(Long num);

    List<NoteDTO> getAllWithWriter(String writerEmail); //작성자의 모든 글 조회



    default Note dtoToEntity(NoteDTO noteDTO){
        Note note = Note.builder()
                .num(noteDTO.getNum())
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .writer( ClubMember.builder().email( noteDTO.getWriterEmail() ).build() )
                .build();
        return note;
    }

    default NoteDTO entityToDto(Note note){
        NoteDTO noteDTO = NoteDTO.builder()
                .num(note.getNum())
                .title(note.getTitle())
                .content(note.getContent())
                .writerEmail(note.getWriter().getEmail())
                .regDate(note.getRegDate())
                .modDate(note.getModDate())
                .build();
        return  noteDTO;
    }


}
