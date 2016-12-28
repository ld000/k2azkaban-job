package job.common;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;

/**
 * @author lidong 12/1/16.
 */
public class PersistenceHelper {

    public static SqlSessionFactory getSqlSessionFactory() {
        DataSource dataSource = new PooledDataSource("com.mysql.jdbc.Driver",
                "jdbc:mysql://192.168.130.64:3306/lingong?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true",
                "root",
                "root");
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);

        configuration.addMappers("com.k2data.job.mapper");

        return new SqlSessionFactoryBuilder().build(configuration);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getMapper(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new MapperProxyHandler<T>(clazz));
    }

}
