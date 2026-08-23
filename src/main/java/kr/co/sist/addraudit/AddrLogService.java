package kr.co.sist.addraudit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddrLogService {
	
	@Autowired
	private AddrLogMapper alm;
	
	public List<AddrLogListDomain> getAllAddrLogList(AddrLogSearchDTO search){
		return alm.selectAllAddrLog(search);
	}
}
