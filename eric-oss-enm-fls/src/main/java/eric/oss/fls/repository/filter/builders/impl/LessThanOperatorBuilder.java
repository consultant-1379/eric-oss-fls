package eric.oss.fls.repository.filter.builders.impl;

import javax.persistence.criteria.*;

import eric.oss.fls.repository.filter.builders.OperatorBuilder;

import java.util.List;

public class LessThanOperatorBuilder implements OperatorBuilder {

    public Predicate build(Root root, CriteriaBuilder builder, String property, List<Object> args) {
        final Object singleArgument = args.get(0);
        return builder.lessThan(root.get(property), (Comparable) singleArgument);
    }
}
