package com.ilyamur.sqlbuilder;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SqlBuilder {

    static final String FROM_EXPRESSION_NOT_DEFINED = "FROM expression not defined";
    static final String SELECT_EXPRESSION_NOT_DEFINED = "SELECT expression not defined";

    private String tableName;
    private List<String> selectFieldNames = Lists.newArrayList();
    private LinkedHashMap<String, Object> whereFieldNames = Maps.newLinkedHashMap();

    public String toSql() {
        checkState(tableName != null, FROM_EXPRESSION_NOT_DEFINED);
        checkState(!selectFieldNames.isEmpty(), SELECT_EXPRESSION_NOT_DEFINED);
        String fieldNamesJoined = StringUtils.join(selectFieldNames, ", ");
        if (whereFieldNames.isEmpty()) {
            return String.format("SELECT %s FROM %s", fieldNamesJoined, tableName);
        } else {
            List<String> wherePairs = whereFieldNames.entrySet().stream()
                    .map(ent -> {
                        Object objValue = ent.getValue();
                        String strValue = (objValue instanceof String || objValue instanceof Character)
                                ? String.format("'%s'", objValue)
                                : objValue.toString();
                        return String.format("%s = %s", ent.getKey(), strValue);
                    })
                    .collect(Collectors.toList());
            String wherePairsJoined = StringUtils.join(wherePairs, " AND ");
            return String.format("SELECT %s FROM %s WHERE %s", fieldNamesJoined, tableName, wherePairsJoined);
        }
    }

    public SqlBuilder from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SqlBuilder select(String... fieldNames) {
        this.selectFieldNames.addAll(Arrays.asList(fieldNames));
        return this;
    }

    public SqlBuilder whereEquals(String fieldName, Object value) {
        this.whereFieldNames.put(fieldName, value);
        return this;
    }
}
