package study.gongsa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.Category;
import study.gongsa.domain.GroupCategory;
import study.gongsa.domain.User;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcTemplateGroupCategoryRepository implements GroupCategoryRepository{
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertIntoGroupCategory;

    @Autowired
    public JdbcTemplateGroupCategoryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        insertIntoGroupCategory = new SimpleJdbcInsert(jdbcTemplate).withTableName("GroupCategory").usingGeneratedKeyColumns("UID");
    }

    @Override
    public Number save(GroupCategory groupCategory) {
        final Map<String, Object> parameters = setParameter(groupCategory);
        return insertIntoGroupCategory.executeAndReturnKey(parameters);
    }

    private RowMapper<GroupCategory> groupCategoryRowMapper() {
        return (rs, rowNum) -> {
            GroupCategory groupCategory = new GroupCategory();
            groupCategory.setUID(rs.getInt("UID"));
            groupCategory.setGroupUID(rs.getInt("groupUID"));
            groupCategory.setCategoryUID(rs.getInt("categoryUID"));
            groupCategory.setCreatedAt(rs.getTimestamp("createdAt"));
            groupCategory.setUpdatedAt(rs.getTimestamp("updatedAt"));
            return groupCategory;
        };
    }

    private HashMap<String, Object> setParameter(GroupCategory groupCategory) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("UID", groupCategory.getUID());
        hashMap.put("groupUID",groupCategory.getGroupUID());
        hashMap.put("categoryUID",groupCategory.getCategoryUID());
        hashMap.put("createdAt",groupCategory.getCreatedAt());
        hashMap.put("updatedAt",groupCategory.getUpdatedAt());
        return hashMap;
    }
}
