package com.gachtaxi.domain.notice.service;

import com.gachtaxi.domain.notice.dto.response.NoticeDTO;
import com.gachtaxi.domain.notice.dto.response.NoticeDTO.NoticeResponse;
import com.gachtaxi.domain.notice.dto.response.NoticeListResponse;
import com.gachtaxi.domain.notice.dto.response.NoticePageableResponse;
import com.gachtaxi.domain.notice.entity.Notice;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeFindService noticeFindService;

    @Transactional(readOnly = true)
    public NoticeDTO.NoticeResponse getNoticeDetail(Long noticeId) {
        Notice notice = noticeFindService.findByNoticeId(noticeId);

        return NoticeDTO.NoticeResponse.from(notice);
    }

    @Transactional(readOnly = true)
    public NoticeListResponse getNoticeListResponse(int pageNumber, int pageSize) {
        Slice<NoticeDTO.NoticeResponse> noticeSlice = getNoticeList(pageNumber, pageSize);
        List<NoticeResponse> noticeList = noticeSlice.getContent().stream().toList();
        NoticePageableResponse pageableResponse = NoticePageableResponse.of(noticeSlice);

        return NoticeListResponse.of(noticeList, pageableResponse);
    }

    private Slice<NoticeDTO.NoticeResponse> getNoticeList(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Slice<Notice> noticeSlice = noticeFindService.findAllNotices(pageable);

        return noticeSlice.map(NoticeDTO.NoticeResponse::from);
    }

}
