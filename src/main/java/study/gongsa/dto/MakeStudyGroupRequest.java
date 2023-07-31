package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import study.gongsa.domain.StudyGroup;

import javax.validation.constraints.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@ApiModel(value="StudyGroupMakeRequest", description = "스터디 그룹 생성 리퀘스트")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MakeStudyGroupRequest {
    @ApiModelProperty(value="그룹명")
    @NotBlank(message = "그룹명은 필수값 입니다")
    String name;

    @ApiModelProperty(value="캠 필수 여부")
    @NotNull(message = "캠 필수 여부는 필수값 입니다.")
    Boolean isCam;

    @ApiModelProperty(value="최대 인원 수")
    @Min(value=4, message="최대 인원 수는 4명부터 입니다.")
    @Max(value=6, message="최대 인원 수는 6명까지 입니다.")
    @NotNull(message = "최대 인원 수는 필수값 입니다.")
    int maxMember;

    @ApiModelProperty(value="방 공개 여부")
    @NotNull(message = "방 공개 여부는 필수값 입니다.")
    Boolean isPrivate;

    @ApiModelProperty(value="그룹 카테고리")
    @NotNull(message = "그룹 카테고리는 필수값 입니다")
    int[] categoryUIDs;

    @ApiModelProperty(value="벌점 유무")
    @NotNull(message = "벌점 유무는 필수값 입니다.")
    Boolean isPenalty;

    @ApiModelProperty(value="최대 가능 벌점 횟수")
    int maxPenalty;

    @ApiModelProperty(value="스터디 재진입 횟수")
    @NotNull(message = "스터디 재진입 횟수는 필수값 입니다.")
    int maxTodayStudy;

    @ApiModelProperty(value="주 목표 공부 시간")
    @Min(value=0, message="주 목표 공부 시간은 최소 0시간입니다.")
    @Max(value=80, message="주 목표 공부 시간은 최대 80시간입니다.")
    @NotNull(message = "하루 목표 공부 시간는 필수값 입니다.")
    int minStudyHour;

    @ApiModelProperty(value="만료 날짜")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "만료 날짜는 필수값 입니다.")
    Date expiredAt;

    public static StudyGroup convertToStudyGroup(MakeStudyGroupRequest req){
        StudyGroup studyGroup = StudyGroup.builder()
                .name(req.getName())
                .isCam(req.getIsCam())
                .isPrivate(req.getIsPrivate())
                .maxMember(req.getMaxMember())
                .maxTodayStudy(req.getMaxTodayStudy())
                .isPenalty(req.getIsPenalty())
                .maxPenalty(req.getMaxPenalty())
                .expiredAt(req.getExpiredAt())
                .minStudyHour(req.getMinStudyHour()+":00:00")
                .build();

        return studyGroup;
    }
}