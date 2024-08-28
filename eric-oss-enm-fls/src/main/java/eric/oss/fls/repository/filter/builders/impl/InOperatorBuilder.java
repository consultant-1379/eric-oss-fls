package eric.oss.fls.repository.filter.builders.impl;

import javax.persistence.criteria.*;

import eric.oss.fls.repository.filter.builders.OperatorBuilder;

import java.util.List;

public class InOperatorBuilder implements OperatorBuilder {

    public Predicate build(Root root, CriteriaBuilder builder, String property, List<Object> args) {
        return root.get(property).in(args);
    }
}