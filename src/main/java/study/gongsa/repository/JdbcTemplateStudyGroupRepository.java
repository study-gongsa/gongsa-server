package study.gongsa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.StudyGroup;
import study.gongsa.domain.UserCategory;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JdbcTemplateStudyGroupRepository implements StudyGroupRepository{
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertIntoStudyGroupAuth;

    @Autowired
    public JdbcTemplateStudyGroupRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        insertIntoStudyGroupAuth = new SimpleJdbcInsert(jdbcTemplate).withTableName("StudyGroup").usingGeneratedKeyColumns("UID");
    }


    @Override
    public List<StudyGroup> findAll(List<Integer> categoryUIDs, String word, Boolean isCam, String align) {
        String sql = "select * "
                    +"from StudyGroup a "
                    +"join GroupCategory b on a.UID = b.groupUID "
                    +"where a.UID >= 1 ";
        if(isCam != null)
            sql += "and isCam = " + isCam + " ";
        if(word.length() != 0)
            sql += "and (a.code LIKE \"%" + word + "%\" or a.name LIKE \"" + word + "%\")";
        if(categoryUIDs != null){
            sql += "and b.categoryUID in (";
            for(int i=0; i<categoryUIDs.size(); i++){
                sql += categoryUIDs.get(i).toString();
                if(i != categoryUIDs.size()-1)
                    sql += ",";
            }
            sql += ")";
        }

        sql += "group by b.groupUID";
        System.out.println(sql);

        return jdbcTemplate.query(sql, studyGroupRowMapper());
    }

    @Override
    public List<StudyGroup> findSameCategoryAllByUID(int uid) {
        String sql = "select * "
                    +"from StudyGroup a "
                    +"join GroupCategory b on a.UID = b.groupUID "
                    +"where categoryUID in (select categoryUID from GroupCategory where groupUID = ?) "
                    +"group by b.groupUID";
        return jdbcTemplate.query(sql, studyGroupRowMapper(), uid);
    }

    @Override
    public List<StudyGroup> findSameCategoryAllByUserUID(int userUID) {
        String sql = "select * "
                +"from StudyGroup a "
                +"join GroupCategory b on a.UID = b.groupUID "
                +"where categoryUID in (select categoryUID from UserCategory where userUID = ?) "
                +"group by b.groupUID";
        return jdbcTemplate.query(sql, studyGroupRowMapper(), userUID);
    }

    private RowMapper<StudyGroup> studyGroupRowMapper() {
        return (rs, rowNum) -> {
            StudyGroup studyGroup = new StudyGroup();
            studyGroup.setUID(rs.getInt("UID"));
            studyGroup.setName(rs.getString("name"));
            studyGroup.setCode(rs.getString("code"));
            studyGroup.setMaxTodayStudy(rs.getInt("maxTodayStudy"));
            studyGroup.setMaxMember(rs.getInt("maxMember"));
            studyGroup.setMaxPenalty(rs.getInt("maxPenalty"));
            studyGroup.setMaxRest(rs.getInt("maxRest"));
            studyGroup.setPrivate(rs.getBoolean("isPrivate"));
            studyGroup.setCam(rs.getBoolean("isCam"));
            studyGroup.setRest(rs.getBoolean("isRest"));
            studyGroup.setPenalty(rs.getBoolean("isPenalty"));
            studyGroup.setCreatedAt(rs.getTimestamp("createdAt"));
            studyGroup.setExpireDate(rs.getTimestamp("expireDate"));

            return studyGroup;
        };
    }
}
