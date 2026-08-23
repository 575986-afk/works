package kr.co.sist.addraudit;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddrLogMapper {
    List<AddrLogListDomain> selectAllAddrLog(AddrLogSearchDTO search);
}