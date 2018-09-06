package com.delllogistics.service.sys;

import com.delllogistics.dto.ResizeImg;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.repository.sys.SysFileRepository;
import com.delllogistics.storage.StorageInterface;
import com.delllogistics.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Service
public class SysFileService {

    private final SysFileRepository sysFileRepository;

    private final StorageInterface storageInterface;

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    public SysFileService(SysFileRepository sysFileRepository, StorageInterface storageInterface) {
        this.sysFileRepository = sysFileRepository;
        this.storageInterface = storageInterface;
    }

    public SysFile findById(Long id){
        return sysFileRepository.findOne(id);
    }

    public String save(SysFile fileBase){
        sysFileRepository.save(fileBase);
        return fileBase.getId().toString();
    }
    public void delete(Long id){
        sysFileRepository.delete(id);
    }



    public Set<SysFile> uploadImgs(MultipartFile[] files,User user,String from) {
        Set<ResizeImg> resizeImgs  = new HashSet<>();
        return uploadImgs(files,user,from,resizeImgs);
    }


    public Set<SysFile> uploadImgs(MultipartFile[] files,User user,String from,Set<ResizeImg> resizeImgs) {
        Set<SysFile> fileBases = new HashSet<>();
        for (MultipartFile file : files) {
            SysFile sysFile =  uploadImg(file,user,from,resizeImgs);
            fileBases.add(sysFile);
        }
        return fileBases;
    }
    public SysFile uploadImg(MultipartFile file,User user,String from) {
        Set<ResizeImg> resizeImgs  = new HashSet<>();
        return uploadImg( file, user,from,resizeImgs);
    }
    /**
     * Description: 创建一个文件对象
     * @param file  文件流对象
     * @param user  上传用户
     * @param from  来源
     * @param resizeImgs  缩略图对象数组
     * @return  SysFile
     */
    private SysFile uploadImg(MultipartFile file,User user,String from,Set<ResizeImg> resizeImgs) {
        SysFile sysFile = getNewSysFile(file);//获取一个上传文件对象
        sysFile.setUser(user);
        sysFile.setSource(from);
        try {
            for (ResizeImg resizeImg :resizeImgs){
                uploadResizeImg(file,sysFile.getUploadName(),resizeImg);//上传缩略图
            }
            boolean uploadResult =storageInterface.uploadFile(sysFile.getUploadName(),file.getBytes());
            if(uploadResult)
            {
                //保存数据库中
                sysFileRepository.save(sysFile);
            }
        }  catch (Exception e) {
            logger.error("上传文件异常：",e);
        }
        return sysFile;
    }
    /**
     * Description: 创建一个文件对象
     * @param file     文件流对象
     * @return SysFile
     */
    private SysFile getNewSysFile(MultipartFile file){
        String tokenFileName = UUID.randomUUID().toString();
        SysFile sysFile = new SysFile();
        String fileName = file.getOriginalFilename();
        sysFile.setType(file.getContentType());
        sysFile.setOriginalName(fileName);
        sysFile.setSize(file.getSize());
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String uploadName = tokenFileName + suffixName;
        sysFile.setUploadName(uploadName);
        sysFile.setUid(tokenFileName);
        sysFile.setPath(storageInterface.getStorageDomain());
        sysFile.setUrl(storageInterface.getStorageDomain() + uploadName);
        return sysFile;
    }



    /**
     * Description: 向FTP服务器上传缩略图文件
     * @param file     原图片
     * @param filefullName   文件全名
     * @param img ResizeImg图片对象
     * @return 成功返回true，否则返回false
     */
    private boolean uploadResizeImg(MultipartFile file, String filefullName, ResizeImg img) throws Exception {
        boolean success = false;
        String suffixName = filefullName.substring(filefullName.lastIndexOf("."));
        String fileName=  filefullName.substring(0,filefullName.lastIndexOf(suffixName));
        String uploadName =fileName +"x"+ img.getWidth()+ suffixName;
        File resizeFile = new File(this.getClass().getResource("/") +uploadName );
        ImageUtils.resize(file.getInputStream(), resizeFile, img.getWidth(), 0);
        InputStream input = new BufferedInputStream(new FileInputStream(resizeFile));
        if (storageInterface.uploadFile(uploadName, input))
        {
            if (resizeFile.exists() && resizeFile.isFile()) {
                if (resizeFile.delete()) {
                    success = true;
                }
            }
        }
        return success;
    }
}

