package com.tere.utils.db;

import java.sql.SQLException;

import com.tere.TereException;
import com.tere.builder.Builder;

public interface DatabaseBackedBuilder<C, E extends TereException> extends Builder<C, E>
{
	public abstract DatabaseBackedBuilder<C, E > fromDatabase(DatabaseUtility databaseUtility, Object... params) throws SQLException, E;
}
