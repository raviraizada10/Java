package org.jdbc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.tcs.vfw.util.O2OLogger;
import com.tcs.vfw.util.jdbc.Impl.JDBCSessionBuilder;
import com.tcs.vfw.util.jdbc.annotation.Column;
import com.tcs.vfw.util.jdbc.annotation.Table;
import com.tcs.vfw.util.jdbc.validator.BasicRowValidator;

public final class JDBCUtil
{
	private static final O2OLogger LOGGER = O2OLogger.getLogger(JDBCUtil.class);

	private static final ConcurrentHashMap<String, SQLMetadata> SQL_METADATA_CACHE = new ConcurrentHashMap<>(100);

	/**
	 * <p>
	 * This method takes a pojo instance and creates a new entry in the
	 * database. Has been build for usage with <b>Hibernate StatelessSession</b>
	 * </p>
	 * 
	 * @param session
	 *            Hibernate session
	 * @param o
	 *            A Pojo instance Which is annotated with {@link Table}
	 *            annotaion
	 * @throws Exception
	 */

	/**
	 * <p>
	 * This method takes a pojo instance and creates a new entry in the
	 * database. Has been build for usage with <b>java.sql.Connection</b>
	 * </p>
	 * 
	 * @param conn
	 *            A java.sql.Connection instance
	 * @param o
	 *            A pojo instance Which is annotated with {@link Table}
	 *            annotaion
	 * @throws Exception
	 */
	public static void insert(final Connection conn, final Object o) throws Exception
	{
		JDBCUtil.saveData(conn, o, null);
	}

	public static void insert(final Connection conn, final List<? extends Object> objectList) throws Exception
	{
		JDBCUtil.saveData(conn, objectList, null);
	}

	/**
	 * <p>
	 * This method takes a pojo instance and creates a new entry in the
	 * database. Has been build for usage with <b>java.sql.Connection</b>. It
	 * allows for custom validations to be built while creating a new database
	 * record by passing an instance of {@link BasicRowValidator}
	 * </p>
	 * 
	 * @param conn
	 *            A java.sql.Connection instance
	 * @param o
	 *            A Pojo instance Which is annotated with {@link Table}
	 *            annotaion
	 * @param rowValidator
	 *            An instance of {@link BasicRowValidator} with custom
	 *            implementation of customRowInsertValidator
	 * @throws Exception
	 */
	public static void insert(final Connection conn, final Object o, final BasicRowValidator rowValidator) throws Exception
	{
		JDBCUtil.saveData(conn, o, rowValidator);
	}

	public static void insert(final Connection conn, final List<? extends Object> ojectList, final BasicRowValidator rowValidator) throws Exception
	{
		JDBCUtil.saveData(conn, ojectList, rowValidator);
	}

	public static void update(final Connection conn, final Object o) throws Exception
	{
		JDBCUtil.updateData(conn, o, null, null);
	}

	public static void update(final Connection conn, final List<? extends Object> o) throws Exception
	{
		JDBCUtil.updateData(conn, o, null, null, true);
	}

	public static void update(final Connection conn, final Object o, final HashSet<String> nullColumnList) throws Exception
	{
		JDBCUtil.updateData(conn, o, null, nullColumnList);
	}

	public static void update(final Connection conn, final List<? extends Object> o, final HashSet<String> nullColumnList) throws Exception
	{
		JDBCUtil.updateData(conn, o, null, nullColumnList, true);
	}

	public static void update(final Connection conn, final Object o, final BasicRowValidator rowValidator, final HashSet<String> nullColumnList) throws Exception
	{
		JDBCUtil.updateData(conn, o, rowValidator, nullColumnList);
	}

	public static void update(final Connection conn, final List<? extends Object> o, final BasicRowValidator rowValidator, final HashSet<String> nullColumnList) throws Exception
	{
		JDBCUtil.updateData(conn, o, rowValidator, nullColumnList, true);
	}

	private static void saveData(final Connection conn, final Object o, final BasicRowValidator rowValidator) throws Exception
	{
		JDBCUtil.validateInput(o);
		JDBCUtil.saveData(conn, Arrays.asList(o), rowValidator);
	}

	private static void updateData(final Connection conn, final Object o, final BasicRowValidator rowValidator, final HashSet<String> nullColumnList) throws Exception
	{
		JDBCUtil.validateInput(o);
		JDBCUtil.updateData(conn, Arrays.asList(o), rowValidator, nullColumnList, false);
	}

