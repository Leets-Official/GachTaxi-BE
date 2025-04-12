package com.gachtaxi.domain.notice.service;


import com.gachtaxi.domain.notice.entity.Notice;
import com.gachtaxi.domain.notice.exception.NoticeNotFoundException;
import com.gachtaxi.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeFindService {

    private final NoticeRepository noticeRepository;

    public Notice findByNoticeId(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(NoticeNotFoundException::new);
    }

    public Slice<Notice> findAllByNotices(Pageable pageable) {
        return noticeRepository.findAllBy(pageable);
    }

}
