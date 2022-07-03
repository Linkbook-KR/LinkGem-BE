package com.linkgem.domain.link;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LinkReader {

    Page<LinkInfo.Search> findAll(Long userId, Pageable pageable);

}
