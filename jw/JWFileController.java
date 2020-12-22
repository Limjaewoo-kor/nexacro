package egovframework.nexacro.jw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import com.nexacro.uiadapter17.spring.core.NexacroException;
import com.nexacro.uiadapter17.spring.core.data.NexacroResult;
import com.nexacro17.xapi.data.DataSet;
import com.nexacro17.xapi.data.datatype.PlatformDataType;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.FileCopyUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.Random;
import com.nexacro.uiadapter17.spring.core.data.NexacroFileResult;
import com.nexacro.uiadapter17.spring.core.util.CharsetUtil;
import com.nexacro17.xapi.tx.PlatformType;

import egovframework.nexacro.jw.service.JwService;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

@Controller
public class JWFileController {
    private Logger logger = LoggerFactory.getLogger(JWFileController.class);
    private static final String SP = File.separator;
    private static final String PATH = "WEB-INF"+SP+"attachFile";  //서버 첨부파일 경로
    private static String sUserPath = "";   //사용자 폴더경로

    @Resource(name = "jwService")
    private JwService jwService;
    
    @Autowired
    private WebApplicationContext appContext; 
  
    /*
      파일 저장 후 저장파일 정보 반환 (화면에서 호출)
     */     
    @RequestMapping(value = "/nexacro/uploadFiles.do" )
    public NexacroResult uploadFiles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
      //MultipartHttpServletRequest 체크
        if(!(request instanceof MultipartHttpServletRequest)) {
            if(logger.isDebugEnabled()) {
              logger.debug("Request is not a MultipartHttpServletRequest");
            }
            return new NexacroResult();
        }
        
        logger.debug("-------------------- nexacro platform uploadFiles ---------------------------");
        
        //반환될 파일저장 정보 Dataset 생성 
        DataSet resultDs = createDataSet4UploadResult();
        
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        //파라미터 처리
        uploadParameters(multipartRequest);
        //파일저장 및 파일정보 반환 Dagaset 셋팅처리
        uploadMultipartFiles(multipartRequest, resultDs);
        
        NexacroResult nexacroResult = new NexacroResult();
        nexacroResult.addDataSet(resultDs);
        nexacroResult.setErrorCode(0);
        nexacroResult.setErrorMsg("File Save Success!");