	private static void saveData(final Connection conn, final List<? extends Object> rows, final BasicRowValidator rowValidator) throws Exception
	{
		try
		{
			JDBCUtil.validateInput(rows);

			final Object firstObject = rows.get(0);

			final SQLMetadata data = JDBCUtil.getMetaData(firstObject.getClass());
			final String autoColumn = data.getAutoIncrementColumn();
			final List<LinkedHashMap<String, Object>> rowList = JDBCUtil.processRowsForInsert(rows, data, rowValidator);

			if (conn != null)
			{
				final JDBCInsertHelper insertHelper = new JDBCInsertHelper(data, rowList, false, null);
				insertHelper.execute(conn);
				JDBCUtil.setAutoFieldValue(rows, autoColumn, insertHelper.getAutoField());
			}
		}
		catch (final SQLException e)
		{
			throw new Exception(e);
		}
	}

	private static void updateData(final Connection conn, final List<? extends Object> o, final BasicRowValidator rowValidator, final HashSet<String> nullColumnList, final boolean bulkUpdate) throws Exception
	{
		try
		{
			JDBCUtil.validateInput(o);

			final Object firstRow = o.get(0);

			final SQLMetadata data = JDBCUtil.getMetaData(firstRow.getClass());

			final List<LinkedHashMap<String, Object>> rowList = JDBCUtil.processRowsForUpdate(o, data, rowValidator, nullColumnList, bulkUpdate);

			if (!rowList.isEmpty())
			{
				if (conn != null)
				{
					final JDBCUpdateHelper updateHelper = new JDBCUpdateHelper(data, rowList, bulkUpdate, nullColumnList);
					updateHelper.execute(conn);
				}
			}
		}
		catch (final SQLException e)
		{
			throw new Exception(e);
		}
	}

	static final List<Object> executeInsertQuery(final Connection conn, final SQLMetadata metadata, final List<LinkedHashMap<String, Object>> rows) throws Exception
	{
		HashMap<String, Integer> columnToSQLTypeMap = null;
		List<Object> returnValue = null;
		LinkedHashMap<String, Object> firstRow = null;
		try
		{
			final JDBCSession session = JDBCSessionBuilder.instance().build(conn);
			final InsertQuery insertQuery = session.prepareInsertQuery();

			firstRow = rows.get(0);

			insertQuery.setTable(metadata.getTableName());

			if (metadata.isSkipAudit())
			{
				insertQuery.skipAudit();
			}

			columnToSQLTypeMap = metadata.getColumnToSQLTypeMap();

			final Set<String> keys = firstRow.keySet();

			for (final String key : keys)
			{
				insertQuery.addColumn(key, columnToSQLTypeMap.get(key).intValue());
			}

			insertQuery.addRowsStrict(rows);

			insertQuery.executeQuery();

			final List<Long> genKeys = insertQuery.getGeneratedKeys();

			returnValue = new ArrayList<Object>(genKeys.size());

			for (final Long l : genKeys)
			{
				returnValue.add(l);
			}

			return returnValue;
		}
		catch (final Exception e)
		{
			throw new Exception(e);
		}
	}

	static final void executeUpdateQuery(final Connection conn, final SQLMetadata metadata, final List<LinkedHashMap<String, Object>> rows, final HashSet<String> nullColumnList) throws Exception
	{
		boolean firstRow = true;
		try
		{
			final JDBCSession session = JDBCSessionBuilder.instance().build(conn);
			final UpdateQuery updateQuery = session.prepareUpdateQuery();
			updateQuery.setTable(metadata.getTableName());
			updateQuery.setPrimaryKeyColumns(new HashSet<String>(Arrays.asList(metadata.getPkColumns())));
			updateQuery.setNullColumnList(nullColumnList);

			if (metadata.isSkipAudit())
			{
				updateQuery.skipAudit();
			}

			String key = null;
			for (final HashMap<String, Object> row : rows)
			{
				if (firstRow)
				{
					for (final Map.Entry<String, Object> e : row.entrySet())
					{
						key = e.getKey();
						updateQuery.addColumn(key, metadata.getColumnToSQLTypeMap().get(key).intValue());
					}
					firstRow = false;
				}
				updateQuery.addRow(row);
			}
			updateQuery.executeQuery();
		}
		catch (final Exception e)
		{
			throw new Exception(e);
		}
	}

