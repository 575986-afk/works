package kr.co.sist.login;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kr.co.sist.signup.AESUtil; // AESUtil 클래스가 있는 패키지 경로에 맞게 임포트

@MappedTypes(String.class)
public class EncryptTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        String encrypted = encrypt(parameter);
        ps.setString(i, encrypted);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String encrypted = rs.getString(columnName);
        return decrypt(encrypted);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String encrypted = rs.getString(columnIndex);
        return decrypt(encrypted);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String encrypted = cs.getString(columnIndex);
        return decrypt(encrypted);
    }

    private String encrypt(String value) {
        if (value == null) return null;
        try {
            return AESUtil.encrypt(value); 
        } catch (Exception e) {
            throw new RuntimeException("암호화 실패", e);
        }
    }

    private String decrypt(String value) {
        if (value == null) return null;
        try {
            // ★ 디버깅용 출력
            String decrypted = AESUtil.decrypt(value);
            return decrypted; 
        } catch (Exception e) {
            throw new RuntimeException("복호화 실패", e);
        }
    }
}