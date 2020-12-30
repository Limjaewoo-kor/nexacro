package egovframework.nexacro.jw.service.impl;


import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("jwMapper")
public interface JwMapper {
	//데이터 조회
    List<Map<String,Object>> selectjw(Map<String,String> param) throws Exception;
    //데이터 등록 수정 삭제
    int insertjw(Map<String,String> param);
    int insertsub(Map<String, String> param);
    void insertfile(String name);
	
    int updatejw(Map<String,String> param);
    int updatesub(Map<String,String> param);
    
	int deletejw(String param) throws Exception;
	int deletejw_pro(String param);

	 List<Map<String,Object>> gridsel(String param) throws Exception;

	 List<Map<String,Object>> pro_up_sel(Map<String, String> param) throws Exception;

		List<Map<String, Object>> selfile();
		
		List<Map<String,Object>> loginid(Map<String,String> param);
}