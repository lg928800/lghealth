package com.lg.mapper;

import com.lg.pojo.Member;

public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);
}
