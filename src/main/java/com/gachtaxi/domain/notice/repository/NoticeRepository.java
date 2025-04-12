package com.gachtaxi.domain.notice.repository;

import com.gachtaxi.domain.notice.entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Slice<Notice> findAllBy(Pageable pageable);

}
