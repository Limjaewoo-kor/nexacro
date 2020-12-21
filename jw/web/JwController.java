package egovframework.nexacro.jw.web;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexacro.uiadapter17.spring.core.annotation.ParamDataSet;
import com.nexacro.uiadapter17.spring.core.annotation.ParamVariable;
import com.nexacro.uiadapter17.spring.core.data.NexacroResult;

import egovframework.nexacro.jw.service.JwService;

@Controller
public class JwController {
  private Logger logger = LoggerFactory.getLogger(JwController.class);
  
    @Resource(name = "jwService")
    private JwService jwService;
    
    @RequestMapping(value="/nexacro/selectjw.do")
  public NexacroResult selectJw(@ParamDataSet(name = "search_info", required = false) Map<String,String> param) {
 
        List<Map<String, Object>> list = null;
        
        try {
        list = jwService.selectjw(param);
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        NexacroResult result = new NexacroResult();
        result.addDataSet("jw_list", list);

        return result;
    }
    
    @RequestMapping(value="/nexacro/insertjw.do")
    public NexacroResult insertJw(@ParamDataSet(name = "store_info", required = false) Map<String,String> param) {
    	int suc = -1;
    	try {
			int a = jwService.insertjw(param);
			int b =jwService.insertsub(param);
			if(a!=-1) {
				suc=a;
			}else if(b!=-1) {
				suc=b;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		NexacroResult result = new NexacroResult();
		result.addVariable("result", suc);
		
		return result;
	}
    
    @RequestMapping(value="/nexacro/updatejw.do")
    public NexacroResult updateJw(@ParamDataSet(name = "store_info", required = false) Map<String,String> param) {
    	int suc = -1;
    	int b = -1;
    	int c = -1;;
    	List<Map<String, Object>> list = null;
		try {
			int a = jwService.updatejw(param);
			list = jwService.pro_up_sel(param);
			if(list.size()==0) {
				 c =jwService.insertsub(param);
			}else {
				 b =jwService.updatesub(param);
			}
			
			
			if(a!=-1) {
				suc=a;
			}else if(c !=-1) {
				suc=c;
			}else if(b != -1) {
				suc=b;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		NexacroResult result = new NexacroResult();
		result.addVariable("result", suc);
		
		return result;
	}
    

    @RequestMapping(value="/nexacro/deletejw.do")
    public NexacroResult deleteJw(@ParamVariable(name = "param", required = false) String param) {
    	int suc = -1;
    	
		try {
			suc = jwService.deletejw(param);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		NexacroResult result = new NexacroResult();
		result.addVariable("result", suc);
		
		return result;
	}
    @RequestMapping(value="/nexacro/deletejw_pro.do")
    public NexacroResult deleteJw_pro(@ParamVariable(name = "param", required = false) String param) {
    	int suc = -1;
    	
		try {
			suc = jwService.deletejw_pro(param);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		NexacroResult result = new NexacroResult();
		result.addVariable("result", suc);
		
		return result;
	}
    
    @RequestMapping(value="/nexacro/gridsel.do")
    public NexacroResult gridsel(@ParamVariable(name = "param", required = false) String param) {
   
          List<Map<String, Object>> list = null;
          
          try {
          list = jwService.gridsel(param);
          }catch(Exception e) {
          	e.printStackTrace();
          }
          
          NexacroResult result = new NexacroResult();
          result.addDataSet("jw_list", list);

          return result;
      }
    
    
    @RequestMapping(value="/nexacro/selectfilelist.do")
    public NexacroResult selectfile() {
   
          List<Map<String, Object>> list = null;
          
          try {
          list = jwService.selfile();
          }catch(Exception e) {
          	e.printStackTrace();
          }
          
          NexacroResult result = new NexacroResult();
          result.addDataSet("jw_list", list);

          return result;
      }
      
  
         
}