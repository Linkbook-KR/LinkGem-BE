package com.linkgem.domain.link;

public interface LinkStore {

    Link create(Link link);

    Long delete(Link link);
}
