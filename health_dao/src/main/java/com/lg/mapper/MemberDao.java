package com.lg.mapper;

import com.lg.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);

    int findByMonthCount(@Param("monthBegin") String monthBegin, @Param("monthEnd") String monthEnd);

    Integer findByTodayMember(@Param("today")String today);

    Integer findAllMember();

    Integer findNewMemberCountAfterDate(@Param("date")String date);
}
