package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.sql.Date;
import java.sql.Timestamp;

@ApiModel(value="StudyGroupMakeRequest", description = "스터디 그룹 생성 리퀘스트")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MakeStudyGroupRequest {
    @ApiModelProperty(value="그룹명")
    @NotBlank(message = "그룹명은 필수값 입니다")
    String name;

    @ApiModelProperty(value="캠 필수 여부")
    @NotNull(message = "캠 필수 여부는 필수값 입니다.")
    boolean isCam;

    @ApiModelProperty(value="최대 인원 수")
    @Min(value=4, message="최대 인원 수는 4명부터 입니다.")
    @Max(value=6, message="최대 인원 수는 6명까지 입니다.")
    @NotNull(message = "최대 인원 수는 필수값 입니다.")
    int maxMember;

    @ApiModelProperty(value="방 공개 여부")
    @NotNull(message = "방 공개 여부는 필수값 입니다.")
    boolean isPrivate;

    @ApiModelProperty(value="그룹 카테고리")
    @NotNull(message = "그룹 카테고리는 필수값 입니다")
    int[] categoryUIDs;

    @ApiModelProperty(value="벌점 유무")
    @NotNull(message = "벌점 유무는 필수값 입니다.")
    boolean isPenalty;

    @ApiModelProperty(value="최대 가능 벌점 횟수")
    int maxPenalty;

    @ApiModelProperty(value="휴가 유무")
    @NotNull(message = "휴가 유무는 필수값 입니다.")
    boolean isRest;

    @ApiModelProperty(value="최대 휴가 횟수")
    int maxRest;

    @ApiModelProperty(value="스터디 재진입 횟수")
    @NotNull(message = "스터디 재진입 횟수는 필수값 입니다.")
    int maxTodayStudy;

    @ApiModelProperty(value="하루 목표 공부 시간")
    @Min(value=0, message="하루 목표 공부 시간은 최소 0시간입니다.")
    @Max(value=24, message="하루 목표 공부 시간은 최대 24시간입니다.")
    @NotNull(message = "하루 목표 공부 시간는 필수값 입니다.")
    int minStudyHour;

    @ApiModelProperty(value="만료 날짜")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "만료 날짜는 필수값 입니다.")
    Date expiredAt;
}