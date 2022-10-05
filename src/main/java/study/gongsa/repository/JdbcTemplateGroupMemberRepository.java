package study.gongsa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.GroupCategory;
import study.gongsa.domain.GroupMember;
import study.gongsa.domain.GroupMemberUserInfo;
import study.gongsa.domain.StudyGroup;

import javax.sql.DataSource;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcTemplateGroupMemberRepository implements GroupMemberRepository{

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertIntoGroupMember;

    @Autowired
    public JdbcTemplateGroupMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        insertIntoGroupMember = new SimpleJdbcInsert(jdbcTemplate).withTableName("GroupMember").usingGeneratedKeyColumns("UID");
    }

    @Override
    public Number save(GroupMember groupMember) {
        final Map<String, Object> parameters = setParameter(groupMember);
        return insertIntoGroupMember.executeAndReturnKey(parameters);
    }

    @Override
    public Optional<GroupMember> findByGroupUIDUserUID(int groupUID, int userUID){
        List<GroupMember> result = jdbcTemplate.query("select * from GroupMember where groupUID = ? and userUID = ?", groupMemberRowMapper(), groupUID, userUID);
        return result.stream().findAny();
    }

    @Override
    public void remove(int uid) {
        String sql = "delete from GroupMember where UID = ?";
        jdbcTemplate.update(sql, uid);
    }

    @Override
    public Optional<GroupMember> findRandUID(int groupUID){
        String sql = "SELECT * "
                    + "FROM GroupMember "
                    + "WHERE groupUID = ? "
                    + "ORDER BY RAND() "
                    + "LIMIT 1";
        List<GroupMember> result = jdbcTemplate.query(sql, groupMemberRowMapper(), groupUID);
        return result.stream().findAny();
    }

    @Override
    public void updateNewReader(int uid){
        String sql = "UPDATE GroupMember SET isLeader = 1 WHERE UID = ?";
        jdbcTemplate.update(sql, uid);
    }

    @Override
    public List<GroupMemberUserInfo> findMemberInfo(int groupUID){
        String with = "WITH MemberInfo AS ( " +
                "SELECT gm.UID AS groupMemberUID, u.UID AS userUID, u.nickname AS nickname, u.imgPath AS imgPath " +
                "FROM GroupMember gm " +
                "INNER JOIN User u ON gm.userUID = u.UID " +
                "WHERE gm.groupUID = ?) ";
        String subQuery1 = "SELECT DENSE_RANK() OVER (ORDER BY IFNULL(SUM(sm.studyTime),0) DESC) AS ranking, " +
                "IFNULL(TIME(SUM(sm.studyTime)),TIME(0)) AS totalStudyTime, mi.userUID " +
                "FROM MemberInfo mi " +
                "LEFT JOIN StudyMember sm ON sm.groupMemberUID = mi.groupMemberUID " +
                "GROUP BY mi.groupMemberUID " +
                "ORDER BY totalStudyTime desc ";
        String subQuery2 = "SELECT IFNULL(sm.studyStatus, 'stop') AS studyStatus, MAX(sm.updatedAt) AS updatedAt, " +
                "mi.userUID, mi.nickname, mi.imgPath " +
                "FROM MemberInfo mi " +
                "LEFT JOIN StudyMember sm ON sm.groupMemberUID = mi.groupMemberUID ";

        String query = with + "SELECT * FROM ( " + subQuery1 + ") AS studyTimeTable  " +
                "INNER JOIN ( "+ subQuery2 +") AS studyStatusTable ON studyTimeTable.userUID = studyStatusTable.userUID";

        List<GroupMemberUserInfo> result = jdbcTemplate.query(query, groupMemberUserInfoRowMapper(), groupUID);
        return result.stream().collect(Collectors.toList());
    }

    private RowMapper<GroupMember> groupMemberRowMapper() {
        return (rs, rowNum) -> {
            GroupMember groupMember = new GroupMember();
            groupMember.setUID(rs.getInt("UID"));
            groupMember.setGroupUID(rs.getInt("groupUID"));
            groupMember.setUserUID(rs.getInt("userUID"));
            groupMember.setPenaltyCnt(rs.getInt("penaltyCnt"));
            groupMember.setReportCnt(rs.getInt("reportCnt"));
            groupMember.setIsLeader(rs.getBoolean("isLeader"));
            groupMember.setCreatedAt(rs.getTimestamp("createdAt"));
            groupMember.setUpdatedAt(rs.getTimestamp("updatedAt"));
            return groupMember;
        };
    }

    private RowMapper<GroupMemberUserInfo> groupMemberUserInfoRowMapper() {
        return (rs, rowNum) -> {
            GroupMemberUserInfo groupMemberUserInfo = new GroupMemberUserInfo();
            groupMemberUserInfo.setUserUID(rs.getInt("userUID"));
            groupMemberUserInfo.setImgPath(rs.getString("imgPath"));
            groupMemberUserInfo.setNickname(rs.getString("nickname"));
            groupMemberUserInfo.setRanking(rs.getInt("ranking"));
            groupMemberUserInfo.setStudyStatus(rs.getString("studyStatus"));
            groupMemberUserInfo.setTotalStudyTime(rs.getTime("totalStudyTime"));

            return groupMemberUserInfo;
        };
    }

    private HashMap<String, Object> setParameter(GroupMember groupMember) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("UID", groupMember.getUID());
        hashMap.put("groupUID",groupMember.getGroupUID());
        hashMap.put("userUID",groupMember.getUserUID());
        hashMap.put("penaltyCnt",groupMember.getPenaltyCnt());
        hashMap.put("reportCnt",groupMember.getReportCnt());
        hashMap.put("isLeader",groupMember.getIsLeader());
        hashMap.put("createdAt",groupMember.getCreatedAt());
        hashMap.put("updatedAt",groupMember.getUpdatedAt());
        return hashMap;
    }
}
