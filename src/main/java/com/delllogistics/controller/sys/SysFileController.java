package com.delllogistics.controller.sys;

import com.delllogistics.dto.EditorImg;
import com.delllogistics.dto.ResizeImg;
import com.delllogistics.dto.Result;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.sys.SysFileService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@RestController
public class SysFileController {
    private final SysFileService sysFileService;
    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    public SysFileController( SysFileService sysFileService) {
        this.sysFileService = sysFileService;
    }


    /**
     * 上传评论图片
     */
    @PostMapping("/sysFile/uploadGoodsComment")
    public Result uploadGoodsComment( @RequestParam("file") MultipartFile[] files, @CurrentUser User user) {
        Set<SysFile> fileBases = sysFileService.uploadImgs(files,user,"goodsComment");//上传图片
        if(fileBases.size()>0){
            return ResultUtil.success(fileBases);
        }
        return ResultUtil.error(1004, "上传失败");
    }

    /**
     * 上传广告图片
     */
    @PostMapping("/sysFile/uploadSysAdvert")
    public Result uploadSysAdvert( @RequestParam("file") MultipartFile[] files, @CurrentUser User user) {
        Set<SysFile> fileBases = sysFileService.uploadImgs(files,user,"sysAdvert");//上传图片
        if(fileBases.size()>0){
            return ResultUtil.success(fileBases);
        }
        return ResultUtil.error(1004, "上传失败");
    }

    /**
     * 商品品牌图片上传
     */
    @PostMapping("/sysFile/uploadGoodsBrandImg")
    public Result uploadGoodsBrandImg(@RequestParam("file") MultipartFile[] files, @CurrentUser User user) {

        Set<SysFile> fileBases = sysFileService.uploadImgs(files,user,"goodsBrand");//上传图片
        if(fileBases.size()>0){
            return ResultUtil.success(fileBases);
        }
        return ResultUtil.error(1004, "上传失败");
    }

    /**
     * 商品系列图片上传
     */
    @PostMapping("/sysFile/uploadGoodsSeriesImg")
    public Result uploadGoodsSeriesImg(@RequestParam("file") MultipartFile[] files,  @CurrentUser User user) {
        Set<SysFile> fileBases = sysFileService.uploadImgs(files,user,"goodsSeries");//上传图片
        if(fileBases.size()>0){
            return ResultUtil.success(fileBases);
        }
        return ResultUtil.error(1004, "上传失败");
    }

    @PostMapping("/sysFile/uploadGoodsEditorImg")
    public EditorImg uploadGoodsEditorImg( @RequestParam("imgs") MultipartFile file,  @CurrentUser User user) {
        EditorImg editorImg = new EditorImg();
            try{
                SysFile sysFile = sysFileService.uploadImg(file,user,"goodsDetailsImg");//上传图片
                editorImg.setLink(sysFile.getUrl());
            } catch (Exception e) {
                editorImg.setError(e.toString());
            }
        return editorImg;
    }

    @PostMapping("/sysFile/uploadGoodsImg")
    public Result uploadGoodsImg( @RequestParam("file") MultipartFile[] files,  @CurrentUser User user) {

        Set<SysFile> fileBases = sysFileService.uploadImgs(files,user,"goodsImg");//上传图片
        if(fileBases.size()>0){
            return ResultUtil.success(fileBases);
        }
        return ResultUtil.error(1004, "上传失败");
    }


    @PostMapping("/sysFile/delete")
    public Result delete(@RequestParam("id") long id) {
        SysFile sysFile =  sysFileService.findById(id);
        if (sysFile != null) {
            sysFileService.delete(sysFile.getId());
            return ResultUtil.success();
        }
        return ResultUtil.error(1004,"删除失败");
    }




}