	private static final LinkedHashMap<String, Object> processRow(final SQLMetadata metadata, final Object o) throws Exception
	{
		if (metadata == null)
		{
			throw new Exception("*** metadata CANNOT BE NULL ***");
		}

		if (o == null)
		{
			throw new Exception("*** object CANNOT BE NULL ***");
		}

		final LinkedHashMap<String, Object> row = new LinkedHashMap<>();

		final Field[] fields = metadata.getFields();

		if ((fields == null) || (fields.length == 0))
		{
			throw new Exception("*** NO FIELDS DEFINED FOR CLASS : " + metadata.getClassName() + " ***");
		}

		final HashMap<String, String> map = metadata.getFieldToColumnMap();
		String columnName = null;
		Object v = null;
		final String autoColumn = metadata.getAutoIncrementColumn();

		for (final Field field : fields)
		{
			field.setAccessible(true);
			columnName = map.get(field.getName());

			if (columnName != null)
			{
				try
				{
					v = field.get(o);

					if (columnName.equals(autoColumn) && (v == null))
					{
						continue;
					}

					row.put(columnName, v);
				}
				catch (IllegalArgumentException | IllegalAccessException e)
				{
					throw new Exception(e);
				}
			}
		}

		return row;
	}

	private static final SQLMetadata getMetaData(final Class<? extends Object> cls) throws Exception
	{
		final O2OLogger logger = JDBCUtil.LOGGER;

		if (cls == null)
		{
			throw new Exception("*** class ATTRIBUTE CANNOT BE NULL ***");
		}

		final String className = cls.getName();

		logger.debug("*** TRYING TO EXTRACT SQLMETADATA OF CLASS : " + className + " FROM CACHE ***");

		SQLMetadata metaData = JDBCUtil.SQL_METADATA_CACHE.get(className);

		if (metaData == null)
		{

			logger.debug("*** SQLMETADATA OF CLASS : " + className + " NOT FOUND IN CACHE BUILDING IT AFRESH ***");

			final Table table = cls.getAnnotation(Table.class);

			if (table == null)
			{
				throw new Exception("*** THE GIVEN OBJECT OF CLASS : " + className + " DOES NOT HAVE Table ANNOTATION ***");
			}

			String tableName = StringUtils.stripToNull(table.tableName());
			final String schema = StringUtils.stripToNull(table.schema());

			if (tableName == null)
			{
				throw new Exception("*** TABLE NAME NOT SPECIFIED FOR CLASS : " + className + " ***");
			}

			logger.debug("*** TABLENAME CORRESPONDING TO CLASS : " + className + " IS " + tableName + " ***");
			logger.debug("*** SCHEMA CORRESPONDING TO CLASS : " + className + " IS " + schema + " ***");

			if (schema != null)
			{
				tableName = schema + "." + tableName;
			}

			final String[] pkColumns = table.primaryKeyColumns();

			if ((pkColumns == null) || (pkColumns.length == 0))
			{
				throw new Exception("*** PRIMARY KEY COLUMN NOT SPECIFIED FOR CLASS : " + className + " ***");
			}

			final Field[] fields = JDBCUtil.getAllFields(cls);

			final int len = fields.length;
			String columnName = null;
			Column column = null;
			String autoIncrementColumn = null;
			String fieldName = null;
			final HashSet<String> columnSet = new HashSet<String>(len);
			final HashMap<String, String> columnToFieldMap = new HashMap<>(len);
			final HashMap<String, String> fieldToColumnMap = new HashMap<>(len);
			final HashMap<String, Integer> columnToSQLTypeMap = new HashMap<>(len);
			final HashSet<String> notNullColumns = new HashSet<>();

			boolean autoIncrement = false;
			for (final Field field : fields)
			{
				field.setAccessible(true);
				column = field.getAnnotation(Column.class);

				if (column != null)
				{
					columnName = column.columnName();
					columnSet.add(columnName);
					autoIncrement = column.autoIncrement();
					fieldName = field.getName();

					if (column.notNull())
					{
						notNullColumns.add(columnName);
					}

					if (autoIncrement)
					{
						if (autoIncrementColumn == null)
						{
							autoIncrementColumn = columnName;
						}
						else
						{
							throw new Exception("*** THERE CAN BE ONLY ONE AUTO INCREMENT COLUMN IN A CLASS, " + columnName + " HAS ALREADY BEEN DECLARED AS AUTOINCREMENT ***");
						}
					}

					columnToFieldMap.put(columnName, fieldName);
					fieldToColumnMap.put(fieldName, columnName);
					columnToSQLTypeMap.put(columnName, Integer.valueOf(column.sqlType().getActualValue()));
				}
			}

			for (final String pkColumn : pkColumns)
			{
				if (!columnSet.contains(pkColumn))
				{
					throw new Exception("*** " + pkColumn + " COLUMN MENTIONED AS PRIMARY KEY IS NOT ASSIGNED TO ANY FIELD OF CLASS : " + className + " ***");
				}
			}

			metaData = new SQLMetadata(className, tableName, pkColumns, fieldToColumnMap, columnToFieldMap, columnToSQLTypeMap, fields, autoIncrementColumn, notNullColumns, table.skipAudit());

			JDBCUtil.SQL_METADATA_CACHE.put(className, metaData);
		}
		return metaData;
	}

