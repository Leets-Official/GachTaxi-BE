package com.gachtaxi.domain.notice.service;

import com.gachtaxi.domain.notice.dto.request.NoticeCreateRequest;
import com.gachtaxi.domain.notice.dto.request.NoticeUpdateRequest;
import com.gachtaxi.domain.notice.dto.response.NoticeDTO;
import com.gachtaxi.domain.notice.entity.Notice;
import com.gachtaxi.domain.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeAdminService {

    private final NoticeFindService noticeFindService;
    private final NoticeRepository noticeRepository;

    @Transactional
    public NoticeDTO.NoticeResponse createNotice(NoticeCreateRequest dto) {
        Notice saved = noticeRepository.save(Notice.of(dto));

        return NoticeDTO.NoticeResponse.from(saved);
    }

    @Transactional
    public NoticeDTO.NoticeResponse updateNotice(Long id, NoticeUpdateRequest dto) {
        Notice notice = noticeFindService.findByNoticeId(id);
        notice.update(dto.title(), dto.content());

        return NoticeDTO.NoticeResponse.from(notice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeFindService.findByNoticeId(id);

        noticeRepository.delete(notice);
    }

}
