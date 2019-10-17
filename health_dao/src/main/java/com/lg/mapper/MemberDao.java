package com.lg.mapper;

import com.lg.pojo.Member;
import org.apache.ibatis.annotations.Param;

public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);

    int findByMonthCount(@Param("monthBegin") String monthBegin, @Param("monthEnd") String monthEnd);
}
