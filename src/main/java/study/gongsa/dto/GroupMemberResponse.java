package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import study.gongsa.domain.GroupMember;
import study.gongsa.domain.GroupMemberUserInfo;
import study.gongsa.domain.StudyGroup;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@ApiModel(value="getGroupMemberReseponse", description = "스터디그룹 멤버 정보")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberResponse {
    int maxMember;
    List<Member> members;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Member{
       Integer userUID;
       String nickname;
       String imgPath;
       String studyStatus;
       Time totalStudyTime;
       Integer ranking;

       public static Member convertToMember(GroupMemberUserInfo groupMemberUserInfo){
           Member member = Member.builder()
                   .userUID(groupMemberUserInfo.getUserUID())
                   .nickname(groupMemberUserInfo.getNickname())
                   .imgPath(groupMemberUserInfo.getImgPath())
                   .studyStatus(groupMemberUserInfo.getStudyStatus())
                   .totalStudyTime(groupMemberUserInfo.getTotalStudyTime())
                   .ranking(groupMemberUserInfo.getRanking())
                   .build();
           return member;
       }
    }
}