	public static final Field[] getAllFields(final Class<?> cls) throws Exception
	{
		final O2OLogger logger = JDBCUtil.LOGGER;
		if (cls == null)
		{
			throw new Exception("*** class ATTRIBUTE CANNOT BE NULL ***");
		}

		final List<Field> fields = new ArrayList<>();

		Class<?> currentClass = cls;
		Field[] fieldArray = null;

		while (currentClass != null)
		{
			logger.debug("*** CURRENT CLASS : " + cls.getName() + " ***");

			fieldArray = currentClass.getDeclaredFields();

			if ((fieldArray != null) && (fieldArray.length > 0))
			{
				fields.addAll(Arrays.asList(fieldArray));
			}

			logger.debug("*** CHANGING CURRENT CLASS TO ITS PARENT ***");

			currentClass = currentClass.getSuperclass();
		}

		logger.debug("*** TOTAL FIELDS FOUND ARE :" + fields.size() + " ***");

		for (final Field f : fields)
		{
			logger.debug("*** FIELD NAME :" + f.getName() + " FROM CLASS : " + f.getClass().getName() + " ***");
		}

		return fields.toArray(new Field[fields.size()]);
	}

	public static Field getField(final Object o, final String name)
	{
		final O2OLogger logger = JDBCUtil.LOGGER;

		Field field = null;
		Class<?> cls = o.getClass();

		while (cls != null)
		{
			try
			{
				field = cls.getDeclaredField(name);
			}
			catch (final NoSuchFieldException e)
			{
				logger.debug("*** FIELD " + name + " NOT FOUND IN CLASS :" + cls.getName() + " CHECKING IN ITS SUPERCLASS");
			}
			catch (final SecurityException e)
			{
				logger.debug("*** FIELD " + name + " NOT FOUND IN CLASS :" + cls.getName() + " CHECKING IN ITS SUPERCLASS");
			}

			if (field != null)
			{
				break;
			}

			cls = cls.getSuperclass();

		}
		return field;
	}

	private static LinkedHashMap<String, Object> validateRowForInsertion(BasicRowValidator rowValidator, final SQLMetadata data, final LinkedHashMap<String, Object> row) throws Exception
	{
		BasicRowValidator rowValid = rowValidator;
		if (rowValid == null)
		{
			rowValid = new BasicRowValidator();

		}

		final LinkedHashMap<String, Object> tempRow = rowValid.rowInsertValidator(row, data);

		return rowValid.customRowInsertValidator(tempRow, data);

	}

	private static LinkedHashMap<String, Object> validateRowForUpdation(BasicRowValidator rowValidator, final SQLMetadata data, final LinkedHashMap<String, Object> row, final HashSet<String> nullColumnList, final boolean forBulkUpdate) throws Exception
	{
		BasicRowValidator rowValid = rowValidator;
		if (rowValid == null)
		{
			rowValid = new BasicRowValidator();
		}

		final LinkedHashMap<String, Object> tempRow = rowValid.rowUpdateValidator(row, data, nullColumnList, forBulkUpdate);

		return rowValid.customRowUpdateValidator(tempRow, data, nullColumnList, forBulkUpdate);
	}

	private static void setAutoValue(final Object o, final String autoColumn, final Object autoFieldValue) throws Exception
	{
		final Field autoField = JDBCUtil.getField(o, autoColumn);

		autoField.setAccessible(true);

		try
		{
			autoField.set(o, autoFieldValue);
		}
		catch (final IllegalArgumentException e)
		{
			throw new Exception(e);
		}
		catch (final IllegalAccessException e)
		{
			throw new Exception(e);
		}
	}