        return nexacroResult;
    }//uploadFiles
    
    /*
       파라미터 셋팅
     */
    private void uploadParameters(MultipartHttpServletRequest multipartRequest) throws NexacroException {
        // parameter and multipart parameter
        Enumeration<String> parameterNames = multipartRequest.getParameterNames();

        while(parameterNames.hasMoreElements()) {
            
            String parameterName = parameterNames.nextElement();
            if(parameterName == null || parameterName.length() == 0) {
                continue;
            }
            
            String value = multipartRequest.getParameter(parameterName);
            
            //화면 FileUpTransfer 의 setPostData 로 셋팅한 저장될 파일경로 String을 셋팅한다. ("file")
            if("filepath".equals(parameterName)) {
              if(value != null && !value.equals("")) {
                // "WEB-INF/attachFile/" + "sample"
                sUserPath = SP + value;
              }
              continue;                
            }
        }//while
    }//uploadParameters
   
    /*
      실제파일 저장 및 저장파일정보 셋팅
     */
    private void uploadMultipartFiles(MultipartHttpServletRequest multipartRequest, DataSet resultDs) throws IOException {
        
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        String filePath = "C:\\Users\\invako\\Desktop\\새 폴더\\workspace\\mfds\\src\\main\\webapp\\images\\imgjw";
//        String filePath = getFilePath();
        
        Set<String> keySet = fileMap.keySet();
        for(String name: keySet) {
        	
        	jwService.insertfile(name);
            MultipartFile multipartFile = fileMap.get(name);

            String originalFilename = multipartFile.getOriginalFilename();

            // IE에서 파일업로드 시 DataSet 파라매터의 Content-Type이 설정되지 않아 여기로 옴. 무시.
            if(originalFilename == null || originalFilename.length() == 0) {
                continue;
            }
            
            File destination = new File(filePath);
            
            if( destination.exists() == false) {
              boolean mkdirs = destination.mkdirs();
              destination.setWritable(true);
              
              logger.debug("-------------- create directory ----------------------" + mkdirs);
            }
            
            File targetFile = new File(filePath+SP+originalFilename);

            InputStream inputStream = multipartFile.getInputStream();
            FileCopyUtils.copy(inputStream, new FileOutputStream(targetFile));
            
            int row = resultDs.newRow();
            resultDs.set(row, "fileid", originalFilename);
            resultDs.set(row, "filename", originalFilename);
            resultDs.set(row, "filesize", targetFile.length());
            
            logger.debug("uploaded file write success. file="+originalFilename);
        }//for
    }//uploadMultipartFiles
    
    /*
    파일을 저장할 절대경로 반환    
     */
    private String getFilePath() {
        ServletContext sc = appContext.getServletContext();
        String realPath = sc.getRealPath("/");
        String uploadPath = realPath + PATH + sUserPath;
        return uploadPath;
    }//getFilePath
    
    /*
    반환용 파일정보 데이터셋 생성
     */
    private DataSet createDataSet4UploadResult() {
        
        DataSet ds = new DataSet("ds_output");
        ds.addColumn("fileid", PlatformDataType.STRING);
        ds.addColumn("filename", PlatformDataType.STRING);
        ds.addColumn("filesize", PlatformDataType.INT);
        
        return ds;
    }//createDataSet4UploadResult
    
