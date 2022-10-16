package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@ApiModel(value="GetMyStudyGroupRankingResponse", description = "내 스터디 그룹 랭킹 조회")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetMyStudyGroupRankResponse {
    List<GroupRank> groupRankList;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupRank{
        int groupUID;
        String name;
        List<GroupMemberResponse.Member> members;
    }
}