	private static void setAutoFieldValue(final List<? extends Object> objectList, final String autoColumn, final List<Object> generatedKeys) throws Exception
	{
		if ((generatedKeys != null) && !generatedKeys.isEmpty())
		{
			final int len = objectList.size();

			for (int i = 0; i < len; i++)
			{
				JDBCUtil.setAutoValue(objectList.get(i), autoColumn, generatedKeys.get(i));
			}
		}
	}

	private static void validateInput(final Object o) throws Exception
	{
		if (o == null)
		{
			throw new Exception("*** INPUT OBJECT CANNOT BE NULL ***");
		}
		else if ((o instanceof List<?>) && ((List<?>) o).isEmpty())
		{
			throw new Exception("*** INPUT LIST CANNOT BE EMPTY ***");
		}
		else if ((o instanceof Collection<?>) && !(o instanceof List<?>))
		{
			throw new Exception("*** INPUT IS NOT AN INSTANCE OF java.util.List ***");
		}
	}

	private static List<LinkedHashMap<String, Object>> processRowsForInsert(final List<? extends Object> rows, final SQLMetadata data, final BasicRowValidator rowValidator) throws Exception
	{
		return JDBCUtil.processRows(rows, data, rowValidator, false, null, false);
	}

	private static List<LinkedHashMap<String, Object>> processRowsForUpdate(final List<? extends Object> rows, final SQLMetadata data, final BasicRowValidator rowValidator, final HashSet<String> nullColumnList, final boolean forBulkUpdate) throws Exception
	{
		return JDBCUtil.processRows(rows, data, rowValidator, true, nullColumnList, forBulkUpdate);
	}

	private static List<LinkedHashMap<String, Object>> processRows(final List<? extends Object> rows, final SQLMetadata data, final BasicRowValidator rowValidator, final boolean forUpdate, final HashSet<String> nullColumnList, final boolean forBulkUpdate) throws Exception
	{
		final List<LinkedHashMap<String, Object>> rowList = new ArrayList<>(rows.size());
		for (final Object o : rows)
		{
			rowList.add(JDBCUtil.validateRow(rowValidator, data, JDBCUtil.processRow(data, o), forUpdate, nullColumnList, forBulkUpdate));
		}
		return rowList;
	}

	private static LinkedHashMap<String, Object> validateRow(final BasicRowValidator rowValidator, final SQLMetadata data, final LinkedHashMap<String, Object> row, final boolean forUpdate, final HashSet<String> nullColumnList, final boolean forBulkUpdate) throws Exception
	{
		return (forUpdate) ? JDBCUtil.validateRowForUpdation(rowValidator, data, row, nullColumnList, forBulkUpdate) : JDBCUtil.validateRowForInsertion(rowValidator, data, row);
	}

}

interface JDBCHelper
{
	List<Object> getAutoField();
}

class JDBCInsertHelper implements JDBCHelper
{
	protected List<Object> autoField;

	protected final SQLMetadata data;

	protected final List<LinkedHashMap<String, Object>> rowList;

	protected final boolean isBulkUpdate;

	protected final HashSet<String> nullColumnList;

	public JDBCInsertHelper(final SQLMetadata data, final List<LinkedHashMap<String, Object>> rowList, final boolean isBulkUpdate, final HashSet<String> nullColumnList)
	{
		this.data = data;
		this.rowList = rowList;
		this.isBulkUpdate = isBulkUpdate;
		this.nullColumnList = nullColumnList;
	}

	public void execute(final Connection connection) throws SQLException
	{
		try
		{
			this.autoField = JDBCUtil.executeInsertQuery(connection, this.data, this.rowList);
		}
		catch (final Exception e)
		{
			throw new SQLException(e);
		}
	}

	@Override
	public List<Object> getAutoField()
	{
		return this.autoField;
	}
}

class JDBCUpdateHelper extends JDBCInsertHelper
{

	public JDBCUpdateHelper(final SQLMetadata data, final List<LinkedHashMap<String, Object>> rowList, final boolean isBulkUpdate, final HashSet<String> nullColumnList)
	{
		super(data, rowList, isBulkUpdate, nullColumnList);
	}

	@Override
	public void execute(final Connection connection) throws SQLException
	{
		try
		{
			JDBCUtil.executeUpdateQuery(connection, this.data, this.rowList, this.nullColumnList);
		}
		catch (final Exception e)
		{
			throw new SQLException(e);
		}
	}
}
