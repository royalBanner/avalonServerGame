package net.sf.l2j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class L2DatabaseFactory
{
	static Logger _log = Logger.getLogger(L2DatabaseFactory.class.getName());
	
	public static enum ProviderType
	{
		MySql,
		MsSql
	}
	
	private static L2DatabaseFactory _instance;
	private ProviderType _providerType;
	private ComboPooledDataSource _source;
	
	public L2DatabaseFactory() throws SQLException
	{
		try
		{
			if (Config.DATABASE_MAX_CONNECTIONS < 2)
			{
				Config.DATABASE_MAX_CONNECTIONS = 2;
				_log.warning("at least " + Config.DATABASE_MAX_CONNECTIONS + " db connections are required.");
			}
			
			_source = new ComboPooledDataSource();
			_source.setAutoCommitOnClose(true);
			
			_source.setInitialPoolSize(10);
			_source.setMinPoolSize(10);
			_source.setMaxPoolSize(Config.DATABASE_MAX_CONNECTIONS);
			
			_source.setAcquireRetryAttempts(0); // try to obtain connections indefinitely (0 = never quit)
			_source.setAcquireRetryDelay(500); // 500 milliseconds wait before try to acquire connection again
			_source.setCheckoutTimeout(0); // 0 = wait indefinitely for new connection
			// if pool is exhausted
			_source.setAcquireIncrement(5); // if pool is exhausted, get 5 more connections at a time
			// cause there is a "long" delay on acquire connection
			// so taking more than one connection at once will make connection pooling
			// more effective.
			
			// this "connection_test_table" is automatically created if not already there
			_source.setAutomaticTestTable("connection_test_table");
			_source.setTestConnectionOnCheckin(false);
			
			// testing OnCheckin used with IdleConnectionTestPeriod is faster than testing on checkout
			
			_source.setIdleConnectionTestPeriod(3600); // test idle connection every 60 sec
			_source.setMaxIdleTime(0); // 0 = idle connections never expire
			// *THANKS* to connection testing configured above
			// but I prefer to disconnect all connections not used
			// for more than 1 hour
			
			// enables statement caching, there is a "semi-bug" in c3p0 0.9.0 but in 0.9.0.2 and later it's fixed
			_source.setMaxStatementsPerConnection(100);
			
			_source.setBreakAfterAcquireFailure(false); // never fail if any way possible
			// setting this to true will make
			// c3p0 "crash" and refuse to work
			// till restart thus making acquire
			// errors "FATAL" ... we don't want that
			// it should be possible to recover
			// _source.setDriverClass(Config.DATABASE_DRIVER);
			_source.setJdbcUrl(Config.DATABASE_URL);
			_source.setUser(Config.DATABASE_LOGIN);
			_source.setPassword(Config.DATABASE_PASSWORD);
			
			/* Test the connection */
			_source.getConnection().close();
			
			if (Config.DEBUG)
			{
				_log.fine("Database Connection Working");
			}
			
			if (Config.DATABASE_DRIVER.toLowerCase().contains("microsoft"))
			{
				_providerType = ProviderType.MsSql;
			}
			else
			{
				_providerType = ProviderType.MySql;
			}
		}
		catch (SQLException x)
		{
			if (Config.DEBUG)
			{
				_log.fine("Database Connection FAILED");
			}
			throw x;
		}
		catch (Exception e)
		{
			if (Config.DEBUG)
			{
				_log.fine("Database Connection FAILED");
			}
			throw new SQLException("could not init DB connection:" + e);
		}
	}
	
	public final String prepQuerySelect(String[] fields, String tableName, String whereClause, boolean returnOnlyTopRecord)
	{
		String msSqlTop1 = "";
		String mySqlTop1 = "";
		if (returnOnlyTopRecord)
		{
			if (getProviderType() == ProviderType.MsSql)
			{
				msSqlTop1 = " Top 1 ";
			}
			if (getProviderType() == ProviderType.MySql)
			{
				mySqlTop1 = " Limit 1 ";
			}
		}
		String query = "SELECT " + msSqlTop1 + safetyString(fields) + " FROM " + tableName + " WHERE " + whereClause + mySqlTop1;
		return query;
	}
	
	public void shutdown()
	{
		try
		{
			_source.close();
		}
		catch (Exception e)
		{
			_log.log(Level.INFO, "", e);
		}
		try
		{
			_source = null;
		}
		catch (Exception e)
		{
			_log.log(Level.INFO, "", e);
		}
	}
	
	public final String safetyString(String[] whatToCheck)
	{
		// NOTE: Use brace as a safety precaution just in case name is a reserved word
		String braceLeft = "`";
		String braceRight = "`";
		if (getProviderType() == ProviderType.MsSql)
		{
			braceLeft = "[";
			braceRight = "]";
		}
		
		String result = "";
		for (String word : whatToCheck)
		{
			if (result != "")
			{
				result += ", ";
			}
			result += braceLeft + word + braceRight;
		}
		return result;
	}
	
	public static L2DatabaseFactory getInstance() throws SQLException
	{
		if (_instance == null)
		{
			_instance = new L2DatabaseFactory();
		}
		return _instance;
	}
	
	public Connection getConnection()
	{
		Connection con = null;
		
		while (con == null)
		{
			try
			{
				con = _source.getConnection();
			}
			catch (SQLException e)
			{
				_log.warning("L2DatabaseFactory: getConnection() failed, trying again " + e);
			}
		}
		return con;
	}
	
	public int getBusyConnectionCount() throws SQLException
	{
		return _source.getNumBusyConnectionsDefaultUser();
	}
	
	public int getIdleConnectionCount() throws SQLException
	{
		return _source.getNumIdleConnectionsDefaultUser();
	}
	
	public final ProviderType getProviderType()
	{
		return _providerType;
	}
}