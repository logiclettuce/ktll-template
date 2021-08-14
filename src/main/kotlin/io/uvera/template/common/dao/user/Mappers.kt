package io.uvera.template.common.dao.user

import io.uvera.template.security.configuration.RoleEnum
import io.uvera.template.util.extensions.getColumn
import io.uvera.template.util.extensions.getRow
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.result.LinkedHashMapRowReducer
import org.jdbi.v3.core.result.RowView
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class UserRowReducer : LinkedHashMapRowReducer<Int, User> {
    override fun accumulate(container: MutableMap<Int, User>, rowView: RowView) {
        val user = container.computeIfAbsent(
            rowView.getColumn("u_id")
        ) { rowView.getRow() }

        rowView.getColumn<Int?>("ur_id")?.let {
            user.roleList.add(rowView.getColumn("ur_role"))
        }
    }
}

class RoleEnumColumnMapper : ColumnMapper<RoleEnum> {
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext): RoleEnum =
        RoleEnum.valueOf(r.getString("ur_role"))
}

