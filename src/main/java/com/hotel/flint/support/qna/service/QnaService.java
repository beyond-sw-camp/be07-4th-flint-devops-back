package com.hotel.flint.support.qna.service;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.support.qna.domain.QnA;
import com.hotel.flint.support.qna.dto.CreateQnaDto;
import com.hotel.flint.support.qna.dto.QnaDetailDto;
import com.hotel.flint.support.qna.dto.QnaListDto;
import com.hotel.flint.support.qna.dto.QnaUpdateDto;
import com.hotel.flint.support.qna.repository.QnaRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class QnaService {

    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public QnaService (QnaRepository qnaRepository, MemberRepository memberRepository) {
        this.qnaRepository = qnaRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * qna 등록
     */
    @Transactional
    public void createQnA(CreateQnaDto dto) {

        // 유효성 검사 추가
        if (dto.getService() == null) {
            throw new IllegalArgumentException("Service 선택은 필수입니다.");
        }
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title 입력은 필수입니다.");
        }
        if (dto.getContents() == null || dto.getContents().trim().isEmpty()) {
            throw new IllegalArgumentException("Contents 입력은 필수입니다.");
        }

        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();//로그인된 이메일 꺼내
        log.info("memberEmail: " + memberEmail);
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new EntityNotFoundException("해당 email에 해당하는 회원이 존재하지 않습니다.")
        );
        qnaRepository.save(dto.toEntity(member));
    }

    /**
     * qna 목록 조회 - 마이페이지에서 본인 것만
     */
    public List<QnaListDto> qnaList(Pageable pageable) {

        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new IllegalArgumentException("해당 email의 회원 없음")
        );

        // 전체 qna list 가져오기
        List<QnaListDto> allQnaList = new ArrayList<>();
        int pageNumber = 0;
        boolean hashMorePage;
        AtomicInteger start = new AtomicInteger((int) pageable.getOffset() + 1);
        do {
            pageable = PageRequest.of(pageNumber, 10);
            Page<QnA> list = qnaRepository.findByMember(pageable, member); //qna 리스트 가져오기

            allQnaList.addAll(list.stream()
                    .map(qna -> qna.listFromEntity((long) start.getAndIncrement()))
                    .collect(Collectors.toList()));
            hashMorePage = list.hasNext();
            pageNumber++;
        } while (hashMorePage);

//        Page<QnA> list = qnaRepository.findByMember(pageable, member); //qna 리스트 가져오기

//        AtomicInteger start = new AtomicInteger((int) pageable.getOffset() + 1);
//
//        List<QnaListDto> listDtos = list.stream()
//                .map(a -> a.listFromEntity((long) start.getAndIncrement()))
//                .collect(Collectors.toList());

        return allQnaList;
    }

    /**
     * qna 단건 조회 - 상세조회
     */
    public QnaDetailDto qnaDetail(Long id) {

        // 로그인된 회원 찾기
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new EntityNotFoundException("해당 email의 회원 없음")
        );
        QnA qna = qnaRepository.findByIdAndMember(id, member).orElseThrow(
                () -> new EntityNotFoundException("해당 id의 qna게시글 없음")
        );

        return qna.detailFromEntity(memberEmail);
    }

    /**
     * qna 수정
     */
    @Transactional
    public void qnaUpdate(Long id, QnaUpdateDto dto) {

        // 유효성 검사 추가
        if (dto.getService() == null) {
            throw new IllegalArgumentException("Service 선택은 필수입니다.");
        }
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title 입력은 필수입니다.");
        }
        if (dto.getContents() == null || dto.getContents().trim().isEmpty()) {
            throw new IllegalArgumentException("Contents 입력은 필수입니다.");
        }

        // 로그인된 회원 찾기
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new EntityNotFoundException("해당 email의 회원 없음")
        );
        QnA qna = qnaRepository.findByIdAndMember(id, member).orElseThrow(
                () -> new EntityNotFoundException("해당 id의 qna게시글 없음")
        );

        qna.updateFromEntity(dto);
    }

    /**
     * qna 삭제
     */
    @Transactional
    public void delete(Long id) {

        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new EntityNotFoundException("해당 email의 회원 없음")
        );
        QnA qna = qnaRepository.findByIdAndMember(id, member).orElseThrow(
                () -> new EntityNotFoundException("해당 id의 qna게시글 없음")
        );
        qnaRepository.delete(qna);
    }

}
