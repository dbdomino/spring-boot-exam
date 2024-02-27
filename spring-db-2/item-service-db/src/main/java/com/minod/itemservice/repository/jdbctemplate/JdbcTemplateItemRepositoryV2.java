package com.minod.itemservice.repository.jdbctemplate;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.ItemSearchCond;
import com.minod.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JDBCTemplate - 파라미터 물음표 순서 말고, 이름으로 바인딩(JdbcTemplate말고 MyBatis 에서도 이런방법이 있음.)
 * NamedParameterJdbcTemplate ;
 *
 * 변경1 : BeanPropertySqlParameterSource 만들어서 template.update(sql, param, keyHolder); 등록
 * 변경2 : SqlParameterSource 맵 만들어서 update(sql, paramMap); 수정
 * 변경3 : Map<String, Object> param = Map.of("id", id); 맵 만들어서 template.queryForObject(sql, param, itemRowMapper());
 * 변경4 : BeanPropertyRowMapper로 RowMapper 직접구현을 객체에 바로 담기도록 수정( 엔티티 필드명 이 객체의 맴버변수 명에 일치하고 포함가능하면)
 *
 *
 */

@Slf4j
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

    // jdbcTemplete 사용 위해서, 외부에서 dataSource 주입받아야 함. jdbcTemplate 사용관례임.
    // 파라미터 물음표 순서 말고, 이름으로 바인딩 NamedParameterJdbcTemplate; // 이름길어서 쓰기싫다. sql도 다바꿔야한다.
    private final JdbcTemplate template2;
    private final NamedParameterJdbcTemplate template;
    public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.template2 = new JdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
//        String sql = "insert into item(item_name, price, quantity) values (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); // KeyHolder? 너는뭐냐? db에서 만들어주는 id값 다음순서를 읽어오는 소스

        String sql = "insert into item(item_name, price, quantity) values (:itemName, :price, :quantity)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(item); // 파라미터로 바인딩, 물음표 빼고
        template.update(sql, param, keyHolder); // jdbcTemplate update 기존과 다르게 적용하기.
        // 나는 이런걸 불편하다고 함. 공부해야되니까. 그러나 다른사람은 편하다고 함. 코드 짧아지니까. 이런 방식이 편해지도록 하자.

        // JDBC Template은 람다식으로 쓴다.
/*
        template2.update( con -> {
            // 자동 증가 키를 쓰려면 keyHolder, new String[]{"id"} 필요하다고 함.
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"}); // new String[]{"id} 이게 뭔지?
            ps.setString(1, item.getItemName());
            ps.setInt(2, item.getPrice());
            ps.setInt(3, item.getQuantity());
            return ps;
        }, keyHolder);
*/

//        long key = keyHolder.getKey().longValue(); // 쿼리 수행은 되는데, NamedParameterJdbcTemplate로 수행시 keyHolder 맵에 키가 id뿐만 아니라 여러개가 들어간다.
        Long key = (Long) keyHolder.getKeys().get("id"); // 이렇게 수정해줌.
        item.setId(key);

        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";
        SqlParameterSource param = new MapSqlParameterSource() // 맵을 만듬.
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId); //이 부분이 별도로 필요해서 BeanPropertySqlParameterSource 안씀.
        template.update(sql, param);

/*        String sql = "update item set item_name=?, price=?, quantity=? where id=?";
        template.update(sql,
                updateParam.getItemName(),
                updateParam.getPrice(),
                updateParam.getQuantity(),
                itemId);*/
        // 이렇게 쓰면 템플릿이 알아서 다해준다고 함.
        // 맘에 안드는건 쓰는 명령어 방식을 외워야 된다는 것.
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id = :id";
        /**
         * 그런데 select item_name 의 경우 setItem_name() 이라는 메서드가 없기 때문에 골치가 아프다.
         * 이런 경우 개발자가 조회 SQL을 다음과 같이 고치면 된다.
         * select item_name as itemName
         * 별칭 as 를 사용해서 SQL 조회 결과의 이름을 변경하는 것이다. 실제로 이 방법은 자주 사용된다.
         */
        try {
            Map<String, Object> param = Map.of("id", id); // 맵 추가
            Item item = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

/*        String sql = "select id, item_name, price, quantity from item where id = ?";
        try { // template.queryForObject() 는 결과가 없으면 예외를 만든다. try catch를 둘러주는 게 좋다.
            Item item = template.queryForObject(sql, itemRowMapper(), id);
            return Optional.of(item); // 옵셔녈 of는 값을 반환하는데 null이면 에러 발생
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // 예외 터졌다면 빈값 옵셔널 던짐.
        }*/

    }
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        // 추가 BeanPropertySqlParameterSource
        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        String sql = "select id, item_name, price, quantity from item order by id desc ";

        // queryForObject(...)는 하나 가져올 때, query(...)는 여러개, list로 가져올때
        template.query(sql, itemRowMapper());

        // jdbcTemplate는 동적쿼리 넣을수는 있는데 별도
        //동적 쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }
        boolean andFlag = false;
//        List<Object> param = new ArrayList<>();
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',:itemName,'%')";
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }
        log.info("sql={}", sql);
        return template.query(sql, param, itemRowMapper()); // 쿼리 다른방식으로 수행, 1개이상의 결과 리스트로 받기.
    }

    private RowMapper<Item> itemRowMapper(){
        // 스프링이 제공하는 방법, 아래는 객체에 그대로 매핑 구현해야함. 필드명과 객체맴버변수 명이 같아 코드중복느낌 남.
        // 필드명과 객체맴버변수 명이 같으면 아래처럼 대체 가능. 객체T 로 값을 주입하고 RowMapper<T> 로 리턴함.
        return BeanPropertyRowMapper.newInstance(Item.class); // camel 변환 지원

        /*return ((rs, rowNum) -> {
            Item item = new Item();
            item.setId(rs.getLong("id"));
            item.setItemName(rs.getString("item_name"));
            item.setPrice(rs.getInt("price"));
            item.setQuantity(rs.getInt("quantity"));
            return item;
        });*/
    }
}
