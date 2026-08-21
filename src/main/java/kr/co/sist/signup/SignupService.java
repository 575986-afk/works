package kr.co.sist.signup;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupService {
	
	private final SignupMapper sDAO;
	private final BCryptPasswordEncoder passwordEncoder;

	public int idDup(String id) {
		return sDAO.selectIdDup(id);
	}
	
	@Transactional
	public int insertUserJoin(UserDTO uDTO) {
		uDTO.setPassword(passwordEncoder.encode(uDTO.getPassword()));
		
		uDTO.setName(uDTO.getName());
		uDTO.setTel(uDTO.getTel());
		uDTO.setEmail(uDTO.getEmail());
		
		sDAO.insertUserJoin(uDTO);
		sDAO.insertUserRole(uDTO);
		return sDAO.insertUserChat(uDTO);
	}
	
	@Transactional
	public int insertManagerJoin(UserDTO uDTO) {
		uDTO.setPassword(passwordEncoder.encode(uDTO.getPassword()));
		
		uDTO.setName(uDTO.getName());
		uDTO.setTel(uDTO.getTel());
		uDTO.setEmail(uDTO.getEmail());
		
		sDAO.insertCompanyInfo(uDTO);
		
		sDAO.insertRank1(uDTO);
		sDAO.insertRank2(uDTO);
		sDAO.insertRank3(uDTO);
		sDAO.insertRank4(uDTO);
		
		sDAO.insertPosition1(uDTO);
		sDAO.insertPosition2(uDTO);
		sDAO.insertPosition3(uDTO);
		sDAO.insertPosition4(uDTO);
		
		sDAO.insertManagerJoin(uDTO);
		
	    sDAO.insertRole(uDTO);
	    
	    return sDAO.insertManagerChat(uDTO);
	}
}
