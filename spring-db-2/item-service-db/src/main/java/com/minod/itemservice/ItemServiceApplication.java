package com.minod.itemservice;

import com.minod.itemservice.config.JpaConfig;
import com.minod.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Slf4j
//@Import(JdbcTemplateConfigV1.class) // 이게있으면 @Configuration 어노테이션 안붙여도 읽어들여짐. @Import가 더 우선순위를 가짐.
//@SpringBootApplication
//@Import(MybatisConfig.class)
@Import(JpaConfig.class)
@SpringBootApplication(scanBasePackages = "com.minod.itemservice") // 오오... 어플리케이션 스캔 범위도 설정가능.
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}


	// embaded 데이터베이스로 테스트에 사용하기
/*	@Bean
	@Profile("test")
	public DataSource dataSource() {
		log.info("메모리 데이터베이스 초기화");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
		*//** 임베디드로 H2 등록해서 사용하기
		 * H2 데이터베이스는 자바로 개발되어 있고, JVM안에서 메모리 모드로 동작하는 특별한 기능을 제공한다. 그래서 애플리케이션을 실행할 때 H2 데이터베이스도 해당 JVM 메모리에 포함해서 함께 실행할 수 있다.
		 * @Profile("test")
		 * 프로필이 test 인 경우에만 데이터소스를 스프링 빈으로 등록한다.
		 * 테스트 케이스에서만 이 데이터소스를 스프링 빈으로 등록해서 사용하겠다는 뜻이다.
		 * dataSource()
		 * jdbc:h2:mem:db : 이 부분이 중요하다. 데이터소스를 만들때 이렇게만 적으면 임베디드 모드(메모리 모드)로 동작하는 H2 데이터베이스를 사용할 수 있다.
		 * DB_CLOSE_DELAY=-1 : 임베디드 모드에서는 데이터베이스 커넥션 연결이 모두 끊어지면 데이터베이스도 종료되는데, 그것을 방지하는 설정이다.
		 * 이 데이터소스를 사용하면 메모리 DB를 사용할 수 있다.
		 *
		 * 스프링 부트 - 기본 SQL 스크립트를 사용해서 데이터베이스를 초기화하는 기능
		 * 메모리 DB는 애플리케이션이 종료될 때 함께 사라지기 때문에, 애플리케이션 실행 시점에 데이터베이스 테이블도 새로 만들어주어야 한다.
		 * JDBC나 JdbcTemplate를 직접 사용해서 테이블을 생성하는 DDL을 호출해도 되지만, 너무 불편하다. 스프링 부트는 SQL 스크립트를 실행해서 애플리케이션 로딩 시점에 데이터베이스를 초기화하는 기능을 제공한다.		 * 다음 파일을 생성하자.
		 * 위치가 src/test 이다. 이 부분을 주의하자. 그리고 파일 이름도 맞아야 한다.
		 * src/test/resources/schema.sql
		 *
		 * 신기한건 테스트환경에서 실행하는데, 왜 여기 메인에서 적어둔 설정이 테스트까지 적용되는 것인가?
		 * 결과적으로 생각하면, 테스트 환경에서 @SpringBootTest 적용시, 여기 main 실행파일을 통해 테스트를 수행한다는 소리가 된다.
		 * 또한 테스트에서 돌릴 경우 profile default가 test로 적용되는 것이다.
		 *
		 * 테스트의 application.properties도 마찬가지다.
		 * 스프링 부트는 개발자에게 정말 많은 편리함을 제공하는데, 임베디드 데이터베이스에 대한 설정도 기본으로 제공한다.
		 * 스프링 부트는 데이터베이스에 대한 별다른 설정이 없으면 임베디드 데이터베이스를 사용한다.
		 * 즉 테스트의 application.properties의 datasource를 지우면, 임베디드 데이베이스를 알아서 지원해준다는 소리다.
		 * (임베디드 데이터베이스 설정을 지우더라도 자동으로 지원해준다. 테스트 DB로 사용하는 입장에선 편리할 수 있다고 하는데, 과연 이게 편리한건가? 머냐이건 좀 무섭다.)
		 *
		 *//*

    }*/

}
