package eric.oss.fls.repository.filter.builders.impl;

import javax.persistence.criteria.*;

import eric.oss.fls.repository.filter.FilterConstants;
import eric.oss.fls.repository.filter.builders.OperatorBuilder;

import java.util.List;

public class NotEqualOperatorBuilder implements OperatorBuilder {

    public Predicate build(Root root, CriteriaBuilder builder, String property, List<Object> args) {
        final Object singleArgument = args.get(0);

        if (singleArgument instanceof String) {
            return builder.notLike(root.get(property), singleArgument.toString().replace(FilterConstants.ASTERISK_CHAR, FilterConstants.PERCENT_CHAR));
        }
        if (singleArgument == null) {
            return builder.isNotNull(root.get(property));
        }
        return builder.notEqual(root.get(property), singleArgument);
    }
}