//////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////file down/////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
    
    /*
    파일 다운로드 - 해당경로의 파일을 NexacroFileResult 에 담아 반환 (화면에서 호출)
   */   
  @RequestMapping(value = "/nexacro/downloadFiles.do")
  public NexacroFileResult downloadFile(HttpServletRequest request) throws Exception {
    
    logger.debug("-------------------- nexacro platform downloadFile ---------------------------");
    String characterEncoding = request.getCharacterEncoding();
    
    if(characterEncoding == null) {
      characterEncoding = PlatformType.DEFAULT_CHAR_SET;
    }
    
    //문자셋 타입
    String charsetOfRequest = CharsetUtil.getCharsetOfRequest(request, characterEncoding);

    //filepath 파라미터 (폴더명)
    String fileDir = request.getParameter("filepath");

    //다운로드 Client 파일명
    String displayFileName = "";
    
    //파일정보를 담고있는 XML문자열(Dataset 의 saveXML())
    String fileInfoXml = request.getParameter("fileInfo");
    
    DataSet dsFileInfo = null;
    
    //파일정보 Dataset saveXML 문자열
    if(fileInfoXml != null) {
      dsFileInfo = new DataSet("fileInfo");
      fileInfoXml = fileInfoXml.replaceAll("&lt;", "<").replaceAll("&quot;", "\"").replaceAll("&gt;", ">");

      dsFileInfo.loadXml(fileInfoXml);
    }
    
    //사용자지정폴더가 넘어왔을시
    if(fileDir != null) {
      fileDir = fileDir + SP;
    }else {
      fileDir = "";          
    }

//    String filePath = getFilePath()+ SP +fileDir;
    String filePath = "C:\\Users\\invako\\Desktop\\새 폴더\\workspace\\mfds\\src\\main\\webapp\\images\\imgjw\\";
    
    String fileName = "";
    File file = null;
    
    if(dsFileInfo.getRowCount() > 1) {
      displayFileName = "첨부파일.zip";
      
      Random rnd = new Random();
      
      String randomStr = String.valueOf(rnd.nextInt(999999999))+String.valueOf(rnd.nextInt(999999999));
      
      file = getCompressZipFile(dsFileInfo, filePath, "compressZip-"+randomStr, charsetOfRequest);
    }else {
      displayFileName = dsFileInfo.getString(0, "displayFileName");
      fileName = dsFileInfo.getString(0, "realFileName");
          
      // was의 uri encoding을 사용안함. (서버의 설정을 변경하여야 함. URIEncoding="UTF-8")
      // already decode..
      // tomcat의 기본 uriencoding 형식 + web.xml의 charsetfilter utf8 (runtime version 은  uriencoding 되지  않고 있음)
      fileName = URLDecoder.decode(fileName, charsetOfRequest);
      // 파일명에 불필요 문자 제거
      fileName = removedPathTraversal(fileName);
      
      String realFileName = filePath +fileName;
      
      logger.debug("     charsetOfRequest :{}",charsetOfRequest);
      logger.debug("     FILE PATH :{}" , filePath);
      logger.debug("     FILE NAME :{}" , fileName);
      logger.debug("     realFileName :{}" , realFileName);
      
      file = new File(realFileName);
    }
    
    NexacroFileResult result = new NexacroFileResult(file);
    result.setOriginalName(displayFileName);
    
    return result;
  }    

  /*
   *   파일명에 불필요한 문자제거
   * */
  private String removedPathTraversal(String fileName) {
    if(fileName == null) {
      return null;
    }
    
    fileName = fileName.replace("/", "");
    fileName = fileName.replace("\\", "");
    fileName = fileName.replace("&", "");
    
    return fileName;
  }

  /*
   *   파일 압축 (압축파일도 서버에 저장을 하기 때문에 Job Scheduler 등으로 이후 삭제하여야 합니다)
   * */
  private File getCompressZipFile(DataSet fileInfo, String realPath, String compressName, String charsetOfRequest) throws IOException {

    String dumpDir = "dummy"+ SP;
    String path = realPath;
    String files[] = new String[fileInfo.getRowCount()];
    
    for(int i=0;i<files.length;i++) {
      files[i] = URLDecoder.decode(fileInfo.getString(i, "realFileName"), charsetOfRequest);
      files[i] = removedPathTraversal(files[i]);
    }

    File destination = new File(path + dumpDir);
    
    if( destination.exists() == false) {
      boolean mkdirs = destination.mkdirs();
      destination.setWritable(true);
      
      logger.debug("-------------- create directory ----------------------{}" , mkdirs);
    }
    
    //buffer size
    int size = 1024;
    byte[] buf = new byte[size];
    String outZipNm = path + dumpDir + compressName +".zip";
    
    File file = new File(outZipNm);
     
    FileInputStream fis = null;
    ZipArchiveOutputStream zos = null;
    BufferedInputStream bis = null;
     
    try {
      // Zip 파일생성
      zos = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(outZipNm))); 
      for( int i=0; i < files.length; i++ ){
        //encoding 설정
        zos.setEncoding("UTF-8");
         
        //buffer에 해당파일의 stream을 입력한다.
        fis = new FileInputStream(path + files[i]);
        bis = new BufferedInputStream(fis,size);
         
        //zip에 넣을 다음 entry 를 가져온다.
        zos.putArchiveEntry(new ZipArchiveEntry(files[i]));                 
         
        //준비된 버퍼에서 집출력스트림으로 write 한다.
        int len;
        while((len = bis.read(buf,0,size)) != -1){
          zos.write(buf,0,len);
        }
         
        bis.close();
        fis.close();
        zos.closeArchiveEntry();

      }
      zos.close();
      
      logger.debug("     charsetOfRequest :{}",charsetOfRequest);
      logger.debug("     FILE PATH :{}" , (path+ dumpDir));
      logger.debug("     FILE NAME :{}.zip", compressName);

    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      if( zos != null ){  zos.close();  }
      if( fis != null ){  fis.close();  }
      if( bis != null ){  bis.close();  }
    }
    
    return file;
  }
}
