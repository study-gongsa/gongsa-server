package study.gongsa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.GroupCategory;
import study.gongsa.domain.GroupMember;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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

    private RowMapper<GroupMember> groupCategoryRowMapper() {
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
