package com.minod.itemservice.repository.jdbctemplate;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.ItemSearchCond;
import com.minod.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBCTemplate 기본
 */

@Slf4j
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {

    // jdbcTemplete 사용 위해서, 외부에서 dataSource 주입받아야 함. jdbcTemplate 사용관례임.
    // Bean으로 등록해 DataSource 등록해도 됨.
    private final JdbcTemplate template;
    public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) values (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();  // KeyHolder? 너는뭐냐? db에서 만들어주는 id값 다음순서를 읽어오는 소스
        // JDBC Template은 람다식으로 쓴다.
        template.update( con -> {
            // 자동 증가 키를 쓰려면 keyHolder, new String[]{"id"} 필요하다고 함.
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"}); // new String[]{"id} 이게 뭔지?
            ps.setString(1, item.getItemName());
            ps.setInt(2, item.getPrice());
            ps.setInt(3, item.getQuantity());
            return ps;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();
        item.setId(key);

        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=?, price=?, quantity=? where id=?";
        template.update(sql,
                updateParam.getItemName(),
                updateParam.getPrice(),
                updateParam.getQuantity(),
                itemId);
        // 이렇게 쓰면 템플릿이 알아서 다해준다고 함.
        // 맘에 안드는건 쓰는 명령어 방식을 외워야 된다는 것.
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id = ?";
//        Item item = template.queryForObject(sql, itemRowMapper(), id);
        // 쿼리결과를 객체로 뽑는거. queryForObject
        // 쿼리 결과를 resultSet으로 받을텐데, 그것을 item 객체로 바꿔주는 기능이 없다.
        // RowMapper<T> 로 반환해주는 코드가 별도로 필요하다. 여기선 itemRowMapper() 를 만든다.
        try { // template.queryForObject() 는 결과가 없으면 예외를 만든다. try catch를 둘러주는 게 좋다.
            Item item = template.queryForObject(sql, itemRowMapper(), id);
            return Optional.of(item); // 옵셔녈 of는 값을 반환하는데 null이면 에러 발생
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // 예외 터졌다면 빈값 옵셔널 던짐.
        }

    }
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        String sql = "select id, item_name, price, quantity from item";

        // queryForObject(...)는 하나 가져올 때, query(...)는 여러개, list로 가져올때
        template.query(sql, itemRowMapper());

        // jdbcTemplate는 동적쿼리 넣을수는 있는데 별도
        //동적 쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }
        boolean andFlag = false;
        List<Object> param = new ArrayList<>();
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',?,'%')";
            param.add(itemName);
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= ?";
            param.add(maxPrice);
        }
        log.info("sql={}", sql);
        return template.query(sql, itemRowMapper(), param.toArray());
    }

    private RowMapper<Item> itemRowMapper(){
        return ((rs, rowNum) -> {
            Item item = new Item();
            item.setId(rs.getLong("id"));
            item.setItemName(rs.getString("item_name"));
            item.setPrice(rs.getInt("price"));
            item.setQuantity(rs.getInt("quantity"));
            return item;
        });
    }
}
