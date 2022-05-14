package aptech.t2008m.shoppingdemo.generator;

import aptech.t2008m.shoppingdemo.entity.Product;
import aptech.t2008m.shoppingdemo.until.StringHelper;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.stream.Stream;

public class IdGenerator implements IdentifierGenerator {
    private String prefix;
    private String tableName;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {

        String query = String.format("select %s from %s",
                session.getEntityPersister(obj.getClass().getName(), obj).getIdentifierPropertyName(),
                tableName);

        Stream<?> ids = session.createQuery(query).stream();

        long max = ids.map(o -> String.valueOf(o).replace(prefix + "_", ""))
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0L);

        if (obj.getClass().getSimpleName().equals("Product")){
            Product product = (Product) obj;
            product.setSlug(StringHelper.toSlug(product.getName() + " " + prefix + "_" + (max + 1)));
        }

        return prefix + "_" + (max + 1);
    }

    @Override
    public void configure(Type type, Properties properties,
                          ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("prefix");
        tableName = properties.getProperty("tableName");
    }
}
