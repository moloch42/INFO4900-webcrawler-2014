package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Entity {

	private State state;

	public State getState() {
		return state;
	}

	protected void setState(State state) {
		this.state = state;
	}

	protected enum State {
		added, modified, deleted, unchanged;
	}

	protected Entity() {
		setState(State.added);
	}

	protected Entity(java.sql.Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
		this.load(pConn, pintEntityID, pblnIsLoadRecursive);
		setState(State.unchanged);
	}

	public abstract void load(java.sql.Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive);

	// protected abstract void loadReferences(java.sql.Connection pConn);

	public int save(java.sql.Connection pConn) {
		int intResult = 0;
		switch (state) {
		case added:
			intResult += insert(pConn);
			break;
		case modified:
			intResult += update(pConn);
			break;
		case deleted:
			intResult += delete(pConn);
			break;
		case unchanged:
		}
		return intResult + saveReferences(pConn);
	}

	protected abstract int saveReferences(Connection pConn);

	protected abstract int update(Connection pConn);

	protected abstract int insert(Connection pConn);

	protected abstract int delete(Connection pConn);

	protected ResultSet executeQuery(java.sql.Connection pConn, String strStatement) {
		Statement stmtNew = null;
		ResultSet rsResult = null;
		try {
			stmtNew = pConn.createStatement();
			rsResult = stmtNew.executeQuery(strStatement);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmtNew != null) {
					stmtNew.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rsResult;
	}

	protected int executeInsert(java.sql.Connection pConn, String strStatement) {
		Statement stmtNew = null;
		ResultSet rsGenKeys = null;
		int intGenKey = 0;
		try {
			stmtNew = pConn.createStatement();
			stmtNew.executeUpdate(strStatement, Statement.RETURN_GENERATED_KEYS);
			rsGenKeys = stmtNew.getGeneratedKeys();

			if (rsGenKeys.first()) {
				intGenKey = rsGenKeys.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmtNew != null) {
					stmtNew.close();
				}
				if (rsGenKeys != null) {
					rsGenKeys.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return intGenKey;
	}

	protected int executeUpdate(java.sql.Connection pConn, String strStatement) {
		Statement stmtNew = null;
		int intResult = 0;
		try {
			stmtNew = pConn.createStatement();
			intResult = stmtNew.executeUpdate(strStatement);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmtNew != null) {
					stmtNew.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return intResult;
	}
	
	@Override
	public String toString() {
		return "Entity:" + state.toString();
	}
}
