package source

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}

import collection.mutable._
import conn.JDBCConn

import scala.collection.mutable

/**
  * Created by fishiung on 2017-11-24 20:08
  */
class MySQLSource(val url: String, val username: String, val password: String) {
  var databaseName: String = null
  val tables = new ArrayBuffer[String]
  val tablesDesc = new HashMap[String, ArrayBuffer[String]]
  var initialized: Boolean = false

  def init(url: String, username: String, password: String):Unit= {
    var conn: Connection = null
    var ps: PreparedStatement = null
    var resultSet: ResultSet = null
    try {
      conn = JDBCConn.conn.MYSQL(url, username, password)
      databaseName = conn.getCatalog
      val getTableNameSQL = "select table_name from information_schema.tables where table_schema= ? and table_type='base table'"
      val getFieldNameAndTypeSQL = "select column_name,column_type from information_schema.columns where table_schema=? and table_name=?"

      //添加当前database的所有表名
      ps = conn.prepareStatement(getTableNameSQL)
      ps.setString(1, databaseName)
      resultSet = ps.executeQuery
      while (resultSet.next) {
        tables + resultSet.getString(1)
      }

      //添加每个表的字段名和字段类型
      ps = conn.prepareStatement(getFieldNameAndTypeSQL)
      for (table <- tables) {
        ps.clearParameters
        ps.setString(1, databaseName)
        ps.setString(2, table)
        resultSet = ps.executeQuery
        val tmp = new ArrayBuffer[String]
        while (resultSet.next()) {
          tmp + (resultSet.getString(1).toLowerCase + "\t" + resultSet.getString(2).toLowerCase)
        }
        tablesDesc.put(table, tmp)
      }
    } catch {
      case e: SQLException => e.printStackTrace()
    } finally {
      try {
        if (resultSet != null) resultSet.close
        if (ps != null) ps.close
        if (conn != null) conn.close
      } catch {
        case e: SQLException => e.printStackTrace()
      }
    }
    initialized = true
  }

  def getTableNames(): Array[String] ={
    if(!initialized) init(url,username,password)
    tables.toArray
  }

  def getTableInfo(tableName:String):(Array[String],Array[String])={
    if(!initialized) init(url,username,password)
    val tmp = tablesDesc.get(tableName)
    if(tmp != null && !tmp.isEmpty) (tmp.iterator.map(x=>))
  }
}
