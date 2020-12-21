package egovframework.nexacro.jw.service.impl;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.nexacro.jw.service.JwService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("jwService")
public class JwServiceImpl extends EgovAbstractServiceImpl implements JwService {
  private Logger logger = LoggerFactory.getLogger(JwServiceImpl.class);
  
    @Resource(name = "jwMapper")
    private JwMapper jwMapper;
    
    @Override
  public List<Map<String,Object>> selectjw(Map<String,String> param) throws Exception {
    return jwMapper.selectjw(param);
  }
    

	@Override
	public int insertjw(Map<String, String> param) throws Exception {
		return jwMapper.insertjw(param);
	}
	
	@Override
	public int insertsub(Map<String, String> param) {
		return jwMapper.insertsub(param);
		}
	
	
	@Override
	public int updatejw(Map<String, String> param) throws Exception {
		return jwMapper.updatejw(param);
	}

	@Override
	public int updatesub(Map<String, String> param) throws Exception {
		return jwMapper.updatesub(param);
	}


	@Override
	public int deletejw(String param) throws Exception {
		 return jwMapper.deletejw(param);
	}


	@Override
	public List<Map<String, Object>> gridsel(String param) throws Exception {
		 return jwMapper.gridsel(param);
	  }


	@Override
	public List<Map<String, Object>> pro_up_sel(Map<String, String> param)throws Exception {
		return jwMapper.pro_up_sel(param);
	}


	@Override
	public int deletejw_pro(String param) {
		 return jwMapper.deletejw_pro(param);
			}


	@Override
	public void insertfile(String name)  {
		jwMapper.insertfile(name);
		
	}


	@Override
	public List<Map<String, Object>> selfile() {
		return jwMapper.selfile();
	}
	

}