package egovframework.nexacro.jw.service;

import java.util.List;
import java.util.Map;

public interface JwService {
  //데이터 조회
    List<Map<String,Object>> selectjw(Map<String,String> param) throws Exception;
    

    //데이터 추가/수정/삭제
	int insertjw(Map<String, String> param) throws Exception;
	int insertsub(Map<String, String> param);
	 void insertfile(String name);
	
	int updatejw(Map<String,String> param) throws Exception;
	int updatesub(Map<String,String> param) throws Exception;

	int deletejw(String param) throws Exception;
	int deletejw_pro(String param);

	 List<Map<String,Object>> gridsel(String param) throws Exception;
	 List<Map<String,Object>> pro_up_sel(Map<String, String> param) throws Exception;


	List<Map<String, Object>> selfile();

	List<Map<String,Object>> loginid(Map<String,String> param);

	
}


	